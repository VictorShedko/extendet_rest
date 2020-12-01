package com.epam.esm.gift_extended.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epam.esm.gift_extended.entity.Certificate;
import com.epam.esm.gift_extended.entity.Order;
import com.epam.esm.gift_extended.entity.User;
import com.epam.esm.gift_extended.exception.ResourceNotFoundedException;
import com.epam.esm.gift_extended.repository.OrderRepository;
import com.epam.esm.gift_extended.service.util.PageSortInfo;

@Service
public class OrderService implements GiftService<Order> {

    private OrderRepository repository;

    private CertificateService certificateService;

    @Autowired
    public void setCertificateService(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private UserService userService;

    @Autowired
    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Order createOrder(Integer userId, List<Integer> certIds) {
        Order order = new Order();
        User user = userService.findById(userId);
        List<Certificate> certificates = certIds.stream()
                .map(certId -> certificateService.findById(certId))
                .collect(Collectors.toList());
        order.setCustomer(user);
        order.setCertificates(certificates);
        order.setOrderCost(countOrderCost(order));
        order.setOrderDate(new Date());
        save(order);
        return order;
    }

    private Float countOrderCost(Order order) {
        return (float) order.getCertificates().stream().mapToDouble(Certificate::getPrice).sum();
    }

    public List<Order> getOrdersByUserId(Integer userId, int page, int size, String sort) {
        PageSortInfo pageSortInfo = PageSortInfo.of(page, size, sort);
        return repository.findByUserId(userId, pageSortInfo);
    }

    @Override
    public List<Order> allWithPagination(int from, int amount, String sort) {
        PageSortInfo pageSortInfo = PageSortInfo.of(from, amount, sort);
        return repository.findAll(pageSortInfo);
    }

    @Override
    public Order findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundedException("order with id", id.toString()));
    }


    @Override
    public void save(Order order) {
        repository.save(order);
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        repository.findById(id).ifPresent(repository::delete);
    }

    @Override
    public Iterable<Order> all() {
        return repository.findAll();
    }

    @Override
    public long countEntities() {
        return repository.count();
    }

    @Override
    public boolean isExist(Order order) {
        return repository.isExist(order);
    }
}
