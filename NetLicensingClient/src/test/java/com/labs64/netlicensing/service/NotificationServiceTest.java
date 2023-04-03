/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.labs64.netlicensing.service;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.Notification;
import com.labs64.netlicensing.domain.entity.impl.NotificationImpl;
import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.domain.vo.Event;
import com.labs64.netlicensing.domain.vo.NotificationType;
import com.labs64.netlicensing.domain.vo.Page;
import com.labs64.netlicensing.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.Path;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Integration tests for {@link ProductService}.
 */
public class NotificationServiceTest extends BaseServiceTest {

    private static final String NOTIFICATION_CUSTOM_PROPERTY = "CustomProperty";

    // *** NLIC Tests ***

    private static Context context;

    @BeforeClass
    public static void setup() {
        context = createContext();
    }

    @Test
    public void testCreate() throws Exception {
        final Notification newNotification = new NotificationImpl();
        newNotification.setName("Test Notification");
        newNotification.setNumber("N001-TEST");
        newNotification.setActive(true);

        final Set<Event> events = new HashSet<>();
        events.add(Event.CREATE_LICENSEE);

        newNotification.setEvents(events);
        newNotification.setType(NotificationType.WEBHOOK);
        newNotification.setURL("http://www.test.test");
        newNotification.setPayload("${event}");
        newNotification.addProperty(NOTIFICATION_CUSTOM_PROPERTY, "Test Value");

        final Notification createdNotification = NotificationService.create(context, newNotification);

        assertNotNull(createdNotification);
        assertEquals("Test Notification", createdNotification.getName());
        assertEquals("N001-TEST", createdNotification.getNumber());
        assertEquals(true, createdNotification.getActive());
        assertEquals(events, createdNotification.getEvents());
        assertEquals(NotificationType.WEBHOOK, createdNotification.getType());
        assertEquals("http://www.test.test", createdNotification.getURL());
        assertEquals("${event}", createdNotification.getPayload());
        assertEquals("Test Value", createdNotification.getProperties().get(NOTIFICATION_CUSTOM_PROPERTY));
    }

    @Test
    public void testNameIsRequired() throws Exception {
        final Notification newNotification = new NotificationImpl();
        newNotification.addEvent(Event.CREATE_LICENSEE);
        newNotification.setType(NotificationType.WEBHOOK);
        newNotification.setURL("http://www.test.test");

        final Exception e = assertThrows(ServiceException.class, () -> {
            NotificationService.create(context, newNotification);
        });

        assertEquals("ValidationException: Notification name is required.", e.getMessage());
    }

    @Test
    public void testEventsIsRequired() throws Exception {
        final Notification newNotification = new NotificationImpl();
        newNotification.setName("Notification 1");
        newNotification.setType(NotificationType.WEBHOOK);
        newNotification.setURL("http://www.test.test");

        final Exception e = assertThrows(ServiceException.class, () -> {
            NotificationService.create(context, newNotification);
        });

        assertEquals("ValidationException: Notification events are required.", e.getMessage());
    }

    @Test
    public void testTypeIsRequired() throws Exception {
        final Notification newNotification = new NotificationImpl();
        newNotification.setName("Notification 1");
        newNotification.addEvent(Event.CREATE_LICENSEE);
        newNotification.setURL("http://www.test.test");
        newNotification.setPayload("${event}");

        final Exception e = assertThrows(ServiceException.class, () -> {
            NotificationService.create(context, newNotification);
        });

        assertEquals("ValidationException: Notification type is required.", e.getMessage());
    }

    @Test
    public void testURLIsRequired() throws Exception {
        final Notification newNotification = new NotificationImpl();
        newNotification.setName("Notification 1");
        newNotification.addEvent(Event.CREATE_LICENSEE);
        newNotification.setType(NotificationType.WEBHOOK);

        final Exception e = assertThrows(ServiceException.class, () -> {
            NotificationService.create(context, newNotification);
        });

        assertEquals("ValidationException: Notification URL must be a valid URL.", e.getMessage());
    }

