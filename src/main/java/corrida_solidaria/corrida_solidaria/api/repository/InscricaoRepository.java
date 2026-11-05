package corrida_solidaria.corrida_solidaria.api.repository;

import corrida_solidaria.corrida_solidaria.api.enums.PercursoProva;
import corrida_solidaria.corrida_solidaria.api.model.InscricaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InscricaoRepository extends JpaRepository<InscricaoEntity, Long> {

    Optional<InscricaoEntity> findByEmail(String email);
    List<InscricaoEntity> findAllByOrderByCodInscricaoAsc();
    boolean existsByEmail(String email);
    long countByPercursoProva(PercursoProva percursoProva);
}
