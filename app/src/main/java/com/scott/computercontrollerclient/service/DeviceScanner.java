package com.scott.computercontrollerclient.service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;


public class DeviceScanner implements Runnable {

	public static final String FLAG_IP_INFO = "IP_INFO";
	private DatagramPacket mPacket;
	private DatagramSocket mSocket;
	private boolean isScanner = true;
	private int port;
	private String gateWay;
	private Thread mThread;
	private IDeviceScanCallback mCallback;
	public static interface IDeviceScanCallback {
		void onScanFinished(String ip);
	}

	public DeviceScanner(boolean isScanner, int port, String gateWay,IDeviceScanCallback callback) {
		this.isScanner = isScanner;
		this.gateWay = gateWay;
		this.port = port;
		mCallback = callback;
	}

	private void init() {
		try {
			if (isScanner) {
				mSocket = new DatagramSocket();
			} else {
				mSocket = new DatagramSocket(port);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("scanner create faild!");
		}
		mThread = new Thread(this);
	}

	public void start() {
		init();
		mThread.start();
	}

	@Override
	public void run() {
		try {
			if (isScanner) {
				scan();
			} else {
				waitScan();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void scan() throws Exception {
		byte[] buf = "IP_GET".getBytes();
		for (int i = 1; i <= 255; i++) {
			InetSocketAddress addr = new InetSocketAddress((gateWay + i), port);
			mPacket = new DatagramPacket(buf, buf.length, addr);
			mSocket.send(mPacket);
		}

		buf = new byte[128];
		mPacket = new DatagramPacket(buf, 128);
		mSocket.receive(mPacket);

		System.out.println("IP:" + new String(buf));
		mCallback.onScanFinished(new String(buf));
	}

	private void waitScan() throws Exception {
		byte[] buf = new byte[128];
		mPacket = new DatagramPacket(buf, 128);
		mSocket.receive(mPacket);
		System.out.println("=============>");

		String ip = FLAG_IP_INFO + ":" + NetWorkUtils.getLocalIpAddr() + ":" +
				InetAddress.getLocalHost().getHostName() + ":";
		mPacket.setData(ip.getBytes());
		mSocket.send(mPacket);
	}
}
