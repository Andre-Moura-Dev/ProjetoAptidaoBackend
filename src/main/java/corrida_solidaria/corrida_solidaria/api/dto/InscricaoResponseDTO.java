package corrida_solidaria.corrida_solidaria.api.dto;

import corrida_solidaria.corrida_solidaria.api.enums.CategoriaPercurso;
import lombok.*;
import java.time.LocalDate;
import corrida_solidaria.corrida_solidaria.api.enums.PercursoProva;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InscricaoResponseDTO {

    private String nome;
    private String email;
    private LocalDate dtNascimento;
    private PercursoProva percursoProva;
    private Integer idadeNaProva;
    private CategoriaPercurso categoriaPercurso;
}
