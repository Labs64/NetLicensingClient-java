package com.labs64.netlicensing.provider.auth;

/**
 */
public class TokenAuthentication implements Authentication {

    private final String token;

    /**
     * Constructor
     *
     * @param token
     */
    public TokenAuthentication(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

}
