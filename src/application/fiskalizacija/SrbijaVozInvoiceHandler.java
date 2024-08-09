package application.fiskalizacija;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import com.google.gson.Gson;

import application.AbstractController;





public class SrbijaVozInvoiceHandler  extends AbstractHandler{
	
	private List<String> _params = null;
	private String _invoice_number = "";

	
	private static final String INVOICE_TYPE = "Normal";
	private static final String TRANSACTION_TYPE = "Sale";
	
	private InvoiceResponse _response_object = null;
	
	public SrbijaVozInvoiceHandler(List<String> params, String invoice_number, Properties properties) {
		super(properties);
		_params = params;
		_invoice_number = invoice_number;
	}
	
	public InvoiceResponse getInvoiceResponse() {
		return _response_object;
	}
	
	
	@Override
	public String handle_request() throws IOException {
		
//		System.setProperty("javax.net.ssl.keyStore", "komplus_keystore.p12");
//		System.setProperty("javax.net.ssl.keyStorePassword", "changeit");

		List<String> vk_amount_list = _params;
		
		InvoiceRequestBean bean = new InvoiceRequestBean(vk_amount_list, _invoice_number,  PoreskaStopa.DESET_POSTO);
		

		String json_request = new Gson().toJson(bean);
				System.out.println("InvoiceHandler json_request, " + json_request);
				


		HttpURLConnection con = (HttpURLConnection)getPoreskaURL().openConnection();
		UUID uuid = UUID.randomUUID();
		String request_id = uuid.toString().substring(0,32);
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json; utf-8");
		con.setRequestProperty("Accept", "application/json");
		con.setRequestProperty("Accept-Language", "sr-Cyrl-RS");
		con.setRequestProperty("RequestId", request_id);
		
		//kentkart
		//con.setRequestProperty("PAC", "TMYD8A");
		//okoioko
		//con.setRequestProperty("PAC", "WMPSA4");
		System.out.println("PAC = " + getPAC());
		con.setRequestProperty("PAC", getPAC());
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
		System.out.println("Response from poreska: " + response.toString());
		
		
		Gson gson = new Gson();
		_response_object = gson.fromJson(response.toString(), InvoiceResponse.class);
		
		System.out.println("response_json: " + _response_object);

		String pfr_date_time = _response_object.getSdcDateTime();
		pfr_date_time = pfr_date_time.replace("T", " ");
		



		int SIRINA_DUZINA_QRCODE = 150;
		//BufferedImage barcode  = new Service().getQRCode(response_json.getVerificationUrl(), SIRINA_DUZINA_QRCODE);
		//ImageUtil.bufferedImageToFile(barcode, "marko_qrcode.png");
		//String qr_code_base64 = ImageUtil.encodeToString(barcode, "png");
		//System.out.println("qr_code_base64 = " + qr_code_base64);
		//System.out.println("qr_code_base64 length = " + qr_code_base64.length());
		String to_return = /*"journal=" +*/ _response_object.getJournal() + "&" +

				/*"qr_code_base64=" +*/ _response_object.getVerificationUrl();


		return to_return;
	}
	
	public static void main(String[] args) throws Exception {
		
		//komplus testni sertifikati
		System.setProperty("javax.net.ssl.keyStore", "Kartomat_Novi_Sad_2.nochain.p12");
		System.setProperty("javax.net.ssl.keyStorePassword", "SDFA2F8B");
		List<String> _vk_amount = new ArrayList<String>();
		_vk_amount.add("1234512345:30");
//		_vk_amount.add("1234512346:250");
//		_vk_amount.add("1234512347:260");
//		_vk_amount.add("1234512348:270");
//		_vk_amount.add("1234512349:280");
		SrbijaVozInvoiceHandler fiskal_handler = new SrbijaVozInvoiceHandler(_vk_amount, "1145/2.0"/*"601/1.0"*/, 
				AbstractController.getProperties());
		try {
			String result = fiskal_handler.handle_request();
			String[] journal_qr_code = result.split("&");
			String _fiscal_journal = journal_qr_code[0];
			String _fiscal_qr_code = journal_qr_code[1];
			System.out.println("_fiscal_journal = " + _fiscal_journal);
			System.out.println("_fiscal_qr_code = " + _fiscal_qr_code);
		}catch(IOException ioe) {
			ioe.printStackTrace();
			System.out.println("Unable to call fiskal service, details: = " + ioe.getMessage());
		}

	}
}
