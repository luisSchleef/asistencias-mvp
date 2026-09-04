# AGENTS.md

App de escritorio Java 17 + Maven + Swing (FlatLaf) + SQLite. MVP escolar de control de asistencia. Texto de UI y comentarios en español.

## Build y verificación

- No hay `mvn` en PATH. En Linux: `~/.m2/wrapper/dists/apache-maven-3.9.16-bin/5grr65jo27hi51sujmtcldfovl/apache-maven-3.9.16/bin/mvn`. En Windows: `& "$env:USERPROFILE\.m2\wrapper\dists\apache-maven-3.8.5-bin\5i5jha092a3i37g0paqnfr15e0\apache-maven-3.8.5\bin\mvn.cmd"`.
- Verificación: `mvn compile` y `mvn test` (JUnit 5 con surefire). No usar `-o` (offline): algunos plugins no están en el cache local.
- La app corre por defecto con la BD en el CWD (`asistencia_db`). La ruta es configurable con `-Dasistencias.db=/ruta` o la env `ASISTENCIAS_DB`. En IntelliJ, Working directory = raíz del proyecto.

## Base de datos

- `asistencia_db` (SQLite) **no se versiona en git** (está en `.gitignore`): no commitear ni restaurar el binario.
- Si el archivo de BD no existe, `dbConexion` ejecuta automáticamente `src/main/resources/db/init.sql` (desde el classpath): crea esquema (roles, tipos_asistencia, usuarios, asistencias) y datos semilla.
- Usuario semilla: correo `luis@empresa.cl`, contraseña `admin123` (hash BCrypt).
- Contraseñas en `usuarios.contrasena` son **hash BCrypt**; `login.autenticar` y `crud.crearUsuario/actualizarUsuario` ya las hashean/verifican. No des-hashear ni comparar texto plano.
- Esquema y código deben mantenerse sincronizados con `init.sql`; si se cambia el esquema, actualizar el script.

## Convenciones

- Nombres de clase en minúscula no estándar (`crud`, `login`, `dbConexion`, `user`); frames con prefijo `frm`. Mantener el estilo, no renombrar.
- UI Swing construida por código (sin .form). Iconos SVG vía `FlatSVGIcon("icons/xxx.svg", w, h)` desde `src/main/resources/icons`.
- `crud` devuelve `List<String[]>` para llenar `DefaultTableModel`. Roles: `ADMIN` / `EMPLEADO`.
- Los métodos administrativos de `crud` reciben `user actor` y validan rol con `requerirAdmin` (lanza `SecurityException`); no depender solo de la UI para la autorización. La UI atrapa `SQLException | SecurityException`.
- Para buscar/filtrar tablas reutilizar `filtroBusqueda.escuchar(txtBuscar, sorter)`; no re-inlinear DocumentListener.
- El tema FlatLaf se setea en `Main` con `FlatArcDarkIJTheme` (dep `flatlaf-intellij-themes`); no es removible.
- `slf4j-nop` existe solo para silenciar el warning SLF4J de sqlite-jdbc; no quitarlo.
