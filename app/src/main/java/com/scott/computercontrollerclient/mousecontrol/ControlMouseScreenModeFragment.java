package com.scott.computercontrollerclient.mousecontrol;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.scott.computercontrollerclient.R;
import com.scott.computercontrollerclient.ScreenImageView;
import com.scott.computercontrollerclient.app.EventContacs;
import com.scott.computercontrollerclient.base.BaseFragment;
import com.scott.computercontrollerclient.event.EventManager;
import com.scott.computercontrollerclient.event.IEvent;
import com.scott.computercontrollerclient.event.IEventCustomer;
import com.scott.computercontrollerclient.event.IEventPerformer;
import com.scott.computercontrollerclient.service.CommunicationSerivce;
import com.scott.computercontrollerclient.utils.BitmapUtils;
import com.scott.computercontrollerclient.utils.Logger;
import com.shilec.plugin.api.common.Contacts;
import com.shilec.plugin.api.common.DataPackge;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/7/23 0023.
 */

@IEventCustomer
public class ControlMouseScreenModeFragment extends BaseFragment implements ScreenImageView.OnMouseMoveListenner{

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
    public boolean isShow = true;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    protected void initViews(View contentView) {
        ivImg.setOnMouseListenner(this);
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_contrlmouse_screen;
    }


    @Override
    public void onResume() {
        super.onResume();
        EventManager.getSingleton().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventManager.getSingleton().unRegister(this);
    }

    @IEventPerformer(type = EventContacs.CMD_COMMUNICATION_ALL)
    public void onEvent(IEvent<DataPackge> event) {
        if(isShow) return;
        DataPackge dataPackge = event.getEvent();
        final Bitmap bitmap = BitmapFactory.decodeByteArray(dataPackge.fileDatas,
                0, dataPackge.fileDatas.length, null);
        final Bitmap bitmap1 = BitmapUtils.rotateBitmap(bitmap, 90);
        ivImg.setImageBitmap(bitmap1);
        try {
            JSONObject jObject = new JSONObject(dataPackge.data);
            jObject = jObject.getJSONObject("mouseInfo");
            //Logger.i("shilec", "mouseInfo:" + jObject);
            int x = jObject.optInt("x");
            int y = jObject.optInt("y");
            ivImg.setMousePoint(1080 - y, x);
            Logger.i("shilec", "x = " + x + ",y = " + y);
            Logger.i("shilec", "Ix = " + ivImg.getWidth() + ",Iy = " + ivImg.getHeight());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tv.setText(tv.getText().toString() + "\r\n" + dataPackge.data);
    }

    @OnClick(R.id.btn_send)
    public void OnSend(View v) {
        String msg = editContent.getText().toString();
        DataPackge dataPackge = new DataPackge();
        dataPackge.data = msg;
        CommunicationSerivce.excuteCmd(getActivity(),dataPackge);
        Logger.i(ControlMouseActivity.class.getSimpleName(), ">>>>>>fas ong>>>>>>>>>" + msg);
    }

    @OnClick(R.id.btn_getpcinfo)
    public void onGetPcInfo(View v) {
        DataPackge dataPackge = new DataPackge();
        dataPackge.code = Contacts.Command.CMD_PC_INFO;
        CommunicationSerivce.excuteCmd(getActivity(),dataPackge);
    }

    @OnClick(R.id.btn_mouseclick)
    public void onMouseClick(View v) {
        DataPackge dataPackge = new DataPackge();
        dataPackge.code = Contacts.Command.CMD_MOUSE_LEFT_CLICK;
        CommunicationSerivce.excuteCmd(getActivity(),dataPackge);
    }


    @Override
    public void onMove(float x, float y) {
        DataPackge data = new DataPackge();
        data.code = Contacts.Command.CMD_MOUSE_MOVE;
        data.data = "{\"x\":" + (1080 - x) + ",\"y\":" + y + "}";
        CommunicationSerivce.excuteCmd(getActivity(),data);
        Logger.i(ControlMouseActivity.class.getSimpleName(), "screen img onMove");
    }
}
