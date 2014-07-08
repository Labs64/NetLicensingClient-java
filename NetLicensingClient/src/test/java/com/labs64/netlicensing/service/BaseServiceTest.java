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
package com.labs64.netlicensing.service;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;

import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.domain.vo.SecurityMode;
import com.labs64.netlicensing.schema.SchemaFunction;
import com.labs64.netlicensing.schema.context.InfoEnum;
import com.labs64.netlicensing.schema.context.Netlicensing;
import com.labs64.netlicensing.schema.context.ObjectFactory;
import com.labs64.netlicensing.util.JAXBUtils;

/**
 * Base class for integration tests for NetLicensing services.
 */
abstract class BaseServiceTest extends JerseyTest {

    static final String REST_API_PATH = "/core/v2/rest";
    static final String BASE_URL_UNITTEST = "http://localhost:9998";
    static final String BASE_URL_PROD = "https://netlicensing.labs64.com";

    static final String BASE_URL = BASE_URL_UNITTEST + REST_API_PATH;

    static final String USER = "demo";
    static final String PASS = "demo";

    static final String TEST_CASE_BASE = "mock/";

    static Context createContext() {
        return new Context()
                .setBaseUrl(BASE_URL)
                .setSecurityMode(SecurityMode.BASIC_AUTHENTICATION)
                .setUsername(USER)
                .setPassword(PASS);
    }

    @Override
    protected final Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        return new ResourceConfig(getResourceClass());
    }

    /**
     * @return NLIC mock resource class
     */
    protected abstract Class<?> getResourceClass();

    // *** Abstract NLIC service test mock resource ***

    static abstract class AbstractNLICServiceResource {

        /** ID of the service, i.e. "product", "licensee", etc */
        private final String serviceId;

        protected final ObjectFactory objectFactory = new ObjectFactory();

        /**
         * @param serviceId
         */
        public AbstractNLICServiceResource(final String serviceId) {
            this.serviceId = serviceId;
        }

        /**
         * Defines common functionality for a "create" service
         * 
         * @param formParams
         * @param defaultPropertyValues
         * @return
         */
        protected Response create(final MultivaluedMap<String, String> formParams,
                final Map<String, String> defaultPropertyValues) {

            final Netlicensing netlicensing = objectFactory.createNetlicensing();
            netlicensing.setItems(objectFactory.createNetlicensingItems());
            netlicensing.getItems().getItem().add(objectFactory.createItem());

            final Map<String, String> propertyValues = new HashMap<String, String>(defaultPropertyValues);
            for (final String paramKey : formParams.keySet()) {
                propertyValues.put(paramKey, formParams.getFirst(paramKey));
            }
            SchemaFunction.updateProperties(netlicensing.getItems().getItem().get(0).getProperty(), propertyValues);

            return Response.ok(netlicensing).build();
        }

        /**
         * Defines common functionality for a "get" service
         * 
         * @return
         */
        protected Response get() {
            final String xmlResourcePath = String.format("%snetlicensing-%s-get.xml", TEST_CASE_BASE, serviceId);
            final Netlicensing netlicensing = JAXBUtils.readObject(xmlResourcePath, Netlicensing.class);
            return Response.ok(netlicensing).build();
        }

        /**
         * Defines common functionality for a "list" service
         * 
         * @return
         */
        protected Response list() {
            final String xmlResourcePath = String.format("%snetlicensing-%s-list.xml", TEST_CASE_BASE, serviceId);
            final Netlicensing netlicensing = JAXBUtils.readObject(xmlResourcePath, Netlicensing.class);
            return Response.ok(netlicensing).build();
        }

        /**
         * Defines common functionality for an "update" service
         * 
         * @param formParams
         * @return
         */
        protected Response update(final MultivaluedMap<String, String> formParams) {
            final String resourcePath = String.format("%snetlicensing-%s-update.xml", TEST_CASE_BASE, serviceId);
            final Netlicensing netlicensing = JAXBUtils.readObject(resourcePath, Netlicensing.class);

            final Map<String, String> propertyValues = new HashMap<String, String>();
            for (final String paramKey : formParams.keySet()) {
                propertyValues.put(paramKey, formParams.getFirst(paramKey));
            }
            SchemaFunction.updateProperties(netlicensing.getItems().getItem().get(0).getProperty(), propertyValues);

            return Response.ok(netlicensing).build();
        }

        /**
         * Defines common functionality for a "delete" service
         * 
         * @param number
         * @param expectedNumber
         * @param forceCascade
         * @return
         */
        protected Response delete(final String number, final String expectedNumber, final boolean forceCascade) {
            final Netlicensing netlicensing = objectFactory.createNetlicensing();

            if (!expectedNumber.equals(number)) {
                SchemaFunction.setSingleInfo(netlicensing, "NotFoundException", InfoEnum.ERROR,
                        String.format("requested %s does not exist", serviceId));
                return Response.status(Response.Status.BAD_REQUEST).entity(netlicensing).build();
            }
            if (!forceCascade) {
                SchemaFunction.setSingleInfo(netlicensing, "UnexpectedValueException", InfoEnum.ERROR,
                        "Unexpected value of parameter 'forceCascade'");
                return Response.status(Response.Status.BAD_REQUEST).entity(netlicensing).build();
            }
            return Response.status(Response.Status.NO_CONTENT).build();
        }
    }

}
