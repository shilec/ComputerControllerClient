package com.scott.computercontrollerclient.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.scott.computercontrollerclient.moudle.DeviceInfo;
import com.shilec.plugin.api.communication.base.ICommunication;
import com.shilec.plugin.api.communication.base.ICommunicationCallback;
import com.shilec.plugin.api.communication.base.ICommunicationProxy;
import com.shilec.plugin.api.communication.base.IMessageCallback;
import com.shilec.plugin.api.communication.impl.BaseCommunicationPoxy;
import com.shilec.plugin.api.communication.impl.CommunicationManager;
import com.shilec.plugin.api.moudle.DataPackge;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/20 0020.
 */

public class CommunicationSerivce extends Service implements ICommunicationCallback,
        IMessageCallback, DeviceScanner.IDeviceScanCallback, Handler.Callback {

    private CommunicationManager cManager;
    private ICommunicationProxy mClientProxy;
    public static final String EXTRA_CMD = "CMD";
    public static final String CMD_START_COMMUNICATION = "START_COMMUNICATION";
    public static final String CMD_SCAN_DEVICE_IP_ADDR = "SCAN_DEVICE";
    private DeviceScanner mIPScanner;
    private List<DeviceInfo> devices;
    private final int PORT = 9008;
    private final String BASE_IP_ADDR = "192.168.199.";
    private Handler handler = new Handler(this);
    private static List<OnFindDevices> observers = new ArrayList<>();



    public interface OnFindDevices {
        void onFind(List<DeviceInfo> devices);
    }

    public static void addDeviceObserver(OnFindDevices l) {
        observers.add(l);
    }

    public static void removeDeviceObserver(OnFindDevices l) {
        observers.remove(l);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        cManager = CommunicationManager.getInstance();
        mClientProxy = cManager.getClientPoxy();
        mClientProxy.addCommunicationCallback(this);
        mClientProxy.addMessageCallback(this);

        devices = new ArrayList<>();
        mIPScanner = new DeviceScanner(true,PORT,BASE_IP_ADDR,this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return super.onStartCommand(intent, flags, startId);
        }
        String cmd = intent.getStringExtra(EXTRA_CMD);
        if (TextUtils.isEmpty(cmd)) {
            return super.onStartCommand(intent, flags, startId);
        }
        switch (cmd) {
            case CMD_SCAN_DEVICE_IP_ADDR:
                scanDevices();
                break;
            case CMD_START_COMMUNICATION:
                startCommunication();
                break;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void scanDevices() {
        mIPScanner.start();
    }

    private void startCommunication() {

    }

    @Override
    public void onConnectionOpened(InetAddress inetAddress) {

    }

    @Override
    public void onTimeOut() {

    }

    @Override
    public void onConnectionClosed(int i) {

    }

    @Override
    public void onMessage(DataPackge dataPackge) {

    }


    @Override
    public void onScanFinished(String ip) {
        System.out.println("ip ======== " + ip);
        if (TextUtils.isEmpty(ip)) return;
        String[] str = ip.split(":");
        DeviceInfo device = new DeviceInfo();
        device.ipAddr = str[1];
        device.name = str[2];

        if (!devices.contains(device)) {
            devices.add(device);
        }
        handler.sendEmptyMessage(0);
    }

    @Override
    public boolean handleMessage(Message msg) {
        for (OnFindDevices l : observers) {
            if (l != null) {
                l.onFind(devices);
            }
        }
        return false;
    }
}
