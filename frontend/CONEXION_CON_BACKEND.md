# Conexión del frontend con el backend

## Qué cambió
| Archivo | Cambio |
|---|---|
| `js/api.js` (nuevo) | Dirección del backend (`API_URL`) y función `api()` que llama a la API mandando el usuario logueado y traduce los errores. |
| `js/auth.js` (nuevo) | Login: valida usuario y contraseña contra `GET /api/auth/me`. Faltaba en el repositorio. |
| `index.html` | Carga `api.js` y muestra el error de login. |
| `dashboard.html` | Menú según el rol (alineado con los permisos del backend). Notas y "Mi situación académica" quedaron comentadas. |
| `alumnos.html` | CRUD contra `/api/alumnos`. Se agregaron fecha de nacimiento (el backend la exige), domicilio y teléfono. |
| `inscripcion.html` | Alumnos, ciclos, cursos, cupos e inscripciones desde la API. Las reglas las valida el backend. |
| `pagos.html` | Cuotas, totales y pagos desde la API. Se agregó "Generar matrícula y cuotas". |
| `notas.html`, `alumno.html` | Sin cambios: todavía usan datos del navegador (el backend no tiene notas ni asistencias). |

## Cómo probarlo
1. Backend corriendo en `http://localhost:8080` (si está en otro lado, cambiar `API_URL` en `js/api.js`).
2. Abrir `index.html` con Live Server (VS Code) o con doble clic.
3. Ingresar con `admin` y la contraseña definida en `app.admin.password` del backend.

## Flujo para la demostración
Alumnos (crear uno) → Inscripción (inscribirlo en un curso) → Cuotas y pagos (elegir el alumno, "Generar matrícula y cuotas", registrar un pago).
