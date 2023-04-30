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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ws.rs.core.MultivaluedMap;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.Notification;
import com.labs64.netlicensing.domain.entity.Product;
import com.labs64.netlicensing.domain.vo.Event;
import com.labs64.netlicensing.domain.vo.NotificationProtocol;

/**
 * Default implementation of {@link Product}.
 */
public class NotificationImpl extends BaseEntityImpl implements Notification {

    private static final long serialVersionUID = -4059438768485180571L;

	private String name;

    private Set<Event> events = new HashSet<>();

    private NotificationProtocol protocol;

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
        getEvents().add(event);
    }

    @Override
    public NotificationProtocol getProtocol() {
        return protocol;
    }

    @Override
    public void setProtocol(final NotificationProtocol protocol) {
        this.protocol = protocol;
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
        reserved.add(Constants.Notification.PROTOCOL);
        reserved.add(Constants.Notification.ENDPOINT);
        reserved.add(Constants.Notification.PAYLOAD);
        return reserved;
    }

    @Override
    protected MultivaluedMap<String, Object> asPropertiesMap() {
        final Set<Event> events = getEvents();
        final NotificationProtocol protocol = getProtocol();

        final MultivaluedMap<String, Object> map = super.asPropertiesMap();
        map.add(Constants.NAME, getName());
        map.add(Constants.Notification.EVENTS, events.stream().map(Enum::name).collect(Collectors.joining(",")));

        if (protocol != null) {
            map.add(Constants.Notification.PROTOCOL, protocol.name());
        }

        if (NotificationProtocol.WEBHOOK.equals(protocol)) {
            map.add(Constants.Notification.ENDPOINT, getProperties().get(Constants.Notification.ENDPOINT));
        }

        map.add(Constants.Notification.PAYLOAD, getPayload());

        return map;
    }
}
