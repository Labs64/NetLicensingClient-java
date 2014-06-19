package com.labs64.netlicensing.domain.vo;

/**
 * This enum defines available token types.
 */
public enum TokenType {

    DEFAULT,

    APIKEY,

    REGISTRATION,

    PASSWORDRESET,

    SHOP;

    public static TokenType parseString(final String token) {
        if (token != null) {
            for (TokenType tokenType : TokenType.values()) {
                if (token.equalsIgnoreCase(tokenType.name())) {
                    return tokenType;
                }
            }
        }
        return null;
    }

}
