package com.liberty52.product.global.adapter.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailSenderImpl implements MailSender {
    private static final String UTF_8 = "UTF-8";
    private final JavaMailSender sender;
    @Value("${liberty52.mail.username}")
    private String OUR_ADDRESS;
    @Value("${liberty52.mail.nickname}")
    private String NICKNAME;
    private String fromAddress;
    private MimeMessage message;
    private MimeMessageHelper messageHelper;

    private void initMailSender() throws MessagingException {
        resetMailSender();

        // Liberty52 <mju.omnm@gmail.com>
        this.fromAddress = this.NICKNAME + " <" + this.OUR_ADDRESS + ">";
        this.message = this.sender.createMimeMessage();
        this.messageHelper = new MimeMessageHelper(this.message, true, UTF_8);
    }

    private void resetMailSender() {
        this.fromAddress = null;
        this.message = null;
        this.messageHelper = null;
    }

    @Override
    public void prepareAndSend(Mail mail) throws MessagingException {
        this.prepare(mail);
        this.send();
    }

    @Override
    public void prepare(Mail mail) throws MessagingException {
        initMailSender();

        this.messageHelper.setFrom(this.fromAddress);
        this.messageHelper.setTo(mail.getTo());
        this.messageHelper.setSubject(mail.getTitle());
        this.messageHelper.setText(mail.getContent(), mail.isUseHtml());
    }

    @Override
    public void send() {
        try {
            this.sender.send(this.message);
        } catch (MailSendException | MailAuthenticationException e) {
            log.error("Mail Sender Error: {}", e.getMessage());
            throw e;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    public static class MailImpl implements Mail {
        String to;
        String title;
        String content;
        boolean useHtml;
    }
}
