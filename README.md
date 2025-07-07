# Zen Task API

Zen Task API is a powerful and intuitive task management solution designed to help you prioritize your tasks effectively using the Eisenhower Matrix. By categorizing tasks based on urgency and importance, you can focus on what truly matters, enhancing your productivity and reducing stress.

## The Eisenhower Matrix

The Eisenhower Matrix is a productivity tool that helps you organize and prioritize tasks by urgency and importance. Tasks are divided into four quadrants:

1.  **Urgent & Important (Do First):** Critical tasks that need immediate attention.
2.  **Important & Not Urgent (Schedule):** Tasks that are important for long-term goals but don't have a pressing deadline.
3.  **Urgent & Not Important (Delegate):** Tasks that need to be done now but don't require your specific skills.
4.  **Not Urgent & Not Important (Delete):** Distractions that should be avoided.

## Features

*   RESTful API for managing users and tasks.
*   Task prioritization based on the Eisenhower Matrix.
*   Secure endpoints with Spring Security.
*   Database migrations managed with Liquibase.
*   Containerized for easy setup and deployment with Docker.

## Technologies Used

*   **Backend:** Java 21, Spring Boot 3
*   **Database:** PostgreSQL
*   **Build Tool:** Maven
*   **Database Migrations:** Liquibase
*   **Containerization:** Docker, Docker Compose

## Prerequisites

Before you begin, ensure you have the following installed:

*   Java 21
*   Apache Maven
*   Docker
*   Docker Compose

## Getting Started

Follow these steps to get the application up and running.

### 1. Clone the repository

```bash
git clone <repository-url>
cd zen-task-api
```

### 2. Configure Environment Variables

The application uses `.env` files to manage environment variables. You will need to create two files at the root of the project:

**`.env`** (for the application)
```properties
# Example:
SPRING_PROFILES_ACTIVE=dev
```

**`.env.db`** (for the database)
```properties
# Example:
POSTGRES_DB=zentasks
POSTGRES_USER=admin
POSTGRES_PASSWORD=secret
```

### 3. Run the Application

The simplest way to run the application and the database is by using Docker Compose:

```bash
docker-compose up -d --build
```

The API will be available at `http://localhost:8080`.

### 4. Running without Docker

You can also run the application directly using Maven. Make sure you have a PostgreSQL instance running and have configured the connection details in `src/main/resources/application-dev.yml`.

```bash
mvn spring-boot:run
```

## Building the Project

To build the project into a JAR file, run the following command:

```bash
mvn clean package
```

The packaged JAR will be located in the `target/` directory.

## API Endpoints

*(This section is a placeholder. You should document your actual API endpoints here.)*

The API provides endpoints for managing users and tasks.

### Tasks

*   `GET /api/tasks` - Get all tasks.
*   `GET /api/tasks/{id}` - Get a task by ID.
*   `POST /api/tasks` - Create a new task.
*   `PUT /api/tasks/{id}` - Update an existing task.
*   `DELETE /api/tasks/{id}` - Delete a task.

### Users

*   `POST /api/users/register` - Register a new user.
*   `POST /api/users/login` - Authenticate a user.
