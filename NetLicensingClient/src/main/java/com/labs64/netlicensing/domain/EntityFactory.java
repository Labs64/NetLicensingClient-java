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
package com.labs64.netlicensing.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.labs64.netlicensing.domain.entity.Country;
import com.labs64.netlicensing.domain.entity.License;
import com.labs64.netlicensing.domain.entity.LicenseTemplate;
import com.labs64.netlicensing.domain.entity.Licensee;
import com.labs64.netlicensing.domain.entity.Notification;
import com.labs64.netlicensing.domain.entity.PaymentMethod;
import com.labs64.netlicensing.domain.entity.Product;
import com.labs64.netlicensing.domain.entity.ProductModule;
import com.labs64.netlicensing.domain.entity.Token;
import com.labs64.netlicensing.domain.entity.Transaction;
import com.labs64.netlicensing.domain.vo.LicenseTypeProperties;
import com.labs64.netlicensing.domain.vo.LicensingModelProperties;
import com.labs64.netlicensing.domain.vo.Page;
import com.labs64.netlicensing.domain.vo.PageImpl;
import com.labs64.netlicensing.domain.vo.ValidationResult;
import com.labs64.netlicensing.exception.ConversionException;
import com.labs64.netlicensing.exception.NetLicensingException;
import com.labs64.netlicensing.exception.WrongResponseFormatException;
import com.labs64.netlicensing.schema.context.Item;
import com.labs64.netlicensing.schema.context.Netlicensing;
import com.labs64.netlicensing.schema.converter.Converter;
import com.labs64.netlicensing.schema.converter.ItemToCountryConverter;
import com.labs64.netlicensing.schema.converter.ItemToLicenseConverter;
import com.labs64.netlicensing.schema.converter.ItemToLicenseTemplateConverter;
import com.labs64.netlicensing.schema.converter.ItemToLicenseTypePropertiesConverter;
import com.labs64.netlicensing.schema.converter.ItemToLicenseeConverter;
import com.labs64.netlicensing.schema.converter.ItemToLicensingModelPropertiesConverter;
import com.labs64.netlicensing.schema.converter.ItemToNotificationConverter;
import com.labs64.netlicensing.schema.converter.ItemToPaymentMethodConverter;
import com.labs64.netlicensing.schema.converter.ItemToProductConverter;
import com.labs64.netlicensing.schema.converter.ItemToProductModuleConverter;
import com.labs64.netlicensing.schema.converter.ItemToTokenConverter;
import com.labs64.netlicensing.schema.converter.ItemToTransactionConverter;
import com.labs64.netlicensing.schema.converter.ItemsToValidationResultConverter;
import com.labs64.netlicensing.util.Visitable;
import com.labs64.netlicensing.util.Visitor;

/**
 * Factory that contains static methods for creating entities
 */
public class EntityFactory {

    private static final Map<Class<?>, Class<?>> entityToConverterMap = new HashMap<>();

    static {
        entityToConverterMap.put(License.class, ItemToLicenseConverter.class);
        entityToConverterMap.put(Licensee.class, ItemToLicenseeConverter.class);
        entityToConverterMap.put(LicenseTemplate.class, ItemToLicenseTemplateConverter.class);
        entityToConverterMap.put(PaymentMethod.class, ItemToPaymentMethodConverter.class);
        entityToConverterMap.put(Product.class, ItemToProductConverter.class);
        entityToConverterMap.put(ProductModule.class, ItemToProductModuleConverter.class);
        entityToConverterMap.put(Token.class, ItemToTokenConverter.class);
        entityToConverterMap.put(Transaction.class, ItemToTransactionConverter.class);
        entityToConverterMap.put(Country.class, ItemToCountryConverter.class);
        entityToConverterMap.put(LicensingModelProperties.class, ItemToLicensingModelPropertiesConverter.class);
        entityToConverterMap.put(LicenseTypeProperties.class, ItemToLicenseTypePropertiesConverter.class);
        entityToConverterMap.put(Notification.class, ItemToNotificationConverter.class);
    }

    private Map<Class<?>, Converter<Item, ?>> convertersCache;

    private Map<Class<?>, Converter<Item, ?>> getConvertersCache() {
        if (convertersCache == null) {
            convertersCache = new HashMap<>();
        }
        return convertersCache;
    }

