package com.epam.esm.gift_extended.security;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.epam.esm.gift_extended.entity.Role;
import com.epam.esm.gift_extended.entity.User;

@Component
public class JWTUserFactory {
    private Map<Role, List<GrantedAuthority>> authorityByRole;


    public JWTUserFactory() {
        authorityByRole=new HashMap<>();
        GrantedAuthority adminAuthority=new SimpleGrantedAuthority(Role.ADMIN.getRoleAsString());
        GrantedAuthority userAuthority=new SimpleGrantedAuthority(Role.USER.getRoleAsString());

        authorityByRole.put(Role.ADMIN,List.of(adminAuthority,userAuthority));
        authorityByRole.put(Role.USER,List.of(userAuthority));
        authorityByRole.put(Role.GUEST,List.of());
    }

    public JWTUser createToken(User user){
        JWTUser jwtUser=new JWTUser();
        List<GrantedAuthority> authorities=authorityByRole.get(user.getRole());
        jwtUser.setAuthorities(authorities);
        jwtUser.setPassword(user.getPassword());
        jwtUser.setUsername(user.getName());
        jwtUser.setId(user.getId());
        jwtUser.setRole(user.getRole());
        return jwtUser;
    }
}
