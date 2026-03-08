package br.com.sctec.desafiopratico.service;

import java.util.List;
import java.util.Optional;

import br.com.sctec.desafiopratico.dto.EmpreendimentoRequest;
import br.com.sctec.desafiopratico.dto.EmpreendimentoResponse;
import br.com.sctec.desafiopratico.exception.ResourceNotFoundException;
import br.com.sctec.desafiopratico.model.Empreendimento;
import br.com.sctec.desafiopratico.model.SegmentoAtuacao;
import br.com.sctec.desafiopratico.repository.EmpreendimentoRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para EmpreendimentoService.
 */
@ExtendWith(MockitoExtension.class)
class EmpreendimentoServiceTest {

    @Mock
    private EmpreendimentoRepository repository;

    @InjectMocks
    private EmpreendimentoService service;

    @Test
    @DisplayName("Deve criar empreendimento com sucesso")
    void deveCriar() {
        EmpreendimentoRequest request = new EmpreendimentoRequest(
                "Tech SC", "João", "Blumenau",
                SegmentoAtuacao.TECNOLOGIA, "joao@tech.com", true);

        Empreendimento entitySalvo = criarEntity(1L, "Tech SC", "Blumenau");
        when(repository.save(any(Empreendimento.class))).thenReturn(entitySalvo);

        EmpreendimentoResponse response = service.criar(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.nome()).isEqualTo("Tech SC");
        verify(repository).save(any(Empreendimento.class));
    }

    @Test
    @DisplayName("Deve listar todos os empreendimentos")
    void deveListarTodos() {
        when(repository.findAll()).thenReturn(List.of(
                criarEntity(1L, "Empresa A", "Blumenau"),
                criarEntity(2L, "Empresa B", "Joinville")));

        List<EmpreendimentoResponse> resultado = service.listarTodos();

        assertThat(resultado).hasSize(2);
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Deve buscar por ID existente")
    void deveBuscarPorId() {
        Empreendimento entity = criarEntity(1L, "Tech SC", "Blumenau");
        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        EmpreendimentoResponse response = service.buscarPorId(1L);

        assertThat(response.nome()).isEqualTo("Tech SC");
    }

    @Test
    @DisplayName("Deve lançar exceção para ID inexistente")
    void deveLancarExcecaoIdInexistente() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarPorId(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("999");
    }

    @Test
    @DisplayName("Deve atualizar empreendimento com sucesso")
    void deveAtualizar() {
        Empreendimento entityExistente = criarEntity(1L, "Nome Antigo", "Blumenau");
        when(repository.findById(1L)).thenReturn(Optional.of(entityExistente));
        when(repository.save(any(Empreendimento.class))).thenReturn(entityExistente);

        EmpreendimentoRequest request = new EmpreendimentoRequest(
                "Nome Novo", "Maria", "Joinville",
                SegmentoAtuacao.COMERCIO, "maria@loja.com", false);

        EmpreendimentoResponse response = service.atualizar(1L, request);

        assertThat(response).isNotNull();
        verify(repository).findById(1L);
        verify(repository).save(any(Empreendimento.class));
    }

    @Test
    @DisplayName("Deve excluir empreendimento com sucesso")
    void deveExcluir() {
        Empreendimento entity = criarEntity(1L, "Para Excluir", "Chapecó");
        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        service.excluir(1L);

        verify(repository).delete(entity);
    }

    @Test
    @DisplayName("Deve lançar exceção ao excluir ID inexistente")
    void deveLancarExcecaoAoExcluirInexistente() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.excluir(999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    private Empreendimento criarEntity(Long id, String nome, String municipio) {
        Empreendimento e = new Empreendimento();
        e.setId(id);
        e.setNome(nome);
        e.setEmpreendedor("Empreendedor Teste");
        e.setMunicipio(municipio);
        e.setSegmento(SegmentoAtuacao.TECNOLOGIA);
        e.setEmail("teste@exemplo.com");
        e.setAtivo(true);
        return e;
    }
}
