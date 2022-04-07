package application.https.impl;

import java.io.*;
import java.net.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateExpiredException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;

public class HttpsSertificateSaver {
	
	public void testConnectionTo(String aURL) throws Exception {
        URL destinationURL = new URL(aURL);
        HttpsURLConnection conn = (HttpsURLConnection) destinationURL.openConnection();
        conn.connect();
        Certificate[] certs = conn.getServerCertificates();
        System.out.println("nb = " + certs.length);
        int i = 1;
        for (Certificate cert : certs) {
            System.out.println("");
            System.out.println("");
            System.out.println("");
            System.out.println("################################################################");
            System.out.println("");
            System.out.println("");
            System.out.println("");
            System.out.println("Certificate is: " + cert);
            if(cert instanceof X509Certificate) {
                try {
                    ( (X509Certificate) cert).checkValidity();
                    System.out.println("Certificate is active for current date");
                    FileOutputStream os = new FileOutputStream("myCert"+i);
                    i++;
                    os.write(cert.getEncoded());
                } catch(CertificateExpiredException cee) {
                    System.out.println("Certificate is expired");
                }
            } else {
                System.err.println("Unknown certificate type: " + cert);
            }
        }
    }
	
	
	public static void main(String[] args) throws Exception{
		String url = "https://ekarta.srbvoz.rs/api/stanica";
		
		HttpsSertificateSaver saver = new HttpsSertificateSaver();
		
		saver.testConnectionTo(url);
		
	}

}
