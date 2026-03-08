# Empreendimentos SC — API REST

Desafio prático de software para o processo de seleção **IA para DEVs** da SCTEC.

API REST desenvolvida em **Java 21 com Spring Boot 3.4.1** para gerenciamento de empreendimentos de Santa Catarina (SC).

## 📋 Objetivo

Criar uma aplicação backend que permita operações CRUD completas para gerenciar empreendimentos registrados em diferentes municípios catarinenses, com segmentação de mercado e validações de dados.

## 🛠️ Stack Tecnológico

| Tecnologia | Versão | Propósito |
|-----------|--------|----------|
| Java | 21 LTS | Linguagem |
| Spring Boot | 3.4.1 | Framework REST |
| Spring Data JPA | 3.2.2 | Persistência |
| H2 Database | 2.2.224 | Banco de dados |
| Jakarta Validation | 3.0.2 | Validações |
| JUnit 5 | 5.9.3 | Testes unitários |
| Mockito | 5.6.1 | Testes com mocks |
| Maven | 3.8.1+ | Build automation |

## 📁 Estrutura do Projeto

```
src/
├── main/java/br/com/sctec/desafiopratico/
│   ├── model/
│   │   ├── Empreendimento.java       (Entidade JPA com timestamps)
│   │   └── SegmentoAtuacao.java      (Enum: TECNOLOGIA, COMERCIO, INDUSTRIA, SERVICOS, AGRONEGOCIO)
│   ├── dto/
│   │   ├── EmpreendimentoRequest.java (Input DTO com validações Jakarta)
│   │   └── EmpreendimentoResponse.java (Output DTO)
│   ├── controller/
│   │   └── EmpreendimentoController.java (Endpoints REST /api/empreendimentos)
│   ├── service/
│   │   └── EmpreendimentoService.java (Lógica de negócio CRUD)
│   ├── repository/
│   │   └── EmpreendimentoRepository.java (JpaRepository com consultas derivadas)
│   ├── exception/
│   │   ├── ResourceNotFoundException.java (Exceção customizada)
│   │   └── GlobalExceptionHandler.java (@RestControllerAdvice)
│   └── EmpreendimentosScApplication.java (@SpringBootApplication)
├── test/java/br/com/sctec/desafiopratico/
│   ├── controller/EmpreendimentoControllerTest.java (13 testes de integração)
│   ├── service/EmpreendimentoServiceTest.java (7 testes unitários)
│   └── EmpreendimentosScApplicationTests.java (1 smoke test)
└── resources/
    ├── application.properties (H2 file-based: ./data/empreendimentos)
    └── application-test.properties (H2 in-memory: mem:testdb)
```

## 🚀 Como Executar

### Pré-requisitos
- Java 21 (Eclipse Adoptium recomendado)
- Maven 3.8.1+

### Build e Testes
```bash
# Compilar e rodar 21 testes
mvn test

# Build com execução de testes
mvn clean package

# Apenas compilar (sem testes)
mvn clean compile
```

### Executar a Aplicação
```bash
mvn spring-boot:run
# OU
java -jar target/empreendimentos-sc-api-1.0.0.jar
```

Aplicação rodará em `http://localhost:8080`

## 📡 Endpoints da API

### Headers obrigatórios
```
Content-Type: application/json
```

### POST /api/empreendimentos — Criar empreendimento

**Request:**
```bash
curl -X POST http://localhost:8080/api/empreendimentos \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Tech Blumenau",
    "empreendedor": "João Silva",
    "municipio": "Blumenau",
    "segmento": "TECNOLOGIA",
    "email": "joao@techblumenau.com.br",
    "ativo": true
  }'
```

**Response:** `201 Created`
```json
{
    "id": 1,
    "nome": "Tech Blumenau",
    "empreendedor": "João Silva",
    "municipio": "Blumenau",
    "segmento": "TECNOLOGIA",
    "email": "joao@techblumenau.com.br",
    "ativo": true,
    "criadoEm": "2026-03-08T17:30:00",
    "atualizadoEm": "2026-03-08T17:30:00"
}
```

