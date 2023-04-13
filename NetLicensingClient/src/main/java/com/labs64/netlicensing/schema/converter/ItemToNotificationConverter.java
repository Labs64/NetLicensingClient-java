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
package com.labs64.netlicensing.schema.converter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.Notification;
import com.labs64.netlicensing.domain.entity.Product;
import com.labs64.netlicensing.domain.entity.impl.NotificationImpl;
import com.labs64.netlicensing.domain.vo.Event;
import com.labs64.netlicensing.domain.vo.NotificationProtocol;
import com.labs64.netlicensing.exception.ConversionException;
import com.labs64.netlicensing.schema.SchemaFunction;
import com.labs64.netlicensing.schema.context.Item;
import com.labs64.netlicensing.schema.context.Property;

/**
 * Convert {@link Item} entity into {@link Product} object.
 */
public class ItemToNotificationConverter extends ItemToEntityBaseConverter<Notification> {

    @Override
    public Notification convert(final Item source) throws ConversionException {
        final Notification target = super.convert(source);

        target.setName(SchemaFunction.propertyByName(source.getProperty(), Constants.NAME).getValue());

        final Set<Event> events = Arrays
                .stream(SchemaFunction.propertyByName(source.getProperty(), Constants.Notification.EVENTS).getValue().split(","))
                .map(Event::parseString)
                .collect(Collectors.toSet());

        target.setEvents(events);
        target.setProtocol(NotificationProtocol.parseString(SchemaFunction.propertyByName(source.getProperty(), Constants.Notification.PROTOCOL).getValue()));
        
        final String endpoint = SchemaFunction.propertyByName(source.getProperty(), Constants.Notification.ENDPOINT).getValue();

        if (endpoint != null) {
            target.addProperty(Constants.Notification.ENDPOINT, endpoint);
        }

        target.setPayload(SchemaFunction.propertyByName(source.getProperty(), Constants.Notification.PAYLOAD).getValue());

        // Custom properties
        for (final Property property : source.getProperty()) {
            if (!NotificationImpl.getReservedProps().contains(property.getName())) {
                target.addProperty(property.getName(), property.getValue());
            }
        }

        return target;
    }

    @Override
    public Notification newTarget() {
        return new NotificationImpl();
    }

}
