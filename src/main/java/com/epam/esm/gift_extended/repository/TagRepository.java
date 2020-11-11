package com.epam.esm.gift_extended.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.epam.esm.gift_extended.entity.Tag;

public interface TagRepository extends PagingAndSortingRepository<Tag,Integer> {

    Optional<Tag> findByName(String name);

    @Query(value = "SELECT t.id,t.name "
            + "FROM gift_ex.user as u "
            + "    JOIN gift_ex.certificate as c on c.user_id=u.user_id"
            + "    JOIN gift_ex.certificate_tags as ct on ct.certificate_id=c.id"
            + "    JOIN gift_ex.tag as t on t.id=ct.tags_id "
            + "WHERE c.user_id=(SELECT gift_ex.user.user_id "
            + "                 FROM gift_ex.user "
            + "           "
            + "          JOIN gift_ex.certificate as c on user.user_id = c.user_id "
            + "                 GROUP BY c.user_id "
            + "                     ORDER BY SUM(c.price) DESC "
            + "                 LIMIT 1) "
            + "    GROUP BY t.id "
            + "    ORDER BY COUNT(t.id) DESC "
            + "    LIMIT 1", nativeQuery = true)
    Optional<Tag> findMostPopularTagFromRichestUserBySumOfCertificatePrice();

}
