package com.epam.esm.gift_extended.service.util;


public class PageSortInfo {
    private int pageSize;
    private int pageNumber;
    private SortDirection sortDirection;

    public PageSortInfo(int pageNumber, int pageSize, SortDirection sortDirection) {
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
        this.sortDirection = sortDirection;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public SortDirection getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(SortDirection sortDirection) {
        this.sortDirection = sortDirection;
    }

    public static PageSortInfo of(int page, int size, String sort){
        return new PageSortInfo( page,size, SortDirection.getByStringOrDefault(sort));
    }
}
