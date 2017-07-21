package com.scott.computercontrollerclient.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017/7/21.</p>
 * <p>Email:     shijl5@lenovo.com</p>
 * <p>Describe:</p>
 */

public class IPUtils {

    public static int getWifiIpAddrInt(Context context) {
        //获取wifi服务
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if(!wifiManager.isWifiEnabled()) {
            return -1;
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        return ipAddress;
    }

    public static String getWifiIpAddr(Context context) {
        int nIp = getWifiIpAddrInt(context);
        if(nIp == -1) {
            return null;
        }
        String ip = intToIp(nIp);
        return ip;
    }

    private static String intToIp(int i) {

        return (i & 0xFF ) + "." +
                ((i >> 8 ) & 0xFF) + "." +
                ((i >> 16 ) & 0xFF) + "." +
                ( i >> 24 & 0xFF) ;
    }

    public static byte[] int2byte(int i) {
        byte[] b = new byte[4];
        b[0] = (byte) ((i >> 24) & 0xff);
        b[1] = (byte) ((i >> 16) & 0xff);
        b[2] = (byte) ((i >> 8) & 0xff);
        b[3] = (byte) (i & 0xff);
        return b;
    }


    public static String getSubnetMask(int ip) {
        NetworkInterface ni = null;
        try {
            ni = NetworkInterface.getByInetAddress(InetAddress.getByAddress(int2byte(ip)));// 搜索绑定了指定IP地址的网络接口
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<InterfaceAddress> list = ni.getInterfaceAddresses();// 获取此网络接口的全部或部分
        // InterfaceAddresses
        // 所组成的列表
        StringBuilder maskStr = new StringBuilder();
        if (list.size() > 0) {
            int mask = list.get(0).getNetworkPrefixLength(); // 子网掩码的二进制1的个数
            int[] maskIp = new int[4];
            for (int i = 0; i < maskIp.length; i++) {
                maskIp[i] = (mask >= 8) ? 255 : (mask > 0 ? (mask & 0xff) : 0);
                mask -= 8;
                maskStr.append(maskIp[i]);
                if (i < maskIp.length - 1) {
                    maskStr.append(".");
                }
            }
        }
        return maskStr.toString();
    }
}
