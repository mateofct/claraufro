> [!IMPORTANT]
> Las credenciales por defecto son (Matrícula: 1111111111k, Contraseña: 123).

# CLARA-UFRO
**Cuentas Limpias y Auditoría de Recursos para Agrupaciones**

Sistema de gestión financiera desarrollado en Java para agrupaciones estudiantiles de la Universidad de La Frontera. Permite el registro, seguimiento y auditoría transparente de ingresos y egresos.

## Avance Final: Mejoras Implementadas
En esta iteración final se consolidaron cambios estructurales críticos:
- **Arquitectura MVC Estricta:** Implementación de un `ControladorPrincipal` que orquesta la navegación y la inyección de dependencias, eliminando el acoplamiento entre Vista y Controlador.
- **Migración a Maven:** Gestión integral de dependencias a través de `pom.xml`.
- **Automatización de Pruebas:** Implementación de un *test suite* utilizando **JUnit 5**, cubriendo las reglas de negocio críticas.
- **Validación Institucional:** Integración de un servicio de validación de matrículas contra una base de datos externa.
- **Consolidación de Datos:** Optimización de la persistencia mediante archivos CSV con relaciones de llave foránea.

## Estructura de Paquetes
- `App`: Punto de entrada (`Launcher`) y orquestación de dependencias.
- `Controlador`: Lógica de negocio, validaciones y gestión de persistencia.
- `Modelo`: Entidades del dominio (`Usuario`, `Movimiento`, `Agrupacion`, `RolUsuario`).
- `Vista`: Interfaces gráficas en Java Swing (patrón de componentes pasivos).

## Autores y Contribuciones
El desarrollo del proyecto final fue un esfuerzo colaborativo y equitativo, donde las decisiones arquitectónicas y la refactorización a MVC fueron ejecutadas en conjunto por todo el equipo.

*   **Mateo Francisco Cortés Torres**: Responsable de la portabilidad a Maven, integración general del proyecto y elaboración técnica del informe final. 
*   **Ying Shi Huang Guo**: Desarrollo de la capa de presentación (`Vista`), implementación del sistema de validación de matrículas y desarrollo de pruebas unitarias (JUnit 5).
*   **Marcelo Alexis Vega Fernández**: Implementación del módulo de *Gestión de Agrupaciones*, soporte en el sistema de validación de matrículas y tareas de *debugging*.

> **Nota sobre el trabajo colaborativo:** El desarrollo del `ControladorPrincipal`, la refactorización a MVC estricto, la adecuación de las `Ventanas` y el proceso de *debugging* fueron labores transversales realizadas en conjunto por los tres integrantes, garantizando que el diseño y la lógica fueran comprendidos y validados por todo el equipo.

> **Nota sobre el desarrollo:** Ante la alta complejidad técnica de la refactorización MVC, el equipo adoptó una estrategia de desarrollo asistido por IA para generar sintaxis técnica bajo supervisión constante, reteniendo la autoridad total sobre el diseño lógico para garantizar la integridad del sistema.

## Guía de Uso
- **Credenciales por defecto:** (Matrícula: `1111111111k`, Contraseña: `123`).
- **Base de datos:** El sistema requiere la presencia de la base de datos `.csv` en la carpeta `/data`.

## Documentación Técnica
- **Diagrama UML:** [Acceder al Diagrama UML completo](https://drive.google.com/file/d/1drGpgHLv-3N-KlEaYeSQAwZ56gUz97wt/view?usp=sharing)
- **JavaDoc:** [Acceder a la documentación JavaDoc](https://drive.google.com/file/d/1tvIQCc8S7TWLooXT8PoaQzTQXs7Fz1qA/view?usp=sharing)

## Futuras Iteraciones (Trabajos Pendientes)
- **Migración a Motor SQL:** Migrar de archivos CSV a un motor de base de datos relacional.
- **Exportación PDF:** Implementar la generación de reportes financieros oficiales para auditorías externas.
- **Criptografía Robusta:** Reemplazar el cifrado César actual por *hashing* estándar (ej. BCrypt).
- **Recuperación de Cuentas:** Implementar un flujo de restablecimiento de contraseñas automatizado.
