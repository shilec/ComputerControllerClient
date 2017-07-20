package com.scott.computercontrollerclient.moudle;

import android.text.TextUtils;

/**
 * Created by Administrator on 2017/7/20 0020.
 */

public class DeviceInfo {
    public String name;
    public String ipAddr;

    @Override
    public boolean equals(Object obj) {
        DeviceInfo info = (DeviceInfo) obj;
        return TextUtils.equals(ipAddr,info.ipAddr);
    }
}
