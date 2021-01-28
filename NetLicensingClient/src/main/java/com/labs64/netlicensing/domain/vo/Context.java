/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.labs64.netlicensing.domain.vo;

import com.labs64.netlicensing.domain.Constants;

/**
 * Provides calling context for the NetLicensing API calls.
 * <p>
 * The Context object may differ depending on the level at which NetLicensing API is called.
 * <p>
 * For the internal Java NetLicensing API the Context provides information about the targeted Vendor.
 */
public class Context extends GenericContext<String> {

    private String publicKey;

    public Context() {
        super(String.class);
    }

    public Context setBaseUrl(final String baseUrl) {
        return (Context) this.setValue(Constants.BASE_URL, baseUrl);
    }

    public String getBaseUrl() {
        return getValue(Constants.BASE_URL);
    }

    public Context setUsername(final String username) {
        return (Context) this.setValue(Constants.USERNAME, username);
    }

    public String getUsername() {
        return getValue(Constants.USERNAME);
    }

    public Context setPassword(final String password) {
        return (Context) this.setValue(Constants.PASSWORD, password);
    }

    public String getPassword() {
        return getValue(Constants.PASSWORD);
    }

    public Context setApiKey(final String apiKey) {
        return (Context) this.setValue(Constants.Token.API_KEY, apiKey);
    }

    public String getApiKey() {
        return getValue(Constants.Token.API_KEY);
    }

    public Context setSecurityMode(final SecurityMode securityMode) {
        return (Context) this.setValue(Constants.SECURITY_MODE, securityMode.toString());
    }

    public SecurityMode getSecurityMode() {
        final String securityMode = getValue(Constants.SECURITY_MODE);
        return securityMode != null ? SecurityMode.valueOf(securityMode) : null;
    }

    public Context setVendorNumber(final String vendorNumber) {
        return (Context) this.setValue(Constants.Vendor.VENDOR_NUMBER, vendorNumber);
    }

    public String getVendorNumber() {
        return getValue(Constants.Vendor.VENDOR_NUMBER);
    }

    /**
     * Sets the public key
     *
     * @param publicKey
     *            publicKey stored on the client side.
     */
    public void setPublicKey(final String publicKey) {
        if (publicKey != null) {
            this.publicKey = publicKey.replaceAll("\\r\\n|\\r|\\n", "").replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "");
        } else {
            this.publicKey = publicKey;
        }
    }

    public String getPublicKey() {
        return publicKey;
    }

}
