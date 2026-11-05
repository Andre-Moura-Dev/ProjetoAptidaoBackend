package corrida_solidaria.corrida_solidaria.api.dto;

import lombok.*;
import java.time.LocalDate;
import corrida_solidaria.corrida_solidaria.api.enums.PercursoProva;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InscricaoRequestDTO {

    private String nome;
    private String email;
    private LocalDate dtNascimento;
    private PercursoProva percursoProva;
}
