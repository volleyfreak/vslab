
# create databases
CREATE DATABASE IF NOT EXISTS `products`;
CREATE DATABASE IF NOT EXISTS `categories`;

# create root user and grant rights
CREATE USER 'user'@'%' IDENTIFIED BY 'user';
GRANT ALL ON *.* TO 'user'@'%';