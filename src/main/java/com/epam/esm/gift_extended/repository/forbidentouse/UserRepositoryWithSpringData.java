package com.epam.esm.gift_extended.repository.forbidentouse;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.epam.esm.gift_extended.entity.User;

public interface UserRepositoryWithSpringData extends PagingAndSortingRepository<User, Integer> {

    @Query(value ="SELECT gift_ex.user.name,gift_ex.user.user_id" + ""
            + "    FROM gift_ex.user"
            + "    JOIN gift_ex.certificate as c on user.user_id = c.user_id"
            + "    GROUP BY c.user_id"
            + "    ORDER BY SUM(c.price) DESC"
            + "    LIMIT 1", nativeQuery = true)
    Optional<User> findRichestByOrderPriceSum();

    Optional<User> findByName(String name);
    List<User> findByNameContains(String partOfName);


}
