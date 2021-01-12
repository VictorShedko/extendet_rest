package com.epam.esm.gift_extended.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.epam.esm.gift_extended.entity.Certificate;
import com.epam.esm.gift_extended.entity.Order;
import com.epam.esm.gift_extended.entity.User;
import com.epam.esm.gift_extended.exception.ResourceNotFoundedException;
import com.epam.esm.gift_extended.service.CertificateService;
import com.epam.esm.gift_extended.service.OrderService;
import com.epam.esm.gift_extended.service.UserService;

@Service
public class JWTUserDetailsService implements UserDetailsService {

    private  UserService userService;
    private  CertificateService certificateService;
    private  OrderService orderService;
    private  JWTUserFactory factory;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setCertificateService(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setFactory(JWTUserFactory factory) {
        this.factory = factory;
    }

    @Override
    public JWTUser loadUserByUsername(String s) throws UsernameNotFoundException {
        User user;
        List<Certificate> certs;
        List<Order> orders;
        try {
            user = userService.findByName(s);
            orders=orderService.getOrdersByUserId(user.getId());
        } catch (ResourceNotFoundedException ex) {
            throw new UsernameNotFoundException("User not founded ", ex);
        }
        return factory.createToken(user,orders);

    }
}
