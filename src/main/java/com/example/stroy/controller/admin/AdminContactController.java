package com.example.stroy.controller.admin;

import com.example.stroy.dto.ContactResponse;
import com.example.stroy.dto.PageResponse;
import com.example.stroy.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/contacts")
@RequiredArgsConstructor
public class AdminContactController {

    private final ContactService contactService;

    @GetMapping
    public PageResponse<ContactResponse> list(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "20") int size) {
        return contactService.list(page, size);
    }

    @PatchMapping("/{id}/processed")
    public ContactResponse markProcessed(@PathVariable Long id,
                                         @RequestParam(defaultValue = "true") boolean value) {
        return contactService.markProcessed(id, value);
    }
}
