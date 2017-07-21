package com.scott.computercontrollerclient.event;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;


import com.scott.computercontrollerclient.utils.Logger;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by shilec on 2017/7/3 0003.
 */

public class EventManager implements Handler.Callback, Runnable ,IEventManager{

    private final static int MAX_EVENT_QUEUE_SIZE = 2000;
    private static EventManager sEventManager;
    private List<Object> mEventCustomers;
    private ArrayBlockingQueue<IEvent> mEvents;
    private Thread mEventLooper;
    private Thread mEventDispatcher;
    private Handler mWorkHanlder;
    private Handler mMainHandler;
    private IEventParser mParser;
    private static final String TAG = "EventManager";

    static {
        Logger.i(ClassLoader.class.getSimpleName(),"EventManager class load !");
    }

    private synchronized static void _init() {
        if(sEventManager == null) {
            sEventManager = new EventManager();
        }
    }

    public synchronized static IEventManager getSingleton() {
        if(sEventManager == null) {
            _init();
        }
        return sEventManager;
    }


    @Override
    public void sendEvent(IEvent event) {
        synchronized (EventManager.class) {
            mEvents.add(event);
            synchronized (sEventManager.mEventDispatcher) {
                if(!mEventCustomers.isEmpty()) {
                    sEventManager.mEventDispatcher.notify();
                }
            }
            //Logger.e(TAG,"SEND EVENT ==== " + event.getEvent());
        }
    }


    private EventManager() {
        mEvents = new ArrayBlockingQueue<IEvent>(MAX_EVENT_QUEUE_SIZE);
        mEventCustomers = new ArrayList<>();
        Looper mainLooper = Looper.getMainLooper();
        mMainHandler = new Handler(mainLooper, this);
        mEventLooper = new Thread(this);
        mEventLooper.start();
        mEventDispatcher = new Thread(new EventDispatcher());
        mEventDispatcher.start();
        mParser = new EventParser();
        Logger.e(TAG,"EventManager create === > ");
    }

    @Override
    public void register(Object customer) {
        synchronized (sEventManager) {
            Class cls = customer.getClass();
            Annotation annotation = cls.getAnnotation(IEventCustomer.class);
            if(annotation == null) {
                Logger.e(TAG,"not a Customer");
                return;
            }
            if (mEventCustomers.contains(customer)) {
                Logger.e(TAG,"contain a Customer");
                return;
            }
            mEventCustomers.add(customer);
        }
    }

    @Override
    public void unRegister(Object customer) {
        synchronized (sEventManager) {
            mEventCustomers.remove(customer);
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        IEvent event = (IEvent) msg.obj;
        onMainEventComming(event);
        return false;
    }

    private void onMainEventComming(IEvent event) {
        for(Object customer : mEventCustomers) {
            mParser.callEventPerformer(event,customer,TargetThread.MAIN_THREAD);
        }
    }

    @Override
    public void run() {
        Looper.prepare();
        mWorkHanlder = new Handler(new WorkHanlder());
        Looper.loop();
    }

    class WorkHanlder implements Handler.Callback {

        @Override
        public boolean handleMessage(Message msg) {
            Logger.e(TAG,"handle msg === >" + msg);
            IEvent event = (IEvent) msg.obj;
            onWrokEventComming(event);
            return false;
        }

        private void onWrokEventComming(IEvent event) {
            for(Object customer : mEventCustomers) {
                mParser.callEventPerformer(event,customer,TargetThread.WORK_THREAD);
            }
        }
    }

    class EventDispatcher implements Runnable {

        @Override
        public void run() {
            while (true) {
                dispatchEvent();
            }
        }

        private void dispatchEvent() {
            synchronized (mEventDispatcher) {
                try {
                    if (mEvents.isEmpty() || mEventCustomers.isEmpty()) {
                        mEventDispatcher.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                IEvent event = mEvents.poll();
                Message msg = Message.obtain();
                msg.obj = event;
                if(mWorkHanlder != null)
                mWorkHanlder.sendMessage(msg);
                msg = Message.obtain();
                msg.obj = event;
                if(mMainHandler != null)
                mMainHandler.sendMessage(msg);
            }
        }
    }
}

