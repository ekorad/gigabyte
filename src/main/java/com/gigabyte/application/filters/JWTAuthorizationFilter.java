package com.gigabyte.application.filters;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gigabyte.application.jwt.JWTFactory;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

public class JWTAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTFactory jwtFactory;

    private static final Logger LOGGER = LoggerFactory.getLogger(JWTAuthorizationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        UsernamePasswordAuthenticationToken auth = getAuthentication(request);
        if (auth == null) {
            chain.doFilter(request, response);
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(auth);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (StringUtils.isNotEmpty(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            String stringJwt = authorizationHeader.replace("Bearer ", "");
            if (StringUtils.isNotEmpty(stringJwt)) {
                try {
                    JWTFactory.JWT jwt = jwtFactory.parseStringJwt(stringJwt);
                    String username = jwt.getUsername();
                    Collection<? extends GrantedAuthority> authorities = jwt.getAuthorities();

                    if (StringUtils.isNotEmpty(username) && !authorities.isEmpty()) {
                        return new UsernamePasswordAuthenticationToken(username, null, authorities);
                    }
                } catch (ExpiredJwtException exc) {
                    LOGGER.warn("Request to parse expired JWT: {} failed: {}", stringJwt, exc);
                } catch (UnsupportedJwtException exc) {
                    LOGGER.warn("Request to parse unsupported JWT: {} failed: {}", stringJwt, exc);
                } catch (MalformedJwtException exc) {
                    LOGGER.warn("Request to parse invalid JWT: {} failed: {}", stringJwt, exc);
                } catch (SignatureException exc) {
                    LOGGER.warn("Request to parse JWT with invalid signature: {} failed: {}", stringJwt, exc);
                } catch (IllegalArgumentException exc) {
                    LOGGER.warn("Request to parse empty or null JWT: {} failed: {}", stringJwt, exc);
                }
            }
        }
        return null;
    }
}
