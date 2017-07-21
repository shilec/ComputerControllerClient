package com.scott.computercontrollerclient.event;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017/7/4.</p>
 * <p>Email:     shijl5@lenovo.com</p>
 * <p>Describe:</p>
 */

public interface IEventManager {
    void sendEvent(IEvent event);
    void register(Object customer);
    void unRegister(Object customer);
}
