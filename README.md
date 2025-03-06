# Dockerización del Proyecto HolaSpring

Este proyecto Spring Boot está dockerizado para facilitar su despliegue y desarrollo.

## Requisitos

- Docker
- Docker Compose

## Estructura de archivos

- `Dockerfile`: Configura la imagen de la aplicación Spring Boot
- `docker-compose.yml`: Configura los servicios de la aplicación y la base de datos
- `application-docker.properties`: Configuración específica para el entorno Docker

## Cómo ejecutar

1. Clona el repositorio
2. Navega a la carpeta del proyecto
3. Construye y ejecuta los contenedores:

```bash
docker-compose up -d
```

4. La aplicación estará disponible en: http://localhost:8080
5. Para detener los contenedores:

```bash
docker-compose down
```

## Servicios

- **mysql**: Base de datos MySQL en el puerto 3306
- **spring-app**: Aplicación Spring Boot en el puerto 8080

## Datos de acceso predeterminados

### Base de datos
- **Usuario**: admin
- **Contraseña**: admin
- **Base de datos**: tarea2

### Aplicación
- **Admin**: admin@sistema.com / admin123
- **Usuario**: user@sistema.com / user123

## Comandos útiles

```bash
# Ver logs de la aplicación
docker-compose logs -f spring-app

# Entrar al contenedor de la aplicación
docker exec -it holaspring-app sh

# Entrar al contenedor de MySQL
docker exec -it mysql-db mysql -u admin -padmin tarea2

# Reiniciar servicios
docker-compose restart
```