// Login: valida usuario y contraseña contra el backend (GET /api/auth/me)
const formLogin = document.getElementById("formLogin");
const errorLogin = document.getElementById("errorLogin");

// Si ya hay una sesión iniciada, ir directo al panel
if (getSesion()) {
  window.location.href = "dashboard.html";
}

formLogin.addEventListener("submit", async function (e) {
  e.preventDefault();
  errorLogin.classList.add("d-none");

  if (!formLogin.checkValidity()) {
    formLogin.classList.add("was-validated");
    return;
  }

  const usuario = document.getElementById("usuario").value.trim();
  const clave = document.getElementById("password").value;
  const auth = armarAuth(usuario, clave);

  try {
    const respuesta = await fetch(API_URL + "/api/auth/me", { headers: { "Authorization": auth } });

    if (respuesta.status === 401) {
      throw new Error("Usuario o contraseña incorrectos.");
    }
    if (!respuesta.ok) {
      throw new Error("No se pudo iniciar sesión (código " + respuesta.status + ").");
    }

    const datos = await respuesta.json();      // { id, username, nombre, rol, activo }
    sessionStorage.setItem("sesion", JSON.stringify({
      usuario: datos.username,
      nombre: datos.nombre,
      rol: datos.rol,
      auth: auth
    }));
    window.location.href = "dashboard.html";
  } catch (err) {
    errorLogin.textContent = (err instanceof TypeError)
      ? "No se pudo conectar con el servidor. Verificá que el backend esté encendido."
      : err.message;
    errorLogin.classList.remove("d-none");
  }
});
