package com.scott.computercontrollerclient.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.scott.computercontrollerclient.R;
import com.scott.computercontrollerclient.app.EventContacs;
import com.scott.computercontrollerclient.event.EventManager;
import com.scott.computercontrollerclient.event.IEvent;
import com.scott.computercontrollerclient.event.IEventCustomer;
import com.scott.computercontrollerclient.event.IEventPerformer;
import com.shilec.plugin.api.moudle.DataPackge;

import butterknife.BindView;

@IEventCustomer
public class ControlMouseActivity extends BaseActivity {

    @BindView(R.id.tv_content)
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_mouse);
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
        tv.setText(tv.getText().toString() + "\r\n" + dataPackge.data);
    }

}