    /**
     * Creates entity of specific class from service response
     *
     * @param netlicensing
     *            service XML response
     * @param entityClass
     *            entity class
     * @return entity class instance created from service response
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     */
    @SuppressWarnings("unchecked")
    public <T> T create(final Netlicensing netlicensing, final Class<T> entityClass)
            throws NetLicensingException {
        if (entityClass == ValidationResult.class) {
            return (T) new ItemsToValidationResultConverter().convert(netlicensing);
        } else {
            final Item item = findSuitableItemOfType(netlicensing, entityClass);
            return converterFor(entityClass).convert(item);
        }
    }

    public class LinkedEntitiesPopulator extends Visitor {

        private final List<Object> linkedEntities;

        public LinkedEntitiesPopulator(final List<Object> linkedEntities) {
            this.linkedEntities = linkedEntities;
        }

        public void visit(final Product product) throws Exception {
            final List<ProductModule> linkedProductModules = new ArrayList<>();
            for (final Iterator<Object> iter = linkedEntities.iterator(); iter.hasNext();) {
                final Object linkedEntity = iter.next();
                if (ProductModule.class.isAssignableFrom(linkedEntity.getClass())) {
                    final ProductModule linkedProductModule = (ProductModule) linkedEntity;
                    if (product.getNumber().equals(linkedProductModule.getProduct().getNumber())) {
                        iter.remove();
                        linkedProductModules.add(linkedProductModule);
                        linkedProductModule.setProduct(product);
                    }
                } else if (Licensee.class.isAssignableFrom(linkedEntity.getClass())) {
                    final Licensee linkedLecensee = (Licensee) linkedEntity;
                    if (product.getNumber().equals(linkedLecensee.getProduct().getNumber())) {
                        iter.remove();
                        linkedLecensee.setProduct(product);
                    }
                }
            }
            for (final ProductModule linkedProductModule : linkedProductModules) {
                if (Visitable.class.isAssignableFrom(linkedProductModule.getClass())) {
                    ((Visitable) linkedProductModule).accept(this);
                }
            }
        }

        public void visit(final ProductModule productModule) throws Exception {
            final List<LicenseTemplate> linkedLicenseTemplates = new ArrayList<>();
            for (final Iterator<Object> iter = linkedEntities.iterator(); iter.hasNext();) {
                final Object linkedEntity = iter.next();
                if (LicenseTemplate.class.isAssignableFrom(linkedEntity.getClass())) {
                    final LicenseTemplate linkedLicenseTemplate = (LicenseTemplate) linkedEntity;
                    if (productModule.getNumber().equals(linkedLicenseTemplate.getProductModule().getNumber())) {
                        iter.remove();
                        linkedLicenseTemplates.add(linkedLicenseTemplate);
                        linkedLicenseTemplate.setProductModule(productModule);
                    }
                }
            }
            for (final LicenseTemplate linkedLicenseTemplate : linkedLicenseTemplates) {
                if (Visitable.class.isAssignableFrom(linkedLicenseTemplate.getClass())) {
                    ((Visitable) linkedLicenseTemplate).accept(this);
                }
            }
        }

        public void visit(final LicenseTemplate licenseTemplate) throws Exception {
            for (final Iterator<Object> iter = linkedEntities.iterator(); iter.hasNext();) {
                final Object linkedEntity = iter.next();
                if (License.class.isAssignableFrom(linkedEntity.getClass())) {
                    final License linkedLicense = (License) linkedEntity;
                    if (licenseTemplate.getNumber().equals(linkedLicense.getLicenseTemplate().getNumber())) {
                        iter.remove();
                        linkedLicense.setLicenseTemplate(licenseTemplate);
                    }
                }
            }
        }
    }

