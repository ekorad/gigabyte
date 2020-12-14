package com.gigabyte.application.jwt;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.gigabyte.application.properties.JWTConfigurationProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTFactory {
    private final JWTConfigurationProperties configProps;

    @Autowired
    public JWTFactory(JWTConfigurationProperties props) {
        this.configProps = props;
    }

    public JWT generateJWT(String username, Collection<? extends GrantedAuthority> authorities) {
        return new JWT(username, authorities);
    }

    public JWT parseStringJwt(String strJwt) {
        return new JWT(strJwt);
    }

    private static String stringifyAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(", "));
    }

    private Collection<? extends GrantedAuthority> deStringifyAuthorities(String strAuthorities) {
        return Stream.of(strAuthorities.split(", ")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public class JWT {
        private final String username;
        private final Date expiration;
        private final Date issuedAt;
        private final Collection<? extends GrantedAuthority> authorities;
        private final String stringJwt;

        private JWT(String username, Collection<? extends GrantedAuthority> authorities) {
            this.username = username;
            this.authorities = authorities;
            this.issuedAt = new Date(System.currentTimeMillis());
            this.expiration = new Date(System.currentTimeMillis() + configProps.getExpiry());

            Map<String, Object> authoritiesClaims = new HashMap<>();
            authoritiesClaims.put("authorities", stringifyAuthorities(authorities));

            this.stringJwt = Jwts.builder().setClaims(authoritiesClaims)
                .setSubject(username)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, configProps.getSecret())
                .compact();
        }

        private JWT(String jwtString) {
            Claims claims = Jwts.parser().setSigningKey(configProps.getSecret())
                .parseClaimsJws(jwtString).getBody();
            
            this.username = claims.getSubject();
            this.issuedAt = claims.getIssuedAt();
            this.expiration = claims.getExpiration();
            
            String strAuthorities = claims.get("authorities", String.class);
            this.authorities = deStringifyAuthorities(strAuthorities);

            this.stringJwt = jwtString;
        }

        public String getUsername() {
            return username;
        }

        public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorities;
        }

        public Date getIssuedAt() {
            return issuedAt;
        }

        public Date getExpiration() {
            return expiration;
        }

        @Override
        public String toString() {
            return stringJwt;
        }

        public long getExpiryMs() {
            return configProps.getExpiry();
        }

    }
}
