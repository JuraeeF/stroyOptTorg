package com.example.stroy.config;

import com.example.stroy.entity.Category;
import com.example.stroy.entity.Product;
import com.example.stroy.entity.Role;
import com.example.stroy.entity.User;
import com.example.stroy.repository.CategoryRepository;
import com.example.stroy.repository.ProductRepository;
import com.example.stroy.repository.UserRepository;
import com.example.stroy.util.Slugs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Locale;

/** Birinchi ishga tushishda admin foydalanuvchi va demo katalogni yaratadi. */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository users;
    private final CategoryRepository categories;
    private final ProductRepository products;
    private final PasswordEncoder encoder;

    @Value("${app.admin.email:admin@stroy.local}")
    private String adminEmail;
    @Value("${app.admin.password:Admin12345!}")
    private String adminPassword;
    @Value("${app.seed-demo-data:true}")
    private boolean seedDemoData;

    @Override
    @Transactional
    public void run(String... args) {
        createAdmin();
        if (seedDemoData && categories.count() == 0) {
            seedCatalog();
        }
    }

    private void createAdmin() {
        String email = adminEmail.trim().toLowerCase(Locale.ROOT);
        if (users.existsByEmail(email)) {
            return;
        }
        users.save(User.builder()
                .fullName("Administrator")
                .email(email)
                .password(encoder.encode(adminPassword))
                .role(Role.ADMIN)
                .build());
        log.warn("Admin yaratildi: {} (parolni darhol almashtiring!)", email);
    }

    private void seedCatalog() {
        Category cement = category("Цемент и сухие смеси", "Цемент, штукатурки, клеи и шпаклёвки для любых строительных работ");
        Category brick = category("Кирпич и блоки", "Кирпич строительный, газоблок, пеноблок, шлакоблок");
        Category metal = category("Арматура и металл", "Строительная арматура, профильные трубы, швеллер, уголок");
        Category roof = category("Кровля и водостоки", "Профнастил, металлочерепица, водосточные системы и доборные элементы");
        Category paint = category("Краски и лаки", "Фасадные и интерьерные краски, грунтовки, лаки, эмали");
        Category insulation = category("Тепло- и гидроизоляция", "Минеральная вата, пенопласт, экструдированный пенополистирол");

        product(cement, "Цемент М400 (50 кг)", "CEM-M400-50", "Yuqori sifatli portlandsement, 50 kg qop", "62000", "мешок", 20, 5000);
        product(cement, "Цемент М500 (50 кг)", "CEM-M500-50", "Tez qotuvchi markali portlandsement, 50 kg qop", "68000", "мешок", 20, 4000);
        product(cement, "Штукатурka гипсовая (30 кг)", "GYP-PLASTER-30", "Ichki devorlar uchun oq gipsli suvoq qorishmasi", "54000", "мешок", 10, 2500);
        product(cement, "Клей для плитки усиленный (25 кг)", "TILE-ADH-25", "Kafel va keramika uchun chidamli yelim", "42000", "мешок", 10, 3000);

        product(brick, "Кирпич красный полнотелый М150", "BRICK-RED-M150", "Qizil pishiq g'isht M150, yuk ko'taruvchi devorlar uchun", "1200", "шт", 500, 100000);
        product(brick, "Газоблок 600x300x200 D500", "AERATED-600", "Avtoklav gazobloki, issiqlikni a'lo darajada saqlaydi", "32000", "шт", 50, 8000);
        product(brick, "Шлакоблок 390x190x188", "CINDER-390", "Bo'shliqli devorbop shlakoblok", "6500", "шт", 100, 15000);

        product(metal, "Арматура А500С Ø12", "REBAR-A500-12", "Issiq prokatli davriy profilli armatura, 12 mm", "9800000", "т", 1, 120);
        product(metal, "Труба профильная 40x40x2", "TUBE-4040-2", "Kvadrat po'lat quvur, 6 metrlik", "38000", "м", 30, 2000);

        product(roof, "Профнастил С8 (оцинкованный)", "PROF-S8-ZN", "Tom va to'siqlar uchun ruxlangan profnastil", "55000", "м²", 50, 6000);
        product(roof, "Металлочерепица Монтеррей 0.45", "ROOF-MONT-45", "Polimer qoplamali tom yopish metallocherepitsasi", "78000", "м²", 30, 4500);

        product(paint, "Краска фасадная водоэмульсионная (20 кг)", "PAINT-EXT-20", "Ob-havoga chidamli tashqi fasad bo'yog'i", "450000", "ведро", 5, 600);
        product(paint, "Грунтовка глубокого проникновения (10 л)", "PRIMER-DP-10", "Devor va shiftlar uchun akril asosli gruntovka", "95000", "канистра", 5, 800);

        product(insulation, "Минеральная вата 50 мм (рулон)", "MIN-WOOL-50", "Ovoz va issiqlik izolyatsiyasi uchun bazalt paxtasi", "180000", "рулон", 10, 1200);
        product(insulation, "Пенопласт ПСБ-С 25 (1000x1000x50)", "FOAM-25-50", "Devor va pol izolyatsiyasi uchun polistirol ko'pik", "24000", "лист", 20, 2500);

        log.info("Demo katalog muvaffaqiyatli yaratildi (6 kategoriya, 15 mahsulot)");
    }

    private Category category(String name, String description) {
        return categories.save(Category.builder()
                .name(name)
                .slug(Slugs.slugify(name))
                .description(description)
                .build());
    }

    private void product(Category category, String name, String sku, String description, String price, String unit, int minQty, int stock) {
        products.save(Product.builder()
                .category(category)
                .name(name)
                .sku(sku)
                .description(description)
                .price(new BigDecimal(price))
                .unit(unit)
                .minOrderQuantity(minQty)
                .stock(stock)
                .active(true)
                .build());
    }
}
