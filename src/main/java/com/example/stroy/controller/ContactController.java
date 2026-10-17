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
@Tag(name = "Aloqa", description = "Bog'lanish xabarlari — ism, telefon va xabar yuboring")
public class ContactController {

    private final ContactService contactService;

    @Operation(summary = "Xabar yuborish", description = "Mijoz ism, telefon va xabar matnini yuboradi. Autentifikatsiya talab qilinmaydi.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Xabar muvaffaqiyatli yuborildi"),
            @ApiResponse(responseCode = "400", description = "Validatsiya xatosi")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContactResponse send(@Valid @RequestBody ContactRequest request) {
        return contactService.create(request);
    }
}
