package com.gigabyte.application.utils;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import com.gigabyte.application.properties.JWTConfigurationProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JWTUtils {
    
    private final String SECRET;
    private final long EXPIRY;

    @Autowired
    public JWTUtils(JWTConfigurationProperties props) {
        this.SECRET = props.getSecret();
        this.EXPIRY = props.getExpiry();
    }

    private boolean isTokenExpired(String jwt) {
        final Date expiration = getExpirationDateFromToken(jwt);
        return expiration.before(new Date());
    }

    private Claims getAllClaimsFromToken(String jwt) {
        return Jwts.parser().setSigningKey(SECRET)
            .parseClaimsJws(jwt).getBody();
    }

    public <T> T getClaimFromtoken(String jwt, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(jwt);
        return claimsResolver.apply(claims);
    }

    public String getUsernameFromToken(String jwt) {
        return getClaimFromtoken(jwt, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String jwt) {
        return getClaimFromtoken(jwt, Claims::getExpiration);
    }

    public String generateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRY))
            .signWith(SignatureAlgorithm.HS512, SECRET).compact();
    }

    public boolean validateToken(String jwt, UserDetails userDetails) {
        final String username = getUsernameFromToken(jwt);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(jwt));
    }

    public long getExpiry() {
        return EXPIRY;
    }
}
