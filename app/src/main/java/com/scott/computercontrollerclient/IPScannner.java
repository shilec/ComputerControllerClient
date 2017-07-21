package com.scott.computercontrollerclient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;


public class IPScannner implements Runnable {

    private int port;
    private Thread mReadThread;
    private boolean isFind = false;
    private int mRetryTimes = 1;
    private String mBaseIpAddr;
    public static final String FLAG_GET_IP = "GET_IP";
    public static final String FLAG_IP_INFO = "IP_INFO";
    public static final String FLAG_IP_FINDED = "FIND_IP";
    private boolean isScanner = true;
    private IScannerCallback mScannerCallback;

    public static interface IScannerCallback {
        void onIpFind(String ip);
    }

    private static IPScannner mScannner;

    private static synchronized void _init(int port, int retryTimes, String baseIpAddr,IScannerCallback iScannerCallback) {
        if(mScannner == null) {
            mScannner = new IPScannner(port, retryTimes,baseIpAddr,iScannerCallback);
        }
    }

    public static IPScannner getInstance(int port, int retryTimes, String baseIpAddr,IScannerCallback iScannerCallback) {
        if(mScannner == null) {
            _init(port, retryTimes, baseIpAddr, iScannerCallback);
        }
        return mScannner;
    }

    private IPScannner(int port, int retryTimes, String baseIpAddr,IScannerCallback iScannerCallback) {
        this.port = port;
        mRetryTimes = retryTimes;
        mBaseIpAddr = baseIpAddr;
        mReadThread = new Thread(this);
        mScannerCallback = iScannerCallback;
    }

    public String getLocalIpAddr() {
        try {
            Enumeration interfaces = null;
            interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) interfaces.nextElement();
                Enumeration addresss = ni.getInetAddresses();
                while (addresss.hasMoreElements()) {
                    InetAddress nextElement = (InetAddress) addresss.nextElement();
                    String hostAddress = nextElement.getHostAddress();
                    if (hostAddress.contains(mBaseIpAddr)) {
                        return hostAddress;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void scanIpAddr() {
        isScanner = true;
        if(mReadThread != null) {
            isFind = true;
            mReadThread = null;
            mReadThread = new Thread(this);
        }
        isFind = false;
        mReadThread.start();

        sendGetCmd();
    }

    public void waitScanIp() {
        isScanner = false;
        isFind = false;
        if(mReadThread != null) {
            mReadThread = new Thread(this);
        }
        mReadThread.start();
    }

    public void run() {
        try {
            getCmd();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getCmd() throws IOException {
        byte[] buf = new byte[128];
        DatagramSocket dSocket = new DatagramSocket(port);
        while (!isFind) {
            DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);
            dSocket.receive(datagramPacket);
            String cmd = new String(buf);
            if (!isScanner) {
                if (cmd.contains(FLAG_GET_IP)) {
                    sendCmd(FLAG_IP_INFO + ":" + getLocalIpAddr() + ":" + InetAddress.getLocalHost().getHostName(),datagramPacket.getAddress().getHostAddress());
                    System.out.println("send:" + getLocalIpAddr());
                } else if (cmd.contains(FLAG_IP_FINDED)) {
                    isFind = true;
                }
            } else {
                if (cmd.contains(FLAG_GET_IP)) {
                    continue;
                } else if (cmd.contains(FLAG_IP_INFO)) {
                    mScannerCallback.onIpFind(cmd);
                    sendCmd(FLAG_IP_FINDED, datagramPacket.getAddress().getHostAddress());
                    isFind = true;
                }
            }
            System.out.println("收到:" + cmd);
        }
        dSocket.close();
    }


    private void sendGetCmd() {
        new Thread(new SendCmdRunnable(FLAG_GET_IP)).start();
    }

    private void sendCmd(String cmd,String dest) {
        new Thread(new SendIpRunnable(cmd,dest)).start();
    }

    final class SendCmdRunnable implements Runnable {

        private DatagramSocket dSocket;
        private String cmd;

        public SendCmdRunnable(String cmd) {
            this.cmd = cmd;
        }

        public void run() {
            try {
                looSendCmd();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void looSendCmd() throws Exception {
            if (dSocket == null) {
                dSocket = new DatagramSocket();
            }
            for (int i = 0; i < mRetryTimes; i++) {
                for (int j = 1; j <= 255; j++) {
                    InetSocketAddress address = new InetSocketAddress(mBaseIpAddr + j, port);
                    DatagramPacket datagramPacket = new DatagramPacket(cmd.getBytes(), cmd.getBytes().length, address);
                    dSocket.send(datagramPacket);
                }
            }
            if(!isFind) {
                mScannerCallback.onIpFind(null);
                isFind = true;
            }
            if (dSocket != null) {
                dSocket.close();
            }
        }
    }

    final class SendIpRunnable implements Runnable {

        private DatagramSocket dSocket;
        private String cmd;
        private String dest;

        public SendIpRunnable(String ip,String dest) {
            this.cmd = ip;
            this.dest = dest;
        }

        public void run() {
            try {
                looSendCmd();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void looSendCmd() throws Exception {
            if (dSocket == null) {
                dSocket = new DatagramSocket();
            }
            for (int i = 0; i < 100; i++) {
                InetSocketAddress address = new InetSocketAddress(dest, port);
                DatagramPacket datagramPacket = new DatagramPacket(cmd.getBytes(), cmd.getBytes().length, address);
                dSocket.send(datagramPacket);
            }
            if (dSocket != null) {
                dSocket.close();
            }
        }
    }
}
