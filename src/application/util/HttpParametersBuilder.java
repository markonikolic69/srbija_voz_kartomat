package application.util;

import java.util.Enumeration;
import java.util.Hashtable;

public class HttpParametersBuilder {
	private Hashtable<String, String> parameters = new Hashtable<String, String>();
	
	
	public HttpParametersBuilder() {
		
	}
	
	public HttpParametersBuilder(String params) {
		parse(params);
	}
	
	
	public  void add(String name, String value)
	{
		parameters.put(name, value);
	}
	
	public String getValue(String key) {
		return parameters.get(key);
	}
	
	public String getQueryString()
	{
		StringBuilder builder = new StringBuilder();
		Enumeration<String> keys = parameters.keys();
		while (keys.hasMoreElements())
		{
			String key = (String) keys.nextElement();
			builder.append(key + "=" + parameters.get(key));
			if (keys.hasMoreElements())
				builder.append('&');
		}
		return builder.toString();
	}
	
	private void parse(String params) {
		String[] key_value_pairs = params.split("&");
		for(String current : key_value_pairs) {
			String[] current_pairs = current.split("=");
			add(current_pairs[0], current_pairs[1]);
		}
	}
}
