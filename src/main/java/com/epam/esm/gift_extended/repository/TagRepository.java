package com.epam.esm.gift_extended.repository;

import java.util.Optional;

import com.epam.esm.gift_extended.entity.Tag;

public interface TagRepository extends GiftRepository<Tag>{

    Optional<Tag> findMostPopularTagFromRichestUserBySumOfCertificatePrice();
}
