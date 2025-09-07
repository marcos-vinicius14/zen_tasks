# 🎯 ZenTasks - Complete Setup Instructions

This guide shows how to run the complete ZenTasks application stack.

## 🚀 Quick Start (Recommended)

### Prerequisites
- Docker & Docker Compose installed
- Git

### 1. Clone and Setup Environment

```bash
# Clone the repository
git clone <repository-url>
cd zen_tasks

# Create environment files for the API
cd zen-task-api
cp .env.example .env
cp .env.db.example .env.db
cd ..
```

### 2. Start All Services

```bash
# From the root directory
docker-compose up --build
```

This command will:
- Start PostgreSQL database on port 5432
- Build and start the Spring Boot API on port 8080  
- Build and start the React frontend on port 3000

### 3. Access the Application

- **Frontend (React)**: http://localhost:3000
- **API (Spring Boot)**: http://localhost:8080
- **Database**: localhost:5432

### 4. Stop Services

```bash
docker-compose down
```

## 🛠️ Development Mode

### Backend Development
```bash
cd zen-task-api
./mvnw spring-boot:run
```

### Frontend Development
```bash
cd zen-task-frontend
npm install
npm start
```

## 📊 Project Architecture

```
zen_tasks/
├── zen-task-api/          # Spring Boot REST API
│   ├── src/               # Java source (Clean Architecture)
│   ├── Dockerfile         # API container
│   └── docker-compose.yml # API + DB services
├── zen-task-frontend/     # React TypeScript app
│   ├── src/               # React source (Clean Architecture)
│   │   ├── components/    # UI components
│   │   ├── pages/         # Route pages
│   │   ├── hooks/         # React Query hooks
│   │   ├── services/      # API & auth services
│   │   └── types/         # TypeScript definitions
│   ├── Dockerfile         # Frontend container (Node + Nginx)
│   └── README.md          # Frontend docs
└── docker-compose.yml    # Full stack orchestration
```

## 🔧 Technology Stack

### Backend
- **Java 17** with Spring Boot
- **PostgreSQL** database
- **JWT** authentication
- **Clean Architecture** with DDD
- **Docker** containerization

### Frontend  
- **React 19** with TypeScript
- **TanStack Query** for server state
- **React Router** for navigation
- **Bun** package manager
- **ESLint & Prettier** code quality
- **Docker** with Nginx serving

## 🌟 Features

- ✅ Task management with Eisenhower Matrix
- ✅ User authentication and registration  
- ✅ Clean Architecture on both backend and frontend
- ✅ Containerized development and deployment
- ✅ Type-safe API integration
- ✅ Real-time data synchronization
- ✅ Production-ready builds

## 📝 API Endpoints

- `POST /v1/register` - User registration
- `POST /v1/login` - User authentication  
- `GET /v1/tasks` - List user tasks
- `POST /v1/tasks` - Create new task
- `PUT /v1/tasks/{id}` - Update task
- `DELETE /v1/tasks/{id}` - Delete task

## 🔐 Environment Configuration

The application uses environment variables for configuration:

### API (.env)
```env
SPRING_PROFILES_ACTIVE=dev
JWT_SECRET=your-secret-key
DB_URL=jdbc:postgresql://db:5432/zentasks
DB_USERNAME=zentasks_user
DB_PASSWORD=zentasks_password
```

### Frontend
```env
REACT_APP_API_URL=http://localhost:8080
```

## 🚀 Production Deployment

The application is fully containerized and ready for production deployment with Docker Compose or Kubernetes.

Both frontend and backend containers are optimized for production with:
- Multi-stage builds for minimal image size
- Static file serving with Nginx
- Environment-based configuration
- Health checks and proper networking