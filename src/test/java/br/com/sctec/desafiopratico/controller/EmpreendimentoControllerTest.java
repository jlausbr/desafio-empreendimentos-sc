package br.com.sctec.desafiopratico.controller;

import br.com.sctec.desafiopratico.model.Empreendimento;
import br.com.sctec.desafiopratico.model.SegmentoAtuacao;
import br.com.sctec.desafiopratico.repository.EmpreendimentoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integração para o EmpreendimentoController.
 * Utiliza banco H2 em memória configurado em application.properties de teste.
 */
@SpringBootTest
@AutoConfigureMockMvc
class EmpreendimentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmpreendimentoRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASE_URL = "/api/empreendimentos";

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    // ==================== CREATE ====================

    @Test
    @DisplayName("POST - Deve criar um empreendimento com sucesso")
    void deveCriarEmpreendimento() throws Exception {
        String json = """
                {
                    "nome": "Tech Blumenau",
                    "empreendedor": "João Silva",
                    "municipio": "Blumenau",
                    "segmento": "TECNOLOGIA",
                    "email": "joao@techblumenau.com.br",
                    "ativo": true
                }
                """;

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nome").value("Tech Blumenau"))
                .andExpect(jsonPath("$.empreendedor").value("João Silva"))
                .andExpect(jsonPath("$.municipio").value("Blumenau"))
                .andExpect(jsonPath("$.segmento").value("TECNOLOGIA"))
                .andExpect(jsonPath("$.email").value("joao@techblumenau.com.br"))
                .andExpect(jsonPath("$.ativo").value(true))
                .andExpect(jsonPath("$.criadoEm").isNotEmpty());
    }

    @Test
    @DisplayName("POST - Deve retornar 400 quando campos obrigatórios estão ausentes")
    void deveRetornar400CamposAusentes() throws Exception {
        String json = """
                {
                    "nome": "",
                    "email": "invalido"
                }
                """;

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes").isArray());
    }

    @Test
    @DisplayName("POST - Deve retornar 400 quando e-mail é inválido")
    void deveRetornar400EmailInvalido() throws Exception {
        String json = """
                {
                    "nome": "Teste",
                    "empreendedor": "Maria",
                    "municipio": "Joinville",
                    "segmento": "COMERCIO",
                    "email": "email-invalido",
                    "ativo": true
                }
                """;

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    // ==================== READ ====================

    @Test
    @DisplayName("GET - Deve listar todos os empreendimentos")
    void deveListarTodos() throws Exception {
        criarEmpreendimentoNoBanco("Empresa A", "Blumenau", SegmentoAtuacao.TECNOLOGIA);
        criarEmpreendimentoNoBanco("Empresa B", "Joinville", SegmentoAtuacao.COMERCIO);

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("GET - Deve buscar empreendimento por ID")
    void deveBuscarPorId() throws Exception {
        Empreendimento salvo = criarEmpreendimentoNoBanco(
                "Tech SC", "Blumenau", SegmentoAtuacao.TECNOLOGIA);

        mockMvc.perform(get(BASE_URL + "/" + salvo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Tech SC"))
                .andExpect(jsonPath("$.municipio").value("Blumenau"));
    }

    @Test
    @DisplayName("GET - Deve retornar 404 para ID inexistente")
    void deveRetornar404IdInexistente() throws Exception {
        mockMvc.perform(get(BASE_URL + "/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.erro").isNotEmpty());
    }

    @Test
    @DisplayName("GET - Deve filtrar por município")
    void deveFiltrarPorMunicipio() throws Exception {
        criarEmpreendimentoNoBanco("Empresa A", "Blumenau", SegmentoAtuacao.TECNOLOGIA);
        criarEmpreendimentoNoBanco("Empresa B", "Joinville", SegmentoAtuacao.COMERCIO);

        mockMvc.perform(get(BASE_URL + "?municipio=Blumenau"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].municipio").value("Blumenau"));
    }

    @Test
    @DisplayName("GET - Deve filtrar por segmento")
    void deveFiltrarPorSegmento() throws Exception {
        criarEmpreendimentoNoBanco("Empresa A", "Blumenau", SegmentoAtuacao.TECNOLOGIA);
        criarEmpreendimentoNoBanco("Empresa B", "Joinville", SegmentoAtuacao.COMERCIO);

        mockMvc.perform(get(BASE_URL + "?segmento=TECNOLOGIA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].segmento").value("TECNOLOGIA"));
    }

    @Test
    @DisplayName("GET - Deve filtrar por nome (busca parcial)")
    void deveFiltrarPorNome() throws Exception {
        criarEmpreendimentoNoBanco("Tech Blumenau", "Blumenau", SegmentoAtuacao.TECNOLOGIA);
        criarEmpreendimentoNoBanco("Comércio Central", "Joinville", SegmentoAtuacao.COMERCIO);

        mockMvc.perform(get(BASE_URL + "?nome=tech"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nome").value("Tech Blumenau"));
    }

    // ==================== UPDATE ====================

    @Test
    @DisplayName("PUT - Deve atualizar um empreendimento")
    void deveAtualizarEmpreendimento() throws Exception {
        Empreendimento salvo = criarEmpreendimentoNoBanco(
                "Nome Original", "Blumenau", SegmentoAtuacao.TECNOLOGIA);

        String json = """
                {
                    "nome": "Nome Atualizado",
                    "empreendedor": "Maria Santos",
                    "municipio": "Joinville",
                    "segmento": "COMERCIO",
                    "email": "maria@empresa.com.br",
                    "ativo": false
                }
                """;

        mockMvc.perform(put(BASE_URL + "/" + salvo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Nome Atualizado"))
                .andExpect(jsonPath("$.municipio").value("Joinville"))
                .andExpect(jsonPath("$.ativo").value(false));
    }

    @Test
    @DisplayName("PUT - Deve retornar 404 ao atualizar ID inexistente")
    void deveRetornar404AoAtualizarInexistente() throws Exception {
        String json = """
                {
                    "nome": "Teste",
                    "empreendedor": "Teste",
                    "municipio": "Teste",
                    "segmento": "TECNOLOGIA",
                    "email": "teste@teste.com",
                    "ativo": true
                }
                """;

        mockMvc.perform(put(BASE_URL + "/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

    // ==================== DELETE ====================

    @Test
    @DisplayName("DELETE - Deve excluir um empreendimento")
    void deveExcluirEmpreendimento() throws Exception {
        Empreendimento salvo = criarEmpreendimentoNoBanco(
                "Para Excluir", "Chapecó", SegmentoAtuacao.AGRONEGOCIO);

        mockMvc.perform(delete(BASE_URL + "/" + salvo.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(BASE_URL + "/" + salvo.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE - Deve retornar 404 ao excluir ID inexistente")
    void deveRetornar404AoExcluirInexistente() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/999"))
                .andExpect(status().isNotFound());
    }

    // ==================== Helper ====================

    private Empreendimento criarEmpreendimentoNoBanco(
            String nome, String municipio, SegmentoAtuacao segmento) {
        Empreendimento e = new Empreendimento();
        e.setNome(nome);
        e.setEmpreendedor("Empreendedor Teste");
        e.setMunicipio(municipio);
        e.setSegmento(segmento);
        e.setEmail("teste@exemplo.com.br");
        e.setAtivo(true);
        return repository.save(e);
    }
}
