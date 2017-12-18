package com.labs64.netlicensing.domain.entity;

public interface LicenseTransactionJoin {

    void setTransaction(final Transaction transaction);

    Transaction getTransaction();

    void setLicense(final License license);

    License getLicense();
}
