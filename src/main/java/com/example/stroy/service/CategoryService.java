package com.example.stroy.service;

import com.example.stroy.dto.CategoryRequest;
import com.example.stroy.dto.CategoryResponse;
import com.example.stroy.entity.Category;
import com.example.stroy.exception.ApiException;
import com.example.stroy.repository.CategoryRepository;
import com.example.stroy.repository.ProductRepository;
import com.example.stroy.util.Mappers;
import com.example.stroy.util.Slugs;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categories;
    private final ProductRepository products;

    @Transactional(readOnly = true)
    public List<CategoryResponse> list() {
        return categories.findAll(Sort.by("name")).stream().map(Mappers::toCategory).toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponse get(Long id) {
        return Mappers.toCategory(find(id));
    }

    @Transactional
    public CategoryResponse create(CategoryRequest req) {
        String name = req.name().trim();
        if (categories.existsByNameIgnoreCase(name)) {
            throw ApiException.conflict("Bunday nomli kategoriya mavjud");
        }
        Category category = Category.builder()
                .name(name)
                .slug(uniqueSlug(baseSlug(req), null))
                .description(req.description())
                .imageUrl(req.imageUrl())
                .build();
        return Mappers.toCategory(categories.save(category));
    }

    @Transactional
    public CategoryResponse update(Long id, CategoryRequest req) {
        Category category = find(id);
        String name = req.name().trim();
        if (categories.existsByNameIgnoreCaseAndIdNot(name, id)) {
            throw ApiException.conflict("Bunday nomli kategoriya mavjud");
        }
        category.setName(name);
        if (req.slug() != null && !req.slug().isBlank()) {
            category.setSlug(uniqueSlug(Slugs.slugify(req.slug()), id));
        }
        category.setDescription(req.description());
        category.setImageUrl(req.imageUrl());
        return Mappers.toCategory(category);
    }

    @Transactional
    public void delete(Long id) {
        Category category = find(id);
        if (products.countByCategoryId(id) > 0) {
            throw ApiException.conflict("Kategoriyada mahsulotlar bor, avval ularni ko'chiring yoki o'chiring");
        }
        categories.delete(category);
    }

    private Category find(Long id) {
        return categories.findById(id).orElseThrow(() -> ApiException.notFound("Kategoriya topilmadi"));
    }

    private String baseSlug(CategoryRequest req) {
        String source = (req.slug() != null && !req.slug().isBlank()) ? req.slug() : req.name();
        return Slugs.slugify(source);
    }

    private String uniqueSlug(String base, Long ignoreId) {
        String candidate = base;
        int n = 2;
        while (categories.findBySlug(candidate).filter(c -> !c.getId().equals(ignoreId)).isPresent()) {
            candidate = base + "-" + n++;
        }
        return candidate;
    }
}
