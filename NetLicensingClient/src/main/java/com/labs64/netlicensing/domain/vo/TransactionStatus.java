package com.labs64.netlicensing.domain.vo;

/**
 * The transaction status enumeration.
 */
public enum TransactionStatus {

    /**
     * Transaction still running.
     */
    PENDING,

    /**
     * Transaction is closed.
     */
    CLOSED,

    /**
     * Transaction is cancelled.
     */
    CANCELLED,

    /**
     * Transaction is failed.
     */
    FAILED
}
