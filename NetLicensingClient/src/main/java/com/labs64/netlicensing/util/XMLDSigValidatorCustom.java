package com.labs64.netlicensing.util;

import java.security.Key;
import java.util.Iterator;

import javax.annotation.Nonnull;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMValidateContext;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.xmldsig.XMLDSigSetup;
import com.helger.xmldsig.XMLDSigValidationResult;

public class XMLDSigValidatorCustom {

    private XMLDSigValidatorCustom() {
    }

    public static boolean containsSignature(@Nonnull final Document aDoc) {
        ValueEnforcer.notNull(aDoc, "Document");

        final NodeList aSignatureNL = aDoc.getElementsByTagNameNS(XMLSignature.XMLNS, XMLDSigSetup.ELEMENT_SIGNATURE);
        return aSignatureNL.getLength() > 0;
    }

    @Nonnull
    public static XMLDSigValidationResult validateSignature(@Nonnull final Document aDoc, @Nonnull final Key aKey)
            throws XMLSignatureException {
        ValueEnforcer.notNull(aDoc, "Document");
        ValueEnforcer.notNull(aKey, "Key");

        // Find Signature element.
        final NodeList aSignatureNL = aDoc.getElementsByTagNameNS(XMLSignature.XMLNS, XMLDSigSetup.ELEMENT_SIGNATURE);
        if (aSignatureNL.getLength() != 1) {
            throw new IllegalArgumentException("Cannot find exactly one Signature element.");
        }
        final Element aSignatureElement = (Element) aSignatureNL.item(0);

        // Create a DOM XMLSignatureFactory that will be used to validate the
        // enveloped signature.
        final XMLSignatureFactory aSignatureFactory = XMLDSigSetup.getXMLSignatureFactory();

        // Create a DOMValidateContext and specify a KeySelector
        // and document context.
        final DOMValidateContext aValidationContext = new DOMValidateContext(aKey, aSignatureElement);

        // Unmarshal the XMLSignature.
        final XMLSignature aSignature;
        try {
            aSignature = aSignatureFactory.unmarshalXMLSignature(aValidationContext);
        } catch (final MarshalException ex) {
            return XMLDSigValidationResult.createSignatureError();
        }

        // Validate the XMLSignature.
        if (aSignature.validate(aValidationContext)) {
            return XMLDSigValidationResult.createSuccess();
        }

        // Core validation failed. Check the signature value.
        if (!aSignature.getSignatureValue().validate(aValidationContext)) {
            return XMLDSigValidationResult.createSignatureError();
        }

        // Check the validation status of each Reference.
        final ICommonsList<Integer> aInvalidReferences = new CommonsArrayList<>();
        final Iterator<?> it = aSignature.getSignedInfo().getReferences().iterator();
        for (int nIndex = 0; it.hasNext(); nIndex++) {
            final Reference aReference = (Reference) it.next();
            if (!aReference.validate(aValidationContext)) {
                aInvalidReferences.add(Integer.valueOf(nIndex));
            }
        }
        return XMLDSigValidationResult.createReferenceErrors(aInvalidReferences);
    }
}