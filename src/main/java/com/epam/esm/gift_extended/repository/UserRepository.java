package com.epam.esm.gift_extended.repository;

import java.util.List;
import java.util.Optional;

import com.epam.esm.gift_extended.entity.User;

public interface UserRepository extends GiftRepository<User> {

    Optional<User> findRichestByOrderPriceSum();

    List<User> findByNameContains(String partOfName);
}
