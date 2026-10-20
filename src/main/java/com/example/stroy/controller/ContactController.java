package com.example.stroy.controller;

import com.example.stroy.dto.ContactRequest;
import com.example.stroy.dto.ContactResponse;
import com.example.stroy.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
@Tag(name = "5. Aloqa / Bog'lanish", description = "Sayt orqali mijoz murojaatlari va xabarlar")
public class ContactController {

    private final ContactService contactService;

    @Operation(summary = "Aloqa formasidan xabar yuborish", description = "Mijoz o'z ismi, telefoni va xabar matnini qoldiradi. Avtorizatsiya talab qilinmaydi.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Xabar muvaffaqiyatli qabul qilindi"),
            @ApiResponse(responseCode = "400", description = "Validatsiya xatosi (ism yoki telefon bo'sh)")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContactResponse send(@Valid @RequestBody ContactRequest request) {
        return contactService.create(request);
    }
}
