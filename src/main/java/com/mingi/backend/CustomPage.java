package com.mingi.backend;

import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Getter
@Setter
public class CustomPage<T> {
    @Setter
    @Getter
    private List<T> data;
    private Pageable pageable;
    private int totalPages;
    private long totalElements;
    private boolean last;
    private int size;
    private int number;
    private boolean first;
    private boolean empty;

    public CustomPage(Page<T> page) {
        this.data = page.getContent();
        this.pageable = page.getPageable();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.last = page.isLast();
        this.size = page.getSize();
        this.number = page.getNumber();
        this.first = page.isFirst();
        this.empty = page.isEmpty();
    }
}
