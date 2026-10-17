package com.example.stroy.controller;

import com.example.stroy.dto.ContactRequest;
import com.example.stroy.dto.ContactResponse;
import com.example.stroy.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
@Tag(name = "5. Qayta aloqa", description = "Mijozlardan murojaat va qayta aloqa xabarlari")
public class ContactController {

    private final ContactService contactService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Murojaat yuborish", description = "Saytdan qayta aloqa so'rovini qoldirish")
    public ContactResponse send(@Valid @RequestBody ContactRequest request) {
        return contactService.create(request);
    }
}
