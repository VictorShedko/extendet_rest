package com.epam.esm.gift_extended.security;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.epam.esm.gift_extended.entity.Role;


enum URIsToValidation  {
    GET_USER_BY_ID(Pattern.compile("^/api/users/[0-9]+/$")),
    MAKE_ORDER(Pattern.compile("^/api/users/[0-9]+/certs$"))
    , GET_CERT_BY_ID(
            Pattern.compile("^/api/gift-certs/[0-9]+/$")), GET_USER_CERT_BY_USER_ID(
            Pattern.compile("^/api/gift-certs/[0-9]+/user$")), NO_NEED_VALIDATION(Pattern.compile(""));
    Pattern uriPattern;

    URIsToValidation(Pattern uriPattern) {
        this.uriPattern = uriPattern;
    }

    static URIsToValidation getURIType(String requestedUri) {
        for (var uri : URIsToValidation.values()) {
            if (uri.uriPattern.matcher(requestedUri).matches()) {
                return uri;
            }
        }
        return URIsToValidation.NO_NEED_VALIDATION;
    }

}

@Component
public class UserDataAccessURIValidator extends RestTemplate {

    private final Map<URIsToValidation, BiPredicate<String, JWTUser>> validationRulesByURI;

    private final BiPredicate<String, JWTUser> GET_USER_BY_ID = (s, user) -> Integer.parseInt(s.split("/")[3])
            == user.getId();
    private final BiPredicate<String, JWTUser> GET_CERT_BY_ID = (s, user) -> {
        return user.getCertIds().contains(Integer.parseInt(s.split("/")[3]));
    };
    private final BiPredicate<String, JWTUser> GET_CERT_BY_USER_ID = (s, user) -> Integer.parseInt(s.split("/")[3])
            == user.getId();
    private final BiPredicate<String, JWTUser> DEFAULT = (s, user) -> true;

    public UserDataAccessURIValidator() {
        validationRulesByURI = new HashMap<>();
        validationRulesByURI.put(URIsToValidation.GET_USER_BY_ID,GET_USER_BY_ID);
        validationRulesByURI.put(URIsToValidation.GET_CERT_BY_ID,GET_CERT_BY_ID);
        validationRulesByURI.put(URIsToValidation.GET_USER_CERT_BY_USER_ID,GET_CERT_BY_USER_ID);
        validationRulesByURI.put(URIsToValidation.NO_NEED_VALIDATION,DEFAULT);
        validationRulesByURI.put(URIsToValidation.MAKE_ORDER,GET_USER_BY_ID);

    }

    public boolean validate(String uri, JWTUser user) {
        return user.getRole() != Role.USER || validationRulesByURI.get(URIsToValidation.getURIType(uri))
                .test(uri, user);
    }
}
