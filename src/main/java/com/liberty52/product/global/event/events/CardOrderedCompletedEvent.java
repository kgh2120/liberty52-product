package com.liberty52.product.global.event.events;

import com.liberty52.product.global.adapter.mail.MailContentMaker;
import com.liberty52.product.global.adapter.mail.title.MailTitle;
import com.liberty52.product.service.entity.Orders;
import com.liberty52.product.service.event.Event;

public class CardOrderedCompletedEvent extends SendMailEvent implements Event {

    public CardOrderedCompletedEvent(Orders order) {
        this(order.getOrderDestination().getReceiverEmail(),
                MailTitle.TITLE_CARD_ORDERED_COMPLETED,
                MailContentMaker.makeCardOrderedCompletedContent(order.getOrderDestination().getReceiverName(), order),
                true);
    }

    public CardOrderedCompletedEvent(String to, String title, String content, boolean isUseHtml) {
        super(to, title, content, isUseHtml);
    }
}
