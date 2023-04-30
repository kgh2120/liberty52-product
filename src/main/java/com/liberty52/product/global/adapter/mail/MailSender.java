package com.liberty52.product.global.adapter.mail;

import jakarta.mail.MessagingException;

public interface MailSender {

    void prepareAndSend(Mail mail) throws MessagingException;
    void prepare(Mail mail) throws MessagingException;
    void send();

    static Mail buildMail(String to, String title, String content, boolean useHtml) {
        return new MailSenderImpl.MailImpl(to, title, content, useHtml);
    }

    interface Mail {

        /**
         * 수신자 이메일 주소
         */
        String getTo();

        /**
         * 메일 제목
         */
        String getTitle();

        /**
         * 메일 내용
         */
        String getContent();

        /**
         * 메일의 HTML 태그 사용 여부
         */
        boolean isUseHtml();

    }

}
