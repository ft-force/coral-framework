package com.ftf.coral.util;

public class ObjectHolder<T> {

    private T value;

    public void set(T value) {
        this.value = value;
    }

    public T value() {
        return this.value;
    }
}
