package com.labs64.netlicensing.domain.entity;

import java.util.Set;

import com.labs64.netlicensing.domain.vo.Event;
import com.labs64.netlicensing.domain.vo.NotificationProtocol;

public interface Notification extends BaseEntity {
    String getName();

    void setName(String name);
    
    Set<Event> getEvents();
    
    void setEvents(Set<Event> events);

    void addEvent(final Event event);
    
    NotificationProtocol getProtocol();

    void setProtocol(NotificationProtocol notificationProtocol);

    String getPayload();

    void setPayload(String payload);
}
