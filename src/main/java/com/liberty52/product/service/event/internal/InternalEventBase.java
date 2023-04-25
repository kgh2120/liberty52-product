package com.liberty52.product.service.event.internal;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public abstract class InternalEventBase<T> extends ApplicationEvent implements InternalEvent<T> {

    protected T body;

    public InternalEventBase(Object source, T body) {
        super(source);
        this.body = body;
    }

}
