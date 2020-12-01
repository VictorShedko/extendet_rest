package com.epam.esm.gift_extended.repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.epam.esm.gift_extended.service.util.PageSortInfo;

public class RepositoryUtil {
    static Query addPaginationToQuery(EntityManager manager,PageSortInfo pagination,String queue){
        Query query = manager.createQuery(
                queue+" "+ pagination.getSortDirection().getTypeAsString());
        query.setFirstResult(pagination.getPageSize() * pagination.getPageNumber());
        query.setMaxResults(pagination.getPageSize());
        return query;
    }

    private RepositoryUtil() {
    }
}
