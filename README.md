# Encurtador de URL — UrlShortner

Projeto desenvolvido como desafio técnico para a vaga de **Estágio em Desenvolvimento** na HSP Software. A opção escolhida foi o **Encurtador de Links**: uma API para encurtar URLs, contabilizar acessos e uma interface simples para gerar e acompanhar os links.

## Stack utilizada

- **Java 21**
- **Spring Boot 4.0.8**
- **Spring Data JPA** (Hibernate)
- **Flyway** — versionamento do schema do banco
- **H2 Database** — persistido em arquivo (não em memória), para sobreviver a reinicializações
- **MapStruct** — mapeamento entre entidade e DTOs
- **Lombok**
- **Bean Validation** (`jakarta.validation`)
- **Thymeleaf** — interface gráfica
- **springdoc-openapi** — documentação da API (Swagger UI)
- **Maven**
- **Docker / Docker Compose**

## Arquitetura

O projeto segue uma arquitetura em camadas organizada por feature. Como só existe uma feature (`url`), o pacote raiz da aplicação concentra apenas o que é transversal ao projeto inteiro:

```
com.iuredev.UrlShortner
├── OpenApiConfig.java          # metadados globais do Swagger
├── UrlShortnerApplication.java
└── url/
    ├── controller/
    │   ├── UrlController.java          # API REST (/api/urls)
    │   ├── UrlRedirectController.java  # redirecionamento (/{shortCode})
    │   └── UrlViewController.java      # serve a interface Thymeleaf (/)
    ├── dto/
    │   ├── request/UrlCreateRequestDTO.java
    │   └── response/
    │       ├── UrlResponseDTO.java
    │       └── UrlStatsResponseDTO.java
    ├── exception/
    │   ├── GlobalExceptionHandler.java  # @RestControllerAdvice com ProblemDetail
    │   └── UrlNotFoundException.java
    ├── mapper/UrlMapper.java            # MapStruct
    ├── model/UrlModel.java
    ├── repository/UrlRepository.java
    └── service/UrlService.java
```

## Funcionalidades

- **Criar link curto** a partir de uma URL original, com validação de entrada.
- **Listar** todos os links já criados.
- **Consultar estatísticas** de um link (URL original, data de criação, quantidade de acessos).
- **Redirecionar** (`302`) para a URL original ao acessar o link curto, incrementando o contador de acessos de forma atômica no banco.
- **Tratamento de erros centralizado**, com respostas no formato `ProblemDetail` (RFC 7807).
- **Interface web** simples (Thymeleaf + JS puro) para gerar links e acompanhar estatísticas, com atualização periódica do contador de acessos.
- **Documentação interativa** da API via Swagger UI.

## O que ficou de fora (e por quê)

Priorizei a qualidade e a organização do escopo essencial em vez de tentar cobrir 100% de funcionalidades extras, dado o prazo do teste:

- **Rota de exclusão (`DELETE`)**: não fazia parte do escopo mínimo definido para o desafio.
- **Paginação na listagem**: a listagem atual (`GET /api/urls`) retorna todos os registros; com poucos dados de teste isso não é um problema, mas numa base maior seria necessário paginar.
- **Responsividade completa da interface**: a página funciona bem em desktop; ajustes para telas pequenas (mobile) ficaram como próximo passo identificado, mas não implementado.
- **Geração de `shortCode` por hash/base62 do ID**: optei por geração aleatória (Base62, 7 caracteres) com checagem de unicidade, por ser mais simples de implementar sem exigir dois `save()` no banco.

## Rotas da API

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/api/urls` | Lista todos os links criados |
| `POST` | `/api/urls` | Cria um novo link curto |
| `GET` | `/api/urls/{shortCode}/stats` | Retorna estatísticas de um link |
| `GET` | `/{shortCode}` | Redireciona para a URL original (`302`) e incrementa o acesso |

A documentação completa e interativa (schemas, exemplos, respostas de erro) está disponível via Swagger UI — veja abaixo como acessar.

## Como rodar localmente (sem Docker)

### Pré-requisitos
- JDK 21
- Maven (ou usar o wrapper `./mvnw` incluso)

### Configuração

Crie um arquivo `.env` na raiz do projeto (baseado no `.env.example`) com o banco H2 em modo arquivo:

```
DATABASE_URL=jdbc:h2:file:./data/urlshortner;DB_CLOSE_ON_EXIT=FALSE
DATABASE_USERNAME=sa
DATABASE_PASSWORD=
```

> Rode a aplicação sempre a partir da raiz do projeto — o caminho do banco é relativo a esse diretório.

Exporte as variáveis do `.env` no seu terminal (ou configure-as na sua IDE) e execute:

```bash
./mvnw spring-boot:run
```

### Acessando

- Interface web: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- Console do H2 (debug do banco): `http://localhost:8080/h2-console`

## Como rodar com Docker

```bash
docker compose up --build
```

O `docker-compose.yml` já monta um volume (`./data:/app/data`), então os dados do H2 persistem entre execuções (`docker compose down` seguido de `docker compose up` não apaga o banco).

Acesse normalmente em `http://localhost:8080`.

## Sobre o uso de IA no desenvolvimento

Este projeto foi desenvolvido com apoio de uma ferramenta de IA (Claude, da Anthropic) ao longo de todas as etapas — desde a definição da arquitetura até a dockerização. O histórico completo dessa interação está anexado junto com esta entrega, conforme solicitado no desafio.