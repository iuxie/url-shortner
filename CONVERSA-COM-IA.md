# Registro de Colaboração com IA — Encurtador de URL

**Modelo utilizado:** Claude Sonnet 5 FREE (Anthropic)

Este documento resume a interação com o modelo de IA ao longo do desenvolvimento do desafio técnico (Encurtador de Links), com foco nas decisões tomadas, no raciocínio por trás delas e no nível de participação da IA em cada etapa. Trechos de código não foram incluídos aqui — o código-fonte final está no próprio repositório.

## 1. Definição de arquitetura e stack

A primeira etapa foi discutir a proposta de projeto antes de qualquer linha de código. Já entrando com uma stack definida (Java, Spring Boot, Spring Data JPA, H2, Lombok, Flyway, Docker, Thymeleaf, Maven), pedi à IA uma análise crítica dessas escolhas e uma sugestão de estrutura em camadas (`controller`, `service`, `repository`, `model`, `dto`, `exception`). A IA levantou um ponto de atenção real sobre concorrência no contador de acessos (`accessCount`), sugerindo desde o início um incremento atômico via query no banco em vez de leitura-e-escrita em memória — decisão que se manteve até o fim do projeto.

Também defini explicitamente que **não** haveria pacote `/config`, mantendo o escopo enxuto para uma API simples.

## 2. Estrutura de pacotes (feature-first)

Ao subir os primeiros arquivos (entidade e migration) para o repositório, ficou claro que a estrutura real era "feature-first" (camadas aninhadas dentro de um pacote `url`), diferente da proposta inicial de camadas soltas na raiz. A IA identificou essa divergência sozinha, comparando o que eu havia descrito com o que já estava commitado, e apresentou as duas opções para eu decidir — optei pela estrutura feature-first, que foi seguida consistentemente no restante do projeto.

## 3. DTOs e Mapper (MapStruct)

Com os DTOs de request/response já implementados por mim, pedi a implementação do `Mapper` usando MapStruct. A IA identificou que o DTO de criação não carrega todos os campos da entidade (id, shortCode, createdAt, accessCount são preenchidos depois, no fluxo de negócio), então esses campos precisam ser explicitamente ignorados no mapeamento — evitando warnings que poderiam virar erros de build caso a política de mapeamento fosse endurecida no futuro.

## 4. Repository e Service

Defini regras específicas para o Service: classe única (sem interface + implementação separada), atributos finais via construtor (sem `@Autowired`), e granularidade de transação (`@Transactional(readOnly = true)` na classe, `@Transactional` só nos métodos de escrita). Ao pedir a implementação, também levantei uma dúvida de design: se o método de redirecionamento deveria ficar no Service ou no Controller. A IA concordou que é regra de negócio e deve ficar no Service — decisão consistente com a separação de responsabilidades que vínhamos seguindo.

Nessa etapa também identificamos que o `Repository`, do jeito que estava, não suportava busca por `shortCode` nem o incremento atômico de acessos — foram adicionados os métodos necessários (busca por código, verificação de existência e o incremento atômico via query), mantendo a decisão de concorrência definida lá no início.

## 5. Tratamento de erros

Pedi um `@RestControllerAdvice` usando `ProblemDetail` (padrão RFC 7807) para centralizar o tratamento de exceções, cobrindo especificamente: URL não encontrada, falha de validação de campos e um fallback genérico para erros inesperados — sem vazar detalhes internos (stack trace) para quem consome a API.

## 6. Controllers da API

Desenvolvi um protótipo inicial do controller REST e pedi para completá-lo. A IA identificou dois problemas no protótipo: métodos de handler declarados como `private` (o Spring exige que sejam públicos) e a ausência do endpoint de criação. Também apontou uma decisão de design importante que eu não tinha explicitado: a rota de redirecionamento não pode compartilhar o prefixo `/api/urls`, senão o link curto deixaria de ser curto — por isso ela precisa estar em um controller separado, mapeado na raiz.

## 7. Interface front-end (Thymeleaf)

Defini o design básico da página (título, campo de texto, botão com cor específica, painel de estatísticas). A implementação completa — HTML, CSS e a lógica de JavaScript para consumir a API — foi majoritariamente conduzida pela IA, dado que meu foco de desenvolvimento é backend. Durante os testes locais, identifiquei um bug de caminho dos arquivos CSS (não carregavam) e reportei o comportamento observado; a IA localizou a causa comparando a estrutura de pastas real com o caminho referenciado no HTML.

Também identifiquei, ao testar a aplicação de ponta a ponta, que o contador de acessos não atualizava em tempo real na interface, e que o banco H2 perdia os dados a cada reinicialização — mesmo após eu mesmo tentar configurar parâmetros de conexão do H2 para tentar resolver. Reportei exatamente o que já tinha tentado. A IA explicou que os parâmetros que usei não controlam persistência entre reinicializações (isso depende do protocolo da URL JDBC, não da configuração de conexão), e propôs tanto a correção de persistência quanto um mecanismo de atualização periódica no front-end para refletir os acessos sem recarregar a página.

## 8. Documentação da API (Swagger/OpenAPI)

Com a estrutura da API estável, pedi a inclusão do springdoc-openapi. Como o projeto já estava numa versão recente do Spring Boot, foi necessário confirmar a versão da biblioteca compatível com essa versão do framework antes de declarar a dependência. Na sequência, pedi a documentação de DTOs e Controllers — decisão adicional identificada durante essa etapa foi esconder da documentação o controller que apenas serve a página HTML, já que ele não é um endpoint de API.

## 9. Dockerização

Assim como o front-end, a criação do `Dockerfile` e do `docker-compose.yml` foi majoritariamente conduzida pela IA, por não ser uma área que eu tenha tanta familiaridade. A decisão mais relevante aqui foi usar um volume Docker para o diretório de dados do H2, resolvendo de forma definitiva o problema de persistência identificado na etapa de testes do front-end, e usando um caminho absoluto (evitando o mesmo tipo de ambiguidade que causou o bug anterior).

## 10. Documentação final (README)

Por fim, pedi a consolidação de tudo em um `README.md`, cobrindo stack, arquitetura, rotas, decisões de escopo (o que ficou de fora e por quê) e instruções de execução local e via Docker.

---

## Observações sobre nível de participação da IA

- **Backend (arquitetura, entidade, DTOs, mapper, repository, service, tratamento de erros, controllers, documentação da API):** conduzido por mim, com a IA atuando como par de desenvolvimento — implementando conforme as regras que eu definia, revisando o que eu já tinha commitado e apontando inconsistências ou problemas reais (concorrência, estrutura de pacotes, rotas conflitantes, bug de persistência).
- **Front-end (Thymeleaf/CSS/JS):** desenvolvido cerca de **80% pela IA**. Tenho noção de front-end, mas meu foco de desenvolvimento sempre foi backend, então optei por delegar a maior parte da implementação, revisando e testando o resultado.
- **Dockerização:** mesma situação do front-end — desenvolvido majoritariamente pela IA, dado que não é uma área que eu domino tanto quanto backend Java.