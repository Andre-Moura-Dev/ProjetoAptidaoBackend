package corrida_solidaria.corrida_solidaria.api.service;

import corrida_solidaria.corrida_solidaria.api.dto.InscricaoRequestDTO;
import corrida_solidaria.corrida_solidaria.api.dto.InscricaoResponseDTO;
import corrida_solidaria.corrida_solidaria.api.dto.InscricaoUpdateDTO;
import corrida_solidaria.corrida_solidaria.api.dto.ResumoResponseDTO;
import corrida_solidaria.corrida_solidaria.api.enums.CategoriaPercurso;
import corrida_solidaria.corrida_solidaria.api.enums.PercursoProva;
import corrida_solidaria.corrida_solidaria.api.exceptions.DefaultExceptionHandler;
import corrida_solidaria.corrida_solidaria.api.model.InscricaoEntity;
import corrida_solidaria.corrida_solidaria.api.repository.InscricaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@Service
public class InscricaoService {

    private static final Pattern REGEX_EMAIL = Pattern.compile("^[a-z0-9+_.-]+@[a-z0-9.-]+\\.[a-z]{2,}$");
    private static final int IDADE_MINIMA = 14;
    private static final int IDADE_MINIMA_10KM = 18;

    @Value("${corrida.data-prova}")
    private String dataProva;

    @Autowired
    private InscricaoRepository inscricaoRepository;

    public List<InscricaoResponseDTO> listarTodas() throws DefaultExceptionHandler {
        try {
            return inscricaoRepository.findAllByOrderByCodInscricaoAsc()
                    .stream()
                    .map(this::converterParaResponse)
                    .toList();
        } catch (Exception e) {
            if (e instanceof DefaultExceptionHandler) {
                throw e;
            } else {
                throw new DefaultExceptionHandler(e);
            }
        }
    }

    public InscricaoResponseDTO consultarPorEmail(String email) throws DefaultExceptionHandler {
        try {
            return converterParaResponse(buscarEntidadePorEmail(email));
        } catch (Exception e) {
            if (e instanceof DefaultExceptionHandler) {
                throw e;
            } else {
                throw new DefaultExceptionHandler(e);
            }
        }
    }

    @Transactional(rollbackFor = DefaultExceptionHandler.class)
    public InscricaoResponseDTO cadastrarInscricao(InscricaoRequestDTO inscricaoRequestDTO) throws DefaultExceptionHandler {
        try {

            String nome = normalizarNome(inscricaoRequestDTO.getNome());
            String email = normalizarEmail(inscricaoRequestDTO.getEmail());
            validarNascimentoEPercurso(inscricaoRequestDTO.getDtNascimento(), inscricaoRequestDTO.getPercursoProva());

            if (inscricaoRepository.existsByEmail(email)) {
                throw new DefaultExceptionHandler(
                        HttpStatus.CONFLICT.value(),
                        "Operação Inválida! Já existe uma inscrição ativa para este email."
                );
            }

            InscricaoEntity inscricao = new InscricaoEntity();

            inscricao.setNome(nome);
            inscricao.setEmail(email);
            inscricao.setDtNascimento(inscricaoRequestDTO.getDtNascimento());
            inscricao.setPercursoProva(inscricaoRequestDTO.getPercursoProva());

            InscricaoEntity inscricaoSalva = inscricaoRepository.save(inscricao);

            return converterParaResponse(inscricaoSalva);

        } catch (DataIntegrityViolationException e) {
            // duas requisições simultâneas com o mesmo e-mail: o UNIQUE do banco irá recusar a segunda
            throw new DefaultExceptionHandler(
                    HttpStatus.CONFLICT.value(),
                    "Operação Inválida! Já existe uma inscrição ativa para este email."
            );
        } catch (Exception e) {
            if (e instanceof DefaultExceptionHandler) {
                throw e;
            } else {
                throw new DefaultExceptionHandler(e);
            }
        }
    }

    @Transactional(rollbackFor = DefaultExceptionHandler.class)
    public InscricaoResponseDTO atualizarInscricao(String email, InscricaoUpdateDTO inscricaoUpdateDTO) {
        try {

            InscricaoEntity inscricao = buscarEntidadePorEmail(email);

            String nome = normalizarNome(inscricaoUpdateDTO.getNome());
            validarNascimentoEPercurso(inscricaoUpdateDTO.getDtNascimento(), inscricaoUpdateDTO.getPercursoProva());

            inscricao.setNome(nome);
            inscricao.setDtNascimento(inscricaoUpdateDTO.getDtNascimento());
            inscricao.setPercursoProva(inscricaoUpdateDTO.getPercursoProva());

            InscricaoEntity inscricaoAtualizada = inscricaoRepository.save(inscricao);

            return converterParaResponse(inscricaoAtualizada);

        } catch (Exception e) {
            if (e instanceof DefaultExceptionHandler) {
                throw e;
            } else {
                throw new DefaultExceptionHandler(e);
            }
        }
    }

