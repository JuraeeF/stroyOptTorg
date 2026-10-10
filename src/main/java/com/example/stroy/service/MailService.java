package com.example.stroy.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/** Adminga xabar yuboradi. app.mail.enabled=false bo'lsa hech narsa qilmaydi. */
@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final ObjectProvider<JavaMailSender> senderProvider;

    @Value("${app.mail.enabled:false}")
    private boolean enabled;
    @Value("${app.mail.to:}")
    private String to;
    @Value("${app.mail.from:no-reply@stroy.local}")
    private String from;

    @Async
    public void send(String subject, String text) {
        if (!enabled || to == null || to.isBlank()) {
            return;
        }
        JavaMailSender sender = senderProvider.getIfAvailable();
        if (sender == null) {
            log.warn("Mail yoqilgan, lekin spring.mail.host sozlanmagan");
            return;
        }
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            sender.send(message);
        } catch (Exception e) {
            log.warn("Mail yuborilmadi: {}", e.getMessage());
        }
    }
}
