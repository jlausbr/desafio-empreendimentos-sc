package br.com.sctec.desafiopratico.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.sctec.desafiopratico.dto.EmpreendimentoRequest;
import br.com.sctec.desafiopratico.dto.EmpreendimentoResponse;
import br.com.sctec.desafiopratico.exception.ResourceNotFoundException;
import br.com.sctec.desafiopratico.model.Empreendimento;
import br.com.sctec.desafiopratico.model.SegmentoAtuacao;
import br.com.sctec.desafiopratico.repository.EmpreendimentoRepository;

/**
 * Camada de serviço com a lógica de negócio dos empreendimentos.
 */
@Service
public class EmpreendimentoService {

    private final EmpreendimentoRepository repository;

    public EmpreendimentoService(EmpreendimentoRepository repository) {
        this.repository = repository;
    }

    public EmpreendimentoResponse criar(EmpreendimentoRequest request) {
        Empreendimento entity = toEntity(request);
        Empreendimento salvo = repository.save(entity);
        return EmpreendimentoResponse.fromEntity(salvo);
    }

    public List<EmpreendimentoResponse> listarTodos() {
        return repository.findAll()
                .stream()
                .map(EmpreendimentoResponse::fromEntity)
                .toList();
    }

    public EmpreendimentoResponse buscarPorId(Long id) {
        Empreendimento entity = findOrThrow(id);
        return EmpreendimentoResponse.fromEntity(entity);
    }

    public List<EmpreendimentoResponse> buscarPorMunicipio(String municipio) {
        return repository.findByMunicipioIgnoreCase(municipio)
                .stream()
                .map(EmpreendimentoResponse::fromEntity)
                .toList();
    }

    public List<EmpreendimentoResponse> buscarPorSegmento(SegmentoAtuacao segmento) {
        return repository.findBySegmento(segmento)
                .stream()
                .map(EmpreendimentoResponse::fromEntity)
                .toList();
    }

    public List<EmpreendimentoResponse> buscarPorStatus(Boolean ativo) {
        return repository.findByAtivo(ativo)
                .stream()
                .map(EmpreendimentoResponse::fromEntity)
                .toList();
    }

    public List<EmpreendimentoResponse> buscarPorNome(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(EmpreendimentoResponse::fromEntity)
                .toList();
    }

    public EmpreendimentoResponse atualizar(Long id, EmpreendimentoRequest request) {
        Empreendimento entity = findOrThrow(id);

        entity.setNome(request.nome());
        entity.setEmpreendedor(request.empreendedor());
        entity.setMunicipio(request.municipio());
        entity.setSegmento(request.segmento());
        entity.setEmail(request.email());
        entity.setAtivo(request.ativo() != null ? request.ativo() : entity.getAtivo());

        Empreendimento atualizado = repository.save(entity);
        return EmpreendimentoResponse.fromEntity(atualizado);
    }

    public void excluir(Long id) {
        Empreendimento entity = findOrThrow(id);
        repository.delete(entity);
    }

    private Empreendimento findOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Empreendimento não encontrado com id: " + id));
    }

    private Empreendimento toEntity(EmpreendimentoRequest request) {
        Empreendimento entity = new Empreendimento();
        entity.setNome(request.nome());
        entity.setEmpreendedor(request.empreendedor());
        entity.setMunicipio(request.municipio());
        entity.setSegmento(request.segmento());
        entity.setEmail(request.email());
        entity.setAtivo(request.ativo() != null ? request.ativo() : true);
        return entity;
    }
}
