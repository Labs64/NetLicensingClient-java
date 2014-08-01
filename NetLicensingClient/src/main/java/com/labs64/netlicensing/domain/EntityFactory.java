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
package com.labs64.netlicensing.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.labs64.netlicensing.converter.Converter;
import com.labs64.netlicensing.domain.entity.License;
import com.labs64.netlicensing.domain.entity.LicenseTemplate;
import com.labs64.netlicensing.domain.entity.Licensee;
import com.labs64.netlicensing.domain.entity.PaymentMethod;
import com.labs64.netlicensing.domain.entity.Product;
import com.labs64.netlicensing.domain.entity.ProductModule;
import com.labs64.netlicensing.domain.entity.Token;
import com.labs64.netlicensing.domain.entity.Transaction;
import com.labs64.netlicensing.domain.entity.ValidationResult;
import com.labs64.netlicensing.domain.vo.Page;
import com.labs64.netlicensing.domain.vo.PageImpl;
import com.labs64.netlicensing.exception.BaseCheckedException;
import com.labs64.netlicensing.exception.WrongResponseFormatException;
import com.labs64.netlicensing.schema.context.Item;
import com.labs64.netlicensing.schema.context.Netlicensing;
import com.labs64.netlicensing.schema.converter.ItemToLicenseConverter;
import com.labs64.netlicensing.schema.converter.ItemToLicenseTemplateConverter;
import com.labs64.netlicensing.schema.converter.ItemToLicenseeConverter;
import com.labs64.netlicensing.schema.converter.ItemToPaymentMethodConverter;
import com.labs64.netlicensing.schema.converter.ItemToProductConverter;
import com.labs64.netlicensing.schema.converter.ItemToProductModuleConverter;
import com.labs64.netlicensing.schema.converter.ItemToTokenConverter;
import com.labs64.netlicensing.schema.converter.ItemToTransactionConverter;
import com.labs64.netlicensing.schema.converter.ItemsToValidationResultConverter;

/**
 * Factory that contains static methods for creating entities
 */
public class EntityFactory {

    private static final Map<Class<?>, Class<?>> entityToConverterMap = new HashMap<Class<?>, Class<?>>();

    static {
        entityToConverterMap.put(License.class, ItemToLicenseConverter.class);
        entityToConverterMap.put(Licensee.class, ItemToLicenseeConverter.class);
        entityToConverterMap.put(LicenseTemplate.class, ItemToLicenseTemplateConverter.class);
        entityToConverterMap.put(PaymentMethod.class, ItemToPaymentMethodConverter.class);
        entityToConverterMap.put(Product.class, ItemToProductConverter.class);
        entityToConverterMap.put(ProductModule.class, ItemToProductModuleConverter.class);
        entityToConverterMap.put(Token.class, ItemToTokenConverter.class);
        entityToConverterMap.put(Transaction.class, ItemToTransactionConverter.class);
    }

    /**
     * Creates entity of specific class from service response
     * 
     * @param netlicensing
     *            service XML response
     * @param entityClass
     *            entity class
     * @return entity class instance created from service response
     * @throws BaseCheckedException
     */
    @SuppressWarnings("unchecked")
    public <T> T create(final Netlicensing netlicensing, final Class<T> entityClass) throws BaseCheckedException {
        if (entityClass == ValidationResult.class) {
            return (T) new ItemsToValidationResultConverter().convert(netlicensing.getItems());
        } else {
            final Item item = findSuitableItemOfType(netlicensing, entityClass);
            return converterFor(entityClass).convert(item);
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
     * @throws BaseCheckedException
     */
    public <T> Page<T> createPage(final Netlicensing netlicensing, final Class<T> entityClass)
            throws BaseCheckedException {
        if (netlicensing.getItems() != null) {
            final List<T> entities = new ArrayList<T>();

            final Converter<Item, T> converter = converterFor(entityClass);
            for (final Item item : extractListOfType(netlicensing, entityClass)) {
                entities.add(converter.convert(item));
            }

            return PageImpl.createInstance(entities,
                    netlicensing.getItems().getPagenumber(),
                    netlicensing.getItems().getItemsnumber(),
                    netlicensing.getItems().getTotalpages(),
                    netlicensing.getItems().getTotalitems(),
                    netlicensing.getItems().getHasnext());
        } else {
            throw new WrongResponseFormatException("Service response is not a page response");
        }
    }

    /**
     * Returns converter that is able to convert an {@link Item} object to an entity of specified class
     * 
     * @param entityClass
     * @return
     */
    @SuppressWarnings("unchecked")
    private <T> Converter<Item, T> converterFor(final Class<T> entityClass) {
        final Class<?> converterClass = entityToConverterMap.get(entityClass);
        if (converterClass == null) {
            throw new IllegalArgumentException("No converter is found for entity of class "
                    + entityClass.getCanonicalName());
        }

        try {
            return (Converter<Item, T>) converterClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("Can not instantiate converter of class " + converterClass.getCanonicalName());
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Can not instantiate converter of class " + converterClass.getCanonicalName());
        }
    }

    /**
     * Finds and returns from {@link Netlicensing} object suitable item of specified type
     * 
     * @param netlicensing
     * @param type
     * @return
     * @throws WrongResponseFormatException
     */
    private Item findSuitableItemOfType(final Netlicensing netlicensing, final Class<?> type)
            throws WrongResponseFormatException {
        if (netlicensing.getItems() != null) {
            for (final Item item : netlicensing.getItems().getItem()) {
                if (type.getSimpleName().equals(item.getType())) {
                    return item;
                }
            }
        }
        throw new WrongResponseFormatException("Service response doesn't contain item of type "
                + type.getCanonicalName());
    }

    /**
     * Extracts list of items of specified type from {@link Netlicensing} object
     * 
     * @param netlicensing
     * @param type
     * @return
     */
    private List<Item> extractListOfType(final Netlicensing netlicensing, final Class<?> type) {
        final List<Item> items = new ArrayList<Item>();
        for (final Item item : netlicensing.getItems().getItem()) {
            if (type.getSimpleName().equals(item.getType())) {
                items.add(item);
            }
        }
        return items;
    }

}
