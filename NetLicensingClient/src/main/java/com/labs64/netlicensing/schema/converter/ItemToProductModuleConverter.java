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
package com.labs64.netlicensing.schema.converter;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.Product;
import com.labs64.netlicensing.domain.entity.ProductModule;
import com.labs64.netlicensing.schema.SchemaFunction;
import com.labs64.netlicensing.schema.context.Item;
import com.labs64.netlicensing.schema.context.Property;

/**
 * Convert {@link Item} entity into {@link ProductModule} object.
 */
public class ItemToProductModuleConverter extends ItemToEntityBaseConverter<ProductModule> {

    @Override
    public ProductModule convert(final Item source) {
        final ProductModule target = super.convert(source);

        target.setName(SchemaFunction.propertyByName(source.getProperty(), Constants.NAME).getValue());
        target.setLicensingModel(SchemaFunction.propertyByName(source.getProperty(),
                Constants.ProductModule.LICENSING_MODEL).getValue());

        target.setProduct(new Product());
        target.getProduct().setNumber(
                SchemaFunction.propertyByName(source.getProperty(), Constants.Product.PRODUCT_NUMBER).getValue());

        // Custom properties
        for (final Property property : source.getProperty()) {
            if (!ProductModule.getReservedProps().contains(property.getName())) {
                target.getProductModuleProperties().put(property.getName(), property.getValue());
            }
        }

        return target;
    }

    @Override
    public ProductModule newTarget() {
        return new ProductModule();
    }

}
