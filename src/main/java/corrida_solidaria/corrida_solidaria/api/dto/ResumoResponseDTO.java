package corrida_solidaria.corrida_solidaria.api.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumoResponseDTO {

    private Long total;
    private Long Km5;
    private Long Km10;
}
