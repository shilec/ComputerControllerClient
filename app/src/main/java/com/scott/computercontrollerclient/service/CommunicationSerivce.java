package com.scott.computercontrollerclient.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.scott.computercontrollerclient.activity.BaseActivity;
import com.scott.computercontrollerclient.app.EventContacs;
import com.scott.computercontrollerclient.event.EventManager;
import com.scott.computercontrollerclient.moudle.CommunicationEvent;
import com.scott.computercontrollerclient.moudle.DeviceInfo;
import com.scott.computercontrollerclient.utils.IPUtils;
import com.scott.computercontrollerclient.utils.Logger;
import com.shilec.plugin.api.communication.base.ICommunicationCallback;
import com.shilec.plugin.api.communication.base.ICommunicationProxy;
import com.shilec.plugin.api.communication.base.IMessageCallback;
import com.shilec.plugin.api.communication.impl.CommunicationManager;
import com.shilec.plugin.api.moudle.DataPackge;
import com.shilec.plugin.api.scanner.DeviceScanner;

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
    public static final String CMD_ADD_DIY_DEVICE = "ADD_DEVICE";
    public static final String CMD_CLOSE_COMMUNICATION = "CLOSE_COMMUNICATION";
    public static final String EXTRA_DATA = "DATA";
    private List<DeviceInfo> devices;
    private final int DEVICE_FIND_PORT = 9008;
    private final int TCP_CONNECTION_PORT = 9009;
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
                startCommunication(intent);
                break;
            case CMD_ADD_DIY_DEVICE:
                addDevice(intent);
                break;
            case CMD_CLOSE_COMMUNICATION:
                closeCommunication();
                break;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void closeCommunication() {
        if(mClientProxy != null) {
            mClientProxy.close();
        }
    }

    private void addDevice(Intent intent) {
        if(BaseActivity.getInstacne() != null) {
            BaseActivity.getInstacne().showLoadingDialog("添加设备","正在检查设备是否可用,请稍后。。。");
        }
        DeviceInfo device = (DeviceInfo) intent.getSerializableExtra(EXTRA_DATA);
        DeviceScanner scanner = DeviceScanner.testConnectionVaild(DEVICE_FIND_PORT,device.ipAddr,this);
        scanner.start();
    }

    private void scanDevices() {

        String ipAddr = IPUtils.getWifiIpAddr(this);
        if(ipAddr == null) {
            if(BaseActivity.getInstacne() != null) {
                BaseActivity.getInstacne().showMsgDialog("手机WIFI未开启,请开启WIFI后重试");
            }
            return;
        }

        if(BaseActivity.getInstacne() != null) {
            BaseActivity.getInstacne().showLoadingDialog("扫描设备","正在扫描设备,请稍后。。。");
        }
        //ipAddr = ipAddr.substring(0,ipAddr.lastIndexOf("."));
        String gateWay = ipAddr.substring(0,ipAddr.lastIndexOf(".") + 1);
        Log.i(CommunicationSerivce.class.getSimpleName(),"gateWay = " + ipAddr + " = " + gateWay);
        DeviceScanner dscanner = DeviceScanner.getScanner(DEVICE_FIND_PORT,gateWay,this);
        dscanner.start();
        //String subNetMask = IPUtils.getSubnetMask(IPUtils.getWifiIpAddrInt(this));
        //Log.i(CommunicationSerivce.class.getSimpleName(),">>>>" + subNetMask);
    }


    private void startCommunication(Intent intent) {
        DeviceInfo info = (DeviceInfo) intent.getSerializableExtra(EXTRA_DATA);
        mClientProxy.startAsClient(info.ipAddr, TCP_CONNECTION_PORT);
    }

    @Override
    public void onConnectionOpened(InetAddress inetAddress) {

    }

    @Override
    public void onTimeOut() {

    }

    @Override
    public void onConnectionClosed(int i) {
        Logger.i(CommunicationSerivce.class.getSimpleName(),"server closed !");
    }

    @Override
    public void onMessage(DataPackge dataPackge) {
        Logger.i(CommunicationSerivce.class.getSimpleName(),"msg = > " + dataPackge.data);
        CommunicationEvent event = new CommunicationEvent();
        event.dataPackge = dataPackge;
        event.eventType = EventContacs.CMD_COMMUNICATION_ALL;
        EventManager.getSingleton().sendEvent(event);
    }


    @Override
    public void onScanFinished(String ip) {
        if(BaseActivity.getInstacne() != null) {
            BaseActivity.getInstacne().dismissLoadingDialog();
        }
        System.out.println("ip ======== " + ip);
        if (TextUtils.isEmpty(ip)) return;
        String[] str = ip.split(":");
        DeviceInfo device = new DeviceInfo();
        device.ipAddr = str[1];
        device.name = str[0];

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
