# tiendaLibros

API REST inicial para una libreria, pensada como primer avance grupal de la materia.

El proyecto usa Spring Boot y una arquitectura simple `Controller -> Service -> Repository`, con persistencia en memoria para no depender de JPA en esta etapa.

## Requisitos cubiertos

- Modelo simple sin JPA.
- Matriz de endpoints.
- Proyecto Spring Boot ejecutable.
- CRUD en memoria.
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

Como el repositorio es en memoria, los datos se pierden cuando se detiene la aplicacion. Esto es intencional en esta etapa del avance.