    /**
     * Creates page of entities of specified class from service response
     *
     * @param netlicensing
     *            service XML response
     * @param entityClass
     *            entity class
     * @return page of entities created from service response
     * @throws com.labs64.netlicensing.exception.NetLicensingException
     */
    public <T> Page<T> createPage(final Netlicensing netlicensing, final Class<T> entityClass)
            throws NetLicensingException {
        if (netlicensing.getItems() != null) {
            final List<T> entities = new ArrayList<>();
            final List<Object> linkedEntities = new ArrayList<>();

            for (final Item item : netlicensing.getItems().getItem()) {
                final Class<?> itemEntityClass = getEntityClassByItemType(item);
                if (entityClass.isAssignableFrom(itemEntityClass)) {
                    entities.add(converterFor(entityClass).convert(item));
                } else {
                    linkedEntities.add(converterFor(itemEntityClass).convert(item));
                }
            }

            if (!linkedEntities.isEmpty()) {
                for (final T entity : entities) {
                    if (Visitable.class.isAssignableFrom(entity.getClass())) {
                        try {
                            ((Visitable) entity).accept(new LinkedEntitiesPopulator(linkedEntities));
                        } catch (final Exception e) {
                            throw new ConversionException("Error processing linked entities.", e);
                        }
                    }
                }
            }

            return PageImpl.createInstance(entities,
                    netlicensing.getItems().getPagenumber(),
                    netlicensing.getItems().getItemsnumber(),
                    netlicensing.getItems().getTotalpages(),
                    netlicensing.getItems().getTotalitems(),
                    netlicensing.getItems().getHasnext());
        } else {
            throw new WrongResponseFormatException("Service response is not a page response.");
        }
    }

    /**
     * Returns converter that is able to convert an {@link Item} object to an entity of specified class
     *
     * @param entityClass
     *            entity class
     * @return {@link Converter} suitable for the given entity class
     */
    @SuppressWarnings("unchecked")
    private <T> Converter<Item, T> converterFor(final Class<T> entityClass) {
        Converter<Item, T> converter = null;
        try {
            converter = (Converter<Item, T>) getConvertersCache().get(entityClass);
        } catch (final ClassCastException e) {
            throw new RuntimeException(
                    "Wrong converter type found for entity class " + entityClass.getCanonicalName() + ".");
        }
        if (converter == null) {
            final Class<?> converterClass = entityToConverterMap.get(entityClass);
            if (converterClass == null) {
                throw new IllegalArgumentException("No converter is found for entity of class "
                        + entityClass.getCanonicalName() + ".");
            }
            try {
                converter = (Converter<Item, T>) converterClass.getConstructor().newInstance();
                getConvertersCache().put(entityClass, converter);
            } catch (final Throwable e) {
                throw new RuntimeException(
                        "Can not instantiate converter of class " + converterClass.getCanonicalName() + ".");
            }
        }
        return converter;
    }

    /**
     * Finds and returns from {@link Netlicensing} object suitable item of specified type
     *
     * @param netlicensing
     *            {@link Netlicensing} response object
     * @param type
     *            class type to be matched
     * @return suitable item of specified type
     * @throws WrongResponseFormatException
     */
    private Item findSuitableItemOfType(final Netlicensing netlicensing, final Class<?> type)
            throws WrongResponseFormatException {
        if (netlicensing.getItems() != null) {
            for (final Item item : netlicensing.getItems().getItem()) {
                if (isItemOfType(item, type)) {
                    return item;
                }
            }
        }
        throw new WrongResponseFormatException("Service response doesn't contain item of type "
                + type.getCanonicalName() + ".");
    }

    /**
     * Check whether the {@link Item} object is of provided type.
     *
     * @param item
     *            {@link Item} object
     * @param type
     *            class type to be matched
     * @return true if item is the XML item of class "type"
     */
    private boolean isItemOfType(final Item item, final Class<?> type) {
        return type.getSimpleName().equals(item.getType())
                || type.getSimpleName().equals(item.getType() + "Properties");
    }

    /**
     * Gets the entity class by response item type.
     *
     * @param item
     *            the item
     * @return the entity class, if match is found
     * @throws WrongResponseFormatException
     *             if match is not found
     */
    private Class<?> getEntityClassByItemType(final Item item) throws WrongResponseFormatException {
        final String itemType = item.getType();
        final String itemTypeProps = itemType.concat("Properties");
        for (final Class<?> entityClass : entityToConverterMap.keySet()) {
            if (entityClass.getSimpleName().equals(itemType) || entityClass.getSimpleName().equals(itemTypeProps)) {
                return entityClass;
            }
        }
        throw new WrongResponseFormatException("Service response contains unexpected item type " + itemType + ".");
    }

}
