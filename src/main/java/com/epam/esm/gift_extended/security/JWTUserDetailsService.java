package com.epam.esm.gift_extended.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.epam.esm.gift_extended.entity.User;
import com.epam.esm.gift_extended.exception.ResourceNotFoundedException;
import com.epam.esm.gift_extended.service.UserService;

@Service
public class JWTUserDetailsService implements UserDetailsService {

    private final UserService userService;

    private final JWTUserFactory factory;

    @Autowired
    public JWTUserDetailsService(UserService userService, JWTUserFactory factory) {
        this.userService = userService;
        this.factory = factory;
    }

    @Override
    public JWTUser loadUserByUsername(String s) throws UsernameNotFoundException {
        User user;
        try {
            user= userService.findByName(s);
        }catch (ResourceNotFoundedException ex){
            throw new  UsernameNotFoundException("User not founded ",ex);
        }
        return factory.createToken(user);

    }
}
