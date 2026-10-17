package com.example.stroy.controller.admin;

import com.example.stroy.dto.ContactResponse;
import com.example.stroy.dto.PageResponse;
import com.example.stroy.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.example.stroy.config.OpenApiConfig.BEARER_AUTH;

@RestController
@RequestMapping("/api/admin/contacts")
@RequiredArgsConstructor
@SecurityRequirement(name = BEARER_AUTH)
@Tag(name = "8. Admin - Murojaatlar", description = "Mijozlardan tushgan murojaatlarni boshqarish (Faqat ADMIN)")
public class AdminContactController {

    private final ContactService contactService;

    @GetMapping
    @Operation(summary = "Murojaatlar ro'yxati", description = "Yangi va ko'rib chiqilgan murojaatlar ro'yxati")
    public PageResponse<ContactResponse> list(
            @Parameter(description = "Sahifa raqami") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Sahifa hajmi") @RequestParam(defaultValue = "20") int size) {
        return contactService.list(page, size);
    }

    @PatchMapping("/{id}/processed")
    @Operation(summary = "Murojaatni ko'rib chiqilgan deb belgilash")
    public ContactResponse markProcessed(
            @PathVariable Long id,
            @Parameter(description = "Ko'rib chiqilganlik holati (true/false)") @RequestParam(defaultValue = "true") boolean value) {
        return contactService.markProcessed(id, value);
    }
}
