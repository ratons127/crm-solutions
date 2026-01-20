package com.betopia.hrm.domain.base.response.pagination;

import org.springframework.data.domain.Page;

public class Links {

    private String first;
    private String last;
    private String prev;
    private String next;

    public static Links fromPage(Page<?> page, String basePath)
    {
        Links links = new Links();

        links.setFirst(basePath + "?page=1");
        links.setLast(basePath + "?page=" + page.getTotalPages());

        if (page.hasPrevious()) {
            links.setPrev(basePath + "?page=" + page.previousPageable().getPageNumber());
        }

        if (page.hasNext()) {
            links.setNext(basePath + "?page=" + page.nextPageable().getPageNumber());
        }

        return links;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getPrev() {
        return prev;
    }

    public void setPrev(String prev) {
        this.prev = prev;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }
}
