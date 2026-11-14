package com.distantmelody.library.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SUResponseHeaders extends SUOption{
	
	Map<String, List<String>> headers;
	
	
	public Map<String, List<String>> getHeaders()
	{
		return headers;
	}

}
