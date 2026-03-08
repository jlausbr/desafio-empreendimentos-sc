package br.com.sctec.desafiopratico.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.sctec.desafiopratico.dto.EmpreendimentoRequest;
import br.com.sctec.desafiopratico.dto.EmpreendimentoResponse;
import br.com.sctec.desafiopratico.model.SegmentoAtuacao;
import br.com.sctec.desafiopratico.service.EmpreendimentoService;

import jakarta.validation.Valid;

/**
 * Controller REST para operações CRUD de empreendimentos.
 */
@RestController
@RequestMapping("/api/empreendimentos")
public class EmpreendimentoController {

    private final EmpreendimentoService service;

    public EmpreendimentoController(EmpreendimentoService service) {
        this.service = service;
    }

    /**
     * Cadastra um novo empreendimento.
     * POST /api/empreendimentos
     */
    @PostMapping
    public ResponseEntity<EmpreendimentoResponse> criar(
            @Valid @RequestBody EmpreendimentoRequest request) {
        EmpreendimentoResponse criado = service.criar(request);
        URI location = URI.create("/api/empreendimentos/" + criado.id());
        return ResponseEntity.created(location).body(criado);
    }

    /**
     * Lista todos os empreendimentos, com filtros opcionais.
     * GET /api/empreendimentos
     * GET /api/empreendimentos?municipio=Florianópolis
     * GET /api/empreendimentos?segmento=TECNOLOGIA
     * GET /api/empreendimentos?ativo=true
     * GET /api/empreendimentos?nome=tech
     */
    @GetMapping
    public ResponseEntity<List<EmpreendimentoResponse>> listar(
            @RequestParam(required = false) String municipio,
            @RequestParam(required = false) SegmentoAtuacao segmento,
            @RequestParam(required = false) Boolean ativo,
            @RequestParam(required = false) String nome) {

        List<EmpreendimentoResponse> resultado;

        if (municipio != null) {
            resultado = service.buscarPorMunicipio(municipio);
        } else if (segmento != null) {
            resultado = service.buscarPorSegmento(segmento);
        } else if (ativo != null) {
            resultado = service.buscarPorStatus(ativo);
        } else if (nome != null) {
            resultado = service.buscarPorNome(nome);
        } else {
            resultado = service.listarTodos();
        }

        return ResponseEntity.ok(resultado);
    }

    /**
     * Busca um empreendimento pelo ID.
     * GET /api/empreendimentos/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmpreendimentoResponse> buscarPorId(@PathVariable Long id) {
        EmpreendimentoResponse response = service.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Atualiza um empreendimento existente.
     * PUT /api/empreendimentos/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmpreendimentoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody EmpreendimentoRequest request) {
        EmpreendimentoResponse atualizado = service.atualizar(id, request);
        return ResponseEntity.ok(atualizado);
    }

    /**
     * Remove um empreendimento.
     * DELETE /api/empreendimentos/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
