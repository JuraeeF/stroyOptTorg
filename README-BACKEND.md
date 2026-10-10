# Stroy backend (Spring Boot 3, Java 17, PostgreSQL, JWT)

СтройОптТорг uchun ulgurji qurilish materiallari savdosi backendi.

## O'rnatish

Talablar: Java 17, Maven 3.6.3 yoki yangiroq, PostgreSQL.

1. PostgreSQL'da baza yarating:
   ```sql
   CREATE DATABASE stroy;
   ```
2. `src/main/resources/application.properties` dagi standart qiymatlar bilan ishlating yoki `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` muhit o'zgaruvchilarini belgilang.
3. Loyihani ishga tushiring:
   ```bash
   mvn spring-boot:run
   ```
4. Ishga tushadigan JAR yaratish:
   ```bash
   mvn clean package
   java -jar target/stroy-backend-0.0.1-SNAPSHOT.jar
   ```

Admin (birinchi ishga tushishda): `admin@stroy.local` / `Admin12345!` — **darhol o'zgartiring** (`ADMIN_EMAIL`, `ADMIN_PASSWORD`).
Productionda albatta `JWT_SECRET` (32+ belgi) bering. Demo ma'lumot kerak bo'lmasa `SEED_DEMO_DATA=false`.

## API

| Metod | URL | Kim |
|---|---|---|
| POST | `/api/auth/register`, `/api/auth/login` | hamma |
| GET | `/api/auth/me` | kirgan foydalanuvchi |
| GET | `/api/categories`, `/api/categories/{id}` | hamma |
| GET | `/api/products?categoryId&q&minPrice&maxPrice&inStock&sort&page&size` | hamma |
| GET | `/api/products/{id}` | hamma |
| POST | `/api/contact` | hamma |
| POST | `/api/orders` | kirgan foydalanuvchi |
| GET | `/api/orders`, `/api/orders/{id}` | egasi (admin hammasini ko'radi) |
| POST | `/api/orders/{id}/cancel` | egasi, faqat NEW holatda |
| POST/PUT/DELETE | `/api/admin/categories[/{id}]` | ADMIN |
| GET/POST/PUT/DELETE | `/api/admin/products[/{id}]` | ADMIN (DELETE = sotuvdan olish) |
| GET/PATCH | `/api/admin/orders`, `/api/admin/orders/{id}/status` | ADMIN |
| GET/PATCH | `/api/admin/contacts`, `/api/admin/contacts/{id}/processed` | ADMIN |
| GET | `/api/admin/stats` | ADMIN |

`sort`: `newest` (default), `price_asc`, `price_desc`, `name`. Himoyalangan so'rovlarda `Authorization: Bearer <token>` yuboriladi.

Sinash uchun `requests.http` faylini IntelliJ ichida oching.

## Biznes qoidalari

- Ulgurji: har mahsulotda `minOrderQuantity`; buyurtmada u hisobga olinadi.
- Buyurtma paytida ombor qoldig'i bloklanib kamayadi; bekor qilinsa qaytariladi.
- Buyurtma holatlari: NEW → CONFIRMED → SHIPPED → DELIVERED (NEW/CONFIRMED dan CANCELLED ga o'tish mumkin).
- Buyurtmada mahsulot nomi va narxi nusxalanadi, keyin narx o'zgarsa ham tarix buzilmaydi.

## Frontend

Каталог, поиск, категории, корзина, вход/регистрация и оформление заказа доступны в браузере по адресу http://localhost:8080 после запуска Spring Boot. Интерфейс находится в src/main/resources/static и использует существующие /api endpoints. Для загрузки каталога должна быть запущена PostgreSQL база stroy.