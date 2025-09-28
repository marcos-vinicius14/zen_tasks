FROM oven/bun:1-alpine AS build

WORKDIR /app

COPY package.json ./

RUN bun install

COPY . .

RUN bun run build

FROM nginx:alpine

COPY nginx.conf /etc/nginx/conf.d/default.conf

COPY src/nginx/ssl/localhost.pem /etc/nginx/ssl/localhost.pem
COPY src/nginx/ssl/localhost-key.pem /etc/nginx/ssl/localhost-key.pem

COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80
EXPOSE 443

# Comando para iniciar o Nginx
CMD ["nginx", "-g", "daemon off;"]