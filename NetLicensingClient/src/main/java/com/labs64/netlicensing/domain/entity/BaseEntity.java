package com.labs64.netlicensing.domain.entity;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.labs64.netlicensing.domain.Constants;

/**
 * Defines fields common to all (or most) of other entities).
 */
public abstract class BaseEntity {

    private String number;

    private Boolean active = Boolean.TRUE;

    public String getNumber() {
        return number;
    }

    public void setNumber(final String number) {
        this.number = number;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(final Boolean active) {
        this.active = active;
    }

    protected void updateBase(final BaseEntity source) {
        assert source != null; // called from derived classes only, source should never be null
        if (StringUtils.isNotBlank(source.getNumber())) {
            setNumber(source.getNumber());
        }
        if (source.getActive() != null) {
            setActive(source.getActive());
        }
    }

    /**
     * List of reserved properties is used for handling of custom properties. Property name that is included in the list
     * can not be used as custom property name. The list is extended by each derived entity class until the final
     * business entity.
     * 
     * @return the list of reserved property names
     */
    public static List<String> getReservedProps() {
        final List<String> reserved = new ArrayList<String>();
        reserved.add(Constants.ID);
        reserved.add(Constants.NUMBER);
        reserved.add(Constants.ACTIVE);
        reserved.add(Constants.DELETED);
        return reserved;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(Constants.NUMBER);
        builder.append("=");
        builder.append(getNumber());
        builder.append(", ");
        builder.append(Constants.ACTIVE);
        builder.append("=");
        builder.append(getActive());
        return builder.toString();
    }

}
