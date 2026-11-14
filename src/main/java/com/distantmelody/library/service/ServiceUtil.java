package com.distantmelody.library.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;

import com.distantmelody.library.json.J;


public class ServiceUtil 
{
	
	public static InputStream streamService(String url,byte[] postData,SUOption... options)
	{
		String proxyAddress = null;
		int proxyPort = 0;
		SUResponseHeaders responseHeaders = null;
		SUResponseCode SUResponseCode = null;
		SUTimeout SUTimeout = null;
		ArrayList<SUHeader> headers = new ArrayList<SUHeader>();
		String method = "POST";
		
		for (SUOption o:options)
		{
			if (o instanceof SUProxy)
			{
				SUProxy s = (SUProxy) o;
				proxyAddress = s.proxyAddress;
				proxyPort = s.proxyPort;
			}
			else if (o instanceof SUResponseHeaders)
			{
				responseHeaders = (SUResponseHeaders) o;
			}
			else if (o instanceof SUResponseCode)
			{
				SUResponseCode = (SUResponseCode) o;
			}
			else if (o instanceof SUHeader)
			{
				headers.add((SUHeader) o);
			}
			else if (o instanceof SUMethod)
			{
				method = ((SUMethod)o).method;
			}
			else if (o instanceof SUTimeout)
			{
			    SUTimeout = (SUTimeout)o;
			}
		}

		try
		{
			URL obj = new URL(url);
			
			HttpURLConnection con = null;
			if (proxyAddress != null)
			{
				Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyAddress, proxyPort));
				con = (HttpURLConnection) obj.openConnection(proxy);
			}
			else
			{
				con = (HttpURLConnection) obj.openConnection();
			}
			if (SUTimeout != null)
			{
			    con.setConnectTimeout(SUTimeout.connectionTimeout);
			    con.setReadTimeout(SUTimeout.readTimeout);
			}
			else
			{
			    con.setConnectTimeout(0);
			    con.setReadTimeout(0);
			}

		
			for(SUHeader h:headers)
			{
				con.setRequestProperty(h.key,h.value);
			}
	 
			con.setRequestMethod(method);
	 
	 
			con.setDoOutput(true);
			if (postData != null && postData.length > 0)
			{
				DataOutputStream wr = new DataOutputStream(con.getOutputStream());
				wr.write(postData);
				wr.flush();
				wr.close();
			}
			
			if (responseHeaders != null)
			{
				responseHeaders.headers = con.getHeaderFields();
			}
	 
			int responseCode = con.getResponseCode();
			if (SUResponseCode != null)
			{
				SUResponseCode.responseCode = responseCode;
			}
	 
			return con.getInputStream();
		}
		/*
		catch(MalformedURLException e)
		{
			return standardErrorResponse("Malformed URL: " + url);
		}
		catch(IOException e)
		{
			return standardErrorResponse("Connection error: " + e.getMessage());
		}
		catch(Exception e)
		{
			return standardErrorResponse("Unknown error: " + e.getMessage());
		}
		*/
		catch(Exception e)
		{    
		    for (SUOption o:options)
		    {
			if (o instanceof SUError)
			{
			    SUError error = (SUError)o;
			    error.exception = e;
			}
		    }
		    return new ByteArrayInputStream(new byte[0]);
		}

	}

	/*
	private static InputStream standardErrorResponse(String error) 
	{
		String json = "{\"status\":\"false\",\"success\":\"false\",\"error_description\":\""+error+"\"}";
		return new ByteArrayInputStream(json.getBytes());
	}
	*/
	
	public static byte[] dataService(String url,byte[] postData,SUOption... options)
	{
		InputStream is = streamService(url,postData,options);
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		int nRead;
		byte[] data = new byte[32];

		try
		{
			while ((nRead = is.read(data, 0, data.length)) != -1) {
				  buffer.write(data, 0, nRead);
			}
	
			buffer.flush();
		}
		catch(IOException e)
		{
			e.printStackTrace();//should not be possible
		}

		return buffer.toByteArray();	
	}
	
	//so this method can be called from PHP via the skylin bridge. In future improve the skylin bridge so this workarond isnt needed.
	public static String stringService(String url,String postData)
	{
		return stringService(url,postData,new SUOption[0]);
	}
	
	public static String stringService(String url,String postData,SUOption... options)
	{
		if (postData == null)
		{
			return new String(dataService(url,new byte[0],options), StandardCharsets.UTF_8);	
		}
		else
		{
			return new String(dataService(url,postData.getBytes(StandardCharsets.UTF_8),options), StandardCharsets.UTF_8);	
		}
	}
	
	public static J JService(String url,Object j,SUOption... options)
	{
	    String ret;
	    if (j == null)
	    {
		ret = stringService(url,null,options);
	    }
	    else
	    {
		ret = stringService(url,j+"",options);
	    }
	    try
	    {
		return J.fromString(ret);
	    }
	    catch (Exception e)
	    {
		for (SUOption o:options)
		{
		    if (o instanceof SUError)
		    {
			SUError error = (SUError)o;
			if (error.getException() == null)
			{
			    error.exception = new ParseException("Unable to parse Json response:"+ret,0);
			}
		    }
		}
		return null;
	    }
	}	
}
