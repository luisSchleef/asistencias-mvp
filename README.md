# MVP de asistencia

Una pequeña empresa de 25 trabajadores y trabajadoras dedicada a la compra y venta de productos químicos ha solicitado la creación de un Mínimo Producto Viable (MVP) para un sistema de registro de asistencia de empleados. La empresa necesita una **aplicación de escritorio** que permita gestionar eficientemente la entrada y salida de sus trabajadores y trabajadoras. Esta solicitud es fundamental para mejorar la organización interna y asegurar el cumplimiento de las normativas laborales, además de optimizar la administración del tiempo y los recursos humanos.

| Login                                                                                          | Menu                                                                                       |
|------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------|
| <img width="336" height="173" alt="Captura de pantalla 2026-08-26 005544" src="https://github.com/user-attachments/assets/e1a63daa-8cff-4c27-86e9-fd1e667560d5" /> | <img width="486" height="313" alt="Captura de pantalla 2026-08-26 005558" src="https://github.com/user-attachments/assets/0f736951-08ee-460f-a00a-2d0f30237451" /> |

| Asistencia                                                                             | Usuarios                                                                                       |
|------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------|
|<img width="586" height="393" alt="Captura de pantalla 2026-08-26 005616" src="https://github.com/user-attachments/assets/02d789ad-05d6-404f-819e-327cd5235e1a" /> | <img width="626" height="413" alt="Captura de pantalla 2026-08-26 005654" src="https://github.com/user-attachments/assets/ca4dc305-1dd0-41c3-b77a-7b892572201a" /> |

## Requerimientos Funcionales

| Requerimiento                   | Descripción |
|---------------------------------|-------------|
| Control de asistencia           | La aplicación debe permitir el control de asistencia de los empleados. Los usuarios entran con correo y contraseña. Marcarán su entrada con un botón y su salida de la misma forma. |
| Reporte de atrasos              | Permitir al Administrador elaborar un reporte de todos los que entren post 9:30 am ("entrada atrasada"). |
| Reporte de salidas anticipadas  | Permitir al Administrador elaborar un reporte de todos los que salgan antes de las 17:30 ("salida anticipada"). |
| Reporte de inasistencias        | Permitir al Administrador elaborar un reporte de todos los que no registraron ni entrada ni salida un día. |
| Crear usuarios                  | El administrador debe ser capaz de crear usuarios. |
| Modificar usuarios              | El administrador debe ser capaz de modificar usuarios. |
| Eliminar usuarios               | El administrador debe ser capaz de eliminar usuarios. |

## Detalle de Requerimientos

### Control de asistencia

- **Descripción:** La aplicación debe permitir el control de asistencia de los empleados. Los usuarios marcarán su entrada con un botón y su salida de la misma forma.
- **Actor:** Usuario
- **Precondiciones:**
    - Los usuarios deben estar creados.
    - Los usuarios deben haber ingresado a la plataforma mediante la ventana login.
- **Flujo Normal:**
    1. Usuario presiona botón Entrada/salida.
    2. El sistema almacena el identificador del usuario, acción (entrada/salida), la fecha y hora actual.
    3. Sistema emite una confirmación del registro correcto de la entrada/salida.
    4. Usuario presiona botón Cerrar sesión.
- **Flujo Alterno:** Usuario presiona botón Cerrar sesión sin presionar botón Entrada/salida.
- **Postcondiciones:** Sistema presenta ventana de Login.

### Reporte de atrasos

- **Descripción:** Permitir al Administrador elaborar un reporte de todos los que entren post 9:30 am ("entrada atrasada").
- **Actor:** Usuario Administrador
- **Precondiciones:**
    - Usuario debe haber ingresado.
    - Usuario debe tener privilegios de administrador.
- **Flujo Normal:**
    1. Usuario selecciona opción "reporte de entradas atrasadas".
    2. Sistema presenta en pantalla todas las entradas posteriores a 9:30 desde la base de datos, indicando el identificador de cada usuario y los días que llegó atrasado.
- **Postcondiciones:** El administrador puede visualizar el reporte de atrasos.

### Reporte de Salidas Anticipadas

- **Descripción:** Permitir al Administrador elaborar un reporte de todos los que salgan antes de las 17:30 ("salida anticipada").
- **Actor:** Usuario Administrador
- **Precondiciones:**
    - El usuario debe haber ingresado.
    - El usuario debe tener privilegios de administrador.
