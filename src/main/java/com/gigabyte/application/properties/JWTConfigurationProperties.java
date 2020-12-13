package com.gigabyte.application.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt.config")
public class JWTConfigurationProperties {
    
    private String secret = "SECRET_KEY";
    private long expiry = 24 * 60 * 60 * 1000; // 24 hours in milliseconds

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpiry() {
        return expiry;
    }

    public void setExpiry(long expiry) {
        this.expiry = expiry;
    }

}
