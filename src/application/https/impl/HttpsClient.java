package application.https.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;


import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;


import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.mashape.unirest.http.Unirest;

import application.https.CommunicationException;

import application.util.*;



public class HttpsClient {
	
	
	static {
	    SSLUtilities.trustAllHostnames();
	    SSLUtilities.trustAllHttpsCertificates();
	}
    
    
	
	public static String getUrl(String url) throws CommunicationException {

		DefaultHttpClient httpclient = null;

		try {
//			System.setProperty("javax.net.ssl.keyStore", "C:\\Users\\38164\\eclipse-workspace\\SrbijaVoz\\srbija_voz.keystore");
//			System.setProperty("javax.net.ssl.trustStore", "C:\\Users\\38164\\eclipse-workspace\\SrbijaVoz\\srbija_voz.keystore");
//			System.setProperty("javax.net.ssl.keyStorePassword", "changeit");
//			System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
			
			httpclient = new DefaultHttpClient();
			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
/*			FileInputStream instream = new FileInputStream(new File(
					PCVoucherUtil.CERT_PATH));*/
			
			// Get current classloader
//			ClassLoader cl = new HttpsClient().getClass().getClassLoader();
//			InputStream  instream = cl.getResourceAsStream("C:\\Users\\38164\\eclipse-workspace\\SrbijaVoz\\srbija_voz.keystore");
			
			// TODO AUTOMATIC HTTPS CERTIFICATE HANDLING			
/*			URL url1 = new URL("http://localhost:8080/pcvoucher/lanus.keystore");
			URLConnection uc = url1.openConnection();*/
						
//			try {
//				trustStore.load(instream, "changeit".toCharArray());
//				
//				// TODO AUTOMATIC HTTPS CERTIFICATE HANDLING
//				//trustStore.load(uc.getInputStream(), "hanibal77".toCharArray());
//			} finally {
//				instream.close();
//			}

			SSLSocketFactory socketFactory = new SSLSocketFactory(trustStore);
			
			socketFactory
					.setHostnameVerifier(socketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			Scheme sch = new Scheme("https", socketFactory, 443);
			//httpclient.getConnectionManager().getSchemeRegistry().register(sch);

			HttpGet httpget = new HttpGet(url);

			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			BufferedReader in
			   = new BufferedReader(new InputStreamReader(entity.getContent()));


			
			String line  = in.readLine();
			System.out.println("content = " + line);
			return line;
			//return response;

		} catch (Exception e) {
			e.printStackTrace();
			throw new CommunicationException();
		} finally {
			// When HttpClient instance is no longer needed,
			// shut down the connection manager to ensure
			// immediate deallocation of all system resources
			
			//httpclient.getConnectionManager().shutdown();
		}

	}
	
	public static String postUrl(String url, String to_post) throws CommunicationException {

		DefaultHttpClient httpclient = null;

		try {
//			System.setProperty("javax.net.ssl.keyStore", "C:\\Users\\38164\\eclipse-workspace\\SrbijaVoz\\srbija_voz.keystore");
//			System.setProperty("javax.net.ssl.trustStore", "C:\\Users\\38164\\eclipse-workspace\\SrbijaVoz\\srbija_voz.keystore");
//			System.setProperty("javax.net.ssl.keyStorePassword", "changeit");
//			System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
			
			httpclient = new DefaultHttpClient();
			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
/*			FileInputStream instream = new FileInputStream(new File(
					PCVoucherUtil.CERT_PATH));*/
			
			// Get current classloader
//			ClassLoader cl = new HttpsClient().getClass().getClassLoader();
//			InputStream  instream = cl.getResourceAsStream("C:\\Users\\38164\\eclipse-workspace\\SrbijaVoz\\srbija_voz.keystore");
			
			// TODO AUTOMATIC HTTPS CERTIFICATE HANDLING			
/*			URL url1 = new URL("http://localhost:8080/pcvoucher/lanus.keystore");
			URLConnection uc = url1.openConnection();*/
						
//			try {
//				trustStore.load(instream, "changeit".toCharArray());
//				
//				// TODO AUTOMATIC HTTPS CERTIFICATE HANDLING
//				//trustStore.load(uc.getInputStream(), "hanibal77".toCharArray());
//			} finally {
//				instream.close();
//			}

			SSLSocketFactory socketFactory = new SSLSocketFactory(trustStore);
			
			socketFactory
					.setHostnameVerifier(socketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			Scheme sch = new Scheme("https", socketFactory, 443);
			//httpclient.getConnectionManager().getSchemeRegistry().register(sch);

			HttpPost http_post = new HttpPost(url);
			

		    StringEntity st_entity = new StringEntity(to_post);
		    http_post.setEntity(st_entity);
		    http_post.setHeader("Accept", "application/json");
		    http_post.setHeader("Content-type", "application/json");

		    HttpResponse response = httpclient.execute(http_post);

			HttpEntity entity = response.getEntity();
			BufferedReader in
			   = new BufferedReader(new InputStreamReader(entity.getContent()));

			
			String line  = in.readLine();
			System.out.println("content = " + line);
			return line;
			//return response;

		} catch (Exception e) {
			e.printStackTrace();
			throw new CommunicationException();
		} finally {
			// When HttpClient instance is no longer needed,
			// shut down the connection manager to ensure
			// immediate deallocation of all system resources
			
			//httpclient.getConnectionManager().shutdown();
		}

	}
	
	
	public static String postUrl(String url, Map<String, Object> fields) throws CommunicationException {

		try {



			com.mashape.unirest.http.HttpResponse<String> jsonResponse_et_log 
			= Unirest.post(url)
			.header("accept", "application/json").header("Content-Type", "application/x-www-form-urlencoded")
			.fields(fields)
			.asString();
			System.out.println(jsonResponse_et_log.getBody());
			return jsonResponse_et_log.getBody();
		}catch(Exception e) {
			throw new CommunicationException();
		}

	}
	
	
	
	
	public final static void main(String[] args) throws Exception {

		HttpsClient client = new HttpsClient();
		String response = client
				//test
				//https://testekarta.srbvoz.rs/etest21/api/
				//test lista vozova
				//https://testekarta.srbvoz.rs/etest21/api/listavozova?stanicaod=16052&stanicado=22001&datum=9-9-2021&brojputnika=1&razred=2
				//test - cene
				//https://testekarta.srbvoz.rs/etest21/api/cene?brojputnika=1&km=18&sifpov=1&rang=6&razred=2
				//test popust
				//https://testekarta.srbvoz.rs/etest21/api/povlastice?smer=1
				//test propvera povlastice
				//https://testekarta.srbvoz.rs/etest21/api/povlastice?brleg=52910&pov=37
				//tes frekventne stanoce
				//https://testekarta.srbvoz.rs/etest21/api/KartomatSV/KT_Vrati_FrekfentneST?KartomatID=1605201
				//terminal kartomat
				//https://testekarta.srbvoz.rs/etest21/api/KartomatSV/KT_Vrati_Terminal?mac_address=987654321
				//produkcija
				//https://ekarta.srbvoz.rs/api/
				.getUrl("https://testekarta.srbvoz.rs/etest21/api/povlastice?smer=1");
//		System.out.println(response.getStatusLine());
//		HttpEntity entity = response.getEntity();
//		
//		 BufferedReader in
//		   = new BufferedReader(new InputStreamReader(entity.getContent()));
//
//
//		
//		String line  = in.readLine();
		System.out.println("content = " + response);

		//entity.consumeContent();

	}

}