- **Flujo Normal:**
    1. El usuario selecciona la opción "reporte de salidas anticipadas".
    2. El sistema presenta en pantalla todas las salidas antes de las 17:30 desde la base de datos, indicando el identificador de cada usuario y los días que salió anticipadamente.
- **Postcondiciones:** El administrador puede visualizar el reporte de salidas anticipadas.

### Reporte de Inasistencias

- **Descripción:** Permitir al Administrador elaborar un reporte de todos los que no registraron ni entrada ni salida un día.
- **Actor:** Usuario Administrador
- **Precondiciones:**
    - El usuario debe haber ingresado.
    - El usuario debe tener privilegios de administrador.
- **Flujo Normal:**
    1. El usuario selecciona la opción "reporte de inasistencias".
    2. El sistema presenta en pantalla todos los días en los que no se registraron ni entradas ni salidas para los usuarios, indicando el identificador de cada usuario.
- **Postcondiciones:** El administrador puede visualizar el reporte de inasistencias.

### Crear Usuarios

- **Descripción:** El administrador debe ser capaz de crear usuarios.
- **Actor:** Usuario Administrador
- **Precondiciones:**
    - El usuario debe haber ingresado.
    - El usuario debe tener privilegios de administrador.
- **Flujo Normal:**
    1. El usuario selecciona la opción "crear usuario".
    2. El usuario ingresa los datos necesarios para la creación del nuevo usuario.
    3. El sistema valida y guarda la información del nuevo usuario.
    4. El sistema confirma la creación del nuevo usuario.
- **Postcondiciones:** El nuevo usuario queda registrado en el sistema.

### Modificar Usuarios

- **Descripción:** El administrador debe ser capaz de modificar usuarios.
- **Actor:** Usuario Administrador
- **Precondiciones:**
    - El usuario debe haber ingresado.
    - El usuario debe tener privilegios de administrador.
- **Flujo Normal:**
    1. El usuario selecciona la opción "modificar usuario".
    2. El usuario selecciona el usuario que desea modificar.
    3. El usuario actualiza los datos necesarios.
    4. El sistema valida y guarda los cambios.
    5. El sistema confirma la modificación del usuario.
- **Postcondiciones:** Los datos del usuario son actualizados en el sistema.

### Eliminar Usuarios

- **Descripción:** El administrador debe ser capaz de eliminar usuarios.
- **Actor:** Usuario Administrador
- **Precondiciones:**
    - El usuario debe haber ingresado.
    - El usuario debe tener privilegios de administrador.
- **Flujo Normal:**
    1. El usuario selecciona la opción "eliminar usuario".
    2. El usuario selecciona el usuario que desea eliminar.
    3. El sistema confirma la acción de eliminación.
    4. El sistema elimina al usuario seleccionado.
- **Postcondiciones:** El usuario es eliminado del sistema.

## Stack de desarrollo

| Capa                        | Tecnología      |
|-----------------------------|-----------------|
| Lenguaje                    | Java 17         |
| Build                       | Maven           |
| Base de datos               | SQLite          |

## Organización del proyecto

```text
Asistencias-mvp/
├── pom.xml                      # Configuración de Maven y dependencias
├── asistencia_db                # Base de datos SQLite
└── src/main/
    ├── java/
    │   ├── Main.java            # Punto de entrada: aplica el tema FlatLaf y abre el login
    │   ├── controller/
    │   │   ├── login.java       # Autenticación de usuarios (correo + contraseña)
    │   │   └── crud.java        # Operaciones CRUD sobre usuarios y asistencia
    │   ├── db/
    │   │   └── dbConexion.java  # Conexión JDBC a la base de datos SQLite
    │   ├── model/
    │   │   └── user.java        # Modelo de datos del usuario
    │   └── ui/
    │       ├── frmLogin.java    # Ventana de inicio de sesión
    │       ├── frmMenu.java     # Ventana principal con el menú
    │       ├── frmUser.java     # Ventana de gestión de usuarios (crear/modificar/eliminar)
    │       ├── frmReporte.java  # Ventana de reportes (atrasos, salidas anticipadas, inasistencias)
    │       └── utils.java       # Utilidades de UI (filtros de búsqueda, etc.)
    └── resources/
        └── icons/               # Iconos SVG usados por FlatSVGIcon en las ventanas
```

Cada capa tiene una responsabilidad única: `ui` contiene las ventanas Swing, `controller` la lógica de negocio (autenticación y CRUD), `model` las entidades y `db` el acceso a la base de datos.
