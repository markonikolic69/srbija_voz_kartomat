package application.https.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;

import com.google.gson.Gson;

import application.data.CenaBean;
import application.data.EtKartaBean;
import application.data.FrekventneStaniceBean;
import application.data.KartaBean;
import application.data.KartomatBean;
import application.data.LegitimacijaBean;
import application.data.PovlasticaBean;
import application.data.PutniciBean;
import application.data.SifraBean;
import application.data.StanicaIDBean;
import application.data.StanicaNaTrasiBean;
import application.data.TokenBean;
import application.data.TokenRequestBean;
import application.data.VozBean;
import application.data.VozDataBean;
import application.https.CommunicationException;
import application.https.ETKartaRequestBean;
import application.https.ETKartaResponseBean;
import application.https.EtKartaResponse;
import application.https.SrbijaVozIface;
import application.https.SrbijaVozIfaceFactory;
import application.util.HttpParametersBuilder;
import application.payment.impl.models.TransactionDataResponse;

import com.google.gson.internal.LinkedTreeMap;

public class SrbijaVozIfaceImpl implements SrbijaVozIface {
	
	
	private String _url = "";
	private int _connection_timeout = 0;
	private int _read_timeout = 0;
	
	private static final int SIF_POV_JEDAN_SMER = 1;
	private static final int SIF_POV_POVRATNA = 20;
	private static final int SIF_POV_PAS = 54;
	private static final int SIF_POV_DETE = 50;
	private static final int SIF_SRB_PLUS_K_13 = 37;
	private static final int SIF_RAIL_PLUS_K_30 = 38;
	
	public SrbijaVozIfaceImpl(String url, int connection_timeout_in_seconds, int read_timeout_in_seconds) {
		_url = url;
		_connection_timeout = connection_timeout_in_seconds * 1000;
		_read_timeout = read_timeout_in_seconds * 1000;
	}
	
	@SuppressWarnings("unchecked")
	public List<StanicaIDBean> getStanicaIDs() throws CommunicationException{
		List<StanicaIDBean> to_return = new ArrayList<StanicaIDBean>();
		String sub_path = "stanica";
		String row_response =  HttpsClient.getUrl(_url + sub_path) ;
		Gson gson = new Gson();
		List outputList = gson.fromJson(row_response, ArrayList.class);
		
		for(Object current : outputList) {
			to_return.add(new StanicaIDBean((LinkedTreeMap)current));
		}
		
		return to_return;
	}
	
	@SuppressWarnings("unchecked")
	public List<VozBean> getListaVozovaNaTrasi(int sifra_stanice_od, int sifra_stanice_do, String datum, int broj_putnika, int razred) throws CommunicationException{
		List<VozBean> to_return = new ArrayList<VozBean>();
		String sub_path = "ListaVozova/ListaVozovaKartomat";//ov je path od starog URL-a "listavozova";
		//novi url izgleda ovako https://webapi1.srbvoz.rs/eKarta/api/ListaVozova/ListaVozovaKartomat?stanicaod=16052&stanicado=16808&datum=3-19-2022&brojputnika=1&razred=2
		sub_path = sub_path + "?";
		sub_path = sub_path + "stanicaod=" + sifra_stanice_od + "&";
		sub_path = sub_path + "stanicado=" + sifra_stanice_do + "&";
		sub_path = sub_path + "datum=" + format_datum(datum)  + "&";
		sub_path = sub_path + "brojputnika="+broj_putnika+"&";
		sub_path = sub_path + "razred="+razred;
		
		System.out.println("getListaVozovaNaTrasi URL = " + _url + sub_path);
		
		String row_response =  HttpsClient.getUrl(_url + sub_path) ;
		Gson gson = new Gson();
		List outputList = gson.fromJson(row_response, ArrayList.class);
		
		for(Object current : outputList) {
			to_return.add(new VozBean((LinkedTreeMap)current));
		}
		
		return to_return;
	}
	

	public CenaBean getCenaPovratna(int broj_putnika,  int kilometraza, int razred_odlazak,  int rang_odlazak, int razred_povratak,  int rang_povratak) throws CommunicationException{
		
		return getCenaPovratnaZaTipPutnika(broj_putnika,  kilometraza, razred_odlazak,  rang_odlazak, razred_povratak, rang_povratak, 
				SIF_POV_POVRATNA);
		
	}
	

	public CenaBean getCenaPovratnaPSE(int broj_putnika,  int kilometraza, int razred_odlazak,  int rang_odlazak, int razred_povratak,  int rang_povratak) 
			throws CommunicationException{
		return getCenaPovratnaZaTipPutnika(broj_putnika,  kilometraza, razred_odlazak,  rang_odlazak, razred_povratak, rang_povratak, 
				SIF_POV_PAS);
	}
	
