package com.liberty52.product.service.event.internal;

import com.liberty52.product.service.event.Event;

public interface InternalEvent<T> extends Event {
    T getBody();
}
