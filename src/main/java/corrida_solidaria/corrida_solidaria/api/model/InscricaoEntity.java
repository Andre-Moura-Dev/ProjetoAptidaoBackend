package corrida_solidaria.corrida_solidaria.api.model;

import corrida_solidaria.corrida_solidaria.api.enums.PercursoProva;
import lombok.*;

import java.time.LocalDate;
import jakarta.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "TBL_INSCRICAO")
public class InscricaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COD_INSCRICAO")
    private Long codInscricao;

    @Column(name = "NOME", nullable = false, length = 100)
    private String nome;

    @Column(name = "EMAIL", nullable = false, unique = true, length = 254)
    private String email;

    @Column(name = "DT_NASCIMENTO", nullable = false)
    private LocalDate dtNascimento;

    @Column(name = "PERCURSO",nullable = false, length = 10)
    private PercursoProva percursoProva;
}
