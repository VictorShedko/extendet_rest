package com.epam.esm.gift_extended.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.epam.esm.gift_extended.entity.Tag;


public interface TagRepository extends GiftRepository<Tag>{

    Optional<Tag> findMostPopularTagFromRichestUserBySumOfCertificatePrice();
}
