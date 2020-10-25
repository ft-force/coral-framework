package com.ftf.coral.core.event;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventRegion {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventBus.class);

    String eventRegion;

    ConcurrentHashMap<Object, ConcurrentHashMap<Object, EventListener[]>> customEventListeners =
        new ConcurrentHashMap<>();
    ConcurrentHashMap<Object, EventListener[]> defaultEventListeners = new ConcurrentHashMap<>();

    public EventRegion(String er) {
        eventRegion = er;
    }

    public String getEventRegion() {
        return eventRegion;
    }

    public void addEventListener(Class eventClass, Object[] eventTypes, EventListener listener) {
        synchronized (eventClass) {
            if (eventTypes == null || eventTypes.length == 0) {
                addEventListener(eventClass, defaultEventListeners, listener);
            } else {

                ConcurrentHashMap<Object, EventListener[]> eventListeners = customEventListeners.get(eventClass);
                if (eventListeners == null) {
                    eventListeners = new ConcurrentHashMap<>();
                    customEventListeners.put(eventClass, eventListeners);
                }

                for (Object eventType : eventTypes) {

                    addEventListener(eventType, eventListeners, listener);
                }
            }
        }
    }

    private void addEventListener(Object key, ConcurrentHashMap<Object, EventListener[]> eventListenersMap,
        EventListener listener) {

        EventListener[] listenersArray = eventListenersMap.get(key);
        if (listenersArray == null) {
            listenersArray = new EventListener[] {listener};
            eventListenersMap.put(key, listenersArray);
        } else {
            for (EventListener l : listenersArray) {
                if (l == listener) {
                    return;
                }
            }

            EventListener[] newListenersArray = new EventListener[listenersArray.length + 1];
            System.arraycopy(listenersArray, 0, newListenersArray, 0, listenersArray.length);
            newListenersArray[newListenersArray.length - 1] = listener;
            eventListenersMap.put(key, newListenersArray);
        }
    }

    private void removeEventListener(Object key, ConcurrentHashMap<Object, EventListener[]> eventListenersMap,
        EventListener listener) {
        EventListener[] listenersArray = eventListenersMap.get(key);
        if (listenersArray == null) {
            return;
        } else {

            ArrayList<EventListener> newListenersArray = new ArrayList<>();
            for (EventListener l : listenersArray) {
                if (l != listener) {
                    newListenersArray.add(l);
                }
            }

            eventListenersMap.put(key, newListenersArray.toArray(new EventListener[newListenersArray.size()]));
        }
    }

    private void replaceEventListener(Object key, ConcurrentHashMap<Object, EventListener[]> eventListenersMap,
        EventListener oldListener, EventListener newListener) {
        EventListener[] listenersArray = eventListenersMap.get(key);
        if (listenersArray == null) {

            ArrayList<EventListener> newListenersArray = new ArrayList<>();
            newListenersArray.add(newListener);
            eventListenersMap.put(key, newListenersArray.toArray(new EventListener[newListenersArray.size()]));
        } else {

            ArrayList<EventListener> newListenersArray = new ArrayList<>();
            for (EventListener l : listenersArray) {
                if (l != oldListener) {
                    newListenersArray.add(l);
                }
            }
            newListenersArray.add(newListener);
            eventListenersMap.put(key, newListenersArray.toArray(new EventListener[newListenersArray.size()]));
        }
    }

    public void removeEventListener(Class eventClass, Object[] eventTypes, EventListener listener) {
        synchronized (eventClass) {
            if (eventTypes == null || eventTypes.length == 0) {
                removeEventListener(eventClass, defaultEventListeners, listener);
            } else {

                ConcurrentHashMap<Object, EventListener[]> eventListenersMap = customEventListeners.get(eventClass);
                if (eventListenersMap == null) {
                    return;
                }

                for (Object eventType : eventTypes) {

                    removeEventListener(eventType, eventListenersMap, listener);
                }
            }
        }
    }

    public void replaceEventListener(Class eventClass, Object[] eventTypes, EventListener oldListener,
        EventListener newListener) {
        synchronized (eventClass) {
            if (eventTypes == null || eventTypes.length == 0) {
                replaceEventListener(eventClass, defaultEventListeners, oldListener, newListener);
            } else {

                ConcurrentHashMap<Object, EventListener[]> eventListenersMap = customEventListeners.get(eventClass);
                if (eventListenersMap == null) {
                    return;
                }

                for (Object eventType : eventTypes) {

                    replaceEventListener(eventType, eventListenersMap, oldListener, newListener);
                }
            }
        }
    }

    public void fireEvent(Event event) {

        ConcurrentHashMap<Object, EventListener[]> customEventListenersMap = customEventListeners.get(event.getClass());
        if (customEventListenersMap != null) {
            EventListener[] listeners = customEventListenersMap.get(event.getEventType());
            fireEvent(event, listeners);
        }

        fireEvent(event, defaultEventListeners.get(event.getClass()));
    }

    private void fireEvent(Event event, EventListener[] listeners) {

        if (listeners != null) {
            for (EventListener l : listeners) {
                if (l != null) {
                    try {
                        if (event.canFireEvent(l)) {

                            l.onEvent(event);

                        }
                    } catch (Throwable e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                }
            }
        }
    }
}