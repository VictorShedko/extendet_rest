package com.epam.esm.gift_extended.service.util;

import com.epam.esm.gift_extended.exception.BadPaginationException;

public class PageSortInfo {

    private int pageSize;
    private int pageNumber;
    private SortDirection sortDirection;

    private PageSortInfo(int pageNumber, int pageSize, SortDirection sortDirection) {
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

    public static PageSortInfo of(int page, int size, String sort) {
        validatePageNumber(page);
        validatePageSize(size);
        return new PageSortInfo(page, size, SortDirection.getByStringOrDefault(sort));
    }

    private static void validatePageNumber(int pageNumber) throws BadPaginationException {
        if(pageNumber<0){
            throw new BadPaginationException("page number must be positive. Given"+pageNumber);
        }
    }


    private static void validatePageSize(int pageSize) throws BadPaginationException {
        if(pageSize<=0){
            throw new BadPaginationException("page size must be positive. Given"+pageSize);
        }
    }
}
