package com.labs64.netlicensing.domain.vo;

/**
 * The transaction source enumeration.
 */
public enum TransactionSource {

    /**
     * Shop transaction.
     */
    SHOP,

    /**
     * Auto transaction for license create.
     */
    AUTO_LICENSE_CREATE,

    /**
     * Auto transaction for license update.
     */
    AUTO_LICENSE_UPDATE,

    /**
     * Auto transaction for license delete.
     */
    AUTO_LICENSE_DELETE,

    /**
     * Auto transaction for licensee create (with automatic licenses).
     */
    AUTO_LICENSEE_CREATE,

    /**
     * Auto transaction for licensee delete with forceCascade.
     */
    AUTO_LICENSEE_DELETE,

    /**
     * Auto transaction for license template delete with forceCascade.
     */
    AUTO_LICENSETEMPLATE_DELETE,

    /**
     * Auto transaction for product module delete with forceCascade.
     */
    AUTO_PRODUCTMODULE_DELETE,

    /**
     * Auto transaction for product delete with forceCascade.
     */
    AUTO_PRODUCT_DELETE,

    /**
     * Auto transaction for transfer licenses between licensee.
     */
    AUTO_LICENSES_TRANSFER,

    /**
     * Transaction for update licenses (inactive with PT LM, Subscription).
     */
    SUBSCRIPTION_UPDATE
}
