package com.epam.esm.gift_extended.repository;

import java.util.List;

import com.epam.esm.gift_extended.entity.Order;
import com.epam.esm.gift_extended.service.util.PageSortInfo;

public interface OrderRepository extends GiftRepository<Order> {

    List<Order> findByUserId(Integer userId, PageSortInfo pageSortInfo);

    List<Order> findByUserId(Integer userId);
}

