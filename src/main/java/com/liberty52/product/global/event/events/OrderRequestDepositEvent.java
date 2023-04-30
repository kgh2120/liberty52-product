package com.liberty52.product.global.event.events;

import com.liberty52.product.global.adapter.mail.MailContentMaker;
import com.liberty52.product.global.adapter.mail.title.MailTitle;
import com.liberty52.product.service.entity.Orders;
import com.liberty52.product.service.event.Event;

public class OrderRequestDepositEvent extends SendMailEvent implements Event {

    public OrderRequestDepositEvent(String authEmail, String authName, Orders order) {
        this(authEmail, MailTitle.TITLE_REQ_DEPOSIT, MailContentMaker.makeOrderRequestDepositContent(authName, order), true);
    }

    public OrderRequestDepositEvent(String to, String title, String content, boolean isUseHtml) {
        super(to, title, content, isUseHtml);
    }

}
