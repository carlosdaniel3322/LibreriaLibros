# LibreriaLibros API

API REST para gestionar libros y revistas. Usa Spring Boot, PostgreSQL, JPA/Hibernate y seguridad basada en JWT.

## Funcionalidades

- CRUD persistente de libros y revistas en PostgreSQL.
- Consulta JPQL para buscar libros por titulo o autor.
- Actualizacion transaccional por lote: si un elemento del lote falla, se revierten todos los cambios.
- Autenticacion mediante JWT.
- Autorizacion por roles `LECTOR`, `EDITOR` y `ADMIN`.

## Arquitectura

```text
Controller -> Service -> Repository -> PostgreSQL
```

- `Libro` y `Revista` son entidades JPA.
- `LibroRepository` y `RevistaRepository` extienden `JpaRepository`.
- Los servicios se inyectan por constructor.

## Configuracion

Requisitos: Java 24, PostgreSQL y una base de datos llamada `biblioteca_pf2026`.

Configura las variables de entorno de PostgreSQL antes de ejecutar:

```powershell
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="TU_CONTRASENA"
```

Hibernate crea o actualiza automaticamente las tablas `libros` y `revistas`.

## Ejecucion

Desde NetBeans, usa `Clean and Build` y luego `Run Project`.

Desde terminal en Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

La API usa el puerto configurado en `application.properties`. Por defecto es `http://localhost:8080`.

## Seguridad y JWT

Primero solicita un token:

```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "lector",
  "password": "lector123"
}
```

La respuesta contiene `accessToken`. En las rutas protegidas envialo como:

```http
Authorization: Bearer <accessToken>
```

| Rol | Permisos sobre `/api/libros` |
| --- | --- |
| `LECTOR` | Consultar libros con `GET` |
| `EDITOR` | Consultar, crear y actualizar |
| `ADMIN` | Todos los permisos, incluido eliminar |

Usuarios de prueba:

| Usuario | Contrasena | Rol |
| --- | --- | --- |
| `lector` | `lector123` | `LECTOR` |
| `editor` | `editor123` | `EDITOR` |
| `admin` | `admin123` | `ADMIN` |

## Endpoints principales

| Metodo | URI | Rol requerido | Respuesta esperada |
| --- | --- | --- | --- |
| POST | `/api/auth/login` | Publico | Token JWT, 200 |
| GET | `/api/libros` | LECTOR, EDITOR o ADMIN | Lista, 200 |
| POST | `/api/libros` | EDITOR o ADMIN | Libro creado, 201 |
| PUT | `/api/libros/{id}` | EDITOR o ADMIN | Libro actualizado, 200 |
| DELETE | `/api/libros/{id}` | ADMIN | Sin contenido, 204 |
| GET | `/api/libros/buscar?texto=spring` | LECTOR, EDITOR o ADMIN | Coincidencias JPQL, 200 |
| PUT | `/api/libros/lote` | EDITOR o ADMIN | Lote actualizado, 200 |
| GET | `/api/revistas` | Token JWT valido | Lista, 200 |

## Consulta JPQL

`LibroRepository` usa `@Query` para buscar libros por titulo o autor, ignorando mayusculas y minusculas. Ejemplo:

```http
GET /api/libros/buscar?texto=spring
```

## Operacion transaccional

`PUT /api/libros/lote` recibe una lista de libros para actualizar. El servicio usa `@Transactional`; si un ID no existe, lanza una excepcion y ninguno de los cambios queda guardado.

## Pruebas automatizadas

Ejecuta:

```powershell
.\mvnw.cmd test
```

Las pruebas incluyen servicios con repositorios simulados, contratos HTTP y seguridad:

- Login con JWT devuelve un token.
- Una ruta protegida sin token devuelve `401`.
- El rol `LECTOR` puede consultar libros.
- El rol `LECTOR` no puede crear libros y recibe `403`.

## Postman

Importa `postman/tiendaLibros.postman_collection.json`.

La carpeta `Autenticacion JWT` inicia sesion como `LECTOR`, `EDITOR` y `ADMIN`, y guarda los tokens automaticamente. Ejecuta primero esa carpeta o toda la coleccion desde Collection Runner. Las solicitudes de Libros y Revistas ya usan el token adecuado y validan sus codigos HTTP con scripts de prueba.
