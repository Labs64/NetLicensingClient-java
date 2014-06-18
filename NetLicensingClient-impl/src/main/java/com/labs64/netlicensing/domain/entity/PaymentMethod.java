package com.labs64.netlicensing.domain.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PaymentMethod entity used internally by NetLicensing.
 */
public class PaymentMethod extends BaseEntity {

    private Map<String, String> paymentMethodProperties;

    public Map<String, String> getPaymentMethodProperties() {
        if (paymentMethodProperties == null) {
            paymentMethodProperties = new HashMap<String, String>();
        }
        return paymentMethodProperties;
    }

    public void setPaymentMethodProperties(final Map<String, String> paymentMethodProperties) {
        this.paymentMethodProperties = paymentMethodProperties;
    }

    public void addProperty(final String property, final String value) {
        getPaymentMethodProperties().put(property, value);
    }

    public void removeProperty(final String property) {
        getPaymentMethodProperties().remove(property);
    }

    /**
     * @see com.labs64.netlicensing.domain.entity.BaseEntity#getReservedProps()
     */
    public static List<String> getReservedProps() {
        return BaseEntity.getReservedProps();
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("PaymentMethod [");
        builder.append(super.toString());
        builder.append(", ");
        if (paymentMethodProperties != null) {
            for (final Map.Entry<String, String> property : paymentMethodProperties.entrySet()) {
                builder.append(", ");
                builder.append(property.getKey());
                builder.append("=");
                builder.append(property.getValue());
            }
        }
        builder.append("]");
        return builder.toString();
    }

}
