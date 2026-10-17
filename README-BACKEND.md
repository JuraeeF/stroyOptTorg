# Stroy backend (Spring Boot 3, Java 17, PostgreSQL, JWT)

СтройОптТорг uchun ulgurji qurilish materiallari savdosi backendi.

## O'rnatish

Talablar: Java 17 (Amazon Corretto 17 tavsiya etiladi), Maven 3.6.3 yoki yangiroq, PostgreSQL.

## Swagger / OpenAPI Hujjatlari

Loyiha ishga tushgandan so'ng Swagger UI interfeysiga kiring:
- 📖 **Swagger UI (interaktiv sinov):** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- 📄 **OpenAPI 3 JSON spetsifikatsiyasi:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

Swagger interfeysida:
1. `/api/auth/login` yoki `/api/auth/register` orqali token oling.
2. Sahifaning tepasidagi **Authorize** tugmasini bosing va olingan JWT tokenni kiriting.
3. Barcha himoyalangan endpointlarni to'g'ridan-to'g'ri Swagger orqali sinab ko'ring!

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