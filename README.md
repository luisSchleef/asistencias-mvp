# MVP de asistencia

---
Una pequeña empresa de 25 trabajadores y trabajadoras dedicada a la compra y venta de productos químicos ha solicitado la creación de un Mínimo Producto Viable (MVP) para un sistema de registro de asistencia de empleados. La empresa necesita una **aplicación de escritorio** que permita gestionar eficientemente la entrada y salida de sus trabajadores y trabajadoras. Esta solicitud es fundamental para mejorar la organización interna y asegurar el cumplimiento de las normativas laborales, además de optimizar la administración del tiempo y los recursos humanos.

---
```
git clone https://github.com/luisSchleef/asistencias-mvp
```
---
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

---
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
