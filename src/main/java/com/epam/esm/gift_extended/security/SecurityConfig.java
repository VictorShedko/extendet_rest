package com.epam.esm.gift_extended.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.epam.esm.gift_extended.filter.JWTFilter;


@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JWTFilter jwtFilter;

    public SecurityConfig(JWTFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .disable()
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()

                .antMatchers(HttpMethod.POST, "/api/users/register")
                .permitAll()

                .antMatchers(HttpMethod.POST, "/api/users/auth")
                .permitAll()

                .antMatchers(HttpMethod.GET, "/api/gift-certs/")
                .permitAll()

                .antMatchers(HttpMethod.GET, "/api/gift-certs/*/")
                .permitAll()

                .antMatchers(HttpMethod.GET, "/api/tags/**")
                .hasAnyAuthority("USER", "ADMIN")

                .antMatchers(HttpMethod.GET, "/api/users/")
                .hasAnyAuthority("ADMIN")

                .antMatchers(HttpMethod.GET, "/api/users/*/")
                .hasAnyAuthority("USER","ADMIN")

                .antMatchers(HttpMethod.GET, "/api/gift-certs/*/user")
                .hasAnyAuthority("USER", "ADMIN")

                .antMatchers(HttpMethod.GET, "/api/orders/*/userOrders")
                .hasAnyAuthority("USER", "ADMIN")

                .antMatchers(HttpMethod.POST, "/api/orders/*/userOrders")
                .hasAnyAuthority("USER", "ADMIN")

                .antMatchers(HttpMethod.POST, "/api/tags/**")
                .hasAuthority("ADMIN")

                .antMatchers(HttpMethod.POST, "/api/users/**")
                .hasAuthority("ADMIN")

                .antMatchers(HttpMethod.DELETE, "/api/**")
                .hasAuthority("ADMIN")

                .antMatchers(HttpMethod.PATCH, "/api/**")
                .hasAuthority("ADMIN")

                .antMatchers(HttpMethod.POST, "/api/gift-certs/**")
                .hasAuthority("ADMIN")

                .antMatchers("/api/**")
                .hasAuthority("ADMIN")

                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}