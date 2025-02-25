# Fase 1: Build dell'app React
FROM node:22 AS build
WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .
RUN npm run build

# Fase 2: Configurazione Nginx per servire i file
FROM nginx:alpine

# Copia il file di configurazione personalizzato di Nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copia i file generati nella directory di Nginx
COPY --from=build /app/dist /usr/share/nginx/html

# Copia lo script entrypoint.sh
COPY entrypoint.sh /entrypoint.sh

# Rendi eseguibile lo script entrypoint.sh
RUN chmod +x /entrypoint.sh

# Espone la porta 80
EXPOSE 80

# Usa lo script entrypoint.sh come entrypoint
ENTRYPOINT ["/entrypoint.sh"]

CMD ["nginx", "-g", "daemon off;"]