package com.liberty52.product.global.event.events;

import com.liberty52.product.global.adapter.mail.MailContentMaker;
import com.liberty52.product.global.adapter.mail.config.MailConstants;
import com.liberty52.product.service.entity.Orders;
import com.liberty52.product.service.event.Event;

public class OrderCanceledEvent extends SendMailEvent implements Event {

    private static final String CONTENT_TITLE = "주문이 취소되었습니다";

    public static OrderCanceledEvent toCustomer(Orders order) {
        return new OrderCanceledEvent(
                order.getOrderDestination().getReceiverEmail(),
                MailConstants.Title.Customer.ORDER_CANCELED,
                MailContentMaker.makeOrderCanceledContent(CONTENT_TITLE,order),
                true);
    }

    public static OrderCanceledEvent toAdmin(Orders order, String adminEmailAddr) {
        return new OrderCanceledEvent(
                adminEmailAddr,
                MailConstants.Title.Admin.ORDER_CANCELED(order.getOrderDestination().getReceiverName()),
                MailContentMaker.makeOrderCanceledContent(CONTENT_TITLE, order),
                true);
    }

    public OrderCanceledEvent(String to, String title, String content, boolean isUseHtml) {
        super(to, title, content, isUseHtml);
    }
}
