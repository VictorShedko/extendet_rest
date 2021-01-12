package com.epam.esm.gift_extended.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.epam.esm.gift_extended.entity.Order;

@Repository
public interface SpringDataOrderRepository extends PagingAndSortingRepository<Order, Integer> {

    List<Order> findByCustomerUserId(Integer userId, Pageable pageSortInfo);

    List<Order> findByCustomerUserId(Integer customerId);
}
