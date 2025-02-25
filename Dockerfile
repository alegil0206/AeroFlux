# Fase 2: Configura Nginx per servire i file
FROM nginx:alpine

# Copia la configurazione personalizzata di Nginx come template
COPY nginx.template.conf /etc/nginx/nginx.template.conf

# Espone la porta 80
EXPOSE 80

# Avvia Nginx
CMD envsubst '${UI_CONTAINER_URL} ${DRONE_IDENTIFICATION_CONTAINER_URL} ${GEOZONE_CONTAINER_URL} ${AUTHORIZATION_CONTAINER_URL} ${WEATHER_CONTAINER_URL}' < /etc/nginx/nginx.template.conf > /etc/nginx/conf.d/default.conf && nginx -g 'daemon off;'