
# API Vendor Management

Sebuah sistem CRUD dasar dalam manajemen vendor yang mengimplementasikan fitur security dan rate limit. 


# Usage

- Java version 17 
- Framework Spring Boot
- Maven
- Database PostgreSQL (pgadmin4)
- Postman APIs testing
- Intellij IDEA code editor


#  Run Locally

- Setelah repository ini di clone, Install dependensi sistem management vendor dengan menggunakan Maven, jika menggunakan intellij bisa langsung klik icon maven, atau bisa dengan perintah berikut:
```cmd
mvn install
```

- Buat database baru postgreSql dengan nama "_**vendor**_".

- Buka _**application.properties**_, lalu sesuaikan configurasi database seperti _username_, _password_, _host_, _port_, dan _name_. sesuaikan dengan database anda.
```properties
# Configuration Database
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD:postgres}
spring.datasource.url=jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:vendor}
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

- Jalankan sistem di main class.

- Setiap endpoint menggunakan Authorization, kecuali register dan login, sehingga untuk endpoint lainnya harus menyertakan token di header dengan Auth Type  yaitu _Bearer token_.
by default akun super adminnya terinisialisasi segera setelah program dijalankan, berikut akunnya:
```json
{
    "username": "admin",
    "password": "12345"
}
```





# API Reference
Untuk melakukan pengujian API, Anda dapat menggunakan Postman. Berikut ini adalah file ekspor dari API Management Vendor. Silakan unduh dan impor file tersebut ke Postman jika diperlukan

[![GPLv3 Collection](https://img.shields.io/badge/Collection-Postman-yellow.svg)](https://github.com/adityaeee/management-vendor/blob/master/Vendor%20Management.postman_collection.json)

## Auth

### Register 

- **Endpoint**
```http
  POST /api/v1/auth/register
```

- **Request Body**
```json
{
    "username": "aditya",
    "password": "aditya"
}
```

- **Response**
```json
{
    "statusCode": 201,
    "message": "successfully save data",
    "data": {
        "username": "aditya",
        "roles": [
            "ROLE_USER"
        ]
    }
}
```

### Login 

- **Endpoint**
```http
  POST /api/v1/auth/login
```

- **Request Body**
```json
{
    "username": "aditya",
    "password": "aditya"
}
```

- **Response**
```json
{
    "statusCode": 202,
    "message": "You have successfully logged in",
    "data": {
        "username": "aditya",
        "token": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJtYW5hZ2VtZW50IiwiaWF0IjoxNzMxODM4ODU2LCJleHAiOjE3MzIwOTgwNTYsInN1YiI6Ijc",
        "roles": [
            "ROLE_USER"
        ]
    }
}
```

## Vendor

### Create Vendor

- **Endpoint**
```http
  POST /api/v1/vendor
```
- **Header**

| Header           | Type    | Description                           |
|:-----------------|:--------|:--------------------------------------|
| `Authorization`  | `Bearer Token` | **Required**. TOKEN with ROLE_ADMIN or ROLE_USER |

- **Request**
```json
{
    "name" : "PT Akina"
}
```


- **Response**
```json
{
    "statusCode": 200,
    "message": "successfully update data vendor",
    "data": {
        "id": "cf6485a5-057e-4a89-a96f-b7be1ab29e7d"
        "name": "PT Akina",
    }
}
```

### Get All Vendor

- **Endpoint**
```http
  GET /api/v1/vendor
```
- **Header**

| Header           | Type    | Description                           |
|:-----------------|:--------|:--------------------------------------|
| `Authorization`  | `Bearer Token` | **Required**. TOKEN with ROLE_ADMIN or ROLE_USER |


- **Response**
```json
{
    "statusCode": 200,
    "message": "Successfully retrieved vendors",
    "data": [
        {
            "id": "1a050af6-7b4d-458f-a0e4-653ac2245e08"
            "name": "PT Akina",
        },
        {
            "id": "69a570e2-6c93-442a-b702-be95b16538a0"
            "name": "PT Biskies",
        }
    ]
}
```

### Get Vendor By Id 

- **Endpoint**
```http
  GET /api/v1/vendor/{id}
```
- **Header**

| Header           | Type    | Description                           |
|:-----------------|:--------|:--------------------------------------|
| `Authorization`  | `Bearer Token` | **Required**. TOKEN with ROLE_ADMIN or ROLE_USER |

- **Path Variable**

| Parameter | Type     | Description                 |
|:----------|:---------|:----------------------------|
| `id`      | `String` | **Required**. id of Vendor |

- **Response**

```json
{
    "statusCode": 200,
    "message": "Successfully get data vendor",
    "data": {
        "id": "c694daa6-76d0-4909-be58-9f57a6bd326d"
        "name": "PT Akina",
    }
}
```

### Update Vendor

- **Endpoint**
```http
  PUT /api/v1/vendor
```
- **Header**

| Header           | Type    | Description                           |
|:-----------------|:--------|:--------------------------------------|
| `Authorization`  | `Bearer Token` | **Required**. TOKEN with ROLE_ADMIN or ROLE_USER |

- **Request Body**
```json
{
    "id" : "c694daa6-76d0-4909-be58-9f57a6bd326d",
    "name" : "PT Akina Multi"
}
```
- **Response**

```json
{
    "statusCode": 200,
    "message": "successfully update data vendor",
    "data": {
        "name": "PT Akina Multi",
        "id": "c694daa6-76d0-4909-be58-9f57a6bd326d"
    }
}
```

### Delete Vendor By Id 

- **Endpoint**
```http
  DELETE /api/v1/vendor/{id}
```
- **Header**

| Header           | Type    | Description                           |
|:-----------------|:--------|:--------------------------------------|
| `Authorization`  | `Bearer Token` | **Required**. TOKEN with ROLE_ADMIN or ROLE_USER |

- **Path Variable**

| Parameter | Type     | Description                 |
|:----------|:---------|:----------------------------|
| `id`      | `String` | **Required**. id of Vendor |

- **Response**

```json
{
    "statusCode": 200,
    "message": "Successfully delete data vendor",
    "data": "Deleted"
}
```
# Features

## Rate Limit
Aplikasi ini menggunakan Bucket4j untuk membatasi jumlah permintaan (rate limiting) yang dapat dilakukan oleh setiap akun pengguna dalam periode waktu tertentu. Implementasi ini difokuskan pada pembatasan berdasarkan pengguna, bukan pada endpoint yang diakses.

### Cara Kerja
- Setiap permintaan API yang diajukan oleh pengguna harus menyertakan token JWT (JSON Web Token) di header Authorization. Token ini digunakan untuk mengidentifikasi pengguna.


- Sistem membuat bucket unik untuk setiap akun berdasarkan ID pengguna yang diekstraksi dari JWT. Bucket ini bertindak sebagai wadah token yang mengatur berapa banyak permintaan yang dapat dilakukan oleh pengguna dalam jangka waktu tertentu.


- Setiap akun memiliki batas jumlah permintaan yaitu 5 request per menit (60 detik).
Jika bucket pengguna kehabisan token, permintaan berikutnya akan ditolak dengan respons rate limit.
```json
{
    "statusCode": 429,
    "message": "429 TOO_MANY_REQUESTS \"Rate limit exceeded, try again later!\"",
    "data": null
}
```


- Token di bucket akan otomatis terisi ulang berdasarkan interval waktu yang ditentukan (60 detik).

## Feedback

If you have any feedback, please contact us via email at adityae@duck.com


## License

[Rifky Aditya](www.linkedin.com/in/mrifkyaditya)


