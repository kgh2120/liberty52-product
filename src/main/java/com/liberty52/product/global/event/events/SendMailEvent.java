package com.liberty52.product.global.event.events;

import com.liberty52.product.service.event.Event;
import lombok.Getter;

@Getter
public class SendMailEvent implements Event {

    private String to;
    private String title;
    private String content;
    private boolean isUseHtml;

    public SendMailEvent(String to, String title, String content, boolean isUseHtml) {
        this.to = to;
        this.title = title;
        this.content = content;
        this.isUseHtml = isUseHtml;
    }

}
