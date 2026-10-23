# e-Bank (Spring Boot)

## Ejecutar con Docker (recomendado)

Solo necesitas [Docker Desktop](https://www.docker.com/products/docker-desktop/). No hace falta instalar Java, Maven ni MySQL.

```bash
docker compose up --build
```

Abre http://localhost:8080 y entra con `admin` / `123`.

- La primera vez tarda unos minutos (descarga MySQL y compila el proyecto).
- La base se llena con `db_bank_management.sql` la primera vez que arranca.
- Los datos se guardan en un volumen de Docker: no se pierden al apagar los contenedores.

Comandos utiles:

| Que quieres hacer | Comando |
|---|---|
| Arrancar en segundo plano | `docker compose up -d --build` |
| Ver los logs de la app | `docker compose logs -f app` |
| Apagar | `docker compose down` |
| Borrar la base y volver a cargar `db_bank_management.sql` | `docker compose down -v` |
| Entrar a MySQL | `docker compose exec db mysql -uroot -p1407 bank_management` |

MySQL tambien queda disponible desde tu PC (MySQL Workbench, DBeaver, etc.) en `localhost:3307`,
usuario `root`, contraseña `1407`. Se usa el puerto 3307 para no chocar con un MySQL que ya tengas instalado.

## Ejecutar sin Docker

Necesitas Java 21 y un MySQL 8 con la base `bank_management` (puedes cargar `db_bank_management.sql`).
Maven no hace falta instalarlo: usa `./mvnw` (o `mvnw.cmd` en Windows).

```bash
./mvnw spring-boot:run
```

Por defecto se conecta a `localhost:3306` con `root` / `1407`. Para cambiarlo sin tocar el codigo:

```bash
DB_URL=jdbc:mysql://localhost:3306/bank_management DB_USERNAME=root DB_PASSWORD=secreto ./mvnw spring-boot:run
```

## Tests

```bash
./mvnw test
```

Los tests usan H2 en memoria, no necesitan MySQL ni Docker.

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
