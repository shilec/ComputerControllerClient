package com.scott.computercontrollerclient.event;

/**
 * Created by shilec on 2017/7/3 0003.
 */

public interface IEvent<T> {
    T getEvent();
    int getEventType();
}
