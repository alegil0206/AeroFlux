#!/bin/sh

# Creazione file env-config.js con le variabili d'ambiente
echo "window.env = {" > /usr/share/nginx/html/env-config.js
echo "  DRONE_IDENTIFICATION_ENDPOINT: \"$DRONE_IDENTIFICATION_ENDPOINT\"," >> /usr/share/nginx/html/env-config.js
echo "  GEO_AWARENESS_ENDPOINT: \"$GEO_AWARENESS_ENDPOINT\"," >> /usr/share/nginx/html/env-config.js
echo "  GEO_AUTHORIZATION_ENDPOINT: \"$GEO_AUTHORIZATION_ENDPOINT\"," >> /usr/share/nginx/html/env-config.js
echo "  WEATHER_ENDPOINT: \"$WEATHER_ENDPOINT\"" >> /usr/share/nginx/html/env-config.js
echo "}" >> /usr/share/nginx/html/env-config.js

# Avvia Nginx
exec "$@"