	public CenaBean getCenaPovratnaDETE(int broj_putnika,  int kilometraza, int razred_odlazak,  int rang_odlazak, int razred_povratak,  int rang_povratak) throws CommunicationException{
		return getCenaPovratnaZaTipPutnika(broj_putnika,  kilometraza, razred_odlazak,  rang_odlazak, razred_povratak, rang_povratak, 
				SIF_POV_DETE);
	}

	
	public CenaBean getCenaPovratnaSRB_PLUS_K_13(int broj_putnika,  int kilometraza, int razred_odlazak,  int rang_odlazak, int razred_povratak,  int rang_povratak) throws CommunicationException{
		return getCenaPovratnaZaTipPutnika(broj_putnika,  kilometraza, razred_odlazak,  rang_odlazak, razred_povratak, rang_povratak, 
				SIF_SRB_PLUS_K_13);
	}
	
	public CenaBean getCenaPovratnaRAIL_PLUS_K_30(int broj_putnika,  int kilometraza, int razred_odlazak,  int rang_odlazak, int razred_povratak,  int rang_povratak) throws CommunicationException{
		return getCenaPovratnaZaTipPutnika(broj_putnika,  kilometraza, razred_odlazak,  rang_odlazak, razred_povratak, rang_povratak, 
				SIF_RAIL_PLUS_K_30);
	}
	
	
	
	private CenaBean getCenaPovratnaZaTipPutnika(int broj_putnika,  int kilometraza, int razred_odlazak,  int rang_odlazak, int razred_povratak,  int rang_povratak, 
			int sifra_povlastice) 
			throws CommunicationException{
		List<CenaBean> to_return = new ArrayList<CenaBean>();
		String sub_path = "Cene/CenaZaKartomatAR";//stari path do cena "cene";
		//https://webapi1.srbvoz.rs/eKarta/api/Cene/CenaZaKartomatAR?brojputnika=1&km=89&sifpov=20&rango=4&rangp=4&razredo=2&razredp=2
		sub_path = sub_path + "?";
		sub_path = sub_path + "brojputnika=" + broj_putnika + "&";
		sub_path = sub_path + "km=" + kilometraza + "&";
		sub_path = sub_path + "sifpov=" + sifra_povlastice  + "&";
		sub_path = sub_path + "rango="+ rang_odlazak + "&";
		sub_path = sub_path + "rangp="+ rang_povratak + "&";
		sub_path = sub_path + "razredo="+ razred_odlazak + "&";
		sub_path = sub_path + "razredp="+ razred_povratak ;

		System.out.println("url za povratne = " + _url + sub_path);
		
		String row_response =  HttpsClient.getUrl(_url + sub_path) ;
		Gson gson = new Gson();
		List outputList = gson.fromJson(row_response, ArrayList.class);
		
		for(Object current : outputList) {
			to_return.add(new CenaBean((LinkedTreeMap)current));
		}
		
		return to_return.size() > 0 ? to_return.get(0) : null;
	}
	
	

	public CenaBean getCenaJedanSmerPSE(int broj_putnika, int razred, int kilometraza,  int rang) throws CommunicationException{
		return getCenaJedanSmerZaTipPutnika(broj_putnika, razred, kilometraza, rang, SIF_POV_PAS);
	}
	
	

	public CenaBean getCenaJedanSmer(int broj_putnika, int razred, int kilometraza,  int rang) throws CommunicationException{
		return getCenaJedanSmerZaTipPutnika(broj_putnika, razred, kilometraza, rang, SIF_POV_JEDAN_SMER);
	}


	public CenaBean getCenaJedanSmerDETE(int broj_putnika, int razred, int kilometraza,  int rang) throws CommunicationException{
		return getCenaJedanSmerZaTipPutnika(broj_putnika, razred, kilometraza, rang, SIF_POV_DETE);
	}
	

	public CenaBean getCenaJedanSmerSRB_PLUS_K_13(int broj_putnika, int razred, int kilometraza,  int rang) throws CommunicationException{
		return getCenaJedanSmerZaTipPutnika(broj_putnika, razred, kilometraza, rang, SIF_SRB_PLUS_K_13);
	}
	

	public CenaBean getCenaJedanSmerRAIL_PLUS_K_30(int broj_putnika, int razred, int kilometraza,  int rang) throws CommunicationException{
		return getCenaJedanSmerZaTipPutnika(broj_putnika, razred, kilometraza, rang, SIF_RAIL_PLUS_K_30);
	}
	
	private CenaBean getCenaJedanSmerZaTipPutnika(int broj_putnika, int razred, int kilometraza,  int rang, int sifra_povlastice) throws CommunicationException{
		List<CenaBean> to_return = new ArrayList<CenaBean>();
		String sub_path = "cene/CenaZaKartomat";//stari path "cene";
		//https://webapi1.srbvoz.rs/eKarta/api/cene/CenaZaKartomat?brojputnika=1&km=89&sifpov=1&rang=4&razred=2
		sub_path = sub_path + "?";
		sub_path = sub_path + "brojputnika=" + broj_putnika + "&";
		sub_path = sub_path + "km=" + kilometraza + "&";
		sub_path = sub_path + "sifpov=" + sifra_povlastice  + "&";
		sub_path = sub_path + "rang="+ rang + "&";

		sub_path = sub_path + "razred="+ razred ;

		String url_full = _url + sub_path;
		System.out.println("getCenaJedanSmerZaTipPutnika, url_full = " + url_full);
		String row_response =  HttpsClient.getUrl(url_full) ;
		Gson gson = new Gson();
		List outputList = gson.fromJson(row_response, ArrayList.class);
		
		for(Object current : outputList) {
			to_return.add(new CenaBean((LinkedTreeMap)current));
		}
		
		return to_return.size() > 0 ? to_return.get(0) : null;
	}
	
