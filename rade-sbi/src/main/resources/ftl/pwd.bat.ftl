use mysql;
UPDATE user SET authentication_string=password('${mysql.password}') where user='root';
FLUSH PRIVILEGES;