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
package com.labs64.netlicensing.domain.entity.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.labs64.netlicensing.domain.entity.PaymentMethod;

/**
 * Default implementation of {@link com.labs64.netlicensing.domain.entity.PaymentMethod}.
 */
public class PaymentMethodImpl extends BaseEntityImpl implements PaymentMethod {

    private Map<String, String> paymentMethodProperties;

    /**
     * @see BaseEntityImpl#getReservedProps()
     */
    public static List<String> getReservedProps() {
        return BaseEntityImpl.getReservedProps();
    }

    @Override
    public Map<String, String> getPaymentMethodProperties() {
        if (paymentMethodProperties == null) {
            paymentMethodProperties = new HashMap<String, String>();
        }
        return paymentMethodProperties;
    }

    public void setPaymentMethodProperties(final Map<String, String> paymentMethodProperties) {
        this.paymentMethodProperties = paymentMethodProperties;
    }

    @Override
    public void addProperty(final String property, final String value) {
        getPaymentMethodProperties().put(property, value);
    }

    @Override
    public void removeProperty(final String property) {
        getPaymentMethodProperties().remove(property);
    }

    @Override
    protected Map<String, Object> asPropertiesMap() {
        final Map<String, Object> map = super.asPropertiesMap();
        if (paymentMethodProperties != null) {
            for (final Map.Entry<String, String> property : paymentMethodProperties.entrySet()) {
                map.put(property.getKey(), property.getValue());
            }
        }
        return map;
    }

}
