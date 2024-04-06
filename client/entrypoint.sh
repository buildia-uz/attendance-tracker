#!/bin/sh

# Replace '__BACKEND_API_URL__' with the actual backend API URL from an environment variable
sed -i "s|__BACKEND_API_URL__|$BACKEND_API_URL|g" /usr/share/nginx/html/config.js

# Start Nginx in the foreground
nginx -g 'daemon off;'
