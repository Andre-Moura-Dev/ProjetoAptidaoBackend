package corrida_solidaria.corrida_solidaria.api.controller;

import corrida_solidaria.corrida_solidaria.api.dto.InscricaoRequestDTO;
import corrida_solidaria.corrida_solidaria.api.dto.InscricaoResponseDTO;
import corrida_solidaria.corrida_solidaria.api.dto.InscricaoUpdateDTO;
import corrida_solidaria.corrida_solidaria.api.dto.ResumoResponseDTO;
import corrida_solidaria.corrida_solidaria.api.exceptions.DefaultExceptionHandler;
import corrida_solidaria.corrida_solidaria.api.service.InscricaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/inscricoes")
public class InscricaoController {

    @Autowired
    private InscricaoService inscricaoService;

    @GetMapping(value = "/listar")
    public ResponseEntity<List<InscricaoResponseDTO>> listarTodas() throws DefaultExceptionHandler {
        List<InscricaoResponseDTO> listaInscricoes = inscricaoService.listarTodas();
        return ResponseEntity.ok(listaInscricoes);
    }

    @PostMapping(value = "/cadastrar")
    public ResponseEntity<InscricaoResponseDTO> cadastrar(@RequestBody InscricaoRequestDTO request) throws DefaultExceptionHandler {
        InscricaoResponseDTO inscricao = inscricaoService.cadastrarInscricao(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .build()
                .toUri();

        return ResponseEntity.created(location).body(inscricao);
    }

    @PutMapping(value = "/atualizar/{email}")
    public ResponseEntity<InscricaoResponseDTO> atualizar(@PathVariable String email, @RequestBody InscricaoUpdateDTO request) throws DefaultExceptionHandler {
        InscricaoResponseDTO inscricaoAtualizada = inscricaoService.atualizarInscricao(email, request);
        return ResponseEntity.ok().body(inscricaoAtualizada);
    }

    @DeleteMapping(value = "/cancelar/{email}")
    public ResponseEntity<Void> cancelar(@PathVariable String email) throws DefaultExceptionHandler {
        inscricaoService.cancelarInscricao(email);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/buscar-por-email/{email}")
    public ResponseEntity<InscricaoResponseDTO> buscarPorEmail(@PathVariable String email) throws DefaultExceptionHandler {
        InscricaoResponseDTO inscricao = inscricaoService.consultarPorEmail(email);
        return ResponseEntity.ok(inscricao);
    }

    @GetMapping(value = "/resumo")
    public ResponseEntity<ResumoResponseDTO> resumo() throws DefaultExceptionHandler {
        ResumoResponseDTO resumo = inscricaoService.resumo();
        return ResponseEntity.ok(resumo);
    }

}
