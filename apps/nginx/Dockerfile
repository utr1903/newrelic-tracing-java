FROM nginx:1.21.6

# Add opentracing module for nginx
# wget -O - https://github.com/opentracing-contrib/nginx-opentracing/releases/download/v0.24.0/linux-amd64-nginx-1.21.6-ngx_http_module.so.tgz \
#   | tar zxf -
ADD ngx_http_opentracing_module.so /etc/nginx/modules

# Add zipkin plugin for nginx open tracing
# wget -O - https://github.com/rnburn/zipkin-cpp-opentracing/releases/download/v0.5.2/linux-amd64-libzipkin_opentracing_plugin.so.gz \
# | gunzip -c > libzipkin_opentracing_plugin.so
ADD libzipkin_opentracing_plugin.so /usr/local/lib/
ADD zipkin-config.json /etc/zipkin/

# Add nginx configuration
ADD nginx.conf /etc/nginx/

ARG nginxName
ARG nginxPort
ARG proxyPort

RUN sed -i s/"NGINX_NAME"/$nginxName/g /etc/nginx/nginx.conf
RUN sed -i s/"NGINX_PORT"/$nginxPort/g /etc/nginx/nginx.conf
RUN sed -i s/"PROXY_PORT"/$proxyPort/g /etc/nginx/nginx.conf
