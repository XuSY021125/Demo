package com.java.mod.entity;

import lombok.Data;

import java.util.List;

@Data
public class Page<T> {

    private int currentPage; // 当前页号
    private int size; // 每页大小
    private long totalElements; // 总记录数
    private List<T> content; // 当前页的数据

    public Page(int currentPage, int size, long totalElements, List<T> content) {
        this.currentPage = currentPage;
        this.size = size;
        this.totalElements = totalElements;
        this.content = content;
    }
}
