> [!IMPORTANT]
> Las credenciales por defecto son (Matrícula: 1, Contraseña: 123).

# CLARA-UFRO
**Cuentas Limpias y Auditoría de Recursos para Agrupaciones**

Sistema de gestión financiera desarrollado en Java para agrupaciones estudiantiles de la Universidad de La Frontera. 
Permite el registro, seguimiento y auditoría transparente de ingresos y egresos.

## Características (Avance Final):
- **Arquitectura:** Migrado a Maven.

## Tecnología:
- **Lenguaje:** Java.
- **Interfaz Gráfica:** Java Swing.
- **Persistencia:** CSV.

## Estructura de Directorios:
- `src/Main.Modelo`: Entidades de datos (`Usuario`, `Movimiento`, `Agrupacion`).
- `src/Main.Controlador`: Lógica de negocio, cálculos financieros y gestores de persistencia/documentos.
- `src/Main.Vista`: Interfaces gráficas (Ventanas, formularios y menús de sesión).
- `src/Main`: Lanzador principal de la aplicación.
- `data/`: Directorio principal de bases de datos `.csv`.
- `data/comprobantes/`: Directorio de almacenamiento de respaldos físicos.

## Futuras Iteraciones:
- **Exportación de Reportes:** Generación e impresión de historiales financieros en formato PDF.
- **Gestión Multigrupo:** Soporte algorítmico para administrar múltiples agrupaciones de forma aislada.
- **Testing Automatizado:** Implementación de pruebas unitarias estrictas utilizando el framework JUnit 5.

## Autores y Contribuciones:
- **Ying Shi Huang Guo**: Programación de paquete `src/Main.Vista`.
- **Marcelo Alexis Vega Fernández**: Avance en el informe.
- **Mateo Francisco Cortés Torres**: Programación de paquete `src/Main.Controlador` e integración arquitectónica.
