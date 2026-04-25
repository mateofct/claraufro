> [!IMPORTANT]
> Las credenciales por defecto son (Matrícula: 1, Contraseña: 123).

# CLARA-UFRO
**Cuentas Limpias y Auditoría de Recursos para Agrupaciones**

Sistema de gestión financiera desarrollado en Java para agrupaciones estudiantiles de la Universidad de La Frontera. 
Permite el registro, seguimiento y auditoría transparente de ingresos y egresos.

## Características (Avance 1):
- **Arquitectura MVC:** Separación entre Modelo, Vista y Controlador.
- **Persistencia de Datos:** Almacenamiento local con uso de archivos CSV.
- **Seguridad:** Sistema de autenticación con cifrado de contraseñas (cifrado césar).
- **Roles de Usuario:**
  - **Admin:** Gestión de usuarios y supervisión global.
  - **Tesorero:** Registro de movimientos y consulta de saldos.
  - **Socio:** Visualización de historial y transparencia activa.

## Tecnología:
- **Lenguaje:** Java.
- **Persistencia:** CSV.
- **Entorno:** Consola.

## Estructura:
- `src/Modelo`: Entidades (`Usuario`, `Movimiento`, `Agrupacion`).
- `src/Controlador`: Lógica de negocio y gestores de archivos/seguridad.
- `src/Vista`: Interfaz de usuario por consola.
- `src/Main`: Lanzador de app.
- `data/`: Carpeta de archivos `.csv`.

## Futuras Iteraciones (Avance 2):
- **Migración a Interfaz Gráfica (Java Swing).**
- **Implementación de Hasheo SHA-256 para contraseñas.**
- **Exportación de reportes en PDF.**
- **Gestión de comprobantes (imágenes/PDF).**

## Autores:
- **Ying Shi Huang Guo**
- **Marcelo Alexis Vega Fernández**
- **Mateo Francisco Cortés Torres**
