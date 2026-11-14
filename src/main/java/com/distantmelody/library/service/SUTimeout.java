package com.distantmelody.library.service;

public class SUTimeout extends SUOption
{
    int connectionTimeout,readTimeout;
    public SUTimeout(int connectionTimeout, int readTimeout)
    {
	this.readTimeout = readTimeout;
	this.connectionTimeout = connectionTimeout;
    }
    public SUTimeout(int timeout)
    {
	this.readTimeout = timeout;
	this.connectionTimeout = timeout;
    }
}
