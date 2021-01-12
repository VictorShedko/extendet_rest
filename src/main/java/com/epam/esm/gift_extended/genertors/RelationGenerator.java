package com.epam.esm.gift_extended.genertors;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.epam.esm.gift_extended.entity.Certificate;
import com.epam.esm.gift_extended.entity.Order;
import com.epam.esm.gift_extended.entity.Tag;
import com.epam.esm.gift_extended.entity.User;
import com.epam.esm.gift_extended.genertors.util.RandomUtil;
import com.epam.esm.gift_extended.service.OrderService;

public class RelationGenerator {

    private static final Integer MAX_TAG_AMOUNT = 50;

    public List<Order> MakeRelation(List<User> users, List<Certificate> certificates, List<Tag> tags, int orderAmount) {
        List<Order> orders = IntStream.of(orderAmount).mapToObj(i -> {
            return new Order();
        }).peek(order -> order.getCertificates().add(RandomUtil.getRandomElementFromList(certificates))).peek(order -> {
            RandomUtil.getRandInt(3);
            IntStream.range(0, 3).forEach(n -> {
                List<Certificate> certs = order.getCertificates();
                Certificate newCert = RandomUtil.getRandomElementFromList(certificates);
                if (!certs.contains(newCert)) {
                    certs.add(newCert);
                }
            });
        }).peek(order -> {
            order.setCustomer(RandomUtil.getRandomElementFromList(users));
        }).peek(order -> {
            order.setOrderDate(new Date());
        }).peek(order -> {
            order.setOrderCost((float) order.getCertificates().stream().mapToDouble(Certificate::getPrice).sum());
        }).collect(Collectors.toList()); certificates.forEach(cert -> {
            int tagsAmount = RandomUtil.getRandInt(MAX_TAG_AMOUNT) + 1;
            cert.setTags(RandomUtil.getRandomSubListWithSize(tagsAmount, tags));
        });
        return orders;
    }

}
