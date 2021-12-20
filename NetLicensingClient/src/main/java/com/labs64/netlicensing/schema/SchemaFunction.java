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
package com.labs64.netlicensing.schema;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import com.labs64.netlicensing.schema.context.Info;
import com.labs64.netlicensing.schema.context.InfoEnum;
import com.labs64.netlicensing.schema.context.Item;
import com.labs64.netlicensing.schema.context.Netlicensing;
import com.labs64.netlicensing.schema.context.ObjectFactory;
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
     * Get {@link Property} derivative by name, converting it to {@link Property}. Property name is not case-sensitive!
     * If property with the given name is not found, a property with empty value is returned.
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

    /**
     * Creates and adds {@link Info} object to {@link Netlicensing} object
     * 
     * @param netlicensing
     *            {@link Netlicensing} object
     * @param id
     *            {@link Info} object identifier
     * @param type
     *            {@link Info} object type
     * @param value
     *            {@link Info} object value
     */
    public static void addInfo(final Netlicensing netlicensing, final String id, final InfoEnum type,
            final String value) {

        final ObjectFactory objectFactory = new ObjectFactory();

        if (netlicensing.getInfos() == null) {
            netlicensing.setInfos(objectFactory.createNetlicensingInfos());
        }

        final Info info = objectFactory.createInfo();
        info.setId(id);
        info.setType(type);
        info.setValue(value);
        netlicensing.getInfos().getInfo().add(info);
    }

    /**
     * Check if {@link Netlicensing} object contains service errors.
     * 
     * @param entity
     *            {@link Netlicensing} object to be checked
     * @return true if any error have been found, otherwise false
     */
    public static boolean hasErrorInfos(final Netlicensing entity) {
        return (entity != null) && (entity.getInfos() != null) && !entity.getInfos().getInfo().isEmpty();
    }

    /**
     * Transform service infos to a string message.
     * 
     * @param response
     *            service response (containing the infos)
     * @return message string
     */
    public static String infosToMessage(final Netlicensing response) {
        final StringBuilder errorMessages = new StringBuilder();
        if (hasErrorInfos(response)) {
            for (final Info info : response.getInfos().getInfo()) {
                if (errorMessages.length() > 0) {
                    errorMessages.append(", ");
                }
                errorMessages.append(info.getId()).append(": ").append(info.getValue().substring(0, 1).toUpperCase())
                .append(info.getValue().substring(1));
            }
        } else {
            errorMessages.append("Infos not provided with the service response.");
        }
        return errorMessages.toString();
    }

}
