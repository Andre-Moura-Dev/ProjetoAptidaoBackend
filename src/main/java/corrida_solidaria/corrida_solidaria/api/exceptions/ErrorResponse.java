package corrida_solidaria.corrida_solidaria.api.exceptions;

import lombok.Data;

@Data
public class ErrorResponse {

    private Integer statusCode;
    private String userMessage;
    private String developerMessage;
}
