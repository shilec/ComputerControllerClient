package com.scott.computercontrollerclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.scott.computercontrollerclient.R;
import com.scott.computercontrollerclient.adapter.DeviceListAdapter;
import com.scott.computercontrollerclient.moudle.DeviceInfo;
import com.scott.computercontrollerclient.service.CommunicationSerivce;
import com.shilec.plugin.api.communication.impl.CommunicationManager;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;

import butterknife.BindView;

public class DeviceListActivity extends BaseActivity implements CommunicationSerivce.OnFindDevices {

    @BindView(R.id.rv_device)
    RecyclerView rvDevices;

    private DeviceListAdapter mAdapter;
    private List<DeviceInfo> mDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        mDevices = new ArrayList<>();
        mAdapter = new DeviceListAdapter(R.layout.item_device, mDevices);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        rvDevices.setLayoutManager(lm);
        rvDevices.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.menu_refresh).setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onFind(List<DeviceInfo> devices) {
        mDevices.clear();
        mDevices.addAll(devices);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CommunicationSerivce.addDeviceObserver(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.menu_refresh) {
            refreshDeviceList();
        }
        return super.onMenuItemClick(item);
    }

    private void refreshDeviceList() {
        Intent intent = new Intent(this, CommunicationSerivce.class);
        intent.putExtra(CommunicationSerivce.EXTRA_CMD, CommunicationSerivce.CMD_SCAN_DEVICE_IP_ADDR);
        startService(intent);

        Log.i("Device","ip = " + getLocalIpAddress());
    }

    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("IpAddress", ex.toString());
        }
        return null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        CommunicationSerivce.removeDeviceObserver(this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof DeviceListActivity;
    }
}
