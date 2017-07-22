package com.scott.computercontrollerclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.scott.computercontrollerclient.R;
import com.scott.computercontrollerclient.ScreenImageView;
import com.scott.computercontrollerclient.app.EventContacs;
import com.scott.computercontrollerclient.event.EventManager;
import com.scott.computercontrollerclient.event.IEvent;
import com.scott.computercontrollerclient.event.IEventCustomer;
import com.scott.computercontrollerclient.event.IEventPerformer;
import com.scott.computercontrollerclient.service.BehaviorRequester;
import com.scott.computercontrollerclient.service.CommunicationSerivce;
import com.scott.computercontrollerclient.utils.Logger;
import com.shilec.plugin.api.common.Contacts;
import com.shilec.plugin.api.common.DataPackge;
import com.shilec.plugin.api.common.ICommadReuqester;

import butterknife.BindView;
import butterknife.OnClick;

@IEventCustomer
public class ControlMouseActivity extends BaseActivity implements ScreenImageView.OnMouseMoveListenner{

    @BindView(R.id.tv_content)
    TextView tv;

    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.btn_getpcinfo)
    Button btnGetPcInfo;
    @BindView(R.id.btn_mouseclick)
    Button btnMouseClick;

    @BindView(R.id.edit_content)
    EditText editContent;

    @BindView(R.id.iv_img)
    public ScreenImageView ivImg;

    private ICommadReuqester<ControlMouseActivity> mReuqester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_mouse);
        mReuqester = new BehaviorRequester();
        ivImg.setOnMouseListenner(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventManager.getSingleton().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventManager.getSingleton().unRegister(this);
    }

    @IEventPerformer(type = EventContacs.CMD_COMMUNICATION_ALL)
    public void onEvent(IEvent<DataPackge> event) {
        DataPackge dataPackge = event.getEvent();
        mReuqester.onRequestCommand(dataPackge, this);
        tv.setText(tv.getText().toString() + "\r\n" + dataPackge.data);
    }

    @OnClick(R.id.btn_send)
    public void OnSend(View v) {
        String msg = editContent.getText().toString();
        DataPackge dataPackge = new DataPackge();
        dataPackge.data = msg;
        CommunicationSerivce.excuteCmd(this,dataPackge);
        Logger.i(ControlMouseActivity.class.getSimpleName(), ">>>>>>fas ong>>>>>>>>>" + msg);
    }

    @OnClick(R.id.btn_getpcinfo)
    public void onGetPcInfo(View v) {
        DataPackge dataPackge = new DataPackge();
        dataPackge.code = Contacts.Command.CMD_PC_INFO;
        CommunicationSerivce.excuteCmd(this,dataPackge);
    }

    @OnClick(R.id.btn_mouseclick)
    public void onMouseClick(View v) {
        DataPackge dataPackge = new DataPackge();
        dataPackge.code = Contacts.Command.CMD_MOUSE_CLICK;
        CommunicationSerivce.excuteCmd(this,dataPackge);
    }


    @Override
    public void onMove(float x, float y) {
        DataPackge data = new DataPackge();
        data.code = Contacts.Command.CMD_MOUSE_MOVE;
        data.data = "{\"x\":" + (1080 - x) + ",\"y\":" + y + "}";
        CommunicationSerivce.excuteCmd(this,data);
        Logger.i(ControlMouseActivity.class.getSimpleName(), "screen img onMove");
    }
}
