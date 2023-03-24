package com.labs64.netlicensing.domain.entity;

import com.labs64.netlicensing.domain.vo.Event;
import com.labs64.netlicensing.domain.vo.NotificationType;

import java.util.Set;

public interface Notification extends BaseEntity {
    String getName();

    void setName(String name);
    
    Set<Event> getEvents();
    
    void setEvents(Set<Event> events);

    void addEvent(final Event event);
    
    NotificationType getType();

    void setType(NotificationType notificationType);

    String getURL();

    void setURL(String url);

    String getPayload();

    void setPayload(String payload);
}
