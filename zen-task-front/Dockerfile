# Estágio 1: Build da aplicação React
# Usamos uma imagem oficial do Bun com base no Alpine Linux para ser leve.
FROM oven/bun:1-alpine AS build

# Define o diretório de trabalho dentro do contêiner.
WORKDIR /app

# --- INÍCIO DAS ALTERAÇÕES ---

# 1. Copia apenas o package.json.
#    Isso garante que o cache de dependências do Docker funcione bem,
#    e ignora a confusão sobre o nome do lockfile (bun.lock vs bun.lockb).
COPY package.json ./

# 2. Instala as dependências a partir do package.json.
#    Removemos a flag '--frozen-lockfile' para forçar a instalação
#    e garantir que o 'styled-components' seja incluído no build.
RUN bun install

# --- FIM DAS ALTERAÇÕES ---

# 3. Copia todo o resto do código fonte do projeto.
COPY . .

# 4. Roda o script de build para gerar os arquivos estáticos.
RUN bun run build

# ---

# Estágio 2: Servidor de produção
# Usamos uma imagem Nginx super leve baseada em Alpine.
FROM nginx:alpine

# Copia sua configuração personalizada do Nginx para dentro do contêiner.
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copia os arquivos estáticos gerados no estágio de build para a pasta
# que o Nginx usa para servir o site.
COPY --from=build /app/build /usr/share/nginx/html

# Expõe a porta 80, que é a porta padrão que o Nginx usa.
EXPOSE 80

# Comando para iniciar o servidor Nginx quando o contêiner for executado.
CMD ["nginx", "-g", "daemon off;"]