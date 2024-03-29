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
package com.labs64.netlicensing.domain.vo;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.Licensee;
import com.labs64.netlicensing.domain.entity.impl.LicenseeImpl;

public class ValidationResult implements Serializable {

    private static final long serialVersionUID = -4160421171524379008L;

    private Calendar ttl;
    private Map<String, Composition> validations;

    private Licensee licensee;

    public void setLicensee(final Licensee licensee) {
        this.licensee = licensee;
    }

    public Licensee getLicensee() {
        return this.licensee;
    }

    @Deprecated
    public void setLicenseeNumber(final String licenseeNumber) {
        if (this.licensee == null) {
            this.licensee = new LicenseeImpl();
        }

        this.licensee.setNumber(licenseeNumber);
    }

    @Deprecated
    public String getLicenseeNumber() {
        return (this.licensee != null) ? this.licensee.getNumber() : null;
    }

    public void setTtl(final Calendar ttl) {
        this.ttl = ttl;
    }

    public Calendar getTtl() {
        return ttl;
    }

    public Map<String, Composition> getValidations() {
        if (validations == null) {
            validations = new HashMap<>();
        }
        return validations;
    }

    public ValidationResult() {
        ttl = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        ttl.add(Calendar.MINUTE, Constants.ValidationResult.DEFAULT_TTL_MINUTES);
    }

    public Composition getProductModuleValidation(final String productModuleNumber) {
        return getValidations().get(productModuleNumber);
    }

    public void setProductModuleValidation(final String productModuleNumber,
            final Composition productModuleValidation) {
        getValidations().put(productModuleNumber, productModuleValidation);
    }

    public void put(final String productModuleNumber, final String key, final String value) {
        getProductModuleValidation(productModuleNumber).put(key, value);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("ValidationResult");
        if (licensee != null && licensee.getNumber() != null) {
            builder.append("(").append(licensee.getNumber()).append(")");
        }
        builder.append(" [");
        boolean first = true;
        for (final Map.Entry<String, Composition> validationEntry : getValidations().entrySet()) {
            if (first) {
                first = false;
            } else {
                builder.append(", ");
            }
            builder.append("ProductModule<");
            builder.append(validationEntry.getKey());
            builder.append(">");
            builder.append(validationEntry.getValue().toString());
        }
        builder.append("]");
        return builder.toString();
    }

}
