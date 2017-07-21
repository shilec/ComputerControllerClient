package com.scott.computercontrollerclient.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.scott.computercontrollerclient.R;
import com.scott.computercontrollerclient.moudle.DeviceInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/7/20 0020.
 */

public class DeviceListAdapter extends BaseQuickAdapter<DeviceInfo>{

    public static interface OnItemClickListenner<T> {
        void onItemClick(T t);
    }

    private OnItemClickListenner l;

    public void setOnItemClickListenner(OnItemClickListenner l) {
        this.l = l;
    }
    public DeviceListAdapter(int layoutResId, List<DeviceInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final DeviceInfo deviceInfo) {
        baseViewHolder.setText(R.id.tv_ip,deviceInfo.ipAddr);
        baseViewHolder.setText(R.id.tv_name,deviceInfo.name);
        baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l.onItemClick(deviceInfo);
            }
        });
    }
}
