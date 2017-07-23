package com.scott.computercontrollerclient.event;


import com.scott.computercontrollerclient.utils.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by shilec on 2017/7/3 0003.
 */

class EventParser implements IEventParser {

    private final String TAG = "EventParser";
    EventParser() {

    }

    @Override
    public void callEventPerformer(IEvent event,Object customer,TargetThread target) {
        Class cls = customer.getClass();
        Method[] ms = cls.getDeclaredMethods();
        for(Method m : ms) {
            IEventPerformer performer = m.getAnnotation(IEventPerformer.class);
            if(performer == null) continue;

            TargetThread target1 = performer.target();
            if(target1 != target) continue;

            int code = performer.type();
            //event.code == performer.code
            //event.code 为子code,perfomer为main code
            //event.code 为main code,perfomer 为子code
            //只要标识main code,则会接受所有main code 的子code
            //code == -1 所有event都接受
            if(event == null) continue;
            if(code != -1 && code != event.getEventType() &&
                    code != (event.getEventType() | 0x0ff)
                    && (code | 0xff) != event.getEventType()) {
                continue;
            }
            m.setAccessible(true);
            try {
                m.invoke(customer,event);
                Logger.e(TAG,"invoke event performer === " + event.getEvent() + ",TARGET = " + target + ",m = " + m.getName());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
