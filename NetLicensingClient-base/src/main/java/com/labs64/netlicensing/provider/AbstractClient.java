package com.labs64.netlicensing.provider;

import java.util.HashMap;
import java.util.Map;

import com.labs64.netlicensing.provider.auth.Authentication;

/**
 */
public abstract class AbstractClient<T extends AbstractClient> {

    protected RestProvider restProvider;

    private Map<String, Object> baseParams = new HashMap<String, Object>();

    /**
     * Create abstract base client.
     *
     * @param restProvider rest provider to be used
     */
    public AbstractClient(final RestProvider restProvider) {
        this.restProvider = restProvider;
    }

    public T authenticate(final Authentication authentication) {
        this.restProvider.authenticate(authentication);
        return (T) this;
    }

    public T authenticate(final String username, final String password) {
        this.restProvider.authenticate(username, password);
        return (T) this;
    }

    public T authenticate(final String token) {
        this.restProvider.authenticate(token);
        return (T) this;
    }

    protected void addBaseParam(final String key, final Object value) {
        baseParams.put(key, value);
    }

    /**
     * @return shallow copy of baseParams
     */
    public Map<String, Object> getBaseParams() {
        return new HashMap<String, Object>() {{
            putAll(baseParams);
        }};
    }

}
