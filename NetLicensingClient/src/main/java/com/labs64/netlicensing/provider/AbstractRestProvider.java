/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.labs64.netlicensing.provider;

import com.labs64.netlicensing.provider.auth.Authentication;
import com.labs64.netlicensing.provider.auth.TokenAuthentication;
import com.labs64.netlicensing.provider.auth.UsernamePasswordAuthentication;

/**
 */
public abstract class AbstractRestProvider implements RestProvider {

    private Authentication authentication;

    private RestProvider.Configuration configuration;

    @Override
    public RestProvider authenticate(final Authentication authentication) {
        this.authentication = authentication;
        return this;
    }

    @Override
    public RestProvider authenticate(final String username, final String password) {
        authentication = new UsernamePasswordAuthentication(username, password);
        return this;
    }

    @Override
    public RestProvider authenticate(final String token) {
        authentication = new TokenAuthentication(token);
        return this;
    }

    protected Authentication getAuthentication() {
        return authentication;
    }

    @Override
    public void configure(final RestProvider.Configuration configuration) {
        this.configuration = configuration;
    }

    protected RestProvider.Configuration getConfiguration() {
        return configuration;
    }

}
