package com.labs64.netlicensing.domain.vo;

/**
 * Enumerates possible security modes for accessing the NetLicensing API.
 * See https://www.labs64.de/confluence/x/pwCo#NetLicensingAPI%28RESTful%29-Security
 * for details.
 */
public enum SecurityMode {
    BASIC_AUTHENTICATION,
    APIKEY_IDENTIFICATION
}
