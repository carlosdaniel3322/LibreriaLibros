# Evidencia de ejecución y pruebas

Guía paso a paso para levantar el backend y el frontend, probar el inicio de sesión con JWT, ejecutar el CRUD protegido y comprobar los datos persistidos en PostgreSQL.

- **Backend:** Spring Boot `4.1.1`, Java `24`, Maven Wrapper `3.9.16`, PostgreSQL, JWT (HS256).
- **Frontend:** Angular `20.3`, Node `24.19.0`, npm `11.17.0`.
- **Puertos:** backend `8080`, frontend `4200`.
- **Base de datos:** `biblioteca_pf2026` en `localhost:5432`.

> Esta guía complementa el `README.md` y la colección `postman/tiendaLibros.postman_collection.json`.

---

## 1. Requisitos previos

| Herramienta | Versión / estado esperado | Verificación |
| --- | --- | --- |
| JDK | 24 (definido en `pom.xml`) | `java -version` |
| Maven | Wrapper incluido (`mvnw` / `mvnw.cmd`) | `./mvnw -v` |
| PostgreSQL | Servicio activo en `5432` | `pg_isready -h localhost -p 5432` |
| Node.js | 24 (compatible con Angular 20) | `node -v` |
| npm | 11 o superior | `npm -v` |

Comprueba `JAVA_HOME`: si no está definido, Maven falla con `The JAVA_HOME environment variable is not defined correctly`.

```bash
# Git Bash / Linux / macOS
export JAVA_HOME="/ruta/al/jdk-24"
```

```powershell
# PowerShell
$env:JAVA_HOME = "C:\ruta\al\jdk-24"
```

---

## 2. Estructura relevante

```text
.
├── src/main/java/com/example/tiendaLibros/   backend Spring Boot
├── src/main/resources/application.properties configuración de BD y JWT
├── src/test/java/.../SeguridadControllerTest.java pruebas de seguridad
├── frontend/                                  aplicación Angular
│   └── src/app/core/                          auth.service, interceptores, guard
├── postman/tiendaLibros.postman_collection.json
├── EVIDENCE.md
└── README.md
```

---

## 3. Levantar el backend

### 3.1. Preparar PostgreSQL

Crea la base de datos si aún no existe:

```bash
psql -h localhost -U postgres -c "CREATE DATABASE biblioteca_pf2026;"
```

Hibernate está configurado con `spring.jpa.hibernate.ddl-auto=update`, por lo que las tablas `libros`, `revistas`, `usuarios`, `roles` y `usuario_roles` se crean o actualizan automáticamente al arrancar.

### 3.2. Configurar credenciales

`application.properties` lee variables de entorno con valores por defecto:

```properties
spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/biblioteca_pf2026}
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD:...}
```

Se recomienda exportar tus propias credenciales:

```bash
# Git Bash
export DB_USERNAME="postgres"
export DB_PASSWORD="TU_CONTRASENA"
```

```powershell
# PowerShell
$env:DB_USERNAME = "postgres"
$env:DB_PASSWORD = "TU_CONTRASENA"
```

Si usas otro host o base de datos, define también `DB_URL`.

### 3.3. Iniciar la aplicación

Opción A — Maven Wrapper:

```bash
# Git Bash / Linux / macOS
./mvnw spring-boot:run
```

```powershell
# PowerShell / cmd
.\mvnw.cmd spring-boot:run
```

Opción B — desde el IDE: ejecutar `com.example.tiendaLibros.TiendaLibrosApplication`.

Opción C — JAR empaquetado:

```bash
./mvnw -DskipTests package
java -jar target/tiendaLibros-0.0.1-SNAPSHOT.jar
```

### 3.4. Verificar el arranque

En la consola debe aparecer `Started TiendaLibrosApplication` y `Tomcat started on port 8080`.

El backend no tiene endpoint público de salud, así que se valida con una ruta protegida:

```bash
# Sin token debe responder 401
curl -i http://localhost:8080/api/libros
```

