# NoiStop API - Spring Boot + JWT

Sistema de gestión de reportes de contaminación sonora con seguridad JWT.

## Requisitos previos

- Java 17
- Maven 3.8+
- PostgreSQL 14+
- IntelliJ IDEA (recomendado)

## Configuración de la base de datos

1. Abre pgAdmin o psql y ejecuta:
```sql
CREATE DATABASE noistop_db;
```

2. Edita `src/main/resources/application.properties` con tu contraseña:
```properties
spring.datasource.password=TU_PASSWORD
```

> Hibernate creará las tablas automáticamente al arrancar (`ddl-auto=update`).

## Ejecutar el proyecto

```bash
mvn spring-boot:run
```

## Seguridad JWT — Cómo usarla

### Paso 1 — Crear un rol y un usuario

Antes de poder hacer login, necesitas al menos un usuario en la BD.
Como `/api/roles` y `/api/usuarios` requieren token, usa un cliente como Postman
o crea los datos directamente en PostgreSQL:

```sql
INSERT INTO rol (nombrerol) VALUES ('ADMIN');
INSERT INTO usuario (nombre, email, password, estado, fk_rolid, created_at, updated_at)
VALUES ('Admin', 'admin@noistop.com',
        '$2a$10$XptfskLsT1l/gNDMJpqH5.MjUeADMsRAf3kUEDSMTzr4F1jMzFKbK', -- password: admin123
        true, 1, NOW(), NOW());
```

O bien, **descomenta temporalmente** la línea `permitAll()` en `WebSecurityConfig` para
crear tu primer usuario vía Swagger, y luego vuelve a comentarla.

### Paso 2 — Hacer Login

```
POST /login
Content-Type: application/json

{
  "email": "admin@noistop.com",
  "password": "admin123"
}
```

Respuesta:
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "tipo": "Bearer"
}
```

### Paso 3 — Usar el token en Swagger

1. Abre **http://localhost:8080/swagger-ui.html**
2. Haz clic en el botón **Authorize 🔒** (esquina superior derecha)
3. Ingresa: `eyJhbGciOiJIUzUxMiJ9...` (solo el token, sin "Bearer")
4. Haz clic en **Authorize** → **Close**
5. Ahora todos los endpoints funcionarán con tu token

### Paso 4 — Usar el token en Postman

Agrega el header en cada request:
```
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

## Documentación interactiva (Swagger UI)

| URL | Descripción |
|-----|-------------|
| http://localhost:8080/swagger-ui.html | Interfaz gráfica con botón Authorize |
| http://localhost:8080/v3/api-docs | Especificación JSON OpenAPI |

## Rutas públicas (sin token)

| Ruta | Descripción |
|------|-------------|
| `POST /login` | Genera el token JWT |
| `/swagger-ui/**` | Documentación Swagger |
| `/v3/api-docs/**` | Spec OpenAPI |

**Todo lo demás requiere** `Authorization: Bearer <token>`.

## Estructura del proyecto

```
noistop/
├── src/main/java/com/noistop/noistop/
│   ├── security/               ← Clases JWT y Spring Security
│   │   ├── JwtTokenUtil.java          (genera/valida tokens)
│   │   ├── JwtRequestFilter.java      (intercepta requests)
│   │   ├── JwtAuthenticationEntryPoint.java (maneja 401)
│   │   ├── JwtUserDetailsService.java (carga usuario por email)
│   │   └── WebSecurityConfig.java     (config de seguridad)
│   ├── entities/     → 9 entidades JPA
│   ├── dtos/         → DTOs + JwtRequestDTO + JwtResponseDTO
│   ├── repositories/ → 9 repositorios JPA
│   ├── services/     → 7 servicios con BCryptPasswordEncoder
│   ├── controllers/  → 7 controllers REST + JwtAuthenticationController
│   ├── exceptions/   → Manejo global de errores
│   └── config/       → SwaggerConfig con Bearer token UI
└── src/main/resources/
    └── application.properties  ← incluye jwt.secret
```

## Notas de seguridad

- Las contraseñas se almacenan **hasheadas con BCrypt** (nunca en texto plano).
- El campo `password` nunca se devuelve en las respuestas.
- El token JWT expira en **5 horas**.
- El campo `estado` del usuario (true/false) habilita/deshabilita la cuenta.
- La autenticación usa el **email** como identificador único.


Sistema de gestión de reportes de contaminación sonora.

## Requisitos previos

- Java 17
- Maven 3.8+
- PostgreSQL 14+
- IntelliJ IDEA (recomendado)

## Configuración de la base de datos

1. Abre pgAdmin o psql y ejecuta:
```sql
CREATE DATABASE noistop_db;
```

