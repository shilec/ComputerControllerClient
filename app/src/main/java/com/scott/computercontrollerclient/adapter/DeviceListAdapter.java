package com.scott.computercontrollerclient.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scott.computercontrollerclient.R;
import com.scott.computercontrollerclient.moudle.DeviceInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/7/20 0020.
 */

public class DeviceListAdapter extends BaseQuickAdapter<DeviceInfo>{

    public DeviceListAdapter(int layoutResId, List<DeviceInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, DeviceInfo deviceInfo) {
        baseViewHolder.setText(R.id.tv_ip,deviceInfo.ipAddr);
        baseViewHolder.setText(R.id.tv_name,deviceInfo.name);
    }
}
