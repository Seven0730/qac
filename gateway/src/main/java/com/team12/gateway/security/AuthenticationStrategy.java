package com.team12.gateway.security;

public interface AuthenticationStrategy {

    boolean authenticate(String token, String secretKey);

}
