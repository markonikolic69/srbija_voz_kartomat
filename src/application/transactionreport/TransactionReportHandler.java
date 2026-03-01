package application.transactionreport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
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
	
	public static void main(String[] args) throws Exception{
		KupovinaTransaction karta_tran = new KupovinaTransaction();
		KartomatData kartomat = new KartomatData();
		kartomat.setId(1);
		kartomat.setName("kartomat1");

		karta_tran.setKartomat(kartomat);

		FiskalizacijaTransactionDto fiskalizacija_transakcija = new FiskalizacijaTransactionDto();
		fiskalizacija_transakcija.setAddress("address");
		fiskalizacija_transakcija.setAmount(100.0);
		fiskalizacija_transakcija.setBusinessName("business_name");
		fiskalizacija_transakcija.setDistrict("district");
		fiskalizacija_transakcija.setInvoiceCounter("invoice_counter");
		fiskalizacija_transakcija.setInvoiceCounterExtension("invoice_counter_extension");
		fiskalizacija_transakcija.setInvoiceNumber("invoice_number");
		fiskalizacija_transakcija.setInvoiceType("invoice_type");
		fiskalizacija_transakcija.setJournal("journal");
		fiskalizacija_transakcija.setLocationName("location_name");
		fiskalizacija_transakcija.setMrc("mrc");
		fiskalizacija_transakcija.setPAC("PAC");
		fiskalizacija_transakcija.setPayment("payment");
		fiskalizacija_transakcija.setRequest_id("request_id");
		fiskalizacija_transakcija.setRequestedBy("requested_by");
		fiskalizacija_transakcija.setSdcDateTime("sdv_date_time");
		fiskalizacija_transakcija.setTin("Tin");
		fiskalizacija_transakcija.setTotalAmount(100);
		fiskalizacija_transakcija.setTransactionType("transaction_type");
		fiskalizacija_transakcija.setVerificationQRCode("verification_qr_code");
		fiskalizacija_transakcija.setVerificationUrl("verification_url");

		karta_tran.setFiskalizacija_transakcija(fiskalizacija_transakcija);

		KartaDto karta = new KartaDto();
		karta.setCena(100.0);
		karta.setKarta_tip("karta_tip");
		karta.setLegitimacija_id("legitimacija");
		karta.setPovratna_cena(1000);
		karta.setSelected_razred_polazak("drugi");
		karta.setSelected_razred_povratak("drugi");
		List<KartaDto> karte = new ArrayList<KartaDto>();
		karte.add(karta);

		karta_tran.setKarte(karte);

		FisklaizacijaTransactionKartaDto fiskal_karta = new FisklaizacijaTransactionKartaDto();
		fiskal_karta.setAmount(100.0);
		fiskal_karta.setName("fiskal_karta_name");
		fiskal_karta.setQuantity(1);
		fiskal_karta.setTotalPrice(100.0);
		fiskal_karta.setUnitPrice(100.0);

		List<FisklaizacijaTransactionKartaDto> fisk_karte = new ArrayList<FisklaizacijaTransactionKartaDto>();
		fisk_karte.add(fiskal_karta);

		karta_tran.setFiskal_karte(fisk_karte);

		FiskalizacijaTransactionItemDto item = new FiskalizacijaTransactionItemDto();
		item.setAmount(100.0);
		item.setCategoryName("category_name");
		item.setLabel("label");
		item.setRate(10.0);

		karta_tran.setFiskal_item(item);

		karta_tran.setBroj_putnika(1);
		karta_tran.setCena_ukupno(100.0);
		karta_tran.setDestination("destination");
		karta_tran.setSelected_train("selected_train");
		karta_tran.setSelected_train_povrtaka("selected_train_povratka");
		karta_tran.setTransaction_time("2025-05-13");
		
		URL url_devellop = new URL("http://212.200.144.87:6443/addNewTransactionRecord");
		
		TransactionReportHandler handler = new TransactionReportHandler(karta_tran, url_devellop);
		handler.handle_request();
	}

}
