package corrida_solidaria.corrida_solidaria.api.exceptions;

import lombok.Getter;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;

public class DefaultExceptionHandler extends RuntimeException {

    @Getter
    private ErrorResponse errorResponse = new ErrorResponse();

    public DefaultExceptionHandler(Integer httpCode) {
        this.errorResponse.setStatusCode(httpCode);
    }

    public DefaultExceptionHandler(Integer httpCode, String userMessage) {
        super(userMessage);
        this.errorResponse.setStatusCode(httpCode);
        this.errorResponse.setUserMessage(userMessage);
    }

    public DefaultExceptionHandler(Integer httpCode, String userMessage, Throwable cause) {
        super(userMessage,cause);
        this.errorResponse.setStatusCode(httpCode);
        this.errorResponse.setUserMessage(userMessage);
    }

    public DefaultExceptionHandler(Integer httpCode, String userMessage, String developerMessage, Throwable cause) {
        super(userMessage, cause);
        this.errorResponse.setStatusCode(httpCode);
        this.errorResponse.setUserMessage(userMessage);
        this.errorResponse.setDeveloperMessage(developerMessage);
    }

    public DefaultExceptionHandler(Throwable cause) {
        super(cause);
        this.errorResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        this.errorResponse.setUserMessage("Operação Inválida! Ocorreu um erro ao executar a operação.");
        this.errorResponse.setDeveloperMessage(ExceptionUtils.getStackTrace(cause));
    }

}
