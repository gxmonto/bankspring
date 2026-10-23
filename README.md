# e-Bank (Spring Boot)

## Requisitos
- Java 21
- Una base de datos: MySQL 8 (por defecto) o PostgreSQL / Supabase

Maven no hace falta instalarlo: usa `./mvnw` (o `mvnw.cmd` en Windows).

## Ejecutar con MySQL local

```bash
docker run -d --name bankdb -p 3306:3306 -e MYSQL_ROOT_PASSWORD=1407 -e MYSQL_DATABASE=bank_management mysql:8
docker exec -i bankdb mysql -uroot -p1407 bank_management < db_bank_management.sql   # datos de ejemplo
./mvnw spring-boot:run
```

Abre http://localhost:8080 y entra con `admin` / `123`.

Para usar otra base o contraseña, sin cambiar el codigo:

```bash
DB_URL=jdbc:mysql://localhost:3306/bank_management DB_USERNAME=root DB_PASSWORD=secreto ./mvnw spring-boot:run
```

## Ejecutar con Supabase

En Supabase: Project Settings > Database > Connection string > JDBC, usando el **Session pooler**.

```bash
SPRING_PROFILES_ACTIVE=supabase \
DB_URL="jdbc:postgresql://aws-0-<region>.pooler.supabase.com:5432/postgres?sslmode=require" \
DB_USERNAME="postgres.<project-ref>" \
DB_PASSWORD="<tu-password>" \
./mvnw spring-boot:run
```

Hibernate crea las tablas. `db_bank_management.sql` es un dump de MySQL y no se puede importar
tal cual en Postgres; crea un usuario y su cuenta con la API (`POST /api/v1/people`, `POST /api/v1/customers`).

## Tests

```bash
./mvnw test
```

Los tests usan H2 en memoria, no necesitan MySQL ni Supabase.

## Depositos y retiros

El login guarda la cuenta del usuario en la sesion (cookie `JSESSIONID`), y las paginas
`/deposit` y `/withdraw` llaman a:

| Metodo | Ruta | Body | Respuesta |
|---|---|---|---|
| GET | `/api/v1/account/balance` | | `{"balance": 1000.00}` |
| POST | `/api/v1/account/deposit` | `{"amount": 100}` | `{"balance": 1100.00}` |
| POST | `/api/v1/account/withdraw` | `{"amount": 100}` | `{"balance": 1000.00}` o 400 si no hay fondos |
| GET | `/api/v1/account/transactions` | | historial |

Sin sesion responden 401. Cada deposito o retiro actualiza `customer.balance` y guarda un registro en `transaction`.
