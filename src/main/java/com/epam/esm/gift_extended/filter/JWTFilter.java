package com.epam.esm.gift_extended.filter;

import static org.springframework.util.StringUtils.hasText;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.epam.esm.gift_extended.security.JWTProvider;
import com.epam.esm.gift_extended.security.JWTUser;
import com.epam.esm.gift_extended.security.JWTUserDetailsService;
import com.epam.esm.gift_extended.security.UserDataAccessURIValidator;

import lombok.extern.java.Log;

@Log
@Component
public class JWTFilter extends GenericFilterBean {

    public static final String AUTHORIZATION = "Authorization";

    private UserDataAccessURIValidator userIdInURIValidator;

    private JWTProvider jwtProvider;

    private JWTUserDetailsService userDetailsService;

    @Autowired
    public void setUserIdInURIValidator(UserDataAccessURIValidator userIdInURIValidator) {
        this.userIdInURIValidator = userIdInURIValidator;
    }

    @Autowired
    public void setJwtProvider(JWTProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Autowired
    public void setUserDetailsService(JWTUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        logger.info("do filter...");
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String token = getTokenFromRequest(httpServletRequest);

        if (token != null && jwtProvider.validateToken(token)) {
            String userLogin = jwtProvider.getLoginFromToken(token);
            String url = httpServletRequest.getRequestURI();
            JWTUser customUserDetails = userDetailsService.loadUserByUsername(userLogin);
            if (userIdInURIValidator.validate(url, customUserDetails)) {
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(customUserDetails,
                        null, customUserDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader(AUTHORIZATION);
        if (hasText(bearer)) {
            return bearer.substring(7);
        }
        return null;
    }
}
