# Instituto – Sistema de gestión

Trabajo práctico integrador. Sistema web para gestionar un instituto: alumnos, inscripciones, matrículas, cuotas y pagos, con login y permisos por rol.

## Estructura del repositorio
| Carpeta | Contenido |
|---|---|
| `backend/` | API REST en Java + Spring Boot + PostgreSQL (ver `backend/README.md`: endpoints, reglas de negocio y permisos). |
| `frontend/` | Pantallas en HTML, CSS (Bootstrap) y JavaScript que consumen la API. |

## Tecnologías
Java 17+ · Spring Boot 3.4 · Spring Data JPA · Spring Security · PostgreSQL · HTML5 · Bootstrap 5 · JavaScript

## Cómo ponerlo en marcha (en una sola computadora)
1. **PostgreSQL**: tenerlo instalado y crear una base vacía: `CREATE DATABASE instituto;`
2. **Backend**: editar `backend/src/main/resources/application.properties` y completar
   - `spring.datasource.password` → la contraseña de PostgreSQL de esa computadora
   - `app.admin.password` → la contraseña que va a tener el usuario `admin` del sistema

   Luego ejecutarlo:
   - Desde un IDE (NetBeans o IntelliJ): abrir la carpeta `backend` como proyecto Maven y ejecutar `InstitutoApplication`, o
   - Desde una terminal, dentro de `backend`: `mvn spring-boot:run`

   Cuando la consola muestre `Started InstitutoApplication`, la API está en `http://localhost:8080`.
   Las tablas se crean solas la primera vez.
3. **Frontend**: abrir `frontend/index.html` en el navegador (doble clic, o con la extensión Live Server de VS Code).
4. **Ingresar** con el usuario `admin` y la contraseña definida en `app.admin.password`.

> Si el backend corre en otra dirección o puerto, cambiar `API_URL` al inicio de `frontend/js/api.js`.

## Roles
| Rol | Qué puede hacer |
|---|---|
| ADMINISTRADOR | Todo, incluida la gestión de usuarios (por API). |
| ADMINISTRATIVO | Alumnos, inscripciones, matrículas, cuotas y pagos. |
| DOCENTE | Consultar alumnos, cursos e inscripciones. |

Los usuarios nuevos se crean con `POST /api/usuarios` (como `admin`), por ejemplo desde Postman.

## Recorrido de demostración
Alumnos (crear uno) → Inscripción (inscribirlo en un curso) → Cuotas y pagos (elegirlo, *Generar matrícula y cuotas*, registrar un pago).

## Importante
No subir al repositorio contraseñas reales: en `application.properties` deben quedar los valores `CAMBIAR_PASSWORD` y `admin1234` de ejemplo.
