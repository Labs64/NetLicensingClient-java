package com.labs64.netlicensing.domain;

import java.util.HashMap;
import java.util.Map;

import com.labs64.netlicensing.converter.Converter;
import com.labs64.netlicensing.domain.Constants.License;
import com.labs64.netlicensing.domain.Constants.PaymentMethod;
import com.labs64.netlicensing.domain.entity.LicenseTemplate;
import com.labs64.netlicensing.domain.entity.Licensee;
import com.labs64.netlicensing.domain.entity.Product;
import com.labs64.netlicensing.domain.entity.ProductModule;
import com.labs64.netlicensing.domain.entity.Token;
import com.labs64.netlicensing.domain.entity.Transaction;
import com.labs64.netlicensing.schema.context.Item;
import com.labs64.netlicensing.schema.converter.ItemToLicenseConverter;
import com.labs64.netlicensing.schema.converter.ItemToLicenseTemplateConverter;
import com.labs64.netlicensing.schema.converter.ItemToLicenseeConverter;
import com.labs64.netlicensing.schema.converter.ItemToPaymentMethodConverter;
import com.labs64.netlicensing.schema.converter.ItemToProductConverter;
import com.labs64.netlicensing.schema.converter.ItemToProductModuleConverter;
import com.labs64.netlicensing.schema.converter.ItemToTokenConverter;
import com.labs64.netlicensing.schema.converter.ItemToTransactionConverter;

/**
 * Factory that contains static methods for creating entities
 */
public class EntityFactory {

    private static final Map<Class<?>, Class<?>> entityToConverterMap = new HashMap<Class<?>, Class<?>>();
    static {
        entityToConverterMap.put(License.class, ItemToLicenseConverter.class);
        entityToConverterMap.put(Licensee.class, ItemToLicenseeConverter.class);
        entityToConverterMap.put(LicenseTemplate.class,
                ItemToLicenseTemplateConverter.class);
        entityToConverterMap.put(PaymentMethod.class, ItemToPaymentMethodConverter.class);
        entityToConverterMap.put(Product.class, ItemToProductConverter.class);
        entityToConverterMap.put(ProductModule.class, ItemToProductModuleConverter.class);
        entityToConverterMap.put(Token.class, ItemToTokenConverter.class);
        entityToConverterMap.put(Transaction.class, ItemToTransactionConverter.class);
    }

    /**
     * Creates entity of specific class from Item element
     *
     * @param item
     * @param entityClass
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T create(Item item, Class<?> entityClass) {
        final Class<?> converterClass = entityToConverterMap.get(entityClass);
        if (converterClass == null) {
            throw new IllegalArgumentException(
                    "No converter is found for entity of class "
                            + entityClass.getCanonicalName());
        }

        Converter<Item, T> converter = null;
        try {
            converter = (Converter<Item, T>) converterClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("Can not instantiate converter of class "
                    + converterClass.getCanonicalName());
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Can not instantiate converter of class "
                    + converterClass.getCanonicalName());
        }

        return converter.convert(item);
    }
}
