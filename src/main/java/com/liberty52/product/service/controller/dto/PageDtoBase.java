package com.liberty52.product.service.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@NoArgsConstructor
public abstract class PageDtoBase<C> {
    private List<C> contents;
    private long totalCount;
    private int numberOfElements;
    private int pageNumber;
    private boolean hasPrev;
    private boolean hasNext;
    private boolean first;
    private boolean last;

    public PageDtoBase(Page<?> page, List<C> contents) {
        this.contents = contents;
        this.totalCount = page.getTotalElements();
        this.numberOfElements = page.getNumberOfElements();
        this.pageNumber = page.getNumber();
        this.hasPrev = page.hasPrevious();
        this.hasNext = page.hasNext();
        this.first = page.isFirst();
        this.last = page.isLast();
    }
}