	@SuppressWarnings("unchecked")
	public List<PovlasticaBean> getPovlastice(int smer/*1 ili 2*/) throws CommunicationException{
		List<PovlasticaBean> to_return = new ArrayList<PovlasticaBean>();
		String sub_path = "povlastice";
		sub_path = sub_path + "?";


		sub_path = sub_path + "smer="+ smer ;

		
		String row_response =  HttpsClient.getUrl(_url + sub_path) ;
		Gson gson = new Gson();
		List outputList = gson.fromJson(row_response, ArrayList.class);
		
		for(Object current : outputList) {
			to_return.add(new PovlasticaBean((LinkedTreeMap)current));
		}
		
		return to_return;
		
	}
	
	@SuppressWarnings("unchecked")
	public boolean provera_legitimacije(int broj_legitimacije, int povlastica_id) throws CommunicationException{
		String sub_path = "povlastice";
		sub_path = sub_path + "?";
		sub_path = sub_path + "brleg=" + broj_legitimacije + "&";

		sub_path = sub_path + "pov="+ povlastica_id ;

		
		String row_response =  HttpsClient.getUrl(_url + sub_path) ;
		Gson gson = new Gson();
		List legitimacija_list = gson.fromJson(row_response, ArrayList.class);
		if(legitimacija_list.isEmpty()) {
			return false;
		}else {
			LegitimacijaBean legitimacija = new LegitimacijaBean((LinkedTreeMap)legitimacija_list.get(0));
			int vazido = Integer.parseInt(legitimacija.getVazI_DO().substring(0,4) + legitimacija.getVazI_DO().substring(5,7) + legitimacija.getVazI_DO().substring(8,10));
			int danas = Integer.parseInt(new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date()));
			return danas <= vazido;
		}
	}
	
	public TokenBean getToken(String username, String password) throws CommunicationException{
		
		String sub_path = "token";
		TokenRequestBean request = new TokenRequestBean();
		request.setUsername(username);
		request.setPassword(password);
		Gson gson = new Gson();
		String row_response =  HttpsClient.postUrl(_url + sub_path, gson.toJson(request)) ;
		
		Object container = gson.fromJson(row_response, TokenBean.class);
		
		return new TokenBean((LinkedTreeMap)container);
	}
	
	@SuppressWarnings("unchecked")
	public List<FrekventneStaniceBean> getFrekventneStanice(String mak_adresa) throws CommunicationException{
		List<FrekventneStaniceBean> to_return = new ArrayList<FrekventneStaniceBean>();
		String sub_path = "KartomatSV/KT_Vrati_FrekfentneST";
		sub_path = sub_path + "?";


		sub_path = sub_path + "MakAdresa="+ mak_adresa ;

		
		String row_response =  HttpsClient.getUrl(_url + sub_path) ;
		Gson gson = new Gson();
		List outputList = gson.fromJson(row_response, ArrayList.class);
		
		for(Object current : outputList) {
			to_return.add(new FrekventneStaniceBean((LinkedTreeMap)current));
		}
		
		return to_return;
	}
	

	public KartomatBean getKartomat(String mac_address) throws CommunicationException{
		String sub_path = "KartomatSV/KT_Vrati_Terminal";
		sub_path = sub_path + "?";


		sub_path = sub_path + "mac_address="+ mac_address ;
		String url_full = _url + sub_path;
		System.out.println("getKartomat URL = " + url_full);
		String row_response =  HttpsClient.getUrl(url_full) ;
		Gson gson = new Gson();
		List kartomati = gson.fromJson(row_response, ArrayList.class);
		if(kartomati.size() > 0) {
			LinkedTreeMap container =  (LinkedTreeMap)kartomati.get(0);
			return new KartomatBean(container);

		}else {
			return null;
		}
		
		
	}
	
	public ETKartaResponseBean upisET_ORDER(int userID, String datum_kupovine, int ID_RELACIJE, int stanica_od, int stanica_do,
			int smer, String via, String vazi_od, String vazi_do, int voz_a, int razred_a, String vreme_polaska_a, String vreme_dolaska_a,
			String voz_r, String razred_r, String vreme_polaska_r, String vreme_dolaska_r, int broj_putnika, double ukupna_cena, 
			EtKartaBean et_karta_data) throws CommunicationException{
		
		String sub_path = "KartomatSV/KT_ET_Order";
		 Map<String, Object> fields = new HashMap<>();
		    fields.put("ID_USER", userID);
		    fields.put("HASH", "");
		    fields.put("DATUM_KUPOVINE", datum_kupovine);
		    fields.put("ID_RELACIJE", ID_RELACIJE);  
		    fields.put("OD", stanica_od);
		    fields.put("DO", stanica_do);
		    fields.put("SMER", smer);
		    fields.put("VIA", via);
		    fields.put("VAZI_OD", vazi_od);
		    fields.put("VAZI_DO", vazi_do);
		    fields.put("VOZA", voz_a);
		    fields.put("RAZREDA", razred_a);
		    fields.put("VREME_POLASKAA", vreme_polaska_a);
		    fields.put("VREME_DOLASKAA", vreme_dolaska_a);
		    fields.put("VOZR", voz_r/*moze null*/);
		    fields.put("RAZREDR", razred_r/*moze null*/);
		    fields.put("VREME_POLASKAR", vreme_polaska_r/*moze null*/);
		    fields.put("VREME_DOLASKAR", vreme_dolaska_r/*moze null*/);
		    fields.put("BROJ_PUTNIKA", broj_putnika);
		    fields.put("KURS_DINARA_ZA_EVRO", 0.0);
		    fields.put("UKUPNA_CENA", ukupna_cena);
		    fields.put("PLACENA", 0);
		    fields.put("DODATNI_OPIS", "");
		    Gson gson = new Gson();
		    String et_karta_data_str = gson.toJson(et_karta_data);
		    System.out.println("ID_USER = " + userID + ", " +
		    		"DATUM_KUPOVINE = " + datum_kupovine + ", " + 
		    		"ID_RELACIJE = " + ID_RELACIJE + ", " +
		    		"OD = " + stanica_od + ", " +
		    		"DO = " + stanica_do + ", " +
		    		"SMER = " + smer + ", " +
		    		"VAZI_OD = " + vazi_od + ", " +
		    		"VAZI_DO = " + vazi_do + ", " +
		    		"VOZA = " + voz_a + ", " +
		    		"RAZREDA = " + razred_a + ", " +
		    		"VOZR = " + voz_r + ", " +
		    		"RAZREDR = " + razred_r + ", " +
		    		"BROJ_PUTNIKA = " + broj_putnika + ", "+
		    		"UKUPNA_CENA = " + ukupna_cena + ", "
		    		
		    		);
		    System.out.println("et_karta_data_str sent = " + et_karta_data_str);
		    fields.put("ETKARTADATA", et_karta_data_str);
		    //fields.put("ETKARTADATA", "{\"vozData\":{\"datum1\":\"2021-12-14T08:08:14.358Z\",\"brojputnika\":1,\"razred\":\"2\",\"voz\":{\"odsifra\":16054,\"dosifra\":21001,\"idvoz\":1036,\"brvoz\":2607,\"nazivvoz\":\"                    \",\"rang\":6,\"vremep\":\"15:38\",\"vremed\":\"16:04             \",\"nazivod\":\"Vukov spomenik\",\"nazivdo\":\"Pancevo varos\",\"idrel\":175483,\"relkm\":21,\"ponuda\":\"Bv        \",\"cenau\":141,\"trajanje_putovanja\":\"00:26\",\"datum_dolaska\":\"2021-12-14T00:00:00\",\"etTrasaVoza\":[{\"iD_VOZ\":1036,\"rb\":1,\"sifrA_STANICE\":16052,\"vremE_DOLASKA\":\"15:30             \",\"vremE_POLASKA\":\"15:30             \",\"naziV_STANICE\":\"Beograd centar\"},{\"iD_VOZ\":1036,\"rb\":2,\"sifrA_STANICE\":16053,\"vremE_DOLASKA\":\"15:33             \",\"vremE_POLASKA\":\"15:34             \",\"naziV_STANICE\":\"Karadjordjev park\"},{\"iD_VOZ\":1036,\"rb\":3,\"sifrA_STANICE\":16054,\"vremE_DOLASKA\":\"15:37             \",\"vremE_POLASKA\":\"15:38             \",\"naziV_STANICE\":\"Vukov spomenik\"},{\"iD_VOZ\":1036,\"rb\":4,\"sifrA_STANICE\":16013,\"vremE_DOLASKA\":\"15:41             \",\"vremE_POLASKA\":\"15:42             \",\"naziV_STANICE\":\"Pancevacki most\"},{\"iD_VOZ\":1036,\"rb\":5,\"sifrA_STANICE\":22001,\"vremE_DOLASKA\":\"15:59             \",\"vremE_POLASKA\":\"16:00             \",\"naziV_STANICE\":\"Pancevo glavna stani\"},{\"iD_VOZ\":1036,\"rb\":6,\"sifrA_STANICE\":21001,\"vremE_DOLASKA\":\"16:04             \",\"vremE_POLASKA\":\"16:05             \",\"naziV_STANICE\":\"Pancevo varos\"},{\"iD_VOZ\":1036,\"rb\":7,\"sifrA_STANICE\":21002,\"vremE_DOLASKA\":\"16:18             \",\"vremE_POLASKA\":\"16:19             \",\"naziV_STANICE\":\"Banatsko Novo Selo\"},{\"iD_VOZ\":1036,\"rb\":8,\"sifrA_STANICE\":21003,\"vremE_DOLASKA\":\"16:29             \",\"vremE_POLASKA\":\"16:30             \",\"naziV_STANICE\":\"Vladimirovac\"},{\"iD_VOZ\":1036,\"rb\":9,\"sifrA_STANICE\":21004,\"vremE_DOLASKA\":\"16:38             \",\"vremE_POLASKA\":\"16:39             \",\"naziV_STANICE\":\"Alibunar\"},{\"iD_VOZ\":1036,\"rb\":10,\"sifrA_STANICE\":21005,\"vremE_DOLASKA\":\"16:45             \",\"vremE_POLASKA\":\"16:46             \",\"naziV_STANICE\":\"Banatski Karlovac\"},{\"iD_VOZ\":1036,\"rb\":11,\"sifrA_STANICE\":21006,\"vremE_DOLASKA\":\"16:50             \",\"vremE_POLASKA\":\"16:51             \",\"naziV_STANICE\":\"Nikolinci\"},{\"iD_VOZ\":1036,\"rb\":12,\"sifrA_STANICE\":21007,\"vremE_DOLASKA\":\"16:57             \",\"vremE_POLASKA\":\"16:58             \",\"naziV_STANICE\":\"Uljma\"},{\"iD_VOZ\":1036,\"rb\":13,\"sifrA_STANICE\":21008,\"vremE_DOLASKA\":\"17:03             \",\"vremE_POLASKA\":\"17:04             \",\"naziV_STANICE\":\"Vlajkovac\"},{\"iD_VOZ\":1036,\"rb\":14,\"sifrA_STANICE\":21009,\"vremE_DOLASKA\":\"17:12             \",\"vremE_POLASKA\":\"17:12             \",\"naziV_STANICE\":\"Vrsac\"}]},\"vozp\":{\"odsifra\":21001,\"dosifra\":16054,\"idvoz\":1387,\"brvoz\":22610,\"nazivvoz\":\"                    \",\"rang\":6,\"vremep\":\"20:06\",\"vremed\":\"20:33             \",\"nazivod\":\"Pancevo varos\",\"nazivdo\":\"Vukov spomenik\",\"idrel\":178662,\"relkm\":21,\"ponuda\":\"Bv        \",\"cenau\":141,\"trajanje_putovanja\":\"00:27\",\"datum_dolaska\":\"2021-12-14T00:00:00\",\"etTrasaVoza\":[{\"iD_VOZ\":1387,\"rb\":1,\"sifrA_STANICE\":21009,\"vremE_DOLASKA\":\"18:58             \",\"vremE_POLASKA\":\"18:58             \",\"naziV_STANICE\":\"Vrsac\"},{\"iD_VOZ\":1387,\"rb\":2,\"sifrA_STANICE\":21008,\"vremE_DOLASKA\":\"19:06             \",\"vremE_POLASKA\":\"19:07             \",\"naziV_STANICE\":\"Vlajkovac\"},{\"iD_VOZ\":1387,\"rb\":3,\"sifrA_STANICE\":21007,\"vremE_DOLASKA\":\"19:12             \",\"vremE_POLASKA\":\"19:13             \",\"naziV_STANICE\":\"Uljma\"},{\"iD_VOZ\":1387,\"rb\":4,\"sifrA_STANICE\":21006,\"vremE_DOLASKA\":\"19:19             \",\"vremE_POLASKA\":\"19:20             \",\"naziV_STANICE\":\"Nikolinci\"},{\"iD_VOZ\":1387,\"rb\":5,\"sifrA_STANICE\":21005,\"vremE_DOLASKA\":\"19:24             \",\"vremE_POLASKA\":\"19:25             \",\"naziV_STANICE\":\"Banatski Karlovac\"},{\"iD_VOZ\":1387,\"rb\":6,\"sifrA_STANICE\":21004,\"vremE_DOLASKA\":\"19:31             \",\"vremE_POLASKA\":\"19:32             \",\"naziV_STANICE\":\"Alibunar\"},{\"iD_VOZ\":1387,\"rb\":7,\"sifrA_STANICE\":21003,\"vremE_DOLASKA\":\"19:40             \",\"vremE_POLASKA\":\"19:41             \",\"naziV_STANICE\":\"Vladimirovac\"},{\"iD_VOZ\":1387,\"rb\":8,\"sifrA_STANICE\":21002,\"vremE_DOLASKA\":\"19:50             \",\"vremE_POLASKA\":\"19:51             \",\"naziV_STANICE\":\"Banatsko Novo Selo\"},{\"iD_VOZ\":1387,\"rb\":9,\"sifrA_STANICE\":21001,\"vremE_DOLASKA\":\"20:05             \",\"vremE_POLASKA\":\"20:06             \",\"naziV_STANICE\":\"Pancevo varos\"},{\"iD_VOZ\":1387,\"rb\":10,\"sifrA_STANICE\":22001,\"vremE_DOLASKA\":\"20:10             \",\"vremE_POLASKA\":\"20:12             \",\"naziV_STANICE\":\"Pancevo glavna stani\"},{\"iD_VOZ\":1387,\"rb\":11,\"sifrA_STANICE\":16013,\"vremE_DOLASKA\":\"20:29             \",\"vremE_POLASKA\":\"20:30             \",\"naziV_STANICE\":\"Pancevacki most\"},{\"iD_VOZ\":1387,\"rb\":12,\"sifrA_STANICE\":16054,\"vremE_DOLASKA\":\"20:33             \",\"vremE_POLASKA\":\"20:34             \",\"naziV_STANICE\":\"Vukov spomenik\"},{\"iD_VOZ\":1387,\"rb\":13,\"sifrA_STANICE\":16053,\"vremE_DOLASKA\":\"20:36             \",\"vremE_POLASKA\":\"20:37             \",\"naziV_STANICE\":\"Karadjordjev park\"},{\"iD_VOZ\":1387,\"rb\":14,\"sifrA_STANICE\":16052,\"vremE_DOLASKA\":\"20:39             \",\"vremE_POLASKA\":\"20:39             \",\"naziV_STANICE\":\"Beograd centar\"}]},\"datum2\":\"2021-12-14T08:08:56.727Z\",\"razredp\":\"2\",\"cenauk\":226},\"cenasmer1\":[],\"cenasmer2\":[[{\"cenau\":198,\"cenao\":156,\"cenad\":42,\"cenab\":0,\"cenar\":0,\"tiprez\":0}]],\"sifraod\":{\"sifra\":16054,\"naziv\":\"Vukov spomenik\"},\"sifrado\":{\"sifra\":21001,\"naziv\":\"Pancevo varos\"},\"smer\":\"2\",\"ukupnacena\":198,\"putnici\":[{\"imeprezime\":\"ALEKSANDAR BABIC\",\"datumrodjenja\":\"10-09-1997\",\"sifrapovlastice\":37,\"idleg\":\"0049116\",\"cenap\":[{\"cenau\":198,\"cenao\":156,\"cenad\":42,\"cenab\":0,\"cenar\":0,\"tiprez\":0}]}],\"uneseneleg\":[\"0049116\"]}");

		String row_response =  HttpsClient.postUrl(_url + sub_path, fields) ;

		System.out.println("row_response = " + row_response);
		if(row_response.contains("error")) {
			String[] splt = row_response.split(":");
			String message = "Nepoznata greška: pokušajte ponovo";
			if(splt.length > 2) {
				message = splt[2].substring(1, splt[2].length() - 2);
			}

			throw new CommunicationException(message);
		}
		Object container = gson.fromJson(row_response, ETKartaResponseBean.class);
		
		return (ETKartaResponseBean)container;
		//return new ETKartaResponseBean((LinkedTreeMap)container);
		
	}
	
	public EtKartaResponse upisUPIT_KA_BANCI(boolean is_pozitivan, int userID, String orderID, double cena, TransactionDataResponse payment_transaction_data) throws CommunicationException{
		
		String sub_path = "KartomatSV/KT_ET_Response";
		
		
		List<KartaBean> to_return = new ArrayList<KartaBean>();
		
		Map<String, Object> fields = new HashMap<>();
		

		fields.put("identifier", payment_transaction_data.get_identifier());
		fields.put("terminalId", userID);
		fields.put("sourceId", orderID);
		fields.put("sequentialNumber", payment_transaction_data.get_sequentialNumber());  
		fields.put("transactionType", payment_transaction_data.get_transactionType());
		fields.put("transactionFlag", is_pozitivan ? "01" : "04");
		fields.put("transactionNumber", payment_transaction_data.get_transactionNumber());
		fields.put("batchNumber", payment_transaction_data.get_batchNumber());
		fields.put("transactionAmount1", String.format("%012d", (int)cena*100));
		fields.put("amountExponent", payment_transaction_data.get_amountExponent());
		fields.put("amountCurrency", payment_transaction_data.get_amountCurrency());
		fields.put("cardDataSource", payment_transaction_data.get_cardDataSource());
		fields.put("cardNumber", payment_transaction_data.get_cardNumber());
		fields.put("expirationData", payment_transaction_data.get_expirationData());
		fields.put("authorizationCode", payment_transaction_data.get_authorizationCode());
		fields.put("tidNumber", payment_transaction_data.get_tidNumber());
		fields.put("midNumber", payment_transaction_data.get_midNumber());
		fields.put("companyName", payment_transaction_data.get_companyName());
		fields.put("displayMessage", payment_transaction_data.get_displayMessage());
		fields.put("inputData", payment_transaction_data.get_inputDada());
		fields.put("emvData", payment_transaction_data.get_emvData());
		fields.put("signatureLinePrintFlag", payment_transaction_data.get_signatureLinePrintFlag());
		fields.put("transactionStatus", payment_transaction_data.get_transactionStatus());
		
		String row_response =  HttpsClient.postUrl(_url + sub_path, fields) ;
		String row_response_replace = row_response.replaceAll("\\\\", "");
//		String[] splt = row_response_replace.split("\\[");
//		String strr_splt_first = splt[0] + splt[1].substring(1);
//		System.out.println("strr_splt_first = " + strr_splt_first);
		System.out.println("row_response = " + row_response);
		System.out.println("row_response_replace = " + row_response_replace);
		String row_response_replace_opet = row_response_replace.replaceAll("\"\\[", "\\[");
		String row_response_replace_opet_opet = row_response_replace_opet.replaceAll("\\]\"", "\\]");
		
		System.out.println("row_response_replace_opet_opet = " + row_response_replace_opet_opet);
		Gson gson = new Gson();
		Object container = gson.fromJson(row_response_replace_opet_opet, EtKartaResponse.class);
		return (EtKartaResponse)container;
		//return new EtKartaResponse((LinkedTreeMap)container);
		
	}

	public void upisKARTA_ODSTAMPANA(String userID, String broj_karte) throws CommunicationException{
		String sub_path = "KartomatSV/etLog";
		
		
	    Map<String, Object> fields = new HashMap<>();
	    fields.put("ID_USER", userID);
	    fields.put("SYSTEM_ID", 6);
	    fields.put("SIFRA", 203);
	    fields.put("DODATNI_OPIS", "ODSTAMPANA KARTA BR : " + broj_karte);
		
		

		
		System.out.println("saljemo = " + fields);
		System.out.println("URL = " + _url + sub_path);


		String row_response =  HttpsClient.postUrl(_url + sub_path, fields) ;
		
		System.out.println("row_response = " + row_response);

	}
	
	
	
	
	
	private String format_datum(String datum) {
		System.out.println("format_datum in SrbijaVozIfaceImpl, datum = " + datum);
		String separator = datum.contains(".") ? "\\.": "-";
		String[] ddMMyyyy = datum.split(separator);
		return "" + Integer.parseInt(ddMMyyyy[1]) + "/" + Integer.parseInt(ddMMyyyy[0]) + "/" + ddMMyyyy[2];
	}
	
	
	public static void main(String[] args) throws Exception{
//		System.out.println("stanice : " + SrbijaVozIfaceFactory.getIfaceTest().getStanicaIDs());
//		System.out.println("lista vozova : " + SrbijaVozIfaceFactory.getIfaceTest().getListaVozovaNaTrasi(16052, 22001, "09.09.2021", 1, 2));
//		System.out.println("cena jedan smer : " + SrbijaVozIfaceFactory.getIfaceTest().getCenaJedanSmer(1, 2, 18, 6));
//		System.out.println("cena povratna : " + SrbijaVozIfaceFactory.getIfaceTest().getCenaPovratna(1, 18, 2, 6, 2, 6));
//		System.out.println("povlastice jedan smer : " + SrbijaVozIfaceFactory.getIfaceTest().getPovlastice(1));
//		System.out.println("povlastice povratna : " + SrbijaVozIfaceFactory.getIfaceTest().getPovlastice(2));
//		System.out.println("provera legitimacije : " + SrbijaVozIfaceFactory.getIfaceTest().provera_legitimacije(52910, 37));
//		System.out.println("kartomat frekventne stanice : " + SrbijaVozIfaceFactory.getIfaceTest().getFrekventneStanice("E8-38-73-6C-73-69-4C-90-8F-73-E8-3E-5F-02-43-D8"));
		
		
		System.out.println("kartomat : " + SrbijaVozIfaceFactory.getIfaceProduction().getKartomat("BC-BA-D9-17-51-90-B6-33-5D-D8-90-55-BD-A5-3F-13"));
		
		
		
//		//user_id = 132
//		//sifra_stanice_A = 16052
//		//naziV_STANICE":"Beograd centar
//		//iD_TERMINALA":1605201
//		////////////////////////////////////////////////////////////////za et karta bean///////////////////////////////////////////
//		EtKartaBean et_karta_data = new EtKartaBean();
//		List<String> uneseneleg = new ArrayList<String>();
//		uneseneleg.add("0049116");
//		et_karta_data.setUneseneleg(uneseneleg);
//		
//		PutniciBean putnici = new PutniciBean();
//		putnici.setImeprezime("1605201");//id terminala
//		putnici.setDatumrodjenja("");
//		putnici.setIdleg("");
//		putnici.setSifrapovlastice(0);
//		CenaBean cena_putnici = new CenaBean();
//		cena_putnici.setCenau(198);
//		cena_putnici.setCenao(198);
//		cena_putnici.setCenab(0);
//		cena_putnici.setCenad(0);
//		cena_putnici.setCenar(0);
//		cena_putnici.setTiprez(0);
//
//		List<CenaBean> cenap = new ArrayList<CenaBean>();
//		cenap.add(cena_putnici);
//		putnici.setCenaa(cenap);
//		List<PutniciBean> putnici_list = new ArrayList<PutniciBean>();
//		putnici_list.add(putnici);
//		et_karta_data.setPutnici(putnici_list);
//		
//		
//		SifraBean sifraod = new SifraBean(); 
//		sifraod.setNaziv("Beograd centar");
//		sifraod.setSifra(16052);
//		et_karta_data.setSifraod(sifraod);
//		
//		SifraBean sifrado = new SifraBean(); 
//		sifrado.setNaziv("Pancevo varos");
//		sifrado.setSifra(21001);
//		et_karta_data.setSifrado(sifrado);
//		CenaBean cenasmer1=new CenaBean();
//		cenasmer1.setCenau(198);
//		cenasmer1.setCenao(198);
//		cenasmer1.setCenab(0);
//		cenasmer1.setCenad(0);
//		cenasmer1.setCenar(0);
//		cenasmer1.setTiprez(0);
//		List<CenaBean> cenasmer1_list = new ArrayList<CenaBean>();
//		cenasmer1_list.add(cenasmer1);
//		et_karta_data.setCenasmer1(new ArrayList<CenaBean>());
//		et_karta_data.setCenasmer2(cenasmer1_list);
//		
//		
//		VozDataBean vozData = new VozDataBean();
//		
//		vozData.setDatum1("2021-12-14T08:08:14.358Z");
//		vozData.setBrojputnika(1);
//		vozData.setRazred(2);
//		vozData.setDatum2("2021-12-14T08:08:56.727Z");
//		vozData.setRazredp(2);
//		vozData.setCenauk(198);
//		
//		VozBean voz = new VozBean();
//		voz.setOdsifra(16052);
//		voz.setDosifra(21001);
//		voz.setIdvoz(1036);
//		voz.setBrvoz(2607);
//		voz.setNazivvoz("");
//		voz.setRang(6);
//		voz.setVremep("15:38");
//		voz.setVremed("16:04");
//		voz.setNazivod("Beograd centar");
//		voz.setNazivdo("Pancevo varos");
//		voz.setIdrel(175483);
//		voz.setRelkm(21);
//		voz.setPonuda("");
//		voz.setCenau(198);
//		voz.setTrajanje_putovanja("00:26");
//		voz.setDatum_dolaska("2021-12-14T00:00:00");
//
//
//
//		List<StanicaNaTrasiBean> etTrasaVoza = new ArrayList<StanicaNaTrasiBean>();
//		StanicaNaTrasiBean stn1 = new StanicaNaTrasiBean();
//		stn1.setiD_VOZ(1036);
//		stn1.setRb(1);
//		stn1.setSifrA_STANICE(16053);
//		stn1.setVremE_POLASKA("15:30");
//		stn1.setVremE_DOLASKA("15:30");
//		stn1.setNaziV_STANICE("Karadjordjev park");
//		etTrasaVoza.add(stn1);
//		voz.setEtTrasaVoza(etTrasaVoza);
//		
//
//		vozData.setVoz(voz);
//		
//		VozBean vozp = new VozBean();
//		
//		vozData.setVozp(vozp);
//		et_karta_data.setVozData(vozData);
//		et_karta_data.setUkupnacena(198);
//		et_karta_data.setSmer(1);
//		
//		SrbijaVozIfaceFactory.getIfaceTest().upisET_ORDER(132, "2022-02-11T00:00:00", 175483, 16052, 21001,
//				1, "1", "2022-02-12", "null", 2607, 2, "15:38", "16:04",
//				"0", "0", "", "", 1, (double)198, 
//				et_karta_data);
//		
//		
//		//////////////////////////////////////////////////////////sledeci poziv
//		
//		
//		TransactionDataResponse t_response = new TransactionDataResponse();
//		t_response.set_identifier("10");
//		t_response.set_terminalId("00");
//		t_response.set_sourceId("00");
//		t_response.set_sequentialNumber("0000");
//		t_response.set_transactionType("01");
//		t_response.set_transactionFlag("02");
//		t_response.set_transactionNumber("000423");
//		t_response.set_batchNumber("0046");
//		t_response.set_transactionDate("280122");
//		t_response.set_transactionTime("104537");
//		t_response.set_transactionAmount1("000000019800");
//		t_response.set_amountExponent("+0");
//		t_response.set_amountCurrency("941");
//		t_response.set_cardDataSource("C");
//		t_response.set_cardNumber("9999999999998449");
//		t_response.set_expirationData("9999");
//		t_response.set_authorizationCode("973455");
//		t_response.set_tidNumber("JH656922");
//		t_response.set_midNumber("UCF60058");
//		t_response.set_companyName("MASTERCARD");
//		t_response.set_displayMessage("ODOBRENO");
//		t_response.set_inputDada("");
//		t_response.set_emvData("5F280206885F2A0209415F340101820219808407A00000000410108701018A0200008E0E000000000000000042031E031F038F0105950500000080019A032110289B0200009C01009F02060000000100009F03060000000000009F0702FFC09F080200029F090200029F0D05B4508400009F0E0500000000009F0F05B4708480009F10120110A04303A20000000000000000000000FF9F120A4D6173746572436172649F130200009F1401009F1701009F1A0206889F2301009F260840AC94784ABC8E179F2701809F33036008089F34031F03029F360201829F3704DEC93DA89F40056000F050019F420209419F5301529F6604000000009F6C020000");
//		t_response.set_signatureLinePrintFlag("0");
//		t_response.set_transactionStatus("C1");
//		
//		
////		EtKartaResponse et_k_response = SrbijaVozIfaceFactory.getIfaceTest().upisUPIT_KA_BANCI(true,132, "SVKOID0000031", 198, t_response);
////		
////		System.out.println("et_k_response = " + et_k_response);
//		
//		
//		///////////////////////////////////////////////////////////novi poziv
//		
//		
////		SrbijaVozIfaceFactory.getIfaceTest().upisKARTA_ODSTAMPANA("132", "SV065000123") ;
		
	}


}
