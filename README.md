> [!IMPORTANT]
> Las credenciales por defecto son (Matrícula: 1, Contraseña: 123).

# CLARA-UFRO
**Cuentas Limpias y Auditoría de Recursos para Agrupaciones**

Sistema de gestión financiera desarrollado en Java para agrupaciones estudiantiles de la Universidad de La Frontera. 
Permite el registro, seguimiento y auditoría transparente de ingresos y egresos.

## Características (Avance 2):
- **Interfaz Gráfica (GUI):** Migración completa del sistema de consola a ventanas interactivas con Java Swing.
- **Persistencia de Datos Síncrona:** Almacenamiento local mediante archivos CSV con escritura en disco bloqueante (`StandardOpenOption.SYNC`) para garantizar la integridad y evitar corrupción por cortes de energía.
- **Gestión de Comprobantes:** Carga, aislamiento mediante identificadores UUID y visualización nativa de comprobantes físicos (PDF, JPG, PNG) como respaldo de transacciones.
- **Auditoría y Búsquedas:** Sistema de filtrado dinámico de movimientos financieros por fecha y tipo (Ingreso/Egreso).
- **Arquitectura MVC:** Separación estricta de responsabilidades entre Modelo, Vista y Controlador.

## Tecnología:
- **Lenguaje:** Java.
- **Interfaz Gráfica:** Java Swing.
- **Persistencia:** CSV.

## Estructura de Directorios:
- `src/Modelo`: Entidades de datos (`Usuario`, `Movimiento`, `Agrupacion`).
- `src/Controlador`: Lógica de negocio, cálculos financieros y gestores de persistencia/documentos.
- `src/Vista`: Interfaces gráficas (Ventanas, formularios y menús de sesión).
- `src/Main`: Lanzador principal de la aplicación.
- `data/`: Directorio principal de bases de datos `.csv`.
- `data/comprobantes/`: Directorio de almacenamiento de respaldos físicos.

## Futuras Iteraciones:
- **Migración a Maven:** Reestructuración de la arquitectura para automatizar dependencias y el ciclo de construcción.
- **Exportación de Reportes:** Generación e impresión de historiales financieros en formato PDF.
- **Gestión Multigrupo:** Soporte algorítmico para administrar múltiples agrupaciones de forma aislada.
- **Testing Automatizado:** Implementación de pruebas unitarias estrictas utilizando el framework JUnit 5.

## Autores y Contribuciones:
- **Ying Shi Huang Guo**: Programación de paquete `src/Vista`.
- **Marcelo Alexis Vega Fernández**: Avance en el informe.
- **Mateo Francisco Cortés Torres**: Programación de paquete `src/Controlador` e integración arquitectónica.
