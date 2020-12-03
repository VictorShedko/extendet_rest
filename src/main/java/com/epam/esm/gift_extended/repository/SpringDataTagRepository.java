package com.epam.esm.gift_extended.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.epam.esm.gift_extended.entity.Order;
import com.epam.esm.gift_extended.entity.Tag;

@Repository
public interface SpringDataTagRepository extends PagingAndSortingRepository<Tag, Integer> {

    Optional<Tag> findByName(String name);

    @Query(value = "SELECT t.id,t.name " + ""
                   + "    FROM user as u "
                   + "    JOIN user_order on u.user_id=user_order.customer_user_id"
                   + "    JOIN user_order_certificates uoc on user_order.id = uoc.order_id "
                   + "    JOIN certificate as c on c.id=uoc.certificates_id"
                   + "    JOIN certificate_tags as ct on ct.certificate_id=c.id"
                   + "    JOIN tag as t on t.id=ct.tags_id " + "WHERE c.user_id="
                   + "(SELECT user.user_id,user.name " + "                     FROM user " + "           "
                   + "                         JOIN user_order as o on user.user_id = o.customer_user_id "
                   + "                 GROUP BY user.user_id " + "                 ORDER BY SUM(o.order_cost) DESC "
                   + "                 LIMIT 1 )"

                   + "GROUP BY t.id " + "    " + "ORDER BY COUNT(t.id) DESC " + "LIMIT 1",nativeQuery = true)
    Optional<Tag> findMostPopularTagFromRichestUserBySumOfCertificatePrice();

    boolean existsByName(String name);

    @Query("SELECT T FROM Certificate C inner join C.tags T where C.id=:certId order by T.name")
    List<Tag> findByCert(int certId, Pageable pageable);
}