    @Transactional(rollbackFor = DefaultExceptionHandler.class)
    public void cancelarInscricao(String email) throws DefaultExceptionHandler {
        try {

            InscricaoEntity inscricao = buscarEntidadePorEmail(email);

            inscricaoRepository.delete(inscricao);

        } catch (Exception e) {
            if (e instanceof DefaultExceptionHandler) {
                throw e;
            } else {
                throw new DefaultExceptionHandler(e);
            }
        }
    }

    public ResumoResponseDTO resumo() throws DefaultExceptionHandler {
        try {
            return new ResumoResponseDTO(
                    inscricaoRepository.count(),
                    inscricaoRepository.countByPercursoProva(PercursoProva.KM_5),
                    inscricaoRepository.countByPercursoProva(PercursoProva.KM_10)
            );
        } catch (Exception e) {
            if (e instanceof DefaultExceptionHandler) {
                throw e;
            } else {
                throw new DefaultExceptionHandler(e);
            }
        }
    }


    private String normalizarNome(String nome) throws DefaultExceptionHandler {
        if (nome == null || nome.isBlank()) {
            throw new DefaultExceptionHandler(
                    HttpStatus.BAD_REQUEST.value(),
                    "Operação Inválida! O campo nome é obrigatório."
            );
        }

        // remove espaços limpos e reduz espaços internos repetidos
        String nomeLimpo = nome.trim().replaceAll("\\s+", " ");

        if (nomeLimpo.length() < 3 || nomeLimpo.length() > 100) {
            throw new DefaultExceptionHandler(
                    HttpStatus.BAD_REQUEST.value(),
                    "Operação Inválida! O nome deve ter entre 3 e 100 caracteres."
            );
        }

        return nomeLimpo;
    }

    private String normalizarEmail(String email) throws DefaultExceptionHandler {
        if (email == null || email.isBlank()) {
            throw new DefaultExceptionHandler(
                    HttpStatus.BAD_REQUEST.value(),
                    "Operação Inválida! O campo e-mail é obrigatório."
            );
        }

        String emailLimpo = email.trim().toLowerCase(Locale.ROOT);

        if (!REGEX_EMAIL.matcher(emailLimpo).matches()) {
            throw new DefaultExceptionHandler(
                    HttpStatus.BAD_REQUEST.value(),
                    "Operação Inválida! Por favor, informe um formato correto de e-mail."
            );
        }

        return emailLimpo;
    }

    private void validarNascimentoEPercurso(LocalDate dataNascimento, PercursoProva percursoProva)
            throws DefaultExceptionHandler {

        if (dataNascimento == null) {
            throw new DefaultExceptionHandler(
                    HttpStatus.BAD_REQUEST.value(),
                    "O campo data de nascimento é obrigatório."
            );
        }

        if (!dataNascimento.isBefore(LocalDate.now())) {
            throw new DefaultExceptionHandler(
                    HttpStatus.BAD_REQUEST.value(),
                    "A data de nascimento deve ser anterior a hoje."
            );
        }

        if (percursoProva == null) {
            throw new DefaultExceptionHandler(
                    HttpStatus.BAD_REQUEST.value(),
                    "O campo percurso é obrigatório (KM_5 ou KM_10)."
            );
        }

        int idade = calcularIdadeNaProva(dataNascimento);

        // Ordem importa: primeiro a idade mínima, depois a restrição dos 10 km
        if (idade < IDADE_MINIMA) {
            throw new DefaultExceptionHandler(
                    HttpStatus.BAD_REQUEST.value(),
                    "A idade mínima para participar é 14 anos."
            );
        }

        if (percursoProva == PercursoProva.KM_10 && idade < IDADE_MINIMA_10KM) {
            throw new DefaultExceptionHandler(
                    HttpStatus.BAD_REQUEST.value(),
                    "O percurso de 10 km é permitido somente a partir de 18 anos."
            );
        }
    }

    private Integer calcularIdadeNaProva(LocalDate dtNascimento) {
        return Period.between(dtNascimento, LocalDate.parse(dataProva)).getYears();
    }

    private CategoriaPercurso calcularCategoria(int idade) {
        if (idade < 18) {
            return CategoriaPercurso.JUVENIL;
        }

        if (idade < 40) {
            return CategoriaPercurso.ADULTO;
        }

        if (idade < 60) {
            return CategoriaPercurso.MASTER;
        }
        return CategoriaPercurso.SENIOR;
    }

    private InscricaoEntity buscarEntidadePorEmail(String email) throws DefaultExceptionHandler {
        String emailNormalizado = email == null ? "" : email.trim().toLowerCase(Locale.ROOT);

        return inscricaoRepository.findByEmail(emailNormalizado)
                .orElseThrow(() -> new DefaultExceptionHandler(
                        HttpStatus.NOT_FOUND.value(),
                        "Operação Inválida! Não existe inscrição para o e-mail informado."
                ));
    }

    private InscricaoResponseDTO converterParaResponse(InscricaoEntity inscricao) {
        int idade = calcularIdadeNaProva(inscricao.getDtNascimento());

        return InscricaoResponseDTO.builder()
                .nome(inscricao.getNome())
                .email(inscricao.getEmail())
                .dtNascimento(inscricao.getDtNascimento())
                .percursoProva(inscricao.getPercursoProva())
                .idadeNaProva(idade)
                .categoriaPercurso(calcularCategoria(idade))
                .build();
    }
}
