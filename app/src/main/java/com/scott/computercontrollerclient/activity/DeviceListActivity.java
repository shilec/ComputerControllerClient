package com.scott.computercontrollerclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
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
public class DeviceListActivity extends BaseActivity
        implements CommunicationSerivce.OnFindDevices,DeviceListAdapter.OnItemClickListenner<DeviceInfo> {

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
        mAdapter.setOnItemClickListenner(this);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        rvDevices.setLayoutManager(lm);
        rvDevices.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.menu_search).setVisible(true);
        menu.findItem(R.id.menu_add).setVisible(true);
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
        if (item.getItemId() == R.id.menu_search) {
            refreshDeviceList();
        } else if(item.getItemId() == R.id.menu_add) {
            addDevice();
        }
        return super.onMenuItemClick(item);
    }

    private void addDevice() {

        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("添加设备")
                .customView(R.layout.dialog_input_content, true)
                .positiveText("ADD")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        EditText editAddr = (EditText) dialog.findViewById(R.id.edit_addr);
                        EditText editName = (EditText) dialog.findViewById(R.id.edit_name);
                        addDevice(editName.getText().toString(),editAddr.getText().toString());
                    }
                })
                .negativeText("CANCEL")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .canceledOnTouchOutside(false)
                .show();
    }

    private void addDevice(String name,String ip) {
        Toast.makeText(this,"name = " + name + ",ip = " + ip,Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this,CommunicationSerivce.class);
        DeviceInfo info = new DeviceInfo();
        info.ipAddr = ip;
        info.name = name;
        intent.putExtra(CommunicationSerivce.EXTRA_CMD,CommunicationSerivce.CMD_ADD_DIY_DEVICE);
        intent.putExtra(CommunicationSerivce.EXTRA_DATA,info);
        startService(intent);
    }

    private void refreshDeviceList() {
        Intent intent = new Intent(this, CommunicationSerivce.class);
        intent.putExtra(CommunicationSerivce.EXTRA_CMD, CommunicationSerivce.CMD_SCAN_DEVICE_IP_ADDR);
        startService(intent);

        //Log.i("Device","ip = " + getLocalIpAddress());
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

    @Override
    public void onItemClick(DeviceInfo deviceInfo) {
//        Toast.makeText(this,deviceInfo.toString(),Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(this,CommunicationSerivce.class);
//        intent.putExtra(CommunicationSerivce.EXTRA_CMD,CommunicationSerivce.CMD_START_COMMUNICATION);
//        intent.putExtra(CommunicationSerivce.EXTRA_DATA,deviceInfo);
//
//        startService(intent);
        Intent intent = new Intent(this,MainMenuActivity.class);
        intent.putExtra(MainMenuActivity.EXTRA_DEVICE_INFO,deviceInfo);
        startActivity(intent);
    }
}
