# tiendaLibros

API REST inicial para una libreria, pensada como primer avance grupal de la materia.

El proyecto usa Spring Boot y una arquitectura `Controller -> Service -> Repository`, con persistencia en PostgreSQL mediante JPA/Hibernate.

## Requisitos cubiertos

- Modelo simple con JPA/Hibernate.
- Matriz de endpoints.
- Proyecto Spring Boot ejecutable.
- CRUD persistente en PostgreSQL.
- TDD minimo con tests de servicio y MockMvc.
- Caso RED -> GREEN -> REFACTOR.
- Coleccion de Postman.
- Guia de ejecucion y estructura.

## Modelo simple

Clases principales:

- `Libro`: modelo del dominio con `id`, `titulo`, `autor`, `isbn`, `precio` y `stock`.
- `Revista`: modelo del dominio con `id`, `titulo`, `editorial`, `issn`, `precio` y `stock`.
- Cada recurso tiene sus propios `Request`, `Response`, `Controller`, `Service` y `Repository`.
- `InMemoryLibroRepository` e `InMemoryRevistaRepository`: implementaciones en memoria.

Relacion conceptual:

- `LibroController` usa `LibroService`.
- `LibroServiceImpl` usa `LibroRepository`.
- `InMemoryLibroRepository` guarda `Libro` en memoria.
- `RevistaController` usa `RevistaService`.
- `RevistaServiceImpl` usa `RevistaRepository`.
- `InMemoryRevistaRepository` guarda `Revista` en memoria.

## Matriz de endpoints

| Metodo | URI | Entrada | Respuesta | HTTP |
| --- | --- | --- | --- | --- |
| GET | `/api/libros` | Ninguna | Lista de libros | 200 |
| GET | `/api/libros/buscar?texto={texto}` | Texto por query | Libros cuyo titulo o autor coincide | 200 |
| GET | `/api/libros/{id}` | `id` por path | Un libro | 200 |
| POST | `/api/libros` | `LibroRequest` JSON | Libro creado | 201 |
| PUT | `/api/libros/{id}` | `id` por path + `LibroRequest` JSON | Libro actualizado | 200 |
| DELETE | `/api/libros/{id}` | `id` por path | Sin contenido | 204 |
| GET | `/api/libros/{id}` | `id` inexistente | Error estandar | 404 |
| POST | `/api/libros` | JSON invalido | Error estandar | 400 |
| GET | `/api/revistas` | Ninguna | Lista de revistas | 200 |
| GET | `/api/revistas/{id}` | `id` por path | Una revista | 200 |
| POST | `/api/revistas` | `RevistaRequest` JSON | Revista creada | 201 |
| PUT | `/api/revistas/{id}` | `id` por path + `RevistaRequest` JSON | Revista actualizada | 200 |
| DELETE | `/api/revistas/{id}` | `id` por path | Sin contenido | 204 |
| GET | `/api/revistas/{id}` | `id` inexistente | Error estandar | 404 |

## Como ejecutar

### Desde NetBeans 25

1. Abrir el proyecto Maven.
2. Seleccionar `TiendaLibrosApplication`.
3. Ejecutar la aplicacion.

### Desde terminal

```bash
./mvnw spring-boot:run
```

En Windows tambien puedes usar:

```bash
mvnw.cmd spring-boot:run
```

La API queda disponible en:

- `http://localhost:8080/api/libros`
- `http://localhost:8080/api/revistas`

### Configuracion de PostgreSQL

La aplicacion se conecta a la base de datos local `biblioteca_pf2026` en el puerto `5432`.
En `src/main/resources/application.properties`, configura la contrasena de tu usuario PostgreSQL mediante la variable `DB_PASSWORD` o reemplaza temporalmente el valor de `spring.datasource.password` para realizar la primera ejecucion.

Hibernate crea o actualiza automaticamente las tablas `libros` y `revistas`.

> Nota: el proyecto esta configurado para Java 24 o superior.

## Como probar la API

Usar Postman, Insomnia o curl.

Ejemplo de creacion:

```http
POST /api/libros
Content-Type: application/json

{
  "titulo": "Clean Code",
  "autor": "Robert C. Martin",
  "isbn": "9780132350884",
  "precio": 50.00,
  "stock": 3
}
```

## Consulta JPQL

`LibroRepository` incluye una consulta JPQL que busca libros por titulo o autor, ignorando mayusculas y minusculas:

```http
GET /api/libros/buscar?texto=spring
```

La consulta se implementa con `@Query` y devuelve los resultados ordenados por titulo.

## Coleccion de Postman

La coleccion esta en `postman/tiendaLibros.postman_collection.json`.

Incluye dos carpetas: `Libros` con CRUD y errores `404`/`400`, y `Revistas` con CRUD y error `404`.

## TDD minimo

Tests incluidos:

- `LibroServiceImplTest`
- `LibroControllerTest`
- `RevistaServiceImplTest`
- `RevistaControllerTest`

Escenario ejemplo para explicar en clase:

1. Se escribe primero el test `debeResponder404CuandoNoExisteElLibro`.
2. Falla porque el controller no conocia el manejo de excepciones.
3. Se implementa `LibroNotFoundException` y `RestExceptionHandler`.
4. El test pasa.
5. Se refactoriza moviendo el mapeo de entidad a respuesta dentro del service.

## Estructura del proyecto

```text
src/main/java/com/example/tiendaLibros
  libro/controller
  libro/dto
  libro/exception
  libro/model
  libro/repository
  libro/service
  revista/controller
  revista/dto
  revista/exception
  revista/model
  revista/repository
  revista/service
src/test/java/com/example/tiendaLibros
  libro/controller
  libro/service
  revista/controller
  revista/service
postman/
```

## Observacion importante

Los datos se guardan en PostgreSQL, por lo que permanecen disponibles despues de detener y volver a iniciar la aplicacion.
