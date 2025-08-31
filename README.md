# **ZenTasks API**

## **Visão Geral**

A ZenTasks API é uma aplicação de gerenciamento de tarefas, desenvolvida com foco em arquitetura limpa e design modular. A API permite que os usuários gerenciem suas tarefas de forma eficiente, utilizando a Matriz de Eisenhower para priorização.

## **Arquitetura**

O projeto segue uma arquitetura de **Monólito Modular** com princípios de **Domain-Driven Design (DDD)**, **Clean Architecture** e um modelo de **Domínio Rico**.

### **Monólito Modular**

A aplicação é estruturada em módulos independentes: tasks e users. Cada módulo é responsável por uma área de negócio específica, o que facilita a manutenção, o desenvolvimento e a escalabilidade do sistema, permitindo que cada domínio evolua com baixo acoplamento.

### **Domain-Driven Design (DDD) & Domínio Rico**

O coração da aplicação é o seu domínio. Cada módulo possui um diretório Domain que contém as entidades, enums e a lógica de negócio. As entidades são ricas em comportamento (**Domínio Rico**), encapsulando as regras de negócio, validações e transições de estado, o que garante a consistência e a integridade dos dados diretamente no núcleo do modelo.

### **Clean Architecture**

A separação de responsabilidades é um pilar fundamental do projeto. Cada módulo é dividido em três camadas principais, seguindo os princípios da Clean Architecture para garantir a independência do domínio em relação a detalhes de infraestrutura:

* **Application**: Orquestra os casos de uso da aplicação. É responsável por receber as requisições, chamar os serviços de domínio e retornar as respostas.  
* **Domain**: Contém a lógica de negócio e as entidades. É a camada mais interna e não depende de nenhuma outra camada.  
* **Infrastructure**: Lida com as preocupações externas, como acesso a banco de dados, provedores de e-mail e segurança.

## **Funcionalidades**

* **Gerenciamento de Usuários**:  
  * Registro de novos usuários (POST /v1/register)  
  * Autenticação de usuários (POST /v1/login)  
* **Gerenciamento de Tarefas**:  
  * Criação de novas tarefas (POST /v1/tasks)  
  * Priorização com a Matriz de Eisenhower: As tarefas são classificadas como urgentes e/ou importantes para determinar o quadrante da matriz (Faça Agora, Agende, Delegue, Elimine).

## **Como Executar via Docker**

Para executar a aplicação, bem como o banco de dados, de forma simples e rápida, utilize o Docker Compose.

### **Pré-requisitos**

* Docker  
* Docker Compose

### **Passos**

1. **Clone o repositório:**  
   git clone \<URL\_DO\_REPOSITORIO\>  
   cd zen-task-api

2. Configure as Variáveis de Ambiente:  
   Crie os arquivos .env e .env.db na raiz do projeto zen-task-api com as seguintes configurações de exemplo:  
```text
   **.env**:  
   SPRING\_PROFILES\_ACTIVE=dev  
   JWT\_SECRET=seu\_super\_secreto\_jwt  
   ISSUER=SEU\_ISSUER  
   EXPIRATION\_HOURS=24  
   DB\_URL=SEU\_DB\_URL  
   DB\_USERNAME=SEU\_USERNAME  
   DB\_PASSWORD=SEU\_PASSWORD  
   **.env.db**:  
   POSTGRES\_DB=SEU\_DB  
   POSTGRES\_USER=SEU\_USER  
   POSTGRES\_PASSWORD=SEU\_PASSWORD
```

4. **Execute a Aplicação com Docker Compose:**  `
```bash
   docker-compose up \-d \--build
```

   A API estará disponível em http://localhost:8080.
