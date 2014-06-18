package com.labs64.netlicensing.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

/**
 */
public final class JAXBUtils {

    public static <T> T readObject(final String resource, final Class<T> expectedType) {
        return readObjectFromInputStream(JAXBUtils.class.getClassLoader().getResourceAsStream(resource), expectedType);
    }

    public static <T> T readObjectFromString(final String content, final Class<T> expectedType) {
        return readObjectFromInputStream(new ByteArrayInputStream(content.getBytes()), expectedType);
    }

    public static <T> T readObjectFromInputStream(final InputStream inputStream, final Class<T> expectedType) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(expectedType);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            JAXBElement<T> element = unmarshaller.unmarshal(new StreamSource(inputStream), expectedType);
            return element.getValue();
        } catch (final JAXBException e) {
            throw new RuntimeException("Cannot process resource!", e);
        }
    }

    public static <T> String xmlEntityToString(final T entity) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(entity.getClass());
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            marshaller.marshal(entity, out);
            return out.toString();
        } catch (final JAXBException e) {
            throw new RuntimeException("Cannot convert object to string !", e);
        }
    }

}