    @Test
    public void testGet() throws Exception {
        final Notification notification = NotificationService.get(context, "N014-TEST");
        assertNotNull(notification);
        assertEquals("N014-TEST", notification.getNumber());
        assertEquals("Notification 14", notification.getName());

        final Set<Event> expectedEvents = new HashSet<>();
        expectedEvents.add(Event.CREATE_LICENSEE);
        expectedEvents.add(Event.CREATE_LICENSE);

        assertEquals(expectedEvents, notification.getEvents());
        assertEquals(NotificationType.WEBHOOK, notification.getType());
        assertEquals("http://www.test.test", notification.getURL());
        assertEquals("${event}", notification.getPayload());
        assertEquals("CustomPropertyValue", notification.getProperties().get(NOTIFICATION_CUSTOM_PROPERTY));
    }

    @Test
    public void testList() throws Exception {
        final Page<Notification> notifications = NotificationService.list(context, null);

        assertNotNull(notifications);
        assertTrue(notifications.hasContent());
        assertEquals(3, notifications.getItemsNumber());
        assertEquals("N001-TEST", notifications.getContent().get(0).getNumber());
        assertEquals("Notification 2", notifications.getContent().get(1).getName());
        assertEquals(Event.CREATE_LICENSEE.name(), notifications.getContent().get(2).getEvents().stream().map(Enum::name).collect(Collectors.joining(",")));
    }

    @Test
    public void testUpdate() throws Exception {
        final Notification notification = new NotificationImpl();
        notification.setName("Notification 2");
        notification.setNumber("N002-TEST");
        notification.addProperty(NOTIFICATION_CUSTOM_PROPERTY, "Test Value");

        final Set<Event> events = new HashSet<>();
        events.add(Event.CREATE_LICENSE);

        notification.setEvents(events);

        final Notification updatedNotification = NotificationService.update(context, "N001-TEST", notification);

        assertNotNull(updatedNotification);
        assertEquals("Notification 2", updatedNotification.getName());
        assertEquals("N002-TEST", updatedNotification.getNumber());
        assertEquals(false, updatedNotification.getActive());
        assertEquals(Event.CREATE_LICENSE.name(), updatedNotification.getEvents().stream().map(Enum::name).collect(Collectors.joining(",")));
    }

    @Test
    public void testDelete() throws Exception {
        NotificationService.delete(context, "N001-TEST");

        final Exception e = assertThrows(ServiceException.class, () -> {
            NotificationService.delete(context, "N001-NONE");
        });
        assertEquals("NotFoundException: Requested notification does not exist", e.getMessage());
    }

    // *** NLIC test mock resource ***

    @Override
    protected Class<?> getResourceClass() {
        return NotificationServiceResource.class;
    }

    @Path(REST_API_PATH + "/notification")
    public static class NotificationServiceResource extends AbstractNLICServiceResource {

        public NotificationServiceResource() {
            super("notification");
        }

        @Override
        public Response create(final MultivaluedMap<String, String> formParams) {
            if (!formParams.containsKey(Constants.NAME)) {
                return errorResponse("ValidationException", "Notification name is required.");
            }

            final boolean isEventsContain = formParams.containsKey(Constants.Notification.EVENTS);
            final boolean isEventEmpty = (isEventsContain)
                    ? StringUtils.isBlank(formParams.get(Constants.Notification.EVENTS).get(0))
                    : true;
            
            if (!isEventsContain || isEventEmpty) {
                return errorResponse("ValidationException", "Notification events are required.");
            }

            if (!formParams.containsKey(Constants.Notification.TYPE)) {
                return errorResponse("ValidationException", "Notification type is required.");
            }

            String type = String.valueOf(formParams.get(Constants.Notification.TYPE).get(0));

            if (NotificationType.WEBHOOK.name().equals(type)) {
                if (!formParams.containsKey(Constants.Notification.URL)) {
                    return errorResponse("ValidationException", "Notification URL must be a valid URL.");
                }
            }


            final Map<String, String> defaultPropertyValues = new HashMap<>();
            defaultPropertyValues.put(Constants.ACTIVE, "true");
            return create(formParams, defaultPropertyValues);
        }

        @Override
        public Response delete(final String notificationNumber, final UriInfo uriInfo) {
            return delete(notificationNumber, "N001-TEST", uriInfo.getQueryParameters());
        }
    }

}
