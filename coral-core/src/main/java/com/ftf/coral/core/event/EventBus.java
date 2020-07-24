package com.ftf.coral.core.event;

import java.util.concurrent.ConcurrentHashMap;

public class EventBus {

    ConcurrentHashMap<String, EventRegion> eventRegionsMap = new ConcurrentHashMap<>();

    public synchronized void addEventListener(String[] eventRegions, Class eventClass, Object[] eventTypes,
                    EventListener listener) {
        if (eventRegions == null || eventRegions.length == 0) {
            addEventListener("DefaultEventRegion", eventClass, eventTypes, listener);
        } else {
            for (String eventRegion : eventRegions) {
                addEventListener(eventRegion, eventClass, eventTypes, listener);
            }
        }

        if (EventListenerLifeCycleEvent.class.equals(eventClass) == false) {
            EventListenerLifeCycleEvent event = new EventListenerLifeCycleEvent();
            event.setEventClass(eventClass);
            event.setEventRegions(eventRegions);
            event.setEventTypes(eventTypes);
            event.setListener(listener);
            event.setEventType(EventListenerLifeCycleEventType.AddEventListener);
            this.fireEvent(eventRegions, event);
        }
    }

    private void addEventListener(String eventRegion, Class eventClass, Object[] eventTypes, EventListener listener) {
        EventRegion er = eventRegionsMap.get(eventRegion);
        if (er == null) {
            er = new EventRegion(eventRegion);
            eventRegionsMap.put(eventRegion, er);
        }
        er.addEventListener(eventClass, eventTypes, listener);
    }

    public synchronized void replaceEventListener(String[] eventRegions, Class eventClass, Object[] eventTypes,
                    EventListener oldListener, EventListener newListener) {
        if (eventRegions == null || eventRegions.length == 0) {
            replaceEventListener("DefaultEventRegion", eventClass, eventTypes, oldListener, newListener);
        } else {
            for (String eventRegion : eventRegions) {
                replaceEventListener(eventRegion, eventClass, eventTypes, oldListener, newListener);
            }
        }

        if (EventListenerLifeCycleEvent.class.equals(eventClass) == false) {
            {
                EventListenerLifeCycleEvent event = new EventListenerLifeCycleEvent();
                event.setEventClass(eventClass);
                event.setEventRegions(eventRegions);
                event.setEventTypes(eventTypes);
                event.setListener(oldListener);
                event.setEventType(EventListenerLifeCycleEventType.RemoveEventListener);
                this.fireEvent(eventRegions, event);
            }

            {
                EventListenerLifeCycleEvent event = new EventListenerLifeCycleEvent();
                event.setEventClass(eventClass);
                event.setEventRegions(eventRegions);
                event.setEventTypes(eventTypes);
                event.setListener(newListener);
                event.setEventType(EventListenerLifeCycleEventType.AddEventListener);
                this.fireEvent(eventRegions, event);
            }
        }
    }

    public synchronized void removeEventListener(String[] eventRegions, Class eventClass, Object[] eventTypes,
                    EventListener listener) {
        if (eventRegions == null || eventRegions.length == 0) {
            removeEventListener("DefaultEventRegion", eventClass, eventTypes, listener);
        } else {
            for (String eventRegion : eventRegions) {
                removeEventListener(eventRegion, eventClass, eventTypes, listener);
            }
        }

        if (EventListenerLifeCycleEvent.class.equals(eventClass) == false) {
            EventListenerLifeCycleEvent event = new EventListenerLifeCycleEvent();
            event.setEventClass(eventClass);
            event.setEventRegions(eventRegions);
            event.setEventTypes(eventTypes);
            event.setListener(listener);
            event.setEventType(EventListenerLifeCycleEventType.RemoveEventListener);
            this.fireEvent(eventRegions, event);
        }
    }

    private void removeEventListener(String eventRegion, Class eventClass, Object[] eventTypes,
                    EventListener listener) {
        EventRegion er = eventRegionsMap.get(eventRegion);
        if (er == null) {
            return;
        }
        er.removeEventListener(eventClass, eventTypes, listener);
    }

    private void replaceEventListener(String eventRegion, Class eventClass, Object[] eventTypes,
                    EventListener oldListener, EventListener newListener) {
        EventRegion er = eventRegionsMap.get(eventRegion);
        if (er == null) {
            return;
        }
        er.replaceEventListener(eventClass, eventTypes, oldListener, newListener);
    }

    public void fireEvent(String[] eventRegions, Event event) {
        event.clearFireHistory();
        fireEvent("DefaultEventRegion", event);

        if (eventRegions != null && eventRegions.length > 0) {
            for (String eventRegion : eventRegions) {
                fireEvent(eventRegion, event);
            }
        } else {
            for (EventRegion er : eventRegionsMap.values()) {
                fireEvent(er.getEventRegion(), event);
            }
        }
    }

    private void fireEvent(String eventRegion, Event event) {
        EventRegion er = eventRegionsMap.get(eventRegion);
        if (er != null) {
            er.fireEvent(event);
        }
    }
}