package com.epam.esm.gift_extended.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epam.esm.gift_extended.entity.Certificate;
import com.epam.esm.gift_extended.entity.Order;
import com.epam.esm.gift_extended.entity.User;
import com.epam.esm.gift_extended.exception.EntityAlreadyAssignedException;
import com.epam.esm.gift_extended.exception.ResourceNotFoundedException;
import com.epam.esm.gift_extended.repository.SpringDataOrderRepository;
import com.epam.esm.gift_extended.service.util.PageSortInfo;

@Service
public class OrderService implements GiftService<Order> {

    private static final String SORT_PARAM = "id";

    private SpringDataOrderRepository repository;

    private CertificateService certificateService;
    private UserService userService;

    @Autowired
    public void setCertificateService(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public OrderService(SpringDataOrderRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Order createOrder(Integer userId, List<Integer> certIds) {
        Order order = new Order();
        User user = userService.findById(userId);

        List<Certificate> orderedCertificates = certIds.stream()
                .map(certId -> certificateService.findById(certId))
                .collect(Collectors.toList());
        List<Certificate> userCertificates = certificateService.findCertificatesByUser(userId);
        for(Certificate certificate:orderedCertificates) {
            if (userCertificates.contains(certificate)) {
                throw new EntityAlreadyAssignedException("some certificate already assigned to this user");
            }
        }
        order.setCustomer(user);
        order.setCertificates(orderedCertificates);
        order.setOrderCost(countOrderCost(order));
        order.setOrderDate(new Date());
        save(order);
        return order;
    }

    private Float countOrderCost(Order order) {
        return (float) order.getCertificates().stream().mapToDouble(Certificate::getPrice).sum();
    }

    public List<Order> getOrdersByUserId(Integer userId, int page, int size, String sort) {
        Pageable pageSortInfo = PageSortInfo.of(page, size, sort, SORT_PARAM);
        return repository.findByCustomerUserId(userId, pageSortInfo);
    }

    public List<Order> getOrdersByUserId(Integer userId) {

        return repository.findByCustomerUserId(userId);
    }

    @Override
    public List<Order> allWithPagination(int from, int amount, String sort) {
        Pageable pageSortInfo = PageSortInfo.of(from, amount, sort, SORT_PARAM);
        return repository.findAll(pageSortInfo).toList();
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
        return repository.existsById(order.getId());
    }
}
