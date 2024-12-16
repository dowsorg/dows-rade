location /dev/assets/${web.name} {
    #alias /diy/saas/acre/dev/web/dist;
    alias ${web.assets};
}

location /dev/statics/${web.name} {
    #alias /diy/saas/acre/dev/web/dist;
    alias ${web.statics};
}

location /dev/web/${web.name} {
    #alias /usr/share/nginx/html/h5;
    #alias /diy/saas/acre/dev/web/dist;
    alias ${web.dist};
    index index.html index.htm;
    try_files $uri $uri/ /dev/web/${web.name}/index.html;
}

location /dev/api/${web.name}/ {
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for,$remote_addr;
    add_header Access-Control-Allow-Origin '*';
    add_header Access-Control-Allow-Methods 'GET, POST, PUT, DELETE, OPTIONS';
    add_header Access-Control-Allow-Headers 'DNT,X-Mx-ReqToken, Keep-Alive, User-Agent,X-Requested-With, If-Modified-Since, Cache-Control,Content-Type, Authorization';
    if ($request_method = "OPTIONS") {
        return 204;
    }
    proxy_pass http://${web.host!"localhost"}:${web.port}/;
}