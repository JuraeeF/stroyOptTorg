# СтройОптТорг Backend (Java 17 Amazon Corretto, Spring Boot 3.3.5, PostgreSQL, JWT, Swagger)

СтройОптТорг uchun ulgurji qurilish materiallari savdosi REST API backendi.

## Tizim talablari va sozlash

- **Java**: Java 17 (Amazon Corretto 17 tavsiya etiladi)
- **Maven**: 3.8+ (yoki IntelliJ IDEA o'rnatilgan Maven)
- **Ma'lumotlar bazasi**: PostgreSQL 14+

### 1. PostgreSQL bazasi yaratish
```sql
CREATE DATABASE stroy;
```

### 2. Sozlamalar (`application.properties`)
Standart sozlamalar:
- DB URL: `jdbc:postgresql://localhost:5432/stroy`
- Username: `postgres`
- Password: `root123`
- Server port: `8080`

### 3. Ishga tushirish
IntelliJ IDEA orqali:
`StroyApplication.java` faylini ochib, **Run** tugmasini bosing.

Yoki terminal orqali:
```bash
mvn spring-boot:run
```

JAR paket yaratish:
```bash
mvn clean package
java -jar target/stroy-backend-0.0.1-SNAPSHOT.jar
```

---

## 📖 Swagger / OpenAPI Hujjatlari

Loyihani ishga tushirgandan so'ng brauzerda quyidagi manzilni oching:
👉 **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

OpenAPI spetsifikatsiyasi (JSON):
👉 **[http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)**

> **Swagger UI orqali avtorizatsiya**:
> 1. `/api/auth/login` orqali tizimga kiring va tokenni oling.
> 2. Yuqori o'ng burchakdagi **"Authorize"** tugmasini bosing.
> 3. Token qiymatini kiriting (masalan: `eyJhbGciOi...`).
> 4. Endi barcha himoyalangan API larni bevosita brauzerdan sinab ko'rishingiz mumkin!

---

## Standart Admin Ma'lumotlari
Birinchi marta ishga tushganda avtomatik ravishda admin foydalanuvchi yaratiladi:
- **Email**: `originalorig31@gmail.com`
- **Parol**: `Admin12345!`
*(Ushbu qiymatlarni `application.properties` orqali o'zgartirishingiz mumkin)*

---

## Asosiy REST API Endpoints

| Metod | URL | Kirish ruxsati | Tavsif |
|---|---|---|---|
| POST | `/api/auth/register` | Ommaviy | Yangi mijozni ro'yxatdan o'tkazish |
| POST | `/api/auth/login` | Ommaviy | Tizimga kirish (JWT token olish) |
| GET | `/api/auth/me` | Kirgan foydalanuvchi | Joriy foydalanuvchi profili |
| GET | `/api/categories` | Ommaviy | Barcha kategoriyalar |
| GET | `/api/categories/{id}` | Ommaviy | Bitta kategoriya |
| GET | `/api/products` | Ommaviy | Mahsulotlar katalogi (filtr, qidiruv, sahifalash) |
| GET | `/api/products/{id}` | Ommaviy | Bitta mahsulot tafsiloti |
| POST | `/api/contact` | Ommaviy | Qayta aloqa / murojaat yuborish |
| POST | `/api/orders` | Kirgan foydalanuvchi | Yangi buyurtma berish (ombor qoldig'i band qilinadi) |
| GET | `/api/orders` | Kirgan foydalanuvchi | Mening buyurtmalarim |
| GET | `/api/orders/{id}` | Buyurtma egasi / Admin | Buyurtma tafsiloti |
| POST | `/api/orders/{id}/cancel` | Buyurtma egasi | Faqat YANGI buyurtmani bekor qilish (tovar omborga qaytadi) |
| POST/PUT/DELETE | `/api/admin/categories` | Faqat ADMIN | Kategoriyalarni boshqarish |
| GET/POST/PUT/DELETE | `/api/admin/products` | Faqat ADMIN | Mahsulotlarni boshqarish (Soft delete) |
| GET/PATCH | `/api/admin/orders` | Faqat ADMIN | Barcha buyurtmalar va ularning statusini yangilash |
| GET/PATCH | `/api/admin/contacts` | Faqat ADMIN | Murojaatlar va ularni ko'rib chiqilgan deb belgilash |
| GET | `/api/admin/stats` | Faqat ADMIN | Savdo va tizim statistikasi |

---

## Veb Foydalanuvchi Interfeysi (Frontend)
Loyiha ishga tushgach, brauzerda to'g'ridan-to'g'ri:
👉 **[http://localhost:8080](http://localhost:8080)**
manziliga kirsangiz, tayyor savdo do'koni veb-interfeysi (katalog, savat, buyurtma berish, shaxsiy kabinet) ochiladi.