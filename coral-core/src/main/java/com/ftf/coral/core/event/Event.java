package com.ftf.coral.core.event;

import java.util.HashSet;

public abstract class Event<T> {

    private HashSet<EventListener<Event<T>>> fireHistory = new HashSet<>();

    public abstract T getEventType();

    protected void clearFireHistory() {
        fireHistory.clear();
    }

    protected boolean canFireEvent(EventListener<Event<T>> l) {

        if (fireHistory.contains(l)) {
            return false;
        }

        fireHistory.add(l);

        return true;
    }
}