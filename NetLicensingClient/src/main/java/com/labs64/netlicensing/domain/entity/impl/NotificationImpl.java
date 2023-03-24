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
package com.labs64.netlicensing.domain.entity.impl;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.*;
import com.labs64.netlicensing.domain.vo.Event;
import com.labs64.netlicensing.domain.vo.NotificationType;

import javax.ws.rs.core.MultivaluedMap;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Default implementation of {@link Product}.
 */
public class NotificationImpl extends BaseEntityImpl implements Notification {
    private String name;

    private Set<Event> events = new HashSet<>();

    private NotificationType type;

    private String url;

    private String payload;


    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public Set<Event> getEvents() {
        return events;
    }

    @Override
    public void setEvents(final Set<Event> events) {
        this.events = events;
    }

    @Override
    public void addEvent(final Event event) {
        final Set<Event> events = getEvents();
        events.add(event);
        setEvents(events);
    }

    @Override
    public NotificationType getType() {
        return type;
    }

    @Override
    public void setType(final NotificationType type) {
        this.type = type;
    }

    @Override
    public String getURL() {
        return url;
    }

    @Override
    public void setURL(final String url) {
        this.url = url;
    }

    @Override
    public String getPayload() {
        return payload;
    }

    @Override
    public void setPayload(final String payload) {
        this.payload = payload;
    }

    public static List<String> getReservedProps() {
        final List<String> reserved = BaseEntityImpl.getReservedProps();
        reserved.add(Constants.Notification.EVENTS);
        reserved.add(Constants.Notification.TYPE);
        reserved.add(Constants.Notification.URL);
        reserved.add(Constants.Notification.PAYLOAD);
        return reserved;
    }

    @Override
    protected MultivaluedMap<String, Object> asPropertiesMap() {
        final Set<Event> events = getEvents();
        final NotificationType type = getType();

        final MultivaluedMap<String, Object> map = super.asPropertiesMap();
        map.add(Constants.NAME, getName());
        map.add(Constants.Notification.EVENTS, events.stream().map(Enum::name).collect(Collectors.joining(",")));

        if (type != null) {
            map.add(Constants.Notification.TYPE, type.name());
        }

        if (NotificationType.WEBHOOK.equals(type)) {
            map.add(Constants.Notification.URL, getURL());
        }

        map.add(Constants.Notification.PAYLOAD, getPayload());

        return map;
    }
}
