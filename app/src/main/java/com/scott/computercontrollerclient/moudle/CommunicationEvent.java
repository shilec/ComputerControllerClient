package com.scott.computercontrollerclient.moudle;

import com.scott.computercontrollerclient.app.EventContacs;
import com.scott.computercontrollerclient.event.IEvent;
import com.shilec.plugin.api.moudle.DataPackge;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017/7/21.</p>
 * <p>Email:     shijl5@lenovo.com</p>
 * <p>Describe:</p>
 */

public class CommunicationEvent implements IEvent<DataPackge> {

    public int eventType = EventContacs.CMD_COMMUNICATION_ALL;
    public DataPackge dataPackge;

    @Override
    public DataPackge getEvent() {
        return dataPackge;
    }

    @Override
    public int getEventType() {
        return eventType;
    }
}
