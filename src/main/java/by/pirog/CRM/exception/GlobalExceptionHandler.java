package by.pirog.CRM.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SellerNotFoundException.class)
    public ErrorResponseException handleSellerNotFound(
            SellerNotFoundException ex,
            HttpServletRequest request) {
        return buildNotFoundProblem(ex, request, "Seller Not Found");
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ErrorResponseException handleTransactionNotFound(
            TransactionNotFoundException ex,
            HttpServletRequest request) {
        return buildNotFoundProblem(ex, request, "Transaction Not Found");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponseException handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ){
        Map<String, String> errors = new HashMap();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Validation error");
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty("method", request.getMethod());
        problemDetail.setProperty("errors", errors);

        return new ErrorResponseException(HttpStatus.BAD_REQUEST, problemDetail, ex);
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponseException handleAll(Exception ex, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setTitle("Unexpected error");
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty("method", request.getMethod());
        return new ErrorResponseException(HttpStatus.INTERNAL_SERVER_ERROR, problemDetail, ex);
    }
    private ErrorResponseException buildNotFoundProblem(
            RuntimeException ex,
            HttpServletRequest request,
            String title) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setTitle(title);
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty("method", request.getMethod());

        return new ErrorResponseException(HttpStatus.NOT_FOUND, problemDetail, ex);
    }

}
