package com.epam.esm.gift_extended.service.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.epam.esm.gift_extended.exception.BadPaginationException;

public class PageSortInfo {


    public static Pageable of(int page, int size, String sortDirection,String sortParam) {
        validatePageNumber(page);
        validatePageSize(size);
        Sort sort=Sort.by(sortParam);
        if(SortDirection.getByStringOrDefault(sortDirection)==SortDirection.DESC){
            sort=sort.descending();
        }
        return PageRequest.of(page, size,sort);
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
