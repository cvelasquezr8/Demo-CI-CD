# spring-boot-demo con Security CI/CD
Demo project

Se requiere al menos Java 11, para verificacion simple:

```bash
mvnw verify
```

Para compilar se incluye el plugins de docker para empaquetar todo el proyecto:

```bash
mvnw clean package jib:dockerBuild
```

En el caso de Windows debe utilizar el script mvnw.cmd en lugar del mvnw

Para levantar el contenedor de la aplicacion, además de las dependencias; se debe ubicarse en el directorio donde se encuentra el archivo docker-compose.yml y ejecutar

```bash
docker compose up
```
