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
package com.labs64.netlicensing.domain.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.vo.Currency;
import com.labs64.netlicensing.domain.vo.TransactionSource;
import com.labs64.netlicensing.domain.vo.TransactionStatus;

/**
 * Default implementation of {@link Transaction}.
 */
public class TransactionImpl extends BaseEntityImpl implements Transaction {

    private TransactionStatus status;

    private TransactionSource source;

    private BigDecimal price;

    private BigDecimal discount;

    private Currency currency;

    private Date dateCreated;

    private Date dateClosed;

    private Map<String, String> transactionProperties;

    /**
     * @see com.labs64.netlicensing.domain.entity.BaseEntityImpl#getReservedProps()
     */
    public static List<String> getReservedProps() {
        final List<String> reserved = BaseEntityImpl.getReservedProps();
        reserved.add(Constants.Transaction.SOURCE);
        reserved.add(Constants.Transaction.STATUS);
        reserved.add(Constants.Transaction.DATE_CREATED);
        reserved.add(Constants.Transaction.DATE_CLOSED);
        reserved.add(Constants.IN_USE);
        reserved.add(Constants.PRICE);
        reserved.add(Constants.DISCOUNT);
        reserved.add(Constants.CURRENCY);
        return reserved;
    }

    @Override
    public TransactionStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(final TransactionStatus status) {
        this.status = status;
    }

    @Override
    public TransactionSource getSource() {
        return source;
    }

    @Override
    public void setSource(final TransactionSource source) {
        this.source = source;
    }

    @Override
    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    @Override
    public BigDecimal getDiscount() {
        return discount;
    }

    @Override
    public void setDiscount(final BigDecimal discount) {
        this.discount = discount;
    }

    @Override
    public Currency getCurrency() {
        return currency;
    }

    @Override
    public void setCurrency(final Currency currency) {
        this.currency = currency;
    }

    @Override
    public Date getDateCreated() {
        if (dateCreated == null) {
            return null;
        } else {
            return new Date(dateCreated.getTime());
        }
    }

    @Override
    public void setDateCreated(final Date dateCreated) {
        if (dateCreated == null) {
            this.dateCreated = new Date();
        } else {
            this.dateCreated = new Date(dateCreated.getTime());
        }
    }

    @Override
    public Date getDateClosed() {
        if (dateClosed == null) {
            return null;
        } else {
            return new Date(dateClosed.getTime());
        }
    }

    @Override
    public void setDateClosed(final Date dateClosed) {
        if (dateClosed == null) {
            this.dateClosed = null;
        } else {
            this.dateClosed = new Date(dateClosed.getTime());
        }
    }

    @Override
    public Map<String, String> getTransactionProperties() {
        if (transactionProperties == null) {
            transactionProperties = new HashMap<String, String>();
        }
        return transactionProperties;
    }

    public void setTransactionProperties(final Map<String, String> transactionProperties) {
        this.transactionProperties = transactionProperties;
    }

    @Override
    public void addProperty(final String property, final String value) {
        getTransactionProperties().put(property, value);
    }

    @Override
    public void removeProperty(final String property) {
        getTransactionProperties().remove(property);
    }

    @Override
    protected Map<String, Object> asPropertiesMap() {
        final Map<String, Object> map = super.asPropertiesMap();
        map.put(Constants.Transaction.STATUS, getStatus());
        map.put(Constants.Transaction.SOURCE, getSource());
        map.put(Constants.PRICE, getPrice());
        map.put(Constants.DISCOUNT, getDiscount());
        map.put(Constants.CURRENCY, getCurrency());
        if (transactionProperties != null) {
            for (final Map.Entry<String, String> property : transactionProperties.entrySet()) {
                map.put(property.getKey(), property.getValue());
            }
        }
        return map;
    }

}
