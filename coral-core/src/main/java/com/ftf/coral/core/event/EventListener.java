package com.ftf.coral.core.event;

public interface EventListener<T extends Event<?>> {

    void onEvent(T event);
}