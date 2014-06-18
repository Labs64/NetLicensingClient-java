package com.labs64.netlicensing.provider;

import com.labs64.netlicensing.provider.auth.Authentication;
import com.labs64.netlicensing.provider.auth.TokenAuthentication;
import com.labs64.netlicensing.provider.auth.UsernamePasswordAuthentication;

/**
 */
public abstract class AbstractRestProvider implements RestProvider {

    protected Authentication authentication;
    protected String basePath;

    public RestProvider authenticate(final Authentication authentication) {
        this.authentication = authentication;
        return this;
    }

    public RestProvider authenticate(final String username, final String password) {
        authentication = new UsernamePasswordAuthentication(username, password);
        return this;
    }

    public RestProvider authenticate(final String token) {
        authentication = new TokenAuthentication(token);
        return this;
    }

    public Authentication getAuthentication() {
        return authentication;
    }

}
