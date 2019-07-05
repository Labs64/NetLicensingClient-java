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
            final JAXBContext jaxbContext = JAXBContext.newInstance(expectedType);
            final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            final JAXBElement<T> element = unmarshaller.unmarshal(new StreamSource(inputStream), expectedType);
            return element.getValue();
        } catch (final JAXBException e) {
            throw new RuntimeException("Cannot process resource.", e);
        }
    }

    public static <T> String xmlEntityToString(final T entity) {
        try {
            final JAXBContext jaxbContext = JAXBContext.newInstance(entity.getClass());
            final Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            marshaller.marshal(entity, out);
            return out.toString();
        } catch (final JAXBException e) {
            throw new RuntimeException("Cannot convert object to string.", e);
        }
    }

}
