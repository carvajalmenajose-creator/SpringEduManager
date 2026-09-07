# SpringEduManager

Aplicación web educativa del Módulo 6 (Desarrollo de aplicaciones JEE con Spring Framework). Permite gestionar estudiantes, cursos y evaluaciones con Spring Boot, Spring MVC, Spring Data JPA, Spring Security y APIs REST.

## Tecnologías

- Java 25
- Spring Boot 4.1.1
- Maven
- Thymeleaf
- Spring Data JPA
- MySQL
- Spring Security (roles ADMIN, PROFESOR y ESTUDIANTE; usuarios en MySQL)

## Requisitos

- JDK 25
- Maven 3.9+
- MySQL en ejecución, con la base de datos `springedumanager` creada:

```sql
CREATE DATABASE springedumanager;
```

La conexión por defecto está en `src/main/resources/application.properties`:

- URL: `jdbc:mysql://localhost:3306/springedumanager`
- Usuario: `root`
- Contraseña: `1234`

Ajusta estos valores si tu MySQL es distinto.

## Ciclo de vida Maven

```bash
./mvnw clean
./mvnw install
./mvnw package
./mvnw spring-boot:run
```

En Windows también puedes usar `mvnw.cmd`.

La aplicación queda disponible en `http://localhost:8080`.

## Acceso (Lección 4)

El login **no** usa usuarios en memoria ni en `application.properties`. Las credenciales viven en la tabla `usuarios` (correo + contraseña BCrypt + rol).

Un **usuario** es quien inicia sesión. **Estudiante** y **profesor** son perfiles académicos ligados a un usuario:

- ADMIN: solo usuario (coordina el sistema, no es estudiante ni profesor)
- PROFESOR: usuario + ficha en `profesores`
- ESTUDIANTE: usuario + ficha en `estudiantes`

| Rol        | Correo                            | Contraseña |
|------------|-----------------------------------|------------|
| ADMIN      | admin@springedumanager.cl         | 1234       |
| PROFESOR   | carla.soto@springedumanager.cl    | 1234       |
| ESTUDIANTE | roberto@correo.cl                 | 1234       |

También pueden entrar `juan@correo.cl` y `ana@correo.cl`.

Al crear un estudiante o un profesor se genera automáticamente su usuario (clave inicial `1234`).

Permisos:

- **ADMIN**: gestiona cursos, profesores, usuarios y puede eliminar estudiantes.
- **PROFESOR**: ve el sistema, registra estudiantes y evaluaciones; no crea cursos.
- **ESTUDIANTE**: consulta cursos, estudiantes, profesores y evaluaciones; no carga datos.

## Módulos web (Lecciones 2 y 3)

- `/home` inicio
- `/estudiantes/listar` perfiles de estudiantes (con usuario de acceso)
- `/profesores/listar` perfiles de profesores
- `/usuarios/listar` credenciales en BD (solo ADMIN)
- `/cursos/listar` listado de cursos; alta/edición restringida a ADMIN
- `/evaluaciones/listar` consulta para todos; alta/edición para ADMIN y PROFESOR

Los datos iniciales se cargan la primera vez que la aplicación arranca (`DataInitializer`).

## APIs REST (Lección 5)

Autenticación: HTTP Basic (mismo usuario y contraseña del login). CSRF desactivado solo en `/api/**` para consumo desde Postman.

### Estudiantes

- `GET /api/estudiantes`
- `GET /api/estudiantes/{id}`
- `POST /api/estudiantes` (ADMIN)
- `PUT /api/estudiantes/{id}` (ADMIN)
- `DELETE /api/estudiantes/{id}` (ADMIN)

Ejemplo `POST`:

```json
{
  "nombre": "María López",
  "email": "maria@correo.cl"
}
```

### Cursos

- `GET /api/cursos`
- `GET /api/cursos/{id}`
- `POST /api/cursos` (ADMIN)
- `PUT /api/cursos/{id}` (ADMIN)
- `DELETE /api/cursos/{id}` (ADMIN)

Ejemplo `POST`:

```json
{
  "nombre": "Spring Boot",
  "descripcion": "Curso de Spring Boot"
}
```

En Postman: Authorization → Basic Auth → usuario y contraseña. Header `Content-Type: application/json` en POST y PUT.

El proyecto incluye `RestTemplate` (`RestTemplateConfig`) y `ApiRestClient` para consumir estos endpoints desde un cliente Java externo.

## Estructura

- `controller` controladores MVC (Thymeleaf)
- `controller.api` controladores REST (`@RestController`)
- `service` reglas de negocio
- `repository` Spring Data JPA
- `model` entidades (`Usuario`, `Estudiante`, `Profesor`, `Curso`, `Evaluacion`)
- `config` seguridad, datos iniciales y RestTemplate
- `templates` vistas Thymeleaf
