package com.ftf.coral.core.event;

public class EventListenerLifeCycleEvent<T extends Event<?>> extends Event<EventListenerLifeCycleEventType> {

    EventListenerLifeCycleEventType eventType;

    Class<T> eventClass;
    Object[] eventTypes;
    String[] eventRegions;
    EventListener<T> listener;

    public Class<T> getEventClass() {
        return eventClass;
    }

    public void setEventClass(Class<T> eventClass) {
        this.eventClass = eventClass;
    }

    public Object[] getEventTypes() {
        return eventTypes;
    }

    public void setEventTypes(Object[] eventTypes) {
        this.eventTypes = eventTypes;
    }

    public EventListener<T> getListener() {
        return listener;
    }

    public void setListener(EventListener<T> listener) {
        this.listener = listener;
    }

    public String[] getEventRegions() {
        return eventRegions;
    }

    public void setEventRegions(String[] eventRegion) {
        this.eventRegions = eventRegion;
    }

    public void setEventType(EventListenerLifeCycleEventType eventType) {
        this.eventType = eventType;
    }

    public EventListenerLifeCycleEventType getEventType() {

        return eventType;
    }
}