package com.epam.esm.gift_extended.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.epam.esm.gift_extended.entity.Order;
import com.epam.esm.gift_extended.entity.User;
import com.epam.esm.gift_extended.service.util.PageSortInfo;

@Repository
public interface SpringDataUserRepository extends PagingAndSortingRepository<User, Integer> {

    List<User> findByNameContains(String pattern, Pageable pageable);

    Optional<User> findByName(String name);

    @Query(value = "SELECT user.user_id,user.name,user.password,user.role " + "                     FROM user " + "           "
                   + "                         JOIN user_order as o on user.user_id = o.customer_user_id "
                   + "                 GROUP BY user.user_id "
                   + "                 ORDER BY SUM(o.order_cost) DESC "
                   + "                 LIMIT 1 ",nativeQuery = true)
    Optional<User> findRichestByOrderPriceSum();

}
