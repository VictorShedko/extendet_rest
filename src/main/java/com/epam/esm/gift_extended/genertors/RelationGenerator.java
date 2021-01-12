package com.epam.esm.gift_extended.genertors;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.epam.esm.gift_extended.entity.Certificate;
import com.epam.esm.gift_extended.entity.Order;
import com.epam.esm.gift_extended.entity.Tag;
import com.epam.esm.gift_extended.entity.User;
import com.epam.esm.gift_extended.genertors.util.RandomUtil;

public class RelationGenerator {

    private static final Integer MAX_TAG_AMOUNT = 50;

    public List<Order> MakeRelation(List<User> users, List<Certificate> certificates, List<Tag> tags, int orderAmount) {
        List<Order> orders = IntStream.of(orderAmount).mapToObj(i -> {
            return new Order();
        }).peek(order -> order.setCertificates(List.of())).collect(Collectors.toList());
        certificates.forEach(cert -> {
            int tagsAmount = RandomUtil.getRandInt(MAX_TAG_AMOUNT) + 1;
            cert.setTags(RandomUtil.getRandomSubListWithSize(tagsAmount, tags));
        });
        return orders;
    }

}
