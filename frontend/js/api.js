// ============================================================
//  Conexión con el backend (API REST de Spring Boot)
//  Se carga ANTES del script de cada pantalla.
// ============================================================

// Dirección del backend. Si el backend corre en otra computadora o puerto, cambiar solo esta línea.
const API_URL = "http://localhost:8080";

class ErrorApi extends Error {
  constructor(estado, mensaje) {
    super(mensaje);
    this.estado = estado;
  }
}

function getSesion() {
  return JSON.parse(sessionStorage.getItem("sesion"));
}

// Escapa texto antes de ponerlo dentro de innerHTML (evita que un dato rompa o inyecte HTML)
function esc(texto) {
  return String(texto ?? "").replace(/[&<>"']/g, c => (
    { "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;" }[c]
  ));
}

// Arma el header "Basic usuario:clave" (soporta tildes y ñ)
function armarAuth(usuario, clave) {
  const bytes = new TextEncoder().encode(usuario + ":" + clave);
  let binario = "";
  bytes.forEach(b => binario += String.fromCharCode(b));
  return "Basic " + btoa(binario);
}

// Convierte la respuesta de error del backend en un mensaje legible
function mensajeDeError(estado, datos) {
  if (datos && datos.errores) {
    return Object.values(datos.errores).join(" · ");   // 400: un mensaje por cada campo inválido
  }
  if (datos && datos.mensaje) {
    return datos.mensaje;                              // 404, 409, 403, 401
  }
  if (estado === 403) return "No tenés permiso para realizar esta operación.";
  return "Ocurrió un error inesperado (código " + estado + ").";
}

// Llama a la API mandando siempre el usuario logueado.
// Uso:  const alumnos = await api("/api/alumnos");
//       await api("/api/alumnos", { method: "POST", body: JSON.stringify(datos) });
async function api(ruta, opciones = {}) {
  const sesion = getSesion();
  const headers = { "Content-Type": "application/json", ...(opciones.headers || {}) };
  if (sesion && sesion.auth) headers["Authorization"] = sesion.auth;

  let respuesta;
  try {
    respuesta = await fetch(API_URL + ruta, { ...opciones, headers });
  } catch (e) {
    throw new ErrorApi(0, "No se pudo conectar con el servidor. Verificá que el backend esté encendido.");
  }

  if (respuesta.status === 401 && sesion) {
    sessionStorage.removeItem("sesion");
    window.location.href = "index.html";
    throw new ErrorApi(401, "Tu sesión no es válida. Volvé a iniciar sesión.");
  }
  if (respuesta.status === 204) return null;

  let datos = null;
  const texto = await respuesta.text();
  if (texto) {
    try { datos = JSON.parse(texto); } catch (e) { /* respuesta sin JSON */ }
  }
  if (!respuesta.ok) {
    throw new ErrorApi(respuesta.status, mensajeDeError(respuesta.status, datos));
  }
  return datos;
}
