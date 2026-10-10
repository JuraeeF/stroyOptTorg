package com.example.stroy.controller;

import com.example.stroy.dto.ContactRequest;
import com.example.stroy.dto.ContactResponse;
import com.example.stroy.service.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContactResponse send(@Valid @RequestBody ContactRequest request) {
        return contactService.create(request);
    }
}