```text
HTTP/1.1 401 Unauthorized
```

Un `HTTP 000` o `curl: (7) Failed to connect` significa que el backend no está escuchando en `8080`.

### 3.5. Datos iniciales

`DataLoader` siembra información solo cuando las tablas están vacías:

**Usuarios**

| Usuario | Contraseña | Roles |
| --- | --- | --- |
| `lector` | `lector123` | `ROLE_LECTOR` |
| `editor` | `editor123` | `ROLE_LECTOR`, `ROLE_EDITOR` |
| `admin` | `admin123` | `ROLE_LECTOR`, `ROLE_EDITOR`, `ROLE_ADMIN` |

Las contraseñas se almacenan con BCrypt.

**Revistas** (si `revistas` está vacía)

| Título | Editorial | ISSN | Precio | Stock |
| --- | --- | --- | --- | --- |
| National Geographic en Español | RBA Revistas | 1234-5678 | 29.90 | 25 |
| Muy Interesante | Zinet Media | 2345-6789 | 19.50 | 18 |
| Historia National Geographic | RBA Revistas | 3456-7890 | 24.90 | 12 |
| Computer Hoy | Axel Springer | 4567-8901 | 15.00 | 30 |

Si la tabla `usuarios` ya contiene registros, los usuarios de prueba no se vuelven a crear.

---

## 4. Levantar el frontend

En otra terminal:

```bash
cd frontend
npm install
npm start
```

Abre `http://localhost:4200`. La compilación de producción se ejecuta con:

```bash
npm run build
# salida: frontend/dist/biblioteca-frontend
```

El frontend consume la API en `http://localhost:8080/api` (definido en `auth.service.ts` y `catalog.service.ts`). El backend permite CORS únicamente desde `http://localhost:4200`; si abres la app con `127.0.0.1` u otro puerto, las peticiones serán bloqueadas y aparecerá `Error 0: Sin conexión con el servidor`.

---

## 5. Probar el flujo completo de inicio de sesión

### 5.1. Login desde la interfaz

1. Abre `http://localhost:4200`.
2. Sin token, el `authGuard` redirige a `/login`.
3. Ingresa `editor` / `editor123` y pulsa **Ingresar**.
4. El componente `LoginPageComponent` envía:

   ```http
   POST http://localhost:8080/api/auth/login
   Content-Type: application/json

   { "username": "editor", "password": "editor123" }
   ```

5. El backend responde `200` con `{ "accessToken": "<JWT>" }`.
6. `AuthService.login()` guarda el token en `localStorage` con la clave `biblioteca_token` y actualiza el signal de sesión.
7. La app navega a `/` y el navbar muestra el rol decodificado del JWT.

### 5.2. Evidenciar el token en el navegador

1. Abre DevTools → **Application** (Chrome/Edge) → **Local Storage** → `http://localhost:4200`.
2. Verifica la entrada `biblioteca_token`.
3. En **Network**, selecciona `login` y revisa la respuesta con `accessToken`.
4. En cualquier petición posterior a `/api/libros` o `/api/revistas`, revisa **Request Headers**:

   ```http
   Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
   ```

El token es emitido con `issuer = utp-dwi`, algoritmo HS256, expira en **1 hora** y contiene el claim `roles` con valores sin prefijo (`LECTOR`, `EDITOR`, `ADMIN`).

### 5.3. Casos que debes evidenciar

| Escenario | Resultado esperado |
| --- | --- |
| Credenciales correctas | Redirección al panel y token en `localStorage` |
| Contraseña incorrecta | Se permanece en `/login` y aparece `Error 401: No autorizado` |
| Acceso directo a `/libros` sin token | Redirección a `/login` |
| Petición al API sin token | `401 Unauthorized` |
| Token expirado o alterado | `401`, se borra el token y se vuelve a `/login` |
| Cerrar sesión | Se elimina `biblioteca_token` y se navega a `/login` |

