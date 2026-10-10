package com.example.stroy.service;

import com.example.stroy.dto.PageResponse;
import com.example.stroy.dto.ProductRequest;
import com.example.stroy.dto.ProductResponse;
import com.example.stroy.entity.Category;
import com.example.stroy.entity.Product;
import com.example.stroy.exception.ApiException;
import com.example.stroy.repository.CategoryRepository;
import com.example.stroy.repository.ProductRepository;
import com.example.stroy.util.Mappers;
import com.example.stroy.util.Paging;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository products;
    private final CategoryRepository categories;

    public record Filter(Long categoryId, String q, BigDecimal minPrice, BigDecimal maxPrice,
                         boolean inStockOnly, boolean includeInactive) {
    }

    @Transactional(readOnly = true)
    public PageResponse<ProductResponse> search(Filter filter, String sort, int page, int size) {
        Page<Product> result = products.findAll(toSpec(filter), Paging.of(page, size, toSort(sort)));
        return PageResponse.from(result.map(Mappers::toProduct));
    }

    /** Ommaviy ko'rinish: faqat faol mahsulotlar. */
    @Transactional(readOnly = true)
    public ProductResponse get(Long id) {
        return products.findById(id)
                .filter(Product::isActive)
                .map(Mappers::toProduct)
                .orElseThrow(() -> ApiException.notFound("Mahsulot topilmadi"));
    }

    @Transactional
    public ProductResponse create(ProductRequest req) {
        String sku = req.sku().trim();
        if (products.existsBySku(sku)) {
            throw ApiException.conflict("Bunday artikul (SKU) mavjud");
        }
        Product product = new Product();
        apply(product, req, sku);
        return Mappers.toProduct(products.save(product));
    }

    @Transactional
    public ProductResponse update(Long id, ProductRequest req) {
        Product product = products.findById(id).orElseThrow(() -> ApiException.notFound("Mahsulot topilmadi"));
        String sku = req.sku().trim();
        if (products.existsBySkuAndIdNot(sku, id)) {
            throw ApiException.conflict("Bunday artikul (SKU) mavjud");
        }
        apply(product, req, sku);
        return Mappers.toProduct(product);
    }

    /** Buyurtmalar tarixi buzilmasligi uchun mahsulot o'chirilmaydi, faqat nofaol qilinadi. */
    @Transactional
    public void deactivate(Long id) {
        Product product = products.findById(id).orElseThrow(() -> ApiException.notFound("Mahsulot topilmadi"));
        product.setActive(false);
    }

    private void apply(Product p, ProductRequest req, String sku) {
        Category category = categories.findById(req.categoryId())
                .orElseThrow(() -> ApiException.badRequest("Kategoriya topilmadi"));
        p.setName(req.name().trim());
        p.setSku(sku);
        p.setDescription(req.description());
        p.setPrice(req.price());
        p.setUnit(req.unit().trim());
        p.setMinOrderQuantity(req.minOrderQuantity());
        p.setStock(req.stock());
        p.setImageUrl(req.imageUrl());
        p.setCategory(category);
        p.setActive(req.active() == null || req.active());
    }

    private static Sort toSort(String sort) {
        if (sort == null) sort = "newest";
        return switch (sort) {
            case "price_asc" -> Sort.by("price").ascending().and(Sort.by("id"));
            case "price_desc" -> Sort.by("price").descending().and(Sort.by("id"));
            case "name" -> Sort.by("name").ascending().and(Sort.by("id"));
            default -> Sort.by("createdAt").descending().and(Sort.by("id").descending());
        };
    }

    private static Specification<Product> toSpec(Filter f) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!f.includeInactive()) {
                predicates.add(cb.isTrue(root.get("active")));
            }
            if (f.categoryId() != null) {
                predicates.add(cb.equal(root.get("category").get("id"), f.categoryId()));
            }
            if (f.q() != null && !f.q().isBlank()) {
                String like = "%" + f.q().trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("name")), like),
                        cb.like(cb.lower(root.get("sku")), like)));
            }
            if (f.minPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), f.minPrice()));
            }
            if (f.maxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), f.maxPrice()));
            }
            if (f.inStockOnly()) {
                predicates.add(cb.greaterThan(root.get("stock"), 0));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
