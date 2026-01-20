package com.betopia.hrm.domain.base.response.pagination;

import org.springframework.data.domain.Page;

public class Meta {

    private int currentPage;
    private int from;
    private int lastPage;
    private String path;
    private int perPage;
    private int to;
    private int total;

    public static Meta fromPage(Page<?> page, String path) {
        Meta meta = new Meta();
        int currentPage = page.getNumber() + 1;
        int from = page.getNumber() * page.getSize() + 1;
        int to = from + page.getNumberOfElements() - 1;

        meta.setCurrentPage(currentPage);
        meta.setFrom(from);
        meta.setLastPage(page.getTotalPages());
        meta.setPath(path);
        meta.setPerPage(page.getSize());
        meta.setTo(to);
        meta.setTotal((int) page.getTotalElements());

        return meta;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }


    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getPerPage() {
        return perPage;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
