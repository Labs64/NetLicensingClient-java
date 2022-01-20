package com.labs64.netlicensing.util;

import java.security.Key;

import javax.annotation.Nonnull;
import javax.xml.crypto.KeySelector;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.helger.commons.ValueEnforcer;
import com.helger.xmldsig.XMLDSigSetup;
import com.helger.xmldsig.XMLDSigValidationResult;
import com.helger.xmldsig.XMLDSigValidator;

public class XMLDSigValidatorCustom {

    private XMLDSigValidatorCustom() {
    }

    /**
     * Verify whether Document contains XML signature.
     * @param aDoc document to be verified
     * @return true if Document contains XML signature
     */
    public static boolean containsSignature(@Nonnull final Document aDoc) {
        return XMLDSigValidator.containsSignature(aDoc);
    }

    /**
     * Validate XML (imline) signature.
     * @param aDoc Document containing inline sign certificate
     * @return XMLDSig validation result
     * @throws XMLSignatureException
     */
    public static XMLDSigValidationResult validateSignature(@Nonnull final Document aDoc)
            throws XMLSignatureException {
        return validateSignature(aDoc, null);
    }

    /**
     * Validate XML (detached) signature.
     * @param aDoc Document signature
     * @return XMLDSig validation result
     * @throws XMLSignatureException
     */
    public static XMLDSigValidationResult validateSignature(@Nonnull final Document aDoc, final Key aKey)
            throws XMLSignatureException {
        ValueEnforcer.notNull(aDoc, "Document");

        // Find Signature element.
        final NodeList aSignatureNL = aDoc.getElementsByTagNameNS(XMLSignature.XMLNS, XMLDSigSetup.ELEMENT_SIGNATURE);
        if (aSignatureNL.getLength() != 1) {
            throw new IllegalArgumentException("Cannot find exactly one Signature element.");
        }
        final Element aSignatureElement = (Element) aSignatureNL.item(0);

        if (aKey == null) {
            return XMLDSigValidator.validateSignature(aDoc, aSignatureElement);
        } else {
            return XMLDSigValidator.validateSignature(aDoc, aSignatureElement, KeySelector.singletonKeySelector(aKey));
        }
    }

}
