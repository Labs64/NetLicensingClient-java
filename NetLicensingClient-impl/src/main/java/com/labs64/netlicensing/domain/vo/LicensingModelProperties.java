package com.labs64.netlicensing.domain.vo;

/**
 * Licensing models must implement this interface.
 * <p>
 * On events that require licensing model specific actions, corresponding event handlers are called. Event handlers that
 * do not return any values expected to validate the passed arguments, possibly modifying them. Must throw exceptions in
 * case validation is not passed.
 */
public interface LicensingModelProperties {

    /**
     * Gets the licensing model name.
     * 
     * @return the licensing model name
     */
    String getName();

}
