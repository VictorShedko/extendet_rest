package com.epam.esm.gift_extended.security;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import org.springframework.stereotype.Component;

import com.epam.esm.gift_extended.entity.Role;

@Component
public class UserIdInURIValidator {

    private final Map<Role, BiFunction<Integer, String, Boolean>> validationRulesByRole;

    private static final BiFunction<Integer, String, Boolean> ADMIN_URI_VALIDATION = (integer, string) -> true;
    private static final BiFunction<Integer, String, Boolean> USER_URI_VALIDATION = (id, uri) -> {
        String[] parts=uri.split("/");

        if(parts.length>3 ){
            return id.equals(Integer.parseInt(parts[3]));
        }
        return true;
    };
    public UserIdInURIValidator() {
            validationRulesByRole = new HashMap<>();
            validationRulesByRole.put(Role.ADMIN,ADMIN_URI_VALIDATION);
            validationRulesByRole.put(Role.USER,USER_URI_VALIDATION);
        }

        public boolean validate (String uri, Integer id, Role role){
            return validationRulesByRole.get(role).apply(id,uri);
        }
    }
