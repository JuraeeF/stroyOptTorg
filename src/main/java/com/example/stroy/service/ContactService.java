package com.example.stroy.service;

import com.example.stroy.dto.ContactRequest;
import com.example.stroy.dto.ContactResponse;
import com.example.stroy.dto.PageResponse;
import com.example.stroy.entity.ContactMessage;
import com.example.stroy.exception.ApiException;
import com.example.stroy.repository.ContactMessageRepository;
import com.example.stroy.util.Mappers;
import com.example.stroy.util.Paging;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactMessageRepository repository;
    private final MailService mailService;

    @Transactional
    public ContactResponse create(ContactRequest req) {
        ContactMessage saved = repository.save(ContactMessage.builder()
                .name(req.name().trim())
                .phone(req.phone().trim())
                .message(req.message())
                .build());
        mailService.send("Yangi murojaat: " + saved.getName(),
                "Telefon: " + saved.getPhone() + "\n\n" + (saved.getMessage() == null ? "" : saved.getMessage()));
        return Mappers.toContact(saved);
    }

    @Transactional(readOnly = true)
    public PageResponse<ContactResponse> list(int page, int size) {
        var pageable = Paging.of(page, size, Sort.by("createdAt").descending());
        return PageResponse.from(repository.findAll(pageable).map(Mappers::toContact));
    }

    @Transactional
    public ContactResponse markProcessed(Long id, boolean processed) {
        ContactMessage m = repository.findById(id).orElseThrow(() -> ApiException.notFound("Murojaat topilmadi"));
        m.setProcessed(processed);
        return Mappers.toContact(m);
    }
}
