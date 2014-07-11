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

import javax.xml.bind.DatatypeConverter;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.Transaction;
import com.labs64.netlicensing.domain.entity.TransactionImpl;
import com.labs64.netlicensing.domain.vo.TransactionSource;
import com.labs64.netlicensing.domain.vo.TransactionStatus;
import com.labs64.netlicensing.exception.ConversionException;
import com.labs64.netlicensing.schema.SchemaFunction;
import com.labs64.netlicensing.schema.context.Item;
import com.labs64.netlicensing.schema.context.Property;
import com.labs64.netlicensing.util.DateUtils;

/**
 * Convert {@link Item} entity into {@link Transaction} object.
 */
public class ItemToTransactionConverter extends ItemToEntityBaseConverter<Transaction> {

    @Override
    public Transaction convert(final Item source) throws ConversionException {
        final Transaction target = super.convert(source);

        target.setStatus(TransactionStatus.valueOf(SchemaFunction.propertyByName(source.getProperty(),
                Constants.Transaction.STATUS)
                .getValue()));
        target.setSource(TransactionSource.valueOf(SchemaFunction.propertyByName(source.getProperty(),
                Constants.Transaction.SOURCE)
                .getValue()));
        if (SchemaFunction.propertyByName(source.getProperty(), Constants.PRICE).getValue() != null) {
            target.setPrice(DatatypeConverter.parseDecimal(SchemaFunction.propertyByName(source.getProperty(),
                    Constants.PRICE).getValue()));
        }
        if (SchemaFunction.propertyByName(source.getProperty(), Constants.DISCOUNT).getValue() != null) {
            target.setDiscount(DatatypeConverter.parseDecimal(SchemaFunction.propertyByName(source.getProperty(),
                    Constants.DISCOUNT).getValue()));
        }
        target.setCurrency(SchemaFunction.propertyByName(source.getProperty(), Constants.CURRENCY).getValue());

        if (SchemaFunction.propertyByName(source.getProperty(), Constants.Transaction.DATE_CREATED).getValue() != null) {
            target.setDateCreated(DateUtils.parseDate(SchemaFunction.propertyByName(
                    source.getProperty(), Constants.Transaction.DATE_CREATED).getValue()).getTime());
        }

        if (SchemaFunction.propertyByName(source.getProperty(), Constants.Transaction.DATE_CLOSED).getValue() != null) {
            target.setDateClosed(DateUtils.parseDate(SchemaFunction.propertyByName(
                    source.getProperty(), Constants.Transaction.DATE_CLOSED).getValue()).getTime());
        }

        // Custom properties
        for (final Property property : source.getProperty()) {
            if (!TransactionImpl.getReservedProps().contains(property.getName())) {
                target.getTransactionProperties().put(property.getName(), property.getValue());
            }
        }

        return target;
    }

    @Override
    public Transaction newTarget() {
        return new TransactionImpl();
    }

}
