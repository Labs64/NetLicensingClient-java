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

    public static final String SEPARATOR = ";";
    public static final String ID = "id";
    public static final String ACTIVE = "active";
    public static final String NUMBER = "number";
    public static final String NAME = "name";
    public static final String VERSION = "version";
    public static final String DELETED = "deleted";
    public static final String CASCADE = "forceCascade";
    public static final String PRICE = "price";
    public static final String GRANDTOTAL = "grandtotal";
    public static final String DISCOUNT = "discount";
    public static final String CURRENCY = "currency";
    public static final String IN_USE = "inUse";
    public static final String PAGE = "page";
    public static final String ITEMS = "items";
    public static final String SORT_ORDER = "sortorder";
    public static final String SORT_DIRECTION = "sortdirection";
    public static final String TABLE_ITEMS_ROWS = "table.items.rows";
    public static final String PATH = "path";
    public static final String FILTER = "filter";
    public static final String FILTER_KEY = "filterkey";
    public static final String FILTER_VALUE = "filtervalue";
    public static final String RELOAD_FILTER_PARAMS = "reloadfilterparams";
    public static final int PRICE_SCALE = 2;
    public static final int MAX_PAGE_ITEMS = 100;
    public static final String BASE_URL = "baseUrl";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String SECURITY_MODE = "securityMode";
    public static final String PROP_ID = "ID";
    public static final String PROP_TTL = "TTL";

    public static final class Utility {
        public static final String ENDPOINT_PATH = "utility";
        public static final String LICENSING_MODELS = "licensingModels";
        public static final String LICENSE_TYPES = "licenseTypes";
        public static final String LICENSING_MODEL_PROPERTIES = "LicensingModelProperties";
        public static final String LICENSE_TYPE = "LicenseType";
    }

    public static final class Validation {
        public static final String NO_ENTITY_FOUND = "FilterValueEntityNotFound";
        public static final String TOKEN_VALIDATION = "TokenValidation";
        public static final String ACCOUNT_VALIDATION = "AccountValidation";
        public static final String ACCOUNT_DUPLICATE_EMAIL = "AccountDuplicateEmail";
        public static final String NO_ACTIVE_PRODUCT_MODULES = "NoActiveProductModules";
    }

    public static final class Internal {
        public static final String ENDPOINT_PATH = "internal";
        public static final String MANAGE_CACHE = "manageCache";
    }

    public static final class Token {
        public static final String ENDPOINT_PATH = "token";
        public static final String ENTITY_NAME = "token";
        public static final String TOKEN_NUMBER = "tokenNumber";
        public static final String EXPIRATION_TIME = "expirationTime";
        public static final String TOKEN_TYPE = "tokenType";

        public static final String API_KEY = "apiKey";
        public static final String MULTIPLE_API_KEY = "multipleApiKey";

        public static final String TOKEN_PROP_EMAIL = "email";
        public static final String TOKEN_PROP_VENDORNUMBER = "vendorNumber";
        public static final String TOKEN_PROP_SHOP_URL = "shopURL";
    }

    public static final class Vendor {
        public static final String VENDOR_NUMBER = "vendorNumber";
    }

    public static final class Product {
        public static final String ENDPOINT_PATH = "product";
        public static final String ENTITY_NAME = "product";
        public static final String PRODUCT_NUMBER = "productNumber";
        public static final String LICENSEE_AUTO_CREATE = "licenseeAutoCreate";
        public static final String DESCRIPTION = "description";
        public static final String LICENSING_INFO = "licensingInfo";
        public static final String DISCOUNTS = "discounts";

        public static final class Discount {
            public static final String TOTAL_PRICE = "totalPrice";
            public static final String AMOUNT_FIX = "amountFix";
            public static final String AMOUNT_PERCENT = "amountPercent";
        }
    }

    public static final class ProductModule {
        public static final String ENDPOINT_PATH = "productmodule";
        public static final String ENTITY_NAME = "productModule";
        public static final String PRODUCT_MODULE_VALIDATION_TYPE = "ProductModuleValidation";
        public static final String PRODUCT_MODULE_NUMBER = "productModuleNumber";
        public static final String PRODUCT_MODULE_NAME = "productModuleName";
        public static final String LICENSING_MODEL = "licensingModel";
    }

    public static final class LicenseTemplate {
        public static final String ENDPOINT_PATH = "licensetemplate";
        public static final String ENTITY_NAME = "licenseTemplate";
        public static final String LICENSE_TEMPLATE_NUMBER = "licenseTemplateNumber";
        public static final String LICENSE_TYPE = "licenseType";
        public static final String AUTOMATIC = "automatic";
        public static final String HIDDEN = "hidden";
        public static final String HIDE_LICENSES = "hideLicenses";
    }

    public static final class Licensee {
        public static final String ENDPOINT_PATH = "licensee";
        public static final String ENDPOINT_PATH_VALIDATE = "validate";
        public static final String ENTITY_NAME = "licensee";
        public static final String LICENSEE_NUMBER = "licenseeNumber";
        public static final String PROP_LICENSEE_NAME = "licenseeName";
        public static final String PROP_EMAIL = "eMail";
    }

    public static final class License {
        public static final String ENDPOINT_PATH = "license";
        public static final String ENTITY_NAME = "license";
        public static final String LICENSE_NUMBER = "licenseNumber";
        public static final String HIDDEN = "hidden";
        public static final String PROP_PARENT_FEATURE = "parentFeature";
        public static final String PROP_TIME_VOLUME = "timeVolume";
        public static final String PROP_START_DATE = "startDate";
    }

    public static final class LicensingModel {
        public static final class TimeLimitedEvaluation {
            public static final String NAME = "TimeLimitedEvaluation";
        }

        public static final class FeatureWithTimeVolume {
            public static final String NAME = "FeatureWithTimeVolume";
            public static final String VALID = "valid";
            public static final String EXPIRES = "expires";
            public static final String RED_THRESHOLD = "redThreshold";
            public static final String YELLOW_THRESHOLD = "yellowThreshold";
            public static final String EXPIRATION_WARNING_LEVEL = "expirationWarningLevel";
            public static final String LEVEL_RED = "red";
            public static final String LEVEL_YELLOW = "yellow";
            public static final String LEVEL_GREEN = "green";
        }

        public static final class TimeVolume {
            public static final String NAME = "TimeVolume";
            public static final String VALID = "valid";
            public static final String EXPIRES = "expires";
        }
    }

    public static final class Transaction {
        public static final String ENDPOINT_PATH = "transaction";
        public static final String ENDPOINT_PATH_SENDORDERCONFIRMATION = "sendorderconfirmation";
        public static final String ENTITY_NAME = "transaction";
        public static final String TRANSACTION_NUMBER = "transactionNumber";
        public static final String STATUS = "status";
        public static final String SOURCE = "source";
        public static final String DATE_CREATED = "datecreated";
        public static final String DATE_CLOSED = "dateclosed";
        public static final String DB_DATE_CREATED = "dateCreated";
        public static final String SOURCE_SHOP_ONLY = "shopOnly";
    }

    public static final class PaymentMethod {
        public static final String ENDPOINT_PATH = "paymentmethod";
        public static final String ENTITY_NAME = "paymentMethod";
        public static final String PAYMENT_METHOD_NUMBER = "paymentMethodNumber";

        public static final class PayPal {
            public static final String SUBJECT = "paypal.subject";
        }
    }

    public static final class Shop {
        public static final String PROP_SHOP_LICENSE_ID = "shop-license-id";
        public static final String PROP_SHOPPING_CART = "shopping-cart";
        public static final String SHOP_URL = "shopURL";
    }

    public static final class ValidationResult {
        public static final String VALIDATION_RESULT_TYPE = "ProductModuleValidation";
    }

    // CHECKSTYLE:ON
}
