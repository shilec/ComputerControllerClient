package com.scott.computercontrollerclient.event;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017/7/4.</p>
 * <p>Email:     shijl5@lenovo.com</p>
 * <p>Describe:</p>
 */

public interface IEventParser {
    void callEventPerformer(IEvent event, Object customer, TargetThread target);
}
