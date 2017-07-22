package com.scott.computercontrollerclient.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.scott.computercontrollerclient.MainActivity;
import com.scott.computercontrollerclient.R;
import com.scott.computercontrollerclient.adapter.MainMenuAdapter;
import com.scott.computercontrollerclient.moudle.DeviceInfo;
import com.scott.computercontrollerclient.moudle.MenuItem;
import com.scott.computercontrollerclient.service.CommunicationSerivce;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainMenuActivity extends BaseActivity implements AdapterView.OnItemClickListener{

    @BindView(R.id.grid_menu)
    GridView gridMenu;

    private MainMenuAdapter adapter;

    private List<MenuItem> menus;

    private DeviceInfo device;
    public static final String EXTRA_DEVICE_INFO = "device";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Intent intent = getIntent();
        device = (DeviceInfo) intent.getSerializableExtra(EXTRA_DEVICE_INFO);
        if(device == null) {
            finish();
            return;
        }

        Intent service = new Intent(this,CommunicationSerivce.class);
        service.putExtra(CommunicationSerivce.EXTRA_CMD,CommunicationSerivce.CMD_START_COMMUNICATION);
        service.putExtra(CommunicationSerivce.EXTRA_DATA,device);
        startService(service);

        initMenu();
        initViews();
        showLoadingDialog("提示","正在建立连接,请稍后。。。",false);
    }

    private void initViews() {
        adapter = new MainMenuAdapter(menus);
        gridMenu.setAdapter(adapter);
        gridMenu.setOnItemClickListener(this);
    }

    private void initMenu() {

        menus = new ArrayList<>();
        MenuItem menu;
        menu = new MenuItem();
        menu.cls = ControlMouseActivity.class;
        menu.iconId = R.drawable.ic_mouse_grey_500_24dp;
        menu.title = "控制";
        menus.add(menu);

        menu = new MenuItem();
        menu.cls = MainActivity.class;
        menu.iconId = R.drawable.ic_folder_shared_grey_500_24dp;
        menu.title = "文件";
        menus.add(menu);

        menu = new MenuItem();
        menu.cls = MainActivity.class;
        menu.iconId = R.drawable.ic_mouse_grey_500_24dp;
        menu.title = "文件";
        menus.add(menu);

        menu = new MenuItem();
        menu.cls = MainActivity.class;
        menu.iconId = R.drawable.ic_mouse_grey_500_24dp;
        menu.title = "文件";
        menus.add(menu);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MenuItem menu = menus.get(position);
        Intent intent  = new Intent(this,menu.cls);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        showCloseDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDeviceConnection();
    }

    private void closeDeviceConnection() {
        Intent intent = new Intent(this,CommunicationSerivce.class);
        intent.putExtra(CommunicationSerivce.EXTRA_CMD,CommunicationSerivce.CMD_CLOSE_COMMUNICATION);
        startService(intent);
    }

    private void showCloseDialog() {
        showConfigmDialog("是否退出设备?", new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                MainMenuActivity.super.onBackPressed();
            }
        });
    }
}
