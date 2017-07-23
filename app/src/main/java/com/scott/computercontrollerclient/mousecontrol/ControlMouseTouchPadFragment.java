package com.scott.computercontrollerclient.mousecontrol;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.scott.computercontrollerclient.R;
import com.scott.computercontrollerclient.base.BaseFragment;
import com.scott.computercontrollerclient.service.CommunicationSerivce;
import com.scott.computercontrollerclient.utils.Logger;
import com.shilec.plugin.api.common.Contacts;
import com.shilec.plugin.api.common.DataPackge;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/7/23 0023.
 */

public class ControlMouseTouchPadFragment extends BaseFragment implements View.OnTouchListener {

    @BindView(R.id.tv_touch)
    TextView tvTouch;
    @BindView(R.id.btn_left)
    Button btnLeft;
    @BindView(R.id.btn_right)
    Button btnRight;

    private float downX;
    private float downY;
    @Override
    protected void initViews(View contentView) {
        tvTouch.setOnTouchListener(this);
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_control_touch;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Logger.i(ControlMouseTouchPadFragment.class.getSimpleName(),"onTouch = " + event.getAction());
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX() - downX;
                float y = event.getY() - downY;
                onTouchMove(x,y);
                downX = event.getX();
                downY = event.getY();
                break;
        }
        return true;
    }

    private void onTouchMove(float x,float y) {
        Logger.i(ControlMouseTouchPadFragment.class.getSimpleName(),"onTouch");
        DataPackge data = new DataPackge();
        data.code = Contacts.Command.CMD_MOUSE_MOVE_NOSCREENSHOT;
        data.data = "{\"x\":" + x + ",\"y\":" + y + "}";
        CommunicationSerivce.excuteCmd(getActivity(),data);
    }

    @OnClick(R.id.btn_left)
    public void onLeftMouseBtnClick(View v) {
        DataPackge dataPackge = new DataPackge(Contacts.Command.CMD_MOUSE_LEFT_CLICK_NOSCREENSHOT);
        CommunicationSerivce.excuteCmd(getActivity(),dataPackge);
    }

    @OnClick(R.id.btn_right)
    public void onRightMouseBtnClick(View v) {
        DataPackge dataPackge = new DataPackge(Contacts.Command.CMD_MOUSE_RIGHT_CLICK_NOSCREENSHOT);
        CommunicationSerivce.excuteCmd(getActivity(),dataPackge);
    }
}
