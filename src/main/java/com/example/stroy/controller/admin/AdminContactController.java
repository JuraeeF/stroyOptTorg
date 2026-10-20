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

import static com.example.stroy.config.OpenApiConfig.BEARER_AUTH;

@RestController
@RequestMapping("/api/admin/contacts")
@RequiredArgsConstructor
@SecurityRequirement(name = BEARER_AUTH)
@Tag(name = "8. Admin — Aloqa xabarlari", description = "Mijoz murojaatlarini ko'rish va qayta ishlash (Faqat ADMIN)")
public class AdminContactController {

    private final ContactService contactService;

    @Operation(summary = "Mijoz xabarlari ro'yxati", description = "Aloqa formasi orqali yuborilgan barcha xabarlarni sahifalab ko'rsatish")
    @ApiResponse(responseCode = "200", description = "Xabarlar ro'yxati olindi")
    @GetMapping
    public PageResponse<ContactResponse> list(
            @Parameter(description = "Sahifa raqami") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Sahifa hajmi") @RequestParam(defaultValue = "20") int size) {
        return contactService.list(page, size);
    }

    @Operation(summary = "Xabarni qayta ishlangan deb belgilash", description = "Mijoz bilan bog'lanilgandan so'ng xabarni processed=true/false qilish")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Holat o'zgartirildi"),
            @ApiResponse(responseCode = "404", description = "Xabar topilmadi")
    })
    @PatchMapping("/{id}/processed")
    public ContactResponse markProcessed(
            @Parameter(description = "Xabar ID raqami") @PathVariable Long id,
            @Parameter(description = "Qayta ishlanganlik holati (true yoki false)") @RequestParam(defaultValue = "true") boolean value) {
        return contactService.markProcessed(id, value);
    }
}
