# Inventory App — Sistem Stok Barang Toko

Skeleton project Spring Boot 3 + PostgreSQL untuk manajemen produk, kategori, dan riwayat pergerakan stok (masuk/keluar).

## Struktur Project

```
inventory-app/
├── docker-compose.yml          # PostgreSQL siap pakai
├── pom.xml
└── src/main/java/com/example/inventory/
    ├── InventoryApplication.java
    ├── entity/          # Category, Product, StockMovement, MovementType
    ├── repository/      # Spring Data JPA repositories
    ├── service/         # Logika bisnis (termasuk transaksi stok)
    ├── controller/      # REST endpoints
    ├── dto/             # Request/Response objects
    └── exception/       # Custom exception + global handler
```

## Cara Menjalankan

### 1. Jalankan PostgreSQL lewat Docker
```bash
docker compose up -d
```
Ini akan membuat database `inventory_db` dengan user `inventory_user` / password `inventory_pass` di port 5432.

Kalau sudah punya PostgreSQL sendiri, cukup sesuaikan `src/main/resources/application.yml`.

### 2. Jalankan aplikasi
```bash
./mvnw spring-boot:run
```
(Kalau belum ada Maven Wrapper, jalankan `mvn spring-boot:run` — pastikan Maven & JDK 17+ sudah terinstal.)

Aplikasi jalan di `http://localhost:8080`.
Dokumentasi API otomatis (Swagger UI): `http://localhost:8080/swagger-ui.html`

Tabel akan dibuat otomatis oleh Hibernate (`ddl-auto: update`) saat pertama kali start.

## Contoh Penggunaan API

### Buat kategori
```http
POST /api/categories
Content-Type: application/json

{ "name": "Minuman", "description": "Produk minuman kemasan" }
```

### Buat produk
```http
POST /api/products
Content-Type: application/json

{
  "name": "Teh Botol 450ml",
  "sku": "TB-450",
  "price": 5000,
  "stock": 100,
  "minStock": 10,
  "categoryId": 1
}
```

### Catat barang masuk (restock)
```http
POST /api/stock-movements
Content-Type: application/json

{ "productId": 1, "type": "IN", "quantity": 50, "note": "Restock dari supplier A" }
```

### Catat barang keluar (terjual)
```http
POST /api/stock-movements
Content-Type: application/json

{ "productId": 1, "type": "OUT", "quantity": 20, "note": "Penjualan harian" }
```
Jika `quantity` OUT melebihi stok yang tersedia, API akan menolak dengan status `400` dan pesan error yang jelas.

### Cek produk dengan stok menipis
```http
GET /api/products/low-stock
```

### Lihat riwayat pergerakan stok suatu produk
```http
GET /api/stock-movements/product/1
```

## Ide Pengembangan Lanjutan
- Tambah Spring Security (login admin/staff, role-based access)
- Tambah endpoint laporan (total nilai stok, produk paling laris per periode)
- Tambah pagination & sorting pada `GET /api/products`
- Tambah unit test untuk `StockService` (kasus stok cukup vs tidak cukup)
- Ganti `ddl-auto: update` dengan Flyway/Liquibase untuk migrasi schema yang lebih terkontrol saat production
