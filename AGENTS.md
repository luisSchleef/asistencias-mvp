# AGENTS.md

App de escritorio Java 17 + Maven + Swing (FlatLaf) + SQLite. MVP escolar de control de asistencia. Texto de UI y comentarios en español.

## Build y verificación

- No hay `mvn` en PATH, ni tests, ni lint, ni CI. La única verificación es compilar:
  `& "$env:USERPROFILE\.m2\wrapper\dists\apache-maven-3.8.5-bin\5i5jha092a3i37g0paqnfr15e0\apache-maven-3.8.5\bin\mvn.cmd" compile`
- No usar `-o` (offline): el compiler-plugin no está en el cache local.
- La app debe correr con CWD = raíz del repo: `dbConexion` abre `jdbc:sqlite:asistencia_db` con ruta relativa. En IntelliJ, Working directory = raíz del proyecto.

## Base de datos

- `asistencia_db` (binario SQLite) está trackeado en git y se ensucia al ejecutar la app; sus cambios se incluyen en commits.
- Contraseñas en `usuarios.contrasena` en texto plano; `login.autenticar` compara directo. No "mejorar" con hash sin plan de migración de datos existentes.

## Convenciones

- Nombres de clase en minúscula no estándar (`crud`, `login`, `dbConexion`, `user`); frames con prefijo `frm`. Mantener el estilo, no renombrar.
- UI Swing construida por código (sin .form). Iconos SVG vía `FlatSVGIcon("icons/xxx.svg", w, h)` desde `src/main/resources/icons`.
- `crud` devuelve `List<String[]>` para llenar `DefaultTableModel`. Roles: `ADMIN` / `EMPLEADO`.
- Para buscar/filtrar tablas reutilizar `filtroBusqueda.escuchar(txtBuscar, sorter)`; no re-inlinear DocumentListener.
- El tema FlatLaf se setea en `Main` con `FlatSolarizedLightIJTheme` (dep `flatlaf-intellij-themes`); no es removible.
- `slf4j-nop` existe solo para silenciar el warning SLF4J de sqlite-jdbc; no quitarlo.
