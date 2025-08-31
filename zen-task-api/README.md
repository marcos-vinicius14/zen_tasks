# ZenTasks API

## Visão Geral

A ZenTasks API é uma aplicação para gerenciamento de tarefas, desenvolvida com foco em arquitetura limpa e design modular. A API permite que os usuários gerenciem suas tarefas de forma eficiente, utilizando a Matriz de Eisenhower para priorização.

## Arquitetura

O projeto segue uma arquitetura de **Monólito Modular** com princípios de **Domain-Driven Design (DDD)** e **Clean Architecture**.

### Monólito Modular

A aplicação é estruturada em módulos independentes: `tasks` e `users`. Cada módulo é responsável por uma área de negócio específica, o que facilita a manutenção, o desenvolvimento e a escalabilidade do sistema.

### Domain-Driven Design (DDD)

O coração da aplicação é o seu domínio. Cada módulo possui um diretório `Domain` que contém as entidades, enums e a lógica de negócio. As entidades são ricas em comportamento (Domínio Rico), encapsulando as regras de negócio e garantindo a consistência dos dados.

### Clean Architecture

A separação de responsabilidades é um pilar fundamental do projeto. Cada módulo é dividido em três camadas principais:

- **Application**: Orquestra os casos de uso da aplicação. É responsável por receber as requisições, chamar os serviços de domínio e retornar as respostas.
- **Domain**: Contém a lógica de negócio e as entidades. É a camada mais interna e não depende de nenhuma outra camada.
- **Infrastructure**: Lida com as preocupações externas, como acesso a banco de dados e segurança.

## Funcionalidades

- **Gerenciamento de Usuários**:
    - Registro de novos usuários (`POST /v1/register`)
    - Autenticação de usuários (`POST /v1/login`)
- **Gerenciamento de Tarefas**:
    - Criação de novas tarefas (`POST /v1/tasks`)
    - Priorização com a Matriz de Eisenhower: As tarefas são classificadas como urgentes e/ou importantes para determinar o quadrante da matriz (Faça Agora, Agende, Delegue, Elimine).

## Testes

O projeto possui testes unitários para garantir a qualidade e o correto funcionamento da lógica de negócio. Os testes existentes cobrem a camada de serviço do módulo de tarefas (`TaskService`).

## Como Executar

### Pré-requisitos

- Java 17
- Maven

### Executando a Aplicação

1. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/zen-task-api.git
   ```
2. Navegue até o diretório do projeto:
   ```bash
   cd zen-task-api
   ```
3. Execute a aplicação com o Maven:
   ```bash
   ./mvnw spring-boot:run
   ```

### Executando os Testes

Para executar os testes, utilize o seguinte comando Maven:

```bash
./mvnw test
```
