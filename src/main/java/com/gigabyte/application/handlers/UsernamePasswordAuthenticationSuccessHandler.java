package com.gigabyte.application.handlers;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gigabyte.application.dtos.JWTDTO;
import com.gigabyte.application.utils.JWTUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class UsernamePasswordAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JWTUtils jwtUtils;

    private static final ObjectMapper JSON_TRANSLATOR = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        String username = authentication.getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String authoritiesAsString = authorities.stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", "));
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", authoritiesAsString);
        JWTDTO jwtDto = new JWTDTO();
        jwtDto.jwt = jwtUtils.generateToken(claims, username);
        jwtDto.expiry = jwtUtils.getExpiry();
        response.setStatus(HttpStatus.CREATED.value());
        JSON_TRANSLATOR.writeValue(response.getWriter(), jwtDto);
    }

}