2. Edita `src/main/resources/application.properties` con tus credenciales:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/noistop_db
spring.datasource.username=postgres
spring.datasource.password=TU_PASSWORD
```

> Hibernate creará las tablas automáticamente al arrancar la aplicación (`ddl-auto=update`).

## Ejecutar el proyecto

```bash
mvn spring-boot:run
```

O desde IntelliJ: abrir `NoistopApplication.java` → Run.

## Documentación interactiva (Swagger UI)

Una vez levantado el servidor, accede a:

| URL | Descripción |
|-----|-------------|
| http://localhost:8080/swagger-ui.html | Interfaz gráfica para probar todos los endpoints |
| http://localhost:8080/v3/api-docs | Especificación JSON OpenAPI |

## Historias de usuario cubiertas

| HU | Descripción | Endpoint |
|----|-------------|----------|
| US01 | Registrar rol | POST /api/roles |
| US02 | Editar rol | PUT /api/roles/{id} |
| US03 | Listar roles | GET /api/roles |
| US04 | Registrar usuario | POST /api/usuarios |
| US05 | Listar usuarios | GET /api/usuarios |
| US06 | Editar usuario | PUT /api/usuarios/{id} |
| US07 | Eliminar usuario | DELETE /api/usuarios/{id} |
| US08 | Buscar usuario por ID | GET /api/usuarios/{id} |
| US09 | Registrar ubicación | POST /api/ubicaciones |
| US10 | Listar ubicaciones | GET /api/ubicaciones |
| US11 | Actualizar ubicación | PUT /api/ubicaciones/{id} |
| US12 | Eliminar ubicación | DELETE /api/ubicaciones/{id} |
| US13 | Reportes por distrito | GET /api/reportes/por-distrito?distrito=X |
| US14 | Registrar reporte | POST /api/reportes |
| US15 | Registrar reporte (ciudadano) | POST /api/reportes |
| US16 | Listar reportes | GET /api/reportes |
| US17 | Editar reporte | PUT /api/reportes/{id} |
| US18 | Eliminar reporte | DELETE /api/reportes/{id} |
| US19 | Registrar evidencia | POST /api/evidencias |
| US20 | Reportes por ubicación | GET /api/reportes/por-ubicacion |
| US21 | Registrar acción/comentario | POST /api/acciones |
| US22 | Eliminar acción/comentario | DELETE /api/acciones/{id} |
| US23 | Historial de acciones por reporte | GET /api/acciones/reporte/{reporteId} |
| US24 | Editar acción/comentario | PUT /api/acciones/{id} |
| US25 | Listar categorías | GET /api/categorias |
| US26 | Reportes por categoría | GET /api/categorias/{id}/reportes |
| US27 | Buscar categoría por nombre | GET /api/categorias?nombre=X |
| US28 | Detalle de categoría por ID | GET /api/categorias/{id} |
| US29 | Eliminar categoría | DELETE /api/categorias/{id} |
| US30 | Editar categoría | PUT /api/categorias/{id} |
| US31 | Crear categoría | POST /api/categorias |
| US32 | Historial de reportes por usuario | GET /api/reportes/historial/usuario/{usuarioId} |
| US33 | Filtrar historial por fecha | GET /api/reportes/historial/usuario/{usuarioId}/filtrar?inicio=...&fin=... |

## Estructura del proyecto

```
noistop/
├── src/main/java/com/noistop/noistop/
│   ├── NoistopApplication.java
│   ├── config/
│   │   └── SwaggerConfig.java
│   ├── entities/           ← Mapeo de tablas BD
│   │   ├── Rol.java
│   │   ├── Usuario.java
│   │   ├── Ubicacion.java
│   │   ├── Medicion.java
│   │   ├── CategoriaRuido.java
│   │   ├── Reporte.java
│   │   ├── AccionAdministrativa.java
│   │   ├── EvidenciaReporte.java
│   │   └── HistorialReporte.java
│   ├── dtos/               ← Contrato HTTP
│   ├── repositories/       ← Acceso a datos (JPA)
│   ├── services/           ← Lógica de negocio
│   ├── controllers/        ← Endpoints REST
│   └── exceptions/         ← Manejo global de errores
└── src/main/resources/
    └── application.properties
```

## Ejemplo de secuencia de pruebas en Swagger

1. `POST /api/roles` → `{"nombreRol": "ADMIN"}`
2. `POST /api/usuarios` → con `rolId: 1`
3. `POST /api/categorias` → `{"nombreCategoria": "Tráfico", "descripcionCategoria": "Ruido vehicular"}`
4. `POST /api/ubicaciones` → con coordenadas de Lima
5. `POST /api/reportes` → asociando usuario, categoría y ubicación
6. `POST /api/evidencias` → asociando al reporte creado
7. `POST /api/acciones` → comentario de autoridad sobre el reporte

## Notas importantes

- El campo `password` nunca se devuelve en las respuestas (seguridad).
- El estado inicial de un reporte es siempre `pendiente`.
- Los cambios de estado en reportes se registran automáticamente en `historial_reporte`.
- Las eliminaciones con dependencias activas retornan `409 Conflict`.
- Los registros no encontrados retornan `404` con mensaje "No existen registros".
