package corrida_solidaria.corrida_solidaria.api.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DefaultExceptionHandler.class)
    public ResponseEntity<ErrorResponse> handleDefaultException(DefaultExceptionHandler ex) {

        return ResponseEntity
                .status(ex.getErrorResponse().getStatusCode())
                .body(ex.getErrorResponse());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {

        ErrorResponse error = new ErrorResponse();
        error.setStatusCode(500);
        error.setUserMessage("Erro interno no servidor.");
        error.setDeveloperMessage(ex.getMessage());

        return ResponseEntity
                .status(500)
                .body(error);
    }
}
