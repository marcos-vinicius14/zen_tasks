# ZenTasks Frontend

Frontend application for the ZenTasks project built with React, TypeScript, and TanStack Query.

## 🚀 Tech Stack

- **React 19** - UI Library
- **TypeScript** - Type Safety
- **TanStack Query** - Server State Management  
- **React Router** - Client-side Routing
- **Bun** - Package Manager and Runtime
- **ESLint & Prettier** - Code Quality
- **Husky** - Git Hooks
- **Docker** - Containerization

## 📁 Project Structure

```
src/
├── components/          # Reusable UI components
│   ├── common/         # Common components
│   └── layout/         # Layout components
├── pages/              # Page components
├── hooks/              # Custom React hooks
├── services/           # API and external services
│   ├── api/           # API client and endpoints
│   └── auth/          # Authentication services
├── types/              # TypeScript type definitions
├── constants/          # Application constants
├── utils/              # Utility functions
└── stores/             # State management (future)
```

## 🛠️ Development

### Prerequisites

- Bun (or Node.js 18+)
- Docker & Docker Compose (for full stack)

### Installation

```bash
# Install dependencies
bun install

# or with npm
npm install
```

### Available Scripts

```bash
# Start development server
bun start
# or npm start

# Build for production
bun run build
# or npm run build

# Run tests
bun test
# or npm test

# Lint code
bun run lint
# or npm run lint

# Format code
bun run format
# or npm run format
```

### Environment Variables

Create a `.env` file in the root of the frontend directory:

```env
REACT_APP_API_URL=http://localhost:8080
```

## 🐳 Docker

### Development with Docker Compose

From the root directory:

```bash
# Start all services (API + Frontend + Database)
docker-compose up --build

# Start in detached mode
docker-compose up -d --build

# Stop services
docker-compose down
```

The frontend will be available at:
- **Frontend**: http://localhost:3000
- **API**: http://localhost:8080

### Build Frontend Docker Image

```bash
# Build the image
docker build -t zentasks-frontend .

# Run the container
docker run -p 3000:80 zentasks-frontend
```

## 🏗️ Architecture

The project follows Clean Architecture principles:

- **Pages**: Top-level route components
- **Components**: Reusable UI components organized by domain
- **Hooks**: Custom hooks for business logic and API integration
- **Services**: External concerns (API calls, authentication)
- **Types**: TypeScript definitions for type safety

## 🔧 Code Quality

The project uses several tools to maintain code quality:

- **ESLint**: Linting with TypeScript and React rules
- **Prettier**: Code formatting
- **Husky**: Pre-commit hooks to run linting and formatting
- **TypeScript**: Static type checking

## 📱 Features

- Task management with Eisenhower Matrix
- User authentication and registration
- Responsive design
- Real-time data fetching with TanStack Query
- Client-side routing
- Error handling and loading states

## 🚀 Deployment

The application is containerized and ready for deployment. The Dockerfile creates a production build served by Nginx with proper caching headers and SPA routing support.