### GET /api/empreendimentos — Listar todos (com filtros opcionais)

**Request:**
```bash
curl http://localhost:8080/api/empreendimentos
curl "http://localhost:8080/api/empreendimentos?municipio=Blumenau&segmento=TECNOLOGIA&ativo=true"
```

**Parâmetros de filtro:**

| Parâmetro | Exemplo | Descrição |
|-----------|---------|-----------|
| `municipio` | `?municipio=Blumenau` | Filtra por município (case-insensitive) |
| `segmento` | `?segmento=TECNOLOGIA` | Filtra por segmento (TECNOLOGIA, COMERCIO, INDUSTRIA, SERVICOS, AGRONEGOCIO) |
| `ativo` | `?ativo=true` | Filtra por status (true/false) |
| `nome` | `?nome=Tech` | Filtra por nome (contém, case-insensitive) |

**Response:** `200 OK`
```json
[
    {
        "id": 1,
        "nome": "Tech Blumenau",
        "empreendedor": "João Silva",
        "municipio": "Blumenau",
        "segmento": "TECNOLOGIA",
        "email": "joao@techblumenau.com.br",
        "ativo": true,
        "criadoEm": "2026-03-08T17:30:00",
        "atualizadoEm": "2026-03-08T17:30:00"
    }
]
```

### GET /api/empreendimentos/{id} — Buscar por ID

**Request:**
```bash
curl http://localhost:8080/api/empreendimentos/1
```

**Response:** `200 OK` (ou `404 Not Found`)

### PUT /api/empreendimentos/{id} — Atualizar

**Request:**
```bash
curl -X PUT http://localhost:8080/api/empreendimentos/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Tech Blumenau Atualizada",
    "empreendedor": "João Silva",
    "municipio": "Blumenau",
    "segmento": "SERVICOS",
    "email": "contato@techblumenau.com.br",
    "ativo": true
  }'
```

**Response:** `200 OK`

### DELETE /api/empreendimentos/{id} — Remover

**Request:**
```bash
curl -X DELETE http://localhost:8080/api/empreendimentos/1
```

**Response:** `204 No Content`

## ✅ Testes

Projeto inclui:
- **13 testes de integração** (EmpreendimentoControllerTest) — validam endpoints REST completos
- **7 testes unitários** (EmpreendimentoServiceTest) — validam lógica de negócio com Mockito
- **1 smoke test** (EmpreendimentosScApplicationTests) — valida contexto Spring

**Cobertura:** CRUD completo, validações, tratamento de erros (404, 400), filtros

```bash
mvn test
# Result: 21 passed, 0 failed
```

## 🔄 Versionamento Git

**6 branches de desenvolvimento:**
1. `feature/modelo-dados` — Entidade, Enum, Repository
2. `feature/camada-servico` — Service + Exceção customizada
3. `feature/api-rest` — Controller, DTOs, Exception Handler
4. `feature/testes` — Testes unitários e de integração
5. `docs/documentacao` — Documentação do projeto

**12 commits distintos + 5 merge commits = 17 commits totais**

## 📝 Validações

- **Nome:** Obrigatório, mínimo 3 caracteres
- **Empreendedor:** Obrigatório, mínimo 3 caracteres
- **Email:** Obrigatório, formato válido
- **Município:** Obrigatório
- **Segmento:** Obrigatório (enum)
- **Ativo:** Obrigatório (boolean)

## 🎯 Próximas Melhorias

- [ ] Autenticação e autorização (JWT)
- [ ] Paginação de resultados
- [ ] Soft delete (status de exclusão lógica)
- [ ] Auditoria de mudanças
- [ ] Cache de consultas frequentes
- [ ] Documentação OpenAPI/Swagger

## 📄 Licença

Projeto desenvolvido para fins educacionais — SCTEC "IA para DEVs" 2026.

## 👤 Autor

Jordan Laus — https://github.com/jlausbr/desafio-empreendimentos-sc