> El token se guarda en `localStorage` por requisito del proyecto. Ten en cuenta que `localStorage` es accesible por JavaScript; para producción se recomienda una cookie `HttpOnly`.

---

## 6. Probar el CRUD protegido con JWT

### 6.1. Obtener el token

Con Git Bash / Linux / macOS:

```bash
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"editor","password":"editor123"}' \
  | sed -E 's/.*"accessToken":"([^"]+)".*/\1/')

echo "$TOKEN"
```

Con PowerShell:

```powershell
$body = @{ username = "editor"; password = "editor123" } | ConvertTo-Json
$TOKEN = (Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/auth/login" `
  -ContentType "application/json" -Body $body).accessToken
$TOKEN
```

Guarda el valor y úsalo como `Authorization: Bearer $TOKEN`.

### 6.2. Matriz de permisos actual

| Endpoint | LECTOR | EDITOR | ADMIN |
| --- | --- | --- | --- |
| `POST /api/auth/login` | Público | Público | Público |
| `GET /api/libros`, `/api/libros/buscar`, `/api/libros/{id}` | Sí | Sí | Sí |
| `POST /api/libros`, `PUT /api/libros/{id}`, `PUT /api/libros/lote` | No (403) | Sí | Sí |
| `DELETE /api/libros/{id}` | No (403) | No (403) | Sí |
| `/api/revistas/**` | Sí | Sí | Sí |

> **Limitación conocida:** `SecurityConfig` solo declara reglas para `/api/libros/**`; `/api/revistas/**` cae en `anyRequest().authenticated()`. Por eso cualquier usuario autenticado, incluido `LECTOR`, puede crear, actualizar o eliminar revistas llamando directamente a la API, aunque la interfaz oculte esos botones. Si se aplica la mejora propuesta, esta tabla debe actualizarse.

### 6.3. Listar y buscar libros (cualquier rol)

```bash
curl -i http://localhost:8080/api/libros \
  -H "Authorization: Bearer $TOKEN"

curl -i "http://localhost:8080/api/libros/buscar?texto=spring" \
  -H "Authorization: Bearer $TOKEN"
```

Esperado: `200 OK` con un arreglo JSON.

### 6.4. Crear un libro (EDITOR o ADMIN)

```bash
curl -i -X POST http://localhost:8080/api/libros \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Spring Boot en acción",
    "autor": "Craig Walls",
    "isbn": "9781633438218",
    "precio": 129.90,
    "stock": 10
  }'
```

Esperado: `201 Created`, cabecera `Location: http://localhost:8080/api/libros/{id}` y cuerpo con el `id` generado.

### 6.5. Actualizar un libro (EDITOR o ADMIN)

Sustituye `{id}` por el valor devuelto al crear:

```bash
curl -i -X PUT http://localhost:8080/api/libros/{id} \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Spring Boot en acción (2.ª edición)",
    "autor": "Craig Walls",
    "isbn": "9781633438218",
    "precio": 139.90,
    "stock": 8
  }'
```

Esperado: `200 OK` con los datos actualizados.

### 6.6. Consultar un libro y provocar un 404

```bash
curl -i http://localhost:8080/api/libros/{id} \
  -H "Authorization: Bearer $TOKEN"

curl -i http://localhost:8080/api/libros/999999 \
  -H "Authorization: Bearer $TOKEN"
```

Esperado: `200 OK` para el existente y `404 Not Found` con `ApiError` (`status`, `error`, `message`, `path`) para el inexistente.

### 6.7. Provocar un 400 de validación

```bash
curl -i -X POST http://localhost:8080/api/libros \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "X",
    "autor": "",
    "isbn": "123",
    "precio": -5,
    "stock": -1
  }'
```

Esperado: `400 Bad Request` con un `message` que resume los campos inválidos (`titulo`, `autor`, `isbn`, `precio`, `stock`). En la interfaz, el interceptor lo muestra como `Error 400: Datos incorrectos`.

### 6.8. Probar 403 con roles

```bash
# 1) Login como editor y creación de un libro
EDITOR_TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"editor","password":"editor123"}' \
  | sed -E 's/.*"accessToken":"([^"]+)".*/\1/')

