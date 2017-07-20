package com.scott.computercontrollerclient.service;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class NetWorkUtils {
	
	public static String getLocalIpAddr() {
		String NET_CARD = "Realtek";
		try {
			Enumeration interfaces = null;
			interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) interfaces.nextElement();
				Enumeration addresss = ni.getInetAddresses();
				while (addresss.hasMoreElements()) {
					InetAddress nextElement = (InetAddress) addresss.nextElement();
					String hostAddress = nextElement.getHostAddress();
					if (ni.getDisplayName().contains(NET_CARD)) {
						return hostAddress;
					}
					//System.out.println("ip = " + hostAddress + ",name = " + ni.getDisplayName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
