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

/** Birinchi ishga tushishda admin foydalanuvchi va (ixtiyoriy) demo katalogni yaratadi. */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository users;
    private final CategoryRepository categories;
    private final ProductRepository products;
    private final PasswordEncoder encoder;

    @Value("${app.admin.email}")
    private String adminEmail;
    @Value("${app.admin.password}")
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
        Category cement = category("Цемент и сухие смеси", "Цемент, штукатурки, клеи и шпаклёвки");
        Category brick = category("Кирпич и блоки", "Кирпич, газоблок, пеноблок");
        Category metal = category("Арматура и металл", "Арматура, трубы, профили");
        Category roof = category("Кровля", "Профнастил, металлочерепица, водостоки");
        Category paint = category("Краски и лаки", "Краски, грунтовки, лаки");

        product(cement, "Цемент М400 (50 кг)", "CEM-M400-50", "62000", "мешок", 20, 5000);
        product(cement, "Цемент М500 (50 кг)", "CEM-M500-50", "68000", "мешок", 20, 4000);
        product(cement, "Штукатурка гипсовая (30 кг)", "GYP-PLASTER-30", "54000", "мешок", 10, 2500);
        product(brick, "Кирпич красный М150", "BRICK-RED-M150", "1200", "шт", 500, 100000);
        product(brick, "Газоблок 600x300x200", "AERATED-600", "32000", "шт", 50, 8000);
        product(metal, "Арматура А500С Ø12", "REBAR-A500-12", "9800000", "т", 1, 120);
        product(roof, "Профнастил С8 (оцинкованный)", "PROF-S8-ZN", "55000", "м²", 50, 6000);
        product(paint, "Краска водоэмульсионная (20 кг)", "PAINT-WB-20", "450000", "ведро", 5, 600);
        log.info("Demo katalog yaratildi");
    }

    private Category category(String name, String description) {
        return categories.save(Category.builder()
                .name(name)
                .slug(Slugs.slugify(name))
                .description(description)
                .build());
    }

    private void product(Category category, String name, String sku, String price, String unit, int minQty, int stock) {
        products.save(Product.builder()
                .category(category)
                .name(name)
                .sku(sku)
                .price(new BigDecimal(price))
                .unit(unit)
                .minOrderQuantity(minQty)
                .stock(stock)
                .active(true)
                .build());
    }
}
