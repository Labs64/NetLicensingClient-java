package com.labs64.netlicensing.service;

import com.labs64.netlicensing.domain.entity.Token;
import com.labs64.netlicensing.domain.vo.Page;
import com.labs64.netlicensing.domain.vo.TokenType;
import com.labs64.netlicensing.exception.BaseCheckedException;
import com.labs64.netlicensing.domain.vo.Context;

/**
 * Provides token entity handling routines.
 */
public interface TokenService {

    /**
     * Gets token by its number.
     * 
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param number
     *            the token number
     * @return the token
     * @throws BaseCheckedException
     *             any subclass of {@linkplain BaseCheckedException}. These exceptions will be transformed to the
     *             corresponding service response messages.
     */
    Token get(Context context, String number) throws BaseCheckedException;

    /**
     * Returns tokens of a vendor.
     * 
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param tokenType
     *            type of tokens to return, if NULL return tokens of all types
     * @param filter
     *            reserved for the future use, must be omitted / set to NULL
     * @return collection of token entities or null/empty list if nothing found.
     * @throws BaseCheckedException
     *             any subclass of {@linkplain BaseCheckedException}. These exceptions will be transformed to the
     *             corresponding service response messages.
     */
    Page<Token> list(Context context, TokenType tokenType, String filter) throws BaseCheckedException;

    /**
     * Creates new token.
     * 
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param newToken
     *            non-null properties will be updated to the provided values, null properties will stay unchanged.
     * @return created token
     * @throws BaseCheckedException
     *             any subclass of {@linkplain BaseCheckedException}. These exceptions will be transformed to the
     *             corresponding service response messages.
     */
    Token create(Context context, Token newToken) throws BaseCheckedException;

    /**
     * Deactivate token by its number.
     * 
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param number
     *            the token number
     * @throws BaseCheckedException
     *             any subclass of {@linkplain BaseCheckedException}. These exceptions will be transformed to the
     *             corresponding service response messages.
     */
    void deactivate(Context context, String number) throws BaseCheckedException;

    /**
     * Get vendor number for apikey.
     * 
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param apiKey
     *            apikey token number
     * @throws BaseCheckedException
     *             any subclass of {@linkplain BaseCheckedException}. These exceptions will be transformed to the
     *             corresponding service response messages.
     */
    String getVendorNumberByApiKey(Context context, String apiKey) throws BaseCheckedException;

}
