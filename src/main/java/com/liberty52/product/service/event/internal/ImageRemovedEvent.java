package com.liberty52.product.service.event.internal;

import com.liberty52.product.service.event.internal.dto.ImageRemovedEventDto;

public class ImageRemovedEvent extends InternalEventBase<ImageRemovedEventDto> {
    public ImageRemovedEvent(Object source, ImageRemovedEventDto body) {
        super(source, body);
    }
}
