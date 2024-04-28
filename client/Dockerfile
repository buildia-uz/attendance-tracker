# Use Nginx image to serve static files
FROM nginx:alpine

# Remove default Nginx configuration and static files
# RUN rm /etc/nginx/conf.d/default.conf
RUN rm -rf /usr/share/nginx/html/*

# Copy custom Nginx configuration (if you have one)
# COPY nginx.conf /etc/nginx/conf.d/

# Copy static content into place
COPY . /usr/share/nginx/html

# Expose port 80
EXPOSE 80

# Use a shell script as the entrypoint to replace the placeholder and run Nginx
COPY entrypoint.sh /
RUN chmod +x /entrypoint.sh
ENTRYPOINT ["/entrypoint.sh"]
