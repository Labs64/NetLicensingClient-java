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

/**
 * The class Constants declares entities field names and properties constants.
 */
public final class Constants {

    private Constants() {
    }

    // CHECKSTYLE:OFF

    public static final String ID = "id";
    public static final String ACTIVE = "active";
    public static final String NUMBER = "number";
    public static final String NAME = "name";
    public static final String VERSION = "version";
    public static final String DELETED = "deleted";
    public static final String CASCADE = "forceCascade";
    public static final String PRICE = "price";
    public static final String DISCOUNT = "discount";
    public static final String CURRENCY = "currency";
    public static final String IN_USE = "inUse";
    public static final String FILTER = "filter";
    public static final String BASE_URL = "baseUrl";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String SECURITY_MODE = "securityMode";
    public static final String PROP_ID = "ID";

    public static final class Utility {
        public static final String ENDPOINT_PATH = "utility";
        public static final String ENDPOINT_PATH_LICENSE_TYPES = "licenseTypes";
        public static final String ENDPOINT_PATH_LICENSING_MODELS = "licensingModels";
        public static final String LICENSING_MODEL_PROPERTIES = "LicensingModelProperties";
        public static final String LICENSE_TYPE = "LicenseType";
    }

    public static final class Token {
        public static final String ENDPOINT_PATH = "token";
        public static final String EXPIRATION_TIME = "expirationTime";
        public static final String TOKEN_TYPE = "tokenType";

        public static final String API_KEY = "apiKey";

        public static final String TOKEN_PROP_EMAIL = "email";
        public static final String TOKEN_PROP_VENDORNUMBER = "vendorNumber";
        public static final String TOKEN_PROP_SHOP_URL = "shopURL";
        public static final String TOKEN_PROP_PRIVATE_KEY = "privateKey";
    }

    public static final class Vendor {
        public static final String VENDOR_NUMBER = "vendorNumber";
        public static final String VENDOR_TYPE = "Vendor";
    }

    public static final class Product {
        public static final String ENDPOINT_PATH = "product";
        public static final String PRODUCT_NUMBER = "productNumber";
        public static final String LICENSEE_AUTO_CREATE = "licenseeAutoCreate";
        public static final String DESCRIPTION = "description";
        public static final String LICENSING_INFO = "licensingInfo";
        public static final String DISCOUNTS = "discounts";
        /**
         * @deprecated please use com.labs64.netlicensing.domain.Constants.ProductModule#PROP_LICENSEE_SECRET_MODE instead.
         */
        @Deprecated
        public static final String PROP_LICENSEE_SECRET_MODE = "licenseeSecretMode";
        public static final String PROP_VAT_MODE = "vatMode";

        public static final class Discount {
            public static final String TOTAL_PRICE = "totalPrice";
            public static final String AMOUNT_FIX = "amountFix";
            public static final String AMOUNT_PERCENT = "amountPercent";
        }
    }

    public static final class ProductModule {
        public static final String ENDPOINT_PATH = "productmodule";
        public static final String PRODUCT_MODULE_NUMBER = "productModuleNumber";
        public static final String PRODUCT_MODULE_NAME = "productModuleName";
        public static final String LICENSING_MODEL = "licensingModel";
        public static final String PROP_LICENSEE_SECRET_MODE = "licenseeSecretMode";
    }

    public static final class LicenseTemplate {
        public static final String ENDPOINT_PATH = "licensetemplate";
        public static final String LICENSE_TEMPLATE_NUMBER = "licenseTemplateNumber";
        public static final String LICENSE_TYPE = "licenseType";
        public static final String AUTOMATIC = "automatic";
        public static final String HIDDEN = "hidden";
        public static final String HIDE_LICENSES = "hideLicenses";
        public static final String PROP_LICENSEE_SECRET = "licenseeSecret";
    }

    public static final class Licensee {
        public static final String ENDPOINT_PATH = "licensee";
        public static final String ENDPOINT_PATH_VALIDATE = "validate";
        public static final String ENDPOINT_PATH_TRANSFER = "transfer";
        public static final String LICENSEE_NUMBER = "licenseeNumber";
        public static final String SOURCE_LICENSEE_NUMBER = "sourceLicenseeNumber";
        public static final String PROP_LICENSEE_NAME = "licenseeName";
        /**
         * @deprecated please use com.labs64.netlicensing.domain.Constants.License#PROP_LICENSEE_SECRET instead.
         */
        @Deprecated
        public static final String PROP_LICENSEE_SECRET = "licenseeSecret";
        public static final String PROP_MARKED_FOR_TRANSFER = "markedForTransfer";
    }

    public static final class License {
        public static final String ENDPOINT_PATH = "license";
        public static final String HIDDEN = "hidden";
        public static final String LICENSE_NUMBER = "licenseNumber";
        public static final String PROP_LICENSEE_SECRET = "licenseeSecret";
    }

    public static final class LicensingModel {

        public static final String VALID = "valid";

        public static final class TryAndBuy {
            public static final String NAME = "TryAndBuy";
        }

        public static final class Rental {
            public static final String NAME = "Rental";
            /**
             * @deprecated please use com.labs64.netlicensing.domain.Constants.LicensingModel#VALID instead.
             */
            @Deprecated
            public static final String VALID = "valid";
            public static final String RED_THRESHOLD = "redThreshold";
            public static final String YELLOW_THRESHOLD = "yellowThreshold";
            public static final String EXPIRATION_WARNING_LEVEL = "expirationWarningLevel";
        }

        public static final class Subscription {
            public static final String NAME = "Subscription";
        }

        public static final class Floating {
            public static final String NAME = "Floating";
        }

        public static final class MultiFeature {
            public static final String NAME = "MultiFeature";
        }

        public static final class PayPerUse {
            public static final String NAME = "PayPerUse";
        }

        public static final class PricingTable {
            public static final String NAME = "PricingTable";
        }

        public static final class Quota {
            public static final String NAME = "Quota";
        }

        public static final class NodeLocked {
            public static final String NAME = "NodeLocked";
        }
    }

    public static final class Transaction {
        public static final String ENDPOINT_PATH = "transaction";
        public static final String TRANSACTION_NUMBER = "transactionNumber";
        public static final String GRAND_TOTAL = "grandTotal";
        public static final String STATUS = "status";
        public static final String SOURCE = "source";
        public static final String DATE_CREATED = "datecreated";
        public static final String DATE_CLOSED = "dateclosed";
        public static final String VAT = "vat";
        public static final String VAT_MODE = "vatMode";
        public static final String LICENSE_TRANSACTION_JOIN = "licenseTransactionJoin";
        public static final String SOURCE_SHOP_ONLY = "shopOnly";
    }

    public static final class PaymentMethod {
        public static final String ENDPOINT_PATH = "paymentmethod";
    }

    public static final class Country {
        public static final String ENDPOINT_PATH = "countries";
        public static final String CODE = "code";
        public static final String NAME = "name";
        public static final String VAT_PERCENT = "vatPercent";
        public static final String IS_EU = "isEu";
    }

    public static final class Shop {
        public static final String PROP_SHOP_LICENSE_ID = "shop-license-id";
        public static final String PROP_SHOPPING_CART = "shopping-cart";
    }

    public static final class ValidationResult {
        public static final String VALIDATION_RESULT_TYPE = "ProductModuleValidation";
        public static final int DEFAULT_TTL_MINUTES = 60 * 24; // 1 day
    }

    // CHECKSTYLE:ON
}
