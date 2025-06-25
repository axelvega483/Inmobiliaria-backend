# 🏠 Sistema de Gestión para Inmobiliaria

Sistema backend para la gestión integral de propiedades inmobiliarias.  
Permite administrar usuarios, clientes, inmuebles, contratos, pagos y transacciones.  
Pensado para digitalizar procesos administrativos y financieros mediante una API REST robusta, modular y segura.

---

## 🌟 Características del Sistema

- Administración de inmuebles con imágenes y detalles completos.
- Asociación de inmuebles a usuarios (ADMIN / GERENTE) y clientes (dueños o compradores/inquilinos).
- Gestión de contratos de compra o alquiler.
- Registro y confirmación de pagos, con generación automática de ticket en PDF.
- Registro de transacciones financieras vinculadas a pagos.
- Creación automática de un usuario administrador si no existen registros.
- Validaciones integradas y DTOs personalizados para facilitar la comunicación.

---

## 🛠️ Tecnologías Utilizadas

**Back-end (API REST)**  
- Java 17  
- Spring Boot  
- Spring Web  
- Spring Data JPA  
- Bean Validation (javax.validation)  
- Lombok  
- MySQL 
- Maven  
- iText PDF para generación de tickets PDF  

---

## 📝 Requerimientos Funcionales

### 👤 Usuarios
- Alta, baja, edición y autenticación de usuarios.
- Roles definidos: `ADMIN`, `GERENTE`.
- Creación automática de usuario administrador si no hay registros.
- Asociación con los inmuebles que administran.

### 🧑‍💼 Clientes
- Registro de clientes como **dueños** o **inquilinos/compradores**.
- Visualización de sus inmuebles alquilados o comprados.
- Asociación con contratos activos o finalizados.

### 🏡 Inmuebles
- Alta, baja, edición y listado de propiedades.
- Atributos: dirección, tipo, precio, estado, disponibilidad, descripción.
- Asociación con:
  - **Usuario** que administra el inmueble.
  - **Cliente** que lo posee o lo alquila.
  - Múltiples imágenes.

### 📄 Contratos
- Registro de contratos de **alquiler** o **compra**.
- Información de inicio, duración (si aplica), monto total, estado.
- Relación directa con inmueble, cliente y pagos.

### 💰 Pagos
- Registro de pagos realizados por contratos.
- Confirmación, anulación o estado pendiente.
- **Generación automática de ticket en PDF** con los detalles del pago.
- Actualización automática del estado del contrato según el pago.

### 🔁 Transacciones
- Registro detallado del movimiento asociado a pagos.
- Puede incluir tipo de operación, fecha, monto y observaciones.
- Útil para auditoría y seguimiento financiero.

---

## ⚙️ Requerimientos No Funcionales

- Validaciones con mensajes personalizados para integridad de datos.
- Seguridad preparada para autenticación y autorización con JWT.
- Modularidad pensada para futuras integraciones (web pública, app mobile, etc).
- Código limpio, documentado, siguiendo principios **SOLID** y buenas prácticas.

---

## 🚀 Posibles Extensiones Futuras

- Gestión de reservas y agendas de visitas.
- Facturación electrónica e integración con AFIP.
- Envío de correos con adjuntos PDF al confirmar pagos o firmar contratos.
- Dashboard con métricas administrativas y financieras.

---
