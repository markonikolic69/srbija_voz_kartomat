package application.https.impl;

import java.util.HashMap;
import java.util.Map;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import application.https.CommunicationException;

public class UnirestPostClient {
	
	
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
	
	
public static void main(String[] args) throws Exception{
		
		
		
	    Map<String, Object> fields = new HashMap<>();
	    fields.put("ID_USER", 132);
	    fields.put("HASH", "");
	    fields.put("DATUM_KUPOVINE", "17-01-2022");
	    fields.put("ID_RELACIJE", 175483);  
	    fields.put("OD", 16052);
	    fields.put("DO", 21009);
	    fields.put("SMER", 1);
	    fields.put("VIA", "01");
	    fields.put("VAZI_OD", "17-01-2022");
	    fields.put("VAZI_DO", "17-01-2022");
	    fields.put("VOZA", 1032);
	    fields.put("RAZREDA", 2);
	    fields.put("VREME_POLASKAA", "10:00");
	    fields.put("VREME_DOLASKAA", "11:46");
	    fields.put("VOZR", null);
	    fields.put("RAZREDR", null);
	    fields.put("VREME_POLASKAR", null);
	    fields.put("VREME_DOLASKAR", null);
	    fields.put("BROJ_PUTNIKA", 1);
	    fields.put("KURS_DINARA_ZA_EVRO", 0.0);
	    fields.put("UKUPNA_CENA", 300);
	    fields.put("PLACENA", 0);
	    fields.put("DODATNI_OPIS", "");
	    //fields.put("ETKARTADATA", "{\"vozData\":{\"datum1\":\"2021-12-14T08:08:14.358Z\",\"brojputnika\":1,\"razred\":\"2\",\"voz\":{\"odsifra\":16054,\"dosifra\":21001,\"idvoz\":1036,\"brvoz\":2607,\"nazivvoz\":\"                    \",\"rang\":6,\"vremep\":\"15:38\",\"vremed\":\"16:04             \",\"nazivod\":\"Vukov spomenik\",\"nazivdo\":\"Pancevo varos\",\"idrel\":175483,\"relkm\":21,\"ponuda\":\"Bv        \",\"cenau\":141,\"trajanje_putovanja\":\"00:26\",\"datum_dolaska\":\"2021-12-14T00:00:00\",\"etTrasaVoza\":[{\"iD_VOZ\":1036,\"rb\":1,\"sifrA_STANICE\":16052,\"vremE_DOLASKA\":\"15:30             \",\"vremE_POLASKA\":\"15:30             \",\"naziV_STANICE\":\"Beograd centar\"},{\"iD_VOZ\":1036,\"rb\":2,\"sifrA_STANICE\":16053,\"vremE_DOLASKA\":\"15:33             \",\"vremE_POLASKA\":\"15:34             \",\"naziV_STANICE\":\"Karadjordjev park\"},{\"iD_VOZ\":1036,\"rb\":3,\"sifrA_STANICE\":16054,\"vremE_DOLASKA\":\"15:37             \",\"vremE_POLASKA\":\"15:38             \",\"naziV_STANICE\":\"Vukov spomenik\"},{\"iD_VOZ\":1036,\"rb\":4,\"sifrA_STANICE\":16013,\"vremE_DOLASKA\":\"15:41             \",\"vremE_POLASKA\":\"15:42             \",\"naziV_STANICE\":\"Pancevacki most\"},{\"iD_VOZ\":1036,\"rb\":5,\"sifrA_STANICE\":22001,\"vremE_DOLASKA\":\"15:59             \",\"vremE_POLASKA\":\"16:00             \",\"naziV_STANICE\":\"Pancevo glavna stani\"},{\"iD_VOZ\":1036,\"rb\":6,\"sifrA_STANICE\":21001,\"vremE_DOLASKA\":\"16:04             \",\"vremE_POLASKA\":\"16:05             \",\"naziV_STANICE\":\"Pancevo varos\"},{\"iD_VOZ\":1036,\"rb\":7,\"sifrA_STANICE\":21002,\"vremE_DOLASKA\":\"16:18             \",\"vremE_POLASKA\":\"16:19             \",\"naziV_STANICE\":\"Banatsko Novo Selo\"},{\"iD_VOZ\":1036,\"rb\":8,\"sifrA_STANICE\":21003,\"vremE_DOLASKA\":\"16:29             \",\"vremE_POLASKA\":\"16:30             \",\"naziV_STANICE\":\"Vladimirovac\"},{\"iD_VOZ\":1036,\"rb\":9,\"sifrA_STANICE\":21004,\"vremE_DOLASKA\":\"16:38             \",\"vremE_POLASKA\":\"16:39             \",\"naziV_STANICE\":\"Alibunar\"},{\"iD_VOZ\":1036,\"rb\":10,\"sifrA_STANICE\":21005,\"vremE_DOLASKA\":\"16:45             \",\"vremE_POLASKA\":\"16:46             \",\"naziV_STANICE\":\"Banatski Karlovac\"},{\"iD_VOZ\":1036,\"rb\":11,\"sifrA_STANICE\":21006,\"vremE_DOLASKA\":\"16:50             \",\"vremE_POLASKA\":\"16:51             \",\"naziV_STANICE\":\"Nikolinci\"},{\"iD_VOZ\":1036,\"rb\":12,\"sifrA_STANICE\":21007,\"vremE_DOLASKA\":\"16:57             \",\"vremE_POLASKA\":\"16:58             \",\"naziV_STANICE\":\"Uljma\"},{\"iD_VOZ\":1036,\"rb\":13,\"sifrA_STANICE\":21008,\"vremE_DOLASKA\":\"17:03             \",\"vremE_POLASKA\":\"17:04             \",\"naziV_STANICE\":\"Vlajkovac\"},{\"iD_VOZ\":1036,\"rb\":14,\"sifrA_STANICE\":21009,\"vremE_DOLASKA\":\"17:12             \",\"vremE_POLASKA\":\"17:12             \",\"naziV_STANICE\":\"Vrsac\"}]},\"vozp\":{\"odsifra\":21001,\"dosifra\":16054,\"idvoz\":1387,\"brvoz\":22610,\"nazivvoz\":\"                    \",\"rang\":6,\"vremep\":\"20:06\",\"vremed\":\"20:33             \",\"nazivod\":\"Pancevo varos\",\"nazivdo\":\"Vukov spomenik\",\"idrel\":178662,\"relkm\":21,\"ponuda\":\"Bv        \",\"cenau\":141,\"trajanje_putovanja\":\"00:27\",\"datum_dolaska\":\"2021-12-14T00:00:00\",\"etTrasaVoza\":[{\"iD_VOZ\":1387,\"rb\":1,\"sifrA_STANICE\":21009,\"vremE_DOLASKA\":\"18:58             \",\"vremE_POLASKA\":\"18:58             \",\"naziV_STANICE\":\"Vrsac\"},{\"iD_VOZ\":1387,\"rb\":2,\"sifrA_STANICE\":21008,\"vremE_DOLASKA\":\"19:06             \",\"vremE_POLASKA\":\"19:07             \",\"naziV_STANICE\":\"Vlajkovac\"},{\"iD_VOZ\":1387,\"rb\":3,\"sifrA_STANICE\":21007,\"vremE_DOLASKA\":\"19:12             \",\"vremE_POLASKA\":\"19:13             \",\"naziV_STANICE\":\"Uljma\"},{\"iD_VOZ\":1387,\"rb\":4,\"sifrA_STANICE\":21006,\"vremE_DOLASKA\":\"19:19             \",\"vremE_POLASKA\":\"19:20             \",\"naziV_STANICE\":\"Nikolinci\"},{\"iD_VOZ\":1387,\"rb\":5,\"sifrA_STANICE\":21005,\"vremE_DOLASKA\":\"19:24             \",\"vremE_POLASKA\":\"19:25             \",\"naziV_STANICE\":\"Banatski Karlovac\"},{\"iD_VOZ\":1387,\"rb\":6,\"sifrA_STANICE\":21004,\"vremE_DOLASKA\":\"19:31             \",\"vremE_POLASKA\":\"19:32             \",\"naziV_STANICE\":\"Alibunar\"},{\"iD_VOZ\":1387,\"rb\":7,\"sifrA_STANICE\":21003,\"vremE_DOLASKA\":\"19:40             \",\"vremE_POLASKA\":\"19:41             \",\"naziV_STANICE\":\"Vladimirovac\"},{\"iD_VOZ\":1387,\"rb\":8,\"sifrA_STANICE\":21002,\"vremE_DOLASKA\":\"19:50             \",\"vremE_POLASKA\":\"19:51             \",\"naziV_STANICE\":\"Banatsko Novo Selo\"},{\"iD_VOZ\":1387,\"rb\":9,\"sifrA_STANICE\":21001,\"vremE_DOLASKA\":\"20:05             \",\"vremE_POLASKA\":\"20:06             \",\"naziV_STANICE\":\"Pancevo varos\"},{\"iD_VOZ\":1387,\"rb\":10,\"sifrA_STANICE\":22001,\"vremE_DOLASKA\":\"20:10             \",\"vremE_POLASKA\":\"20:12             \",\"naziV_STANICE\":\"Pancevo glavna stani\"},{\"iD_VOZ\":1387,\"rb\":11,\"sifrA_STANICE\":16013,\"vremE_DOLASKA\":\"20:29             \",\"vremE_POLASKA\":\"20:30             \",\"naziV_STANICE\":\"Pancevacki most\"},{\"iD_VOZ\":1387,\"rb\":12,\"sifrA_STANICE\":16054,\"vremE_DOLASKA\":\"20:33             \",\"vremE_POLASKA\":\"20:34             \",\"naziV_STANICE\":\"Vukov spomenik\"},{\"iD_VOZ\":1387,\"rb\":13,\"sifrA_STANICE\":16053,\"vremE_DOLASKA\":\"20:36             \",\"vremE_POLASKA\":\"20:37             \",\"naziV_STANICE\":\"Karadjordjev park\"},{\"iD_VOZ\":1387,\"rb\":14,\"sifrA_STANICE\":16052,\"vremE_DOLASKA\":\"20:39             \",\"vremE_POLASKA\":\"20:39             \",\"naziV_STANICE\":\"Beograd centar\"}]},\"datum2\":\"2021-12-14T08:08:56.727Z\",\"razredp\":\"2\",\"cenauk\":226},\"cenasmer1\":[],\"cenasmer2\":[[{\"cenau\":198,\"cenao\":156,\"cenad\":42,\"cenab\":0,\"cenar\":0,\"tiprez\":0}]],\"sifraod\":{\"sifra\":16054,\"naziv\":\"Vukov spomenik\"},\"sifrado\":{\"sifra\":21001,\"naziv\":\"Pancevo varos\"},\"smer\":\"2\",\"ukupnacena\":198,\"putnici\":[{\"imeprezime\":\"ALEKSANDAR BABIC\",\"datumrodjenja\":\"10-09-1997\",\"sifrapovlastice\":37,\"idleg\":\"0049116\",\"cenap\":[{\"cenau\":198,\"cenao\":156,\"cenad\":42,\"cenab\":0,\"cenar\":0,\"tiprez\":0}]}],\"uneseneleg\":[\"0049116\"]}");
	    fields.put("ETKARTADATA", "{\"vozData\":{\"datum1\":\"2021-12-14T08:08:14.358Z\",\"brojputnika\":1,\"razred\":\"2\",\"voz\":{\"odsifra\":16054,\"dosifra\":21001,\"idvoz\":1036,\"brvoz\":2607,\"nazivvoz\":\"                    \",\"rang\":6,\"vremep\":\"15:38\",\"vremed\":\"16:04             \",\"nazivod\":\"Vukov spomenik\",\"nazivdo\":\"Pancevo varos\",\"idrel\":175483,\"relkm\":21,\"ponuda\":\"Bv        \",\"cenau\":141,\"trajanje_putovanja\":\"00:26\",\"datum_dolaska\":\"2021-12-14T00:00:00\",\"etTrasaVoza\":[{\"iD_VOZ\":1036,\"rb\":1,\"sifrA_STANICE\":16052,\"vremE_DOLASKA\":\"15:30             \",\"vremE_POLASKA\":\"15:30             \",\"naziV_STANICE\":\"Beograd centar\"},{\"iD_VOZ\":1036,\"rb\":2,\"sifrA_STANICE\":16053,\"vremE_DOLASKA\":\"15:33             \",\"vremE_POLASKA\":\"15:34             \",\"naziV_STANICE\":\"Karadjordjev park\"},{\"iD_VOZ\":1036,\"rb\":3,\"sifrA_STANICE\":16054,\"vremE_DOLASKA\":\"15:37             \",\"vremE_POLASKA\":\"15:38             \",\"naziV_STANICE\":\"Vukov spomenik\"},{\"iD_VOZ\":1036,\"rb\":4,\"sifrA_STANICE\":16013,\"vremE_DOLASKA\":\"15:41             \",\"vremE_POLASKA\":\"15:42             \",\"naziV_STANICE\":\"Pancevacki most\"},{\"iD_VOZ\":1036,\"rb\":5,\"sifrA_STANICE\":22001,\"vremE_DOLASKA\":\"15:59             \",\"vremE_POLASKA\":\"16:00             \",\"naziV_STANICE\":\"Pancevo glavna stani\"},{\"iD_VOZ\":1036,\"rb\":6,\"sifrA_STANICE\":21001,\"vremE_DOLASKA\":\"16:04             \",\"vremE_POLASKA\":\"16:05             \",\"naziV_STANICE\":\"Pancevo varos\"},{\"iD_VOZ\":1036,\"rb\":7,\"sifrA_STANICE\":21002,\"vremE_DOLASKA\":\"16:18             \",\"vremE_POLASKA\":\"16:19             \",\"naziV_STANICE\":\"Banatsko Novo Selo\"},{\"iD_VOZ\":1036,\"rb\":8,\"sifrA_STANICE\":21003,\"vremE_DOLASKA\":\"16:29             \",\"vremE_POLASKA\":\"16:30             \",\"naziV_STANICE\":\"Vladimirovac\"},{\"iD_VOZ\":1036,\"rb\":9,\"sifrA_STANICE\":21004,\"vremE_DOLASKA\":\"16:38             \",\"vremE_POLASKA\":\"16:39             \",\"naziV_STANICE\":\"Alibunar\"},{\"iD_VOZ\":1036,\"rb\":10,\"sifrA_STANICE\":21005,\"vremE_DOLASKA\":\"16:45             \",\"vremE_POLASKA\":\"16:46             \",\"naziV_STANICE\":\"Banatski Karlovac\"},{\"iD_VOZ\":1036,\"rb\":11,\"sifrA_STANICE\":21006,\"vremE_DOLASKA\":\"16:50             \",\"vremE_POLASKA\":\"16:51             \",\"naziV_STANICE\":\"Nikolinci\"},{\"iD_VOZ\":1036,\"rb\":12,\"sifrA_STANICE\":21007,\"vremE_DOLASKA\":\"16:57             \",\"vremE_POLASKA\":\"16:58             \",\"naziV_STANICE\":\"Uljma\"},{\"iD_VOZ\":1036,\"rb\":13,\"sifrA_STANICE\":21008,\"vremE_DOLASKA\":\"17:03             \",\"vremE_POLASKA\":\"17:04             \",\"naziV_STANICE\":\"Vlajkovac\"},{\"iD_VOZ\":1036,\"rb\":14,\"sifrA_STANICE\":21009,\"vremE_DOLASKA\":\"17:12             \",\"vremE_POLASKA\":\"17:12             \",\"naziV_STANICE\":\"Vrsac\"}]},\"vozp\":{\"odsifra\":21001,\"dosifra\":16054,\"idvoz\":1387,\"brvoz\":22610,\"nazivvoz\":\"                    \",\"rang\":6,\"vremep\":\"20:06\",\"vremed\":\"20:33             \",\"nazivod\":\"Pancevo varos\",\"nazivdo\":\"Vukov spomenik\",\"idrel\":178662,\"relkm\":21,\"ponuda\":\"Bv        \",\"cenau\":141,\"trajanje_putovanja\":\"00:27\",\"datum_dolaska\":\"2021-12-14T00:00:00\",\"etTrasaVoza\":[{\"iD_VOZ\":1387,\"rb\":1,\"sifrA_STANICE\":21009,\"vremE_DOLASKA\":\"18:58             \",\"vremE_POLASKA\":\"18:58             \",\"naziV_STANICE\":\"Vrsac\"},{\"iD_VOZ\":1387,\"rb\":2,\"sifrA_STANICE\":21008,\"vremE_DOLASKA\":\"19:06             \",\"vremE_POLASKA\":\"19:07             \",\"naziV_STANICE\":\"Vlajkovac\"},{\"iD_VOZ\":1387,\"rb\":3,\"sifrA_STANICE\":21007,\"vremE_DOLASKA\":\"19:12             \",\"vremE_POLASKA\":\"19:13             \",\"naziV_STANICE\":\"Uljma\"},{\"iD_VOZ\":1387,\"rb\":4,\"sifrA_STANICE\":21006,\"vremE_DOLASKA\":\"19:19             \",\"vremE_POLASKA\":\"19:20             \",\"naziV_STANICE\":\"Nikolinci\"},{\"iD_VOZ\":1387,\"rb\":5,\"sifrA_STANICE\":21005,\"vremE_DOLASKA\":\"19:24             \",\"vremE_POLASKA\":\"19:25             \",\"naziV_STANICE\":\"Banatski Karlovac\"},{\"iD_VOZ\":1387,\"rb\":6,\"sifrA_STANICE\":21004,\"vremE_DOLASKA\":\"19:31             \",\"vremE_POLASKA\":\"19:32             \",\"naziV_STANICE\":\"Alibunar\"},{\"iD_VOZ\":1387,\"rb\":7,\"sifrA_STANICE\":21003,\"vremE_DOLASKA\":\"19:40             \",\"vremE_POLASKA\":\"19:41             \",\"naziV_STANICE\":\"Vladimirovac\"},{\"iD_VOZ\":1387,\"rb\":8,\"sifrA_STANICE\":21002,\"vremE_DOLASKA\":\"19:50             \",\"vremE_POLASKA\":\"19:51             \",\"naziV_STANICE\":\"Banatsko Novo Selo\"},{\"iD_VOZ\":1387,\"rb\":9,\"sifrA_STANICE\":21001,\"vremE_DOLASKA\":\"20:05             \",\"vremE_POLASKA\":\"20:06             \",\"naziV_STANICE\":\"Pancevo varos\"},{\"iD_VOZ\":1387,\"rb\":10,\"sifrA_STANICE\":22001,\"vremE_DOLASKA\":\"20:10             \",\"vremE_POLASKA\":\"20:12             \",\"naziV_STANICE\":\"Pancevo glavna stani\"},{\"iD_VOZ\":1387,\"rb\":11,\"sifrA_STANICE\":16013,\"vremE_DOLASKA\":\"20:29             \",\"vremE_POLASKA\":\"20:30             \",\"naziV_STANICE\":\"Pancevacki most\"},{\"iD_VOZ\":1387,\"rb\":12,\"sifrA_STANICE\":16054,\"vremE_DOLASKA\":\"20:33             \",\"vremE_POLASKA\":\"20:34             \",\"naziV_STANICE\":\"Vukov spomenik\"},{\"iD_VOZ\":1387,\"rb\":13,\"sifrA_STANICE\":16053,\"vremE_DOLASKA\":\"20:36             \",\"vremE_POLASKA\":\"20:37             \",\"naziV_STANICE\":\"Karadjordjev park\"},{\"iD_VOZ\":1387,\"rb\":14,\"sifrA_STANICE\":16052,\"vremE_DOLASKA\":\"20:39             \",\"vremE_POLASKA\":\"20:39             \",\"naziV_STANICE\":\"Beograd centar\"}]},\"datum2\":\"2021-12-14T08:08:56.727Z\",\"razredp\":\"2\",\"cenauk\":226},\"cenasmer1\":[],\"cenasmer2\":[[{\"cenau\":198,\"cenao\":156,\"cenad\":42,\"cenab\":0,\"cenar\":0,\"tiprez\":0}]],\"sifraod\":{\"sifra\":16054,\"naziv\":\"Vukov spomenik\"},\"sifrado\":{\"sifra\":21001,\"naziv\":\"Pancevo varos\"},\"smer\":\"2\",\"ukupnacena\":198,\"putnici\":[{\"imeprezime\":\"ALEKSANDAR BABIC\",\"datumrodjenja\":\"10-09-1997\",\"sifrapovlastice\":37,\"idleg\":\"0049116\",\"cenap\":[{\"cenau\":198,\"cenao\":156,\"cenad\":42,\"cenab\":0,\"cenar\":0,\"tiprez\":0}]}],\"uneseneleg\":[\"0049116\"]}");

	    

		
	    
	    
		HttpResponse<String> jsonResponse 
	      = Unirest.post("https://testekarta.srbvoz.rs/etest21/api/KartomatSV/KT_ET_Order")
	    		  .header("accept", "application/json").header("Content-Type", "application/x-www-form-urlencoded")
	      .fields(fields)
	      .asString();
		System.out.println(jsonResponse.getBody());
	    
	    
	    
	    
	    
	    //////////////////////////////////////////////////////////poziv posto se karta odstampa
		
		

		
		
		
//	    Map<String, Object> fields_et_log = new HashMap<>();
//	    fields_et_log.put("ID_USER", 132);
//	    fields_et_log.put("SYSTEM_ID", 6);
//	    fields_et_log.put("SIFRA", 203);
//	    fields_et_log.put("DODATNI_OPIS", "ODSTAMPANA KARTA BR : 2204700003");
//	    
//		HttpResponse<String> jsonResponse_et_log 
//	      = Unirest.post("https://testekarta.srbvoz.rs/etest21/api/KartomatSV/etLog")
//	    		  .header("accept", "application/json").header("Content-Type", "application/x-www-form-urlencoded")
//	      .fields(fields_et_log)
//	      .asString();
//		System.out.println(jsonResponse_et_log.getBody());
		
		
	//////////////////////////////////////////upis u KT_response//////////////////////////////////////////////////////
		
//		Map<String, Object> fields_kt_response = new HashMap<>();
//		
//
//		fields_kt_response.put("identifier", "10");
//		fields_kt_response.put("terminalId", 132);
//		fields_kt_response.put("sourceId", "SVKOID0000030");
//		fields_kt_response.put("sequentialNumber", "0000");  
//		fields_kt_response.put("transactionType", "01");
//		fields_kt_response.put("transactionFlag", "01");
//		fields_kt_response.put("transactionNumber", "000423");
//		fields_kt_response.put("batchNumber", "0046");
//		fields_kt_response.put("transactionAmount1", "000000010000");
//		fields_kt_response.put("amountExponent", "+0");
//		fields_kt_response.put("amountCurrency", "941");
//		fields_kt_response.put("cardDataSource", "C");
//		fields_kt_response.put("cardNumber", "9999999999998449");
//		fields_kt_response.put("expirationData", "9999");
//		fields_kt_response.put("authorizationCode", "973455");
//		fields_kt_response.put("tidNumber", "JH656922");
//		fields_kt_response.put("midNumber", "UCF60058");
//		fields_kt_response.put("companyName", "MASTERCARD");
//		fields_kt_response.put("displayMessage", "ODOBRENO");
//		fields_kt_response.put("inputData", "");
//		fields_kt_response.put("emvData", "5F280206885F2A0209415F340101820219808407A00000000410108701018A0200008E0E000000000000000042031E031F038F0105");
//		fields_kt_response.put("signatureLinePrintFlag", "0");
//		fields_kt_response.put("transactionStatus", "C1");
//
//
//		
//	    
//		HttpResponse<String> jsonResponse_karta 
//	      = Unirest.post("https://testekarta.srbvoz.rs/etest21/api/KartomatSV/KT_ET_Response")
//	    		  .header("accept", "application/json").header("Content-Type", "application/x-www-form-urlencoded")
//	      .fields(fields_kt_response)
//	      .asString();
//		System.out.println(jsonResponse_karta.getBody());
		
	}

}
