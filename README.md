# API Inteligente de Controle de Gastos com Spring Boot e Spring AI

Projeto desenvolvido como parte do desafio **"Desenvolvendo sua API Inteligente com Reconhecimento de Fala e Spring Boot"** da Digital Innovation One (DIO).

A aplicação consiste em uma API para gerenciamento de transações financeiras utilizando **Java, Spring Boot, Spring AI, MySQL e Docker**.

Além das funcionalidades básicas de cadastro e consulta de transações, a aplicação possui integração com Inteligência Artificial para reconhecimento de fala, processamento de comandos e geração de respostas em áudio.

## Tecnologias utilizadas

- Java 25
- Spring Boot 4
- Spring AI
- Spring Web
- Spring Data JPA
- Hibernate
- MySQL
- Docker
- Docker Compose
- Gradle
- OpenAI API

## Funcionalidades

A API permite:

- Cadastrar uma nova transação financeira;
- Consultar transações por categoria;
- Calcular o valor total gasto em uma categoria;
- Receber comandos através de arquivos de áudio;
- Converter áudio em texto;
- Processar comandos utilizando Inteligência Artificial;
- Utilizar Tool Calling para executar funcionalidades da aplicação;
- Converter a resposta da IA novamente para áudio.

## Categorias

Atualmente o projeto trabalha com as seguintes categorias:

- `GROCERIES` - compras de mercado;
- `PHARMA` - gastos relacionados a farmácia;
- `AUTO` - gastos relacionados a automóveis.

## Arquitetura

O projeto foi organizado utilizando separação de responsabilidades entre as camadas:

```text
src/main/java/dio/budgeting
│
├── application
│   ├── input
│   ├── output
│   ├── ListTransactionsByCategoryUseCase.java
│   ├── PersistTransactionUseCase.java
│   └── TotalSpentByCategoryUseCase.java
│
├── domain
│   ├── Category.java
│   ├── Transaction.java
│   ├── TransactionId.java
│   └── TransactionRepository.java
│
├── infrastructure
│   ├── http
│   └── persistence
│
└── BudgetingApplication.java
```

## Banco de dados

O projeto utiliza MySQL executado através do Docker.

O arquivo `compose.yml` é responsável pela criação do container do banco de dados.

Para iniciar o banco:

```bash
docker compose up -d
```

Para verificar se o container está funcionando:

```bash
docker compose ps
```

Quando estiver funcionando corretamente, o container deverá apresentar o status `healthy`.

## Configuração da OpenAI API

A aplicação utiliza uma variável de ambiente chamada:

```text
OPENAI_API_KEY
```

No PowerShell, ela pode ser configurada para a sessão atual utilizando:

```powershell
$env:OPENAI_API_KEY="SUA_API_KEY"
```

> A API Key não deve ser adicionada diretamente ao código-fonte ou enviada para o GitHub.

## Executando a aplicação

Com o Docker e o banco de dados em funcionamento, execute:

```powershell
.\gradlew.bat bootRun
```

Quando a aplicação iniciar corretamente, será apresentada uma mensagem semelhante a:

```text
Tomcat started on port 8080
Started BudgetingApplication
```

A API ficará disponível localmente na porta:

```text
http://localhost:8080
```

## Endpoints

### Criar uma transação

```http
POST /transactions
```

Exemplo de corpo:

```json
{
  "description": "Compra no mercado",
  "category": "GROCERIES",
  "amount": 100
}
```

### Consultar transações por categoria

```http
GET /transactions/{category}
```

Exemplo:

```http
GET /transactions/GROCERIES
```

### Consultar total gasto por categoria

```http
GET /transactions/total/{category}
```

Exemplo:

```http
GET /transactions/total/GROCERIES
```

Esse endpoint retorna a soma de todas as transações cadastradas na categoria informada.

### Processamento utilizando Inteligência Artificial

```http
POST /transactions/ai
```

O endpoint recebe um arquivo de áudio através de uma requisição `multipart/form-data`.

O fluxo da funcionalidade é:

```text
Arquivo de áudio
      ↓
Reconhecimento de fala
      ↓
Transcrição do áudio
      ↓
Spring AI / ChatClient
      ↓
Tool Calling
      ↓
Execução do caso de uso
      ↓
Resposta da IA
      ↓
Conversão de texto para áudio
      ↓
Arquivo MP3
```

## Tool Calling

Os casos de uso podem ser disponibilizados ao modelo de Inteligência Artificial através da anotação `@Tool` do Spring AI.

O projeto possui ferramentas para:

```text
persist-transaction
list-transactions-by-category
total-spent-by-category
```

Dessa forma, o modelo pode interpretar uma solicitação do usuário e selecionar a ferramenta adequada para executar uma operação na aplicação.

## Melhoria implementada

Como melhoria em relação ao projeto base, foi implementada uma funcionalidade para **calcular o total gasto por categoria**.

Foi criado o caso de uso:

```text
TotalSpentByCategoryUseCase
```

Ele consulta todas as transações de uma determinada categoria e calcula a soma dos valores.

A funcionalidade também foi registrada como uma ferramenta do Spring AI:

```java
@Tool(
    name = "total-spent-by-category",
    description = "Calcula o valor total gasto em uma categoria"
)
```

Além da utilização através do Tool Calling, foi criado um endpoint REST para permitir que a funcionalidade seja testada diretamente:

```http
GET /transactions/total/{category}
```

Por exemplo:

```http
GET /transactions/total/GROCERIES
```

Essa abordagem permite testar a regra de negócio independentemente da disponibilidade da API de Inteligência Artificial.

## Testes realizados

Durante o desenvolvimento foram realizados testes de:

- Inicialização da aplicação Spring Boot;
- Conexão entre Spring Boot e MySQL;
- Inicialização do MySQL através do Docker;
- Persistência de transações;
- Consulta de transações por categoria;
- Cálculo do total gasto por categoria;
- Envio de arquivo de áudio para o endpoint de IA.

A funcionalidade de cálculo por categoria foi validada diretamente através do endpoint REST.

## Observação sobre a OpenAI API

As funcionalidades de reconhecimento de fala, processamento com modelo de IA e geração de áudio dependem de uma chave válida da OpenAI API e de disponibilidade de créditos/cota na conta utilizada.

Caso a conta não possua cota disponível, a API poderá retornar o erro:

```text
HTTP 429 - insufficient_quota
```

Esse erro está relacionado à disponibilidade da API externa e não à conexão da aplicação com o banco de dados ou às funcionalidades REST locais.

## Autor

Projeto desenvolvido para fins educacionais durante o desafio de Spring Boot e Spring AI da **Digital Innovation One (DIO)**.
