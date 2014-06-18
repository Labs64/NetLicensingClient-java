package com.labs64.netlicensing.provider.auth;

/**
 */
public class UsernamePasswordAuthentication implements Authentication {

    private final String username;
    private final String password;

    /**
     * Constructor
     *
     * @param username for basic HTTP authentication
     * @param password for basic HTTP authentication
     */
    public UsernamePasswordAuthentication(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