# 2) El editor intenta eliminar: 403
curl -i -X DELETE http://localhost:8080/api/libros/{id} \
  -H "Authorization: Bearer $EDITOR_TOKEN"

# 3) Login como admin
ADMIN_TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}' \
  | sed -E 's/.*"accessToken":"([^"]+)".*/\1/')

# 4) El admin elimina: 204
curl -i -X DELETE http://localhost:8080/api/libros/{id} \
  -H "Authorization: Bearer $ADMIN_TOKEN"
```

### 6.9. CRUD de revistas

```bash
# Listar
curl -i http://localhost:8080/api/revistas \
  -H "Authorization: Bearer $TOKEN"

# Crear
curl -i -X POST http://localhost:8080/api/revistas \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Revista de Prueba",
    "editorial": "Editorial Demo",
    "issn": "9876-5432",
    "precio": 12.50,
    "stock": 5
  }'

# Actualizar
curl -i -X PUT http://localhost:8080/api/revistas/{id} \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Revista de Prueba actualizada",
    "editorial": "Editorial Demo",
    "issn": "9876-5432",
    "precio": 14.00,
    "stock": 7
  }'

# Eliminar
curl -i -X DELETE http://localhost:8080/api/revistas/{id} \
  -H "Authorization: Bearer $TOKEN"
```

Respuestas esperadas: `200`, `201`, `200` y `204`.

### 6.10. Alternativa con Postman

1. Importa `postman/tiendaLibros.postman_collection.json`.
2. Ejecuta primero la carpeta **Autenticación JWT**; guarda automáticamente los tokens de `LECTOR`, `EDITOR` y `ADMIN`.
3. Ejecuta las carpetas de Libros y Revistas. Cada request valida el código HTTP esperado.

---

## 7. Validar los datos en PostgreSQL

### 7.1. Conectarse

```bash
psql -h localhost -U postgres -d biblioteca_pf2026
```

### 7.2. Verificar tablas

```sql
\dt
```

Deben aparecer: `libros`, `revistas`, `usuarios`, `roles`, `usuario_roles`.

### 7.3. Verificar usuarios y roles sembrados

```sql
SELECT u.id, u.username, u.enabled, r.nombre AS rol
FROM usuarios u
JOIN usuario_roles ur ON ur.usuario_id = u.id
JOIN roles r ON r.id = ur.rol_id
ORDER BY u.id, r.nombre;
```

Esperado: `lector` con `ROLE_LECTOR`; `editor` con `ROLE_LECTOR` y `ROLE_EDITOR`; `admin` con los tres roles.

Verifica que la contraseña esté cifrada:

```sql
SELECT username, left(password, 7) AS prefijo_hash FROM usuarios ORDER BY id;
```

Esperado: prefijos tipo `$2a$10$` o `$2b$10$` (BCrypt), nunca texto plano.

### 7.4. Verificar las revistas iniciales

```sql
SELECT id, titulo, editorial, issn, precio, stock
FROM revistas
ORDER BY id;
```

Esperado: 4 filas con los datos de la sección 3.5.

### 7.5. Verificar el CRUD de libros

```sql
-- Libro creado con la API
SELECT id, titulo, autor, isbn, precio, stock
FROM libros
WHERE isbn = '9781633438218';

-- Libro actualizado: debe reflejar título, precio y stock nuevos
SELECT id, titulo, precio, stock
FROM libros
WHERE isbn = '9781633438218';

