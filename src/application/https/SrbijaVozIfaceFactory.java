package application.https;

import application.https.impl.SrbijaVozIfaceImpl;

public class SrbijaVozIfaceFactory {
	
	
	public static SrbijaVozIface getIface(String url, int connection_timeout, int read_timeout) {
		return new SrbijaVozIfaceImpl(url, connection_timeout, read_timeout );
	}
//	https://testekarta.srbvoz.rs/etest21/api/ 
//	https://webapi1.srbvoz.rs/eKarta_pTest/api/	
	public static SrbijaVozIface getIfaceTest() {
		return getIface("https://webapi1.srbvoz.rs/eKarta_pTest/api/", 3000, 12000);
	}

	public static SrbijaVozIface getIfaceProduction() {
		return getIface("https://webapi1.srbvoz.rs/eKarta/api/", 3000, 12000);
	}
	
}
