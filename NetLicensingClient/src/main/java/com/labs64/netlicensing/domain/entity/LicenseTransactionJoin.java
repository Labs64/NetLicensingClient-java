package com.labs64.netlicensing.domain.entity;

import java.io.Serializable;

public interface LicenseTransactionJoin extends Serializable {

    void setTransaction(final Transaction transaction);

    Transaction getTransaction();

    void setLicense(final License license);

    License getLicense();
}
