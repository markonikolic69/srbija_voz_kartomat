package application.fiskalizacija;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;




public abstract class AbstractHandler {
	
	private Properties _properties;

	public AbstractHandler(Properties properties) {
		_properties = properties;
	}

	public abstract String handle_request() throws IOException;


	protected URL getPoreskaURL() throws MalformedURLException{
		String poreska_url = _properties.getProperty("poreska.url", "https://vsdc.suf.purs.gov.rs/api/v3/invoices");
		System.out.println("poreska_url = " + poreska_url);
		return new URL(poreska_url);
	}


	public String getPAC() {
		String poreska_pac = _properties.getProperty("poreska.pac", "TMYD8A");
		System.out.println("poreska_pac = " + poreska_pac);
		return poreska_pac;
	}



}
