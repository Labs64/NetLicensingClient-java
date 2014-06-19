package com.labs64.netlicensing.schema;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import com.labs64.netlicensing.schema.context.Item;
import com.labs64.netlicensing.schema.context.Netlicensing;
import com.labs64.netlicensing.schema.context.Property;

/**
 * SchemaFunction defines utility functions for the schema classes.
 */
public final class SchemaFunction {

    private SchemaFunction() {
        // utility class constructor
    }

    /**
     * Get {@link Property} by name. Property name is not case-sensitive!
     * 
     * @param properties
     *            properties collection
     * @param name
     *            property name
     * @return {@link Property} object or "null"-property (w/o value) if no property with the given name is present
     */
    public static Property propertyByName(final List<Property> properties, final String name) {
        return propertyByName(properties, name, null);
    }

    /**
     * Get {@link Property} by name. Property name is not case-sensitive! If property with the given name is not found,
     * a property with provided default value is returned.
     * 
     * @param properties
     *            properties collection
     * @param name
     *            property name
     * @param defaultValue
     *            default value to be used if no property found
     * @return {@link Property} object with found or default value
     */
    public static Property propertyByName(final List<Property> properties, final String name, final String defaultValue) {
        for (final Property property : properties) {
            if (name.equalsIgnoreCase(property.getName())) {
                return property;
            }
        }
        return new Property(defaultValue, name);
    }

    /**
     * Get url-encoded Property value by name. Property name is not case-sensitive!
     * 
     * @param properties
     *            properties collection
     * @param name
     *            property name
     * @return {String} value or "null"-String if no property with the given name is present
     */
    public static String propertyEncodedValueByName(final List<Property> properties, final String name) {
        final Property property = propertyByName(properties, name, null);
        if (property != null) {
            try {
                return java.net.URLEncoder.encode(property.getValue(), "UTF-8");
            } catch (final UnsupportedEncodingException e) {
                return property.getValue();
            }
        }
        return null;
    }

    /**
     * Get {@link PropertyBase} derivative by name, converting it to {@link Property}. Property name is not
     * case-sensitive! If property with the given name is not found, a property with empty value is returned.
     * 
     * @param properties
     *            properties collection
     * @param name
     *            property name
     * @return {@link Property} object with found or empty value
     */
    public static Property entityPropertyByName(final Map<String, String> properties, final String name) {
        for (final Map.Entry<String, String> property : properties.entrySet()) {
            if (name.equalsIgnoreCase(property.getKey())) {
                return new Property(property.getValue(), name);
            }
        }
        return new Property("", name);
    }

    /**
     * Get {@link Item} by existing property value. Comparison is not case-sensitive!
     * 
     * @param items
     *            items collection
     * @param propertyName
     *            property name
     * @param propertyValue
     *            property value
     * @return {@link Item} object or "null" if no property with the given name is present
     */
    public static Item findItemByProperty(final Netlicensing.Items items, final String propertyName,
            final String propertyValue) {
        if (propertyValue != null) {
            for (final Item item : items.getItem()) {
                final String value = propertyByName(item.getProperty(), propertyName).getValue();
                if (propertyValue.equalsIgnoreCase(value)) {
                    return item;
                }
            }
        }
        return null;
    }

    /**
     * Get {@link com.labs64.netlicensing.schema.context.List} by name from {@link Item}.
     * 
     * @param item
     *            item containing (multiple) lists
     * @param listName
     *            the value of the list "name" attribute
     * @return {@link com.labs64.netlicensing.schema.context.List} object or "null" if no list with the given name is
     *         present
     */
    public static com.labs64.netlicensing.schema.context.List findListByName(final Item item, final String listName) {
        for (final com.labs64.netlicensing.schema.context.List list : item.getList()) {
            if (listName.equals(list.getName())) {
                return list;
            }
        }
        return null;
    }

}
