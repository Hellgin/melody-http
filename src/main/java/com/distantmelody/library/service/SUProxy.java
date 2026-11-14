package com.distantmelody.library.service;

public class SUProxy extends SUOption{

	String proxyAddress;
	int proxyPort;
	public SUProxy(String proxyAddress, int proxyPort) {
		this.proxyAddress = proxyAddress;
		this.proxyPort = proxyPort;
	}

}
