package com.example.stroy.controller.admin;

import com.example.stroy.dto.ContactResponse;
import com.example.stroy.dto.PageResponse;
import com.example.stroy.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/contacts")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Admin — Xabarlar", description = "Aloqa xabarlarini ko'rish va boshqarish (ADMIN)")
public class AdminContactController {

    private final ContactService contactService;

    @Operation(summary = "Xabarlar ro'yxati", description = "Barcha aloqa xabarlarini sahifalab qaytaradi")
    @ApiResponse(responseCode = "200", description = "Xabarlar ro'yxati")
    @GetMapping
    public PageResponse<ContactResponse> list(
            @Parameter(description = "Sahifa raqami") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Sahifadagi elementlar soni") @RequestParam(defaultValue = "20") int size) {
        return contactService.list(page, size);
    }

    @Operation(summary = "Xabarni qayta ishlangan deb belgilash", description = "Xabarning processed holatini o'zgartiradi")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Holat yangilandi"),
            @ApiResponse(responseCode = "404", description = "Xabar topilmadi")
    })
    @PatchMapping("/{id}/processed")
    public ContactResponse markProcessed(
            @Parameter(description = "Xabar ID raqami") @PathVariable Long id,
            @Parameter(description = "Qayta ishlangan holati (true/false)") @RequestParam(defaultValue = "true") boolean value) {
        return contactService.markProcessed(id, value);
    }
}
