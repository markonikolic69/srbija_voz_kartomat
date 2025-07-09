package application.transactionreport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import application.fiskalizacija.InvoiceRequestBean;
import application.fiskalizacija.InvoiceResponse;
import application.fiskalizacija.PoreskaStopa;

public class TransactionReportHandler implements Runnable{
	
	private static final Logger logger = Logger.getLogger("TransactionReportHandler"); 
	
	private KupovinaTransaction bean;
	private URL _transactioReportURL;
	
	public TransactionReportHandler(KupovinaTransaction to_send, URL transactioReportURL) {
		bean = to_send;
		_transactioReportURL = transactioReportURL;
	}
	
	
	
	public void run() {
		try {
			handle_request();
		}catch(Exception e) {
			logger.info(" Exception when try to sendTotransactionReport, details: " + e.getMessage(), e);
		}
	}
	
	
	public void handle_request() throws IOException {
		

		String json_request = new Gson().toJson(bean);
				System.out.println("TransactionReportHandler json_request, " + json_request);
				


		HttpURLConnection con = (HttpURLConnection)_transactioReportURL.openConnection();
		UUID uuid = UUID.randomUUID();
		String request_id = uuid.toString().substring(0,32);
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json; utf-8");
		con.setRequestProperty("Accept", "application/json");
		con.setRequestProperty("Accept-Language", "sr-Cyrl-RS");
		con.setRequestProperty("RequestId", request_id);
		con.setRequestProperty("JWT", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3MjY3ODU3MjgsInN1YiI6Nzk0NiwidHlwZSI6ImNvbnN1bWVyIn0.cPMlWTkfr4qnhei54niEhIwi8wEY8dhzDcgi925jYh4");
		
		//kentkart
		//con.setRequestProperty("PAC", "TMYD8A");


		con.setDoOutput(true);



		OutputStream os = con.getOutputStream();
		byte[] input = json_request.getBytes("utf-8");
		os.write(input, 0, input.length);			

		BufferedReader br = new BufferedReader(
				new InputStreamReader(con.getInputStream(), "utf-8"));

		StringBuilder response = new StringBuilder();
		String responseLine = null;
		while ((responseLine = br.readLine()) != null) {
			response.append(responseLine.trim());

			
		}
		System.out.println("Response from transaction_report: " + response.toString());
		
		
//		Gson gson = new Gson();
//		_response_object = gson.fromJson(response.toString(), InvoiceResponse.class);
//		

	}

}
