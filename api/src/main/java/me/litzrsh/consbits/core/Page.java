package me.litzrsh.consbits.core;

import lombok.Getter;

import java.util.List;

@SuppressWarnings({"unused"})
@Getter
public class Page<T> {

    private final int page;
    private final int perPage;
    private final long totalItems;
    private final List<T> contents;

    public <S extends SearchParams> Page(List<T> contents, S params, long totalItems) {
        this.page = params.getPage();
        this.perPage = params.getPerPage();
        this.totalItems = totalItems;
        this.contents = contents;
    }

    public long getTotalPages() {
        long totalPages = (long) Math.ceil((double) totalItems / (double) perPage);
        return Math.max(1, totalPages);
    }
}