-- Libro eliminado: debe devolver 0 filas
SELECT id, titulo
FROM libros
WHERE isbn = '9781633438218';
```

Cada `POST`, `PUT` o `DELETE` debe reflejarse inmediatamente en estas consultas. Con `spring.jpa.show-sql=true`, la consola del backend también muestra el SQL generado.

### 7.6. Verificar el CRUD de revistas

```sql
SELECT id, titulo, editorial, issn, precio, stock
FROM revistas
WHERE issn = '9876-5432';
```

Tras el `DELETE`, la consulta no debe devolver filas.

### 7.7. Comprobar que no se almacenan tokens

```sql
SELECT column_name
FROM information_schema.columns
WHERE table_name IN ('usuarios', 'libros', 'revistas')
ORDER BY table_name, ordinal_position;
```

No debe existir una columna para JWT: la autenticación es *stateless* y el token solo vive en el cliente (`localStorage`) y en la cabecera de cada petición.

---

## 8. Checklist de evidencias

Marca cada punto con captura de pantalla o salida de terminal:

- [ ] Consola del backend con `Started TiendaLibrosApplication` y `Tomcat started on port 8080`.
- [ ] `curl -i http://localhost:8080/api/libros` sin token → `401`.
- [ ] Pantalla `/login` del frontend.
- [ ] `POST /api/auth/login` con `200` y `accessToken` en DevTools → Network.
- [ ] `biblioteca_token` visible en DevTools → Application → Local Storage.
- [ ] Petición a `/api/libros` con `Authorization: Bearer ...` en Request Headers.
- [ ] Tabla de libros y de revistas renderizada tras el login.
- [ ] Intento de contraseña incorrecta con alerta `Error 401: No autorizado`.
- [ ] `POST /api/libros` → `201` y fila nueva en PostgreSQL.
- [ ] `PUT /api/libros/{id}` → `200` y valores actualizados en PostgreSQL.
- [ ] `DELETE /api/libros/{id}` como `editor` → `403`.
- [ ] `DELETE /api/libros/{id}` como `admin` → `204` y fila eliminada.
- [ ] `GET /api/libros/999999` → `404`.
- [ ] `POST /api/libros` con datos inválidos → `400`.
- [ ] Salida de `psql` con usuarios, roles y revistas sembradas.

---

## 9. Solución de problemas

| Síntoma | Causa probable | Solución |
| --- | --- | --- |
| `Error 0: Sin conexión con el servidor` | Backend apagado o en otro puerto | Verifica `curl -i http://localhost:8080/api/libros` y arranca el backend |
| `The JAVA_HOME environment variable is not defined correctly` | JDK no configurado | Define `JAVA_HOME` apuntando a un JDK 24 |
| `Connection refused` a PostgreSQL | Servicio detenido o credenciales incorrectas | Inicia PostgreSQL y revisa `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` |
| CORS bloqueado en el navegador | Origen distinto de `http://localhost:4200` | Usa exactamente ese origen o ajusta `corsConfigurationSource()` |
| `401` constante tras el login | Token expirado (1 h) o `jwt.secret` distinto entre arranques | Vuelve a iniciar sesión |
| `403` al crear libros | El usuario no es `EDITOR` ni `ADMIN` | Usa `editor` o `admin` |
| `500` al repetir un ISBN o ISSN | Restricción `unique` sin manejo de conflicto | Usa un ISBN/ISSN distinto; el backend aún no devuelve `409` |
| `npm start` falla por versión de Node | Node incompatible con Angular 20 | Usa Node 20.19+, 22.12+ o 24 |

---

## 10. Notas y limitaciones conocidas

1. **Permisos de revistas:** el backend no restringe por rol el CRUD de `/api/revistas/**`; la interfaz oculta acciones según el JWT, pero la API acepta cualquier token válido.
2. **409 Conflict:** el frontend tiene un mensaje preparado, pero el backend no convierte la violación de unicidad en `409`; actualmente produce `500`.
3. **Persistencia del token:** se usa `localStorage` por requisito académico; en producción conviene una cookie `HttpOnly` y protección CSRF/XSS.
4. **URL del API:** está fija en `http://localhost:8080/api`; si cambia el host o el puerto hay que ajustar los servicios Angular y CORS.
