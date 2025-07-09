package application;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import application.data.CenaBean;
import application.data.EtKartaBean;
import application.data.PutniciBean;
import application.data.PutniciBeanPovratna;
import application.data.PutnikIface;
import application.data.SifraBean;
import application.data.VozBean;
import application.data.VozDataBean;
import application.fiskalizacija.InvoiceRequestBean;
import application.fiskalizacija.InvoiceResponse;
import application.fiskalizacija.ItemBean;
import application.fiskalizacija.SrbijaVozInvoiceHandler;
import application.https.CommunicationException;
import application.https.ETKartaResponseBean;
import application.https.EtKartaResponse;
import application.https.KartaStampaBean;
import application.https.SrbijaVozIfaceFactory;
import application.payment.PayTenPaymentIface;
import application.payment.PaymentException;
import application.payment.PaymentFactory;
import application.payment.impl.models.TransactionDataResponse;
import application.transactionreport.*;
import application.util.PrinterService;


public class KartaPaymentControler implements Runnable{
	
	private IPaymentCallbackInfo _callback;
	
	private int _user_id = -1;
	private int _id_kartomata = -1;
	private String _naziv_stanice = "";
	
	private SimpleDateFormat _sdf_stampa = new SimpleDateFormat("dd-MM-yyyy");
	private SimpleDateFormat sat_min = new SimpleDateFormat("HH:mm");
	private static VozBean _selected_voz = null;
	private static VozBean _selected_voz_povratak = null;
	private int _selected_razred_polazak = -1;
	private int _selected_razred_povratak = -1;
	private int _broj_putnika = -1;
	private double _ukupna_cena = 0.0; 
	
	private static CenaBean _prvi_putnik_cena = null;
	private static CenaBean _drugi_putnik_cena = null;
	private static CenaBean _treci_putnik_cena = null;
	private static CenaBean _cetvrti_putnik_cena = null;
	private static CenaBean _peti_putnik_cena = null;
	
	private static CenaBean _prvi_putnik_povratna_cena = null;
	private static CenaBean _drugi_putnik_povratna_cena = null;
	private static CenaBean _treci_putnik_povratna_cena = null;
	private static CenaBean _cetvrti_putnik_povratna_cena = null;
	private static CenaBean _peti_putnik_povratna_cena = null;
	
	private int _prva_karta_tip = 0;
	private int _druga_karta_tip = 0;
	private int _treca_karta_tip = 0;
	private int _cetvrta_karta_tip = 0;
	private int _peta_karta_tip = 0;
	
	
	private String _prva_karta_tip_id_legitimacije = "";
	private String _druga_karta_tip_id_legitimacije = "";
	private String _treca_karta_tip_id_legitimacije = "";
	private String _cetvrta_karta_tip_id_legitimacije = "";
	private String _peta_karta_tip_id_legitimacije = "";
	
	
	private boolean uspesna_kupovina = false;
	
	private boolean  _is_petnaest_dana = false;
	
	private static final Logger logger = Logger.getLogger("KartaPaymentControler");
	
	private String _fiscal_journal = "";
	private String _fiscal_qr_code = "";
	
	List<String> _vk_amount = new ArrayList<String>();
	

	
	
	List putnici = null;
	
	private String _transaction_id;
	
	private String _transactionReportURL;
	
	
	public KartaPaymentControler(IPaymentCallbackInfo callback, int user_id, int id_kartomata, String naziv_stanice,
			VozBean selected_voz, VozBean selected_voz_povratak, int selectet_razred_polazak, int selected_razred_povratak,
			int broj_putnika, double ukupna_cena, CenaBean prvi_putnik_cena, CenaBean drugi_putnik_cena, CenaBean treci_putnik_cena, CenaBean cetvrti_putnik_cena, 
			CenaBean peti_putnik_cena, CenaBean prvi_putnik_povratna_cena, CenaBean drugi_putnik_povratna_cena, CenaBean treci_putnik_povratna_cena, 
			CenaBean cetvrti_putnik_povratna_cena, CenaBean peti_putnik_povratna_cena, int prva_karta_tip, int druga_karta_tip, int treca_karta_tip,
			int cetvrta_karta_tip, int peta_karta_tip, boolean is_petnaest_dana,
			String prva_karta_tip_id_legitimacije, String druga_karta_tip_id_legitimacije, String treca_karta_tip_id_legitimacije,
			String cetvrta_karta_tip_id_legitimacije, String peta_karta_tip_id_legitimacije, String transactionReportURL) {
		_is_petnaest_dana = is_petnaest_dana;
		_callback = callback;
		_user_id = user_id;
		_naziv_stanice = naziv_stanice;
		_id_kartomata = id_kartomata;
		_selected_voz = selected_voz;
		_selected_voz_povratak = selected_voz_povratak;
		_selected_razred_polazak = selectet_razred_polazak;
		_selected_razred_povratak = selected_razred_povratak;
		_broj_putnika = broj_putnika;
		_ukupna_cena = ukupna_cena;
		_prvi_putnik_cena = prvi_putnik_cena;
		_drugi_putnik_cena = drugi_putnik_cena;
		_treci_putnik_cena = treci_putnik_cena;
		_cetvrti_putnik_cena = cetvrti_putnik_cena;
		_peti_putnik_cena = peti_putnik_cena;
		
		_prvi_putnik_povratna_cena = prvi_putnik_povratna_cena;
		_drugi_putnik_povratna_cena = drugi_putnik_povratna_cena;
		_treci_putnik_povratna_cena = treci_putnik_povratna_cena;
		_cetvrti_putnik_povratna_cena = cetvrti_putnik_povratna_cena;
		_peti_putnik_povratna_cena = peti_putnik_povratna_cena;
		
		_prva_karta_tip = prva_karta_tip;
		_druga_karta_tip = druga_karta_tip;
		_treca_karta_tip = treca_karta_tip;
		_cetvrta_karta_tip = cetvrta_karta_tip;
		_peta_karta_tip = peta_karta_tip;
		
		_prva_karta_tip_id_legitimacije = prva_karta_tip_id_legitimacije;
		_druga_karta_tip_id_legitimacije = druga_karta_tip_id_legitimacije;
		_treca_karta_tip_id_legitimacije = treca_karta_tip_id_legitimacije;
		_cetvrta_karta_tip_id_legitimacije = cetvrta_karta_tip_id_legitimacije;
		_peta_karta_tip_id_legitimacije = peta_karta_tip_id_legitimacije;
		
		_transactionReportURL = transactionReportURL;
	}
	
	
	public void run() {
		logger.info("run, parametri : _is_petnaest_dana = " + _is_petnaest_dana + ", _user_id = " + _user_id + 
				", _naziv_stanice = " + _naziv_stanice +
				", _id_kartomata = " + _id_kartomata +
				", _selected_voz = " + _selected_voz +
				", _selected_voz_povratak = " + _selected_voz_povratak +
				", _selected_razred_polazak = " + _selected_razred_polazak +
				", _selected_razred_povratak = " + _selected_razred_povratak +
				", _broj_putnika = " + _broj_putnika +
				", _ukupna_cena = " + _ukupna_cena +
				", _prvi_putnik_cena = " + _prvi_putnik_cena +
				", _drugi_putnik_cena = " + _drugi_putnik_cena +
				", _treci_putnik_cena = " + _treci_putnik_cena +
				", _cetvrti_putnik_cena = " + _cetvrti_putnik_cena +
				", _peti_putnik_cena = " + _peti_putnik_cena +
				", _prvi_putnik_povratna_cena = " + _prvi_putnik_povratna_cena +
				", _drugi_putnik_povratna_cena = " + _drugi_putnik_povratna_cena +
				", _treci_putnik_povratna_cena = " + _treci_putnik_povratna_cena +
				", _cetvrti_putnik_povratna_cena = " + _cetvrti_putnik_povratna_cena +
				", _peti_putnik_povratna_cena = " + _peti_putnik_povratna_cena +
				", _prva_karta_tip = " + _prva_karta_tip +
				", _druga_karta_tip = " + _druga_karta_tip +
				", _treca_karta_tip = " + _treca_karta_tip +
				", _cetvrta_karta_tip = " + _cetvrta_karta_tip +
				", _peta_karta_tip = " + _peta_karta_tip + 
				", _prva_karta_tip_id_legitimacije = " + _prva_karta_tip_id_legitimacije + 
				", _druga_karta_tip_id_legitimacije = " + _druga_karta_tip_id_legitimacije + 
				", _treca_karta_tip_id_legitimacije = " + _treca_karta_tip_id_legitimacije + 
				", _cetvrta_karta_tip_id_legitimacije = " + _cetvrta_karta_tip_id_legitimacije + 
				", _peta_karta_tip_id_legitimacije = " + _peta_karta_tip_id_legitimacije 
				);
		String message = "";
		try {
			message = plati_kartu_sequence();
		}catch(Exception e) {
			e.printStackTrace();
			logger.error("Greska u placanju , details: " + e.getMessage(), e );
		}
		try {
			SrbijaVozInvoiceHandler f_handler = fiskalizuj();
			try {
				sendTotransactionReport(f_handler);
			}catch(Throwable e) {
				logger.info(" Exception when try to sendTotransactionReport, details: " + e.getMessage(), e);
			}
		}catch(Exception e) {
			e.printStackTrace();
			logger.error("Greska u fiskalizaciji , details: " + e.getMessage(), e );
		}
		_callback.setPaymentSessionMessage(uspesna_kupovina, message, _broj_putnika, _fiscal_journal, _fiscal_qr_code);
	}
	
	
	private String plati_kartu_sequence() {
		logger.info("--> plati_kartu_sequence");
		
		

//		Platform.runLater(() -> { 

			String _error_message = "Uspešna kupovina";

			String datum_kupovine = _sdf_stampa.format(new Date());

			int ID_RELACIJE = _selected_voz.getIdrel();
			int stanica_od = _selected_voz.getOdsifra();
			int stanica_do = _selected_voz.getDosifra();
			int smer = _selected_voz_povratak == null ? 1: 2;
			String via = "";
			//String vazi_od = datum_kupovine;
			String vazi_od = _selected_voz.getDatumPolaska().replace(".", "-");
			if(_is_petnaest_dana) {
				Date datum_polaska = null;
				try{
					datum_polaska = _sdf_stampa.parse(vazi_od);
				}catch(Exception e) {
					logger.error("parse date exception when parsing datum_polaska = "+datum_polaska+", date is today");
					datum_polaska = new Date();
				}
			}
			//TODO - kada saljemo
			String vazi_do = _selected_voz.get_trajanje_datum().replace(".", "-");
			
			
//			String vazi_do = _selected_voz.getDatumPolaska().replace(".", "-");
//			String vazi_od = vazi_do;
//			if(_is_petnaest_dana) {
//				Date datum_polaska = null;
//				try{
//					datum_polaska = _sdf_stampa.parse(vazi_do);
//				}catch(Exception e) {
//					datum_polaska = new Date();
//				}
//				Calendar petnast_dana_u_buducnost = Calendar.getInstance();
//				petnast_dana_u_buducnost.setTime(datum_polaska);
//				petnast_dana_u_buducnost.add(Calendar.DAY_OF_YEAR, 15);
//				vazi_do = _sdf_stampa.format(petnast_dana_u_buducnost.getTime());
//			}
			
			int voz_a = _selected_voz.getBrvoz();
			int razred_a = _selected_razred_polazak;
			String vreme_polaska_a = _selected_voz.getVremep();
			String vreme_dolaska_a = _selected_voz.getVremed();
			String voz_r = _selected_voz_povratak == null ? null : "" + _selected_voz_povratak.getBrvoz();
			String razred_r =  _selected_voz_povratak ==  null ? null : "" + _selected_razred_povratak;
			String vreme_polaska_r = _selected_voz_povratak ==  null ? null : _selected_voz_povratak.getVremep();
			String vreme_dolaska_r = _selected_voz_povratak ==  null ? null : _selected_voz_povratak.getVremed();
			//int broj_putnika = Integer.parseInt( broj_putnika_lbl.getText() );
			//double ukupna_cena = Double.parseDouble(ukupna_cena_value_lbl.getText());

			EtKartaBean et_karta_data = new EtKartaBean();
			et_karta_data.setUkupnacena(_ukupna_cena);
			et_karta_data.setSmer(smer);

			VozDataBean voz_data = new VozDataBean();
			voz_data.setVoz(_selected_voz);
			voz_data.setVozp(_selected_voz_povratak);
			voz_data.setBrojputnika(_broj_putnika);
			voz_data.setCenauk((int)_ukupna_cena);	
			
			voz_data.setDatum1(formirajDatumVreme(_selected_voz.getDatumPolaska(), vreme_polaska_a));
			//bug na koji je Jelena ukazala. U slucaju povratne karte, treba setovati na datum povratka
			String datum2 = _selected_voz_povratak ==  null ? _selected_voz.getDatumPolaska() : _selected_voz_povratak.get_datum_vreme_polaska();
			voz_data.setDatum2(formirajDatumVreme(datum2/*_selected_voz.getDatumPolaska()*/, vreme_dolaska_a));
			System.out.println("datum1 = " + voz_data.getDatum1());
			System.out.println("datum2 = " + voz_data.getDatum2());
//			voz_data.setDatum1("2021-12-14T08:08:14.727Z");
//			voz_data.setDatum2("2021-12-14T08:08:56.727Z");
			voz_data.setRazred(razred_a);
			voz_data.setRazredp(razred_a);	
			et_karta_data.setVozData(voz_data);

			SifraBean sifraOD = new SifraBean();
			sifraOD.setNaziv(_selected_voz.getNazivod());
			sifraOD.setSifra(_selected_voz.getOdsifra());
			et_karta_data.setSifraod(sifraOD);

			SifraBean sifraDO = new SifraBean();
			sifraDO.setNaziv(_selected_voz.getNazivdo());
			sifraDO.setSifra(_selected_voz.getDosifra());
			
			
			List<String> uneseneleg = new ArrayList<String>();
			uneseneleg.add("");
			et_karta_data.setUneseneleg(uneseneleg);

			List<CenaBean> cena1 = new ArrayList<CenaBean>();


			

			if(smer == 2) {
				et_karta_data.setSifrado(sifraDO);
				//povratna
				List<CenaBean> cena2 = new ArrayList<CenaBean>();
				if(_prvi_putnik_povratna_cena != null) {
					cena2.add(_prvi_putnik_povratna_cena);
				}
				if(_drugi_putnik_povratna_cena != null) {
					cena2.add(_drugi_putnik_povratna_cena);
				}
				if(_treci_putnik_cena != null) {
					cena2.add(_treci_putnik_povratna_cena);
				}
				if(_cetvrti_putnik_povratna_cena != null) {
					cena2.add(_cetvrti_putnik_povratna_cena);
				}
				if(_peti_putnik_povratna_cena != null) {
					cena2.add(_peti_putnik_povratna_cena);
				}
				et_karta_data.setCenasmer2(cena2);
			}else {
				if(_prvi_putnik_cena != null) {
					cena1.add(_prvi_putnik_cena);
				}
				if(_drugi_putnik_cena != null) {
					cena1.add(_drugi_putnik_cena);
				}
				if(_treci_putnik_cena != null) {
					cena1.add(_treci_putnik_cena);
				}
				if(_cetvrti_putnik_cena != null) {
					cena1.add(_cetvrti_putnik_cena);
				}
				if(_peti_putnik_cena != null) {
					cena1.add(_peti_putnik_cena);
				}
				
			}
			
			et_karta_data.setCenasmer1(cena1);

			///////////////////////////putnici /////////////////////////////////////////

			putnici =  smer == 1 ? new ArrayList<PutniciBean>() : new ArrayList<PutniciBeanPovratna>();
			
			PutnikIface putnik1 = smer == 1 ? new PutniciBean() : new PutniciBeanPovratna();
			putnik1.setImeprezime("" + _id_kartomata);
			putnik1.setDatumrodjenja("2022.03.14");
			putnik1.setIdleg(_prva_karta_tip_id_legitimacije);

			putnik1.setSifrapovlastice(getSifraPovlastice(_prva_karta_tip));
			
			List<CenaBean> cena_put_1 = new ArrayList<CenaBean>();
			cena_put_1.add( smer == 2 ? _prvi_putnik_povratna_cena : _prvi_putnik_cena);
			putnik1.setCenaa(cena_put_1);
			putnici.add(putnik1);
			if(_broj_putnika > 1) {
				PutnikIface putnik2 = smer == 1 ? new PutniciBean() : new PutniciBeanPovratna();
				putnik2.setImeprezime("" + _id_kartomata);
				putnik2.setDatumrodjenja("2022.03.14");
				putnik2.setIdleg(_druga_karta_tip_id_legitimacije);
				putnik2.setSifrapovlastice(getSifraPovlastice(_druga_karta_tip));
				List<CenaBean> cena_put_2 = new ArrayList<CenaBean>();
				cena_put_2.add( smer == 2 ? _drugi_putnik_povratna_cena : _drugi_putnik_cena);
				putnik2.setCenaa(cena_put_2);
				putnici.add(putnik2);
			}

			if(_broj_putnika > 2) {
				PutnikIface putnik3 = smer == 1 ? new PutniciBean() : new PutniciBeanPovratna();
				putnik3.setImeprezime("" + _id_kartomata);
				putnik3.setDatumrodjenja("2022.03.14");
				putnik3.setIdleg(_treca_karta_tip_id_legitimacije);

				putnik3.setSifrapovlastice(getSifraPovlastice(_treca_karta_tip));
				List<CenaBean> cena_put_3 = new ArrayList<CenaBean>();
				cena_put_3.add( smer == 2 ? _treci_putnik_povratna_cena : _treci_putnik_cena);
				putnik3.setCenaa(cena_put_3);
				putnici.add(putnik3);
			}

			if(_broj_putnika > 3) {
				PutnikIface putnik4 = smer == 1 ? new PutniciBean() : new PutniciBeanPovratna();
				putnik4.setImeprezime("" + _id_kartomata);
				putnik4.setDatumrodjenja("2022.03.14");
				putnik4.setIdleg(_cetvrta_karta_tip_id_legitimacije);
				putnik4.setSifrapovlastice(getSifraPovlastice(_cetvrta_karta_tip));
				List<CenaBean> cena_put_4 = new ArrayList<CenaBean>();
				cena_put_4.add( smer == 2 ? _cetvrti_putnik_povratna_cena : _cetvrti_putnik_cena);
				putnik4.setCenaa(cena_put_4);
				putnici.add(putnik4);
			}

			if(_broj_putnika > 4) {
				PutnikIface putnik5 = smer == 1 ? new PutniciBean() : new PutniciBeanPovratna();
				putnik5.setImeprezime("" + _id_kartomata);
				putnik5.setDatumrodjenja("2022.03.14");
				putnik5.setIdleg(_peta_karta_tip_id_legitimacije);
				putnik5.setSifrapovlastice(getSifraPovlastice(_peta_karta_tip));
				List<CenaBean> cena_put_5 = new ArrayList<CenaBean>();
				cena_put_5.add( smer == 2 ? _peti_putnik_povratna_cena : _peti_putnik_cena);
				putnik5.setCenaa(cena_put_5);
				putnici.add(putnik5);
			}
			et_karta_data.setPutnici(putnici);
			////////////////////////end putnici/////////////////////////////////////////
			PayTenPaymentIface payment_iface = null;

			try {
				logger.info("poziv srbija voz api, upisET_ORDER, parametri: "+
						", _user_id" + _user_id+ 
						", datum_kupovine = " + datum_kupovine+ 
						", ID_RELACIJE = " + ID_RELACIJE+ 
						", stanica_od = " + stanica_od+ 
						", stanica_do = " + stanica_do+ 
						", smer = " + smer+ 
						", via = " + via+ 
						", vazi_od = " + vazi_od+ 
						", vazi_do = " + vazi_do+ 
						", voz_a = " + voz_a+ 
						", razred_a = " + razred_a+ 
						", vreme_polaska_a = " + vreme_polaska_a+ 
						", vreme_dolaska_a = " + vreme_dolaska_a+ 
						", voz_r = " + voz_r+ 
						", razred_r = " + razred_r+ 
						", vreme_polaska_r = " + vreme_polaska_r+ 
						", vreme_dolaska_r = " + vreme_dolaska_r+ 
						", _broj_putnika = " + _broj_putnika+ 
						", _ukupna_cena = " + _ukupna_cena+ 
						", et_karta_data = " + et_karta_data 
						);

				ETKartaResponseBean et_response = SrbijaVozIfaceFactory.getIface(MainUIController.SV_API_URL, MainUIController.SV_API_CONN_TIME, MainUIController.SV_API_READ_TIME).upisET_ORDER(_user_id, datum_kupovine, ID_RELACIJE, stanica_od, stanica_do,
						smer, via, vazi_od, vazi_do, voz_a, razred_a, vreme_polaska_a, vreme_dolaska_a,
						voz_r, razred_r, vreme_polaska_r, vreme_dolaska_r, _broj_putnika, _ukupna_cena, 
						et_karta_data);
			
				logger.info("poziv srbija voz api, upisET_ORDER, returned : "+ et_response);
				
				
				//			ETKartaResponseBean et_response = new ETKartaResponseBean();
				//			et_response.setAmount("" + ukupna_cena );
				//			et_response.setOid("SVKOID0000031");
				//			et_response.setClientid("13IN060595");

				//placanje preko paytem-a

				payment_iface = PaymentFactory.getPaymentInterface(MainUIController.PAYMENT_IP_ADDRESS, MainUIController.PAYMENT_PORT);

				logger.info("payment call, params : et_response.getAmount() = "+ et_response.getAmount() + ", " + 
						"et_response.get_order_id() = "+ et_response.get_order_id() + ", " + 
						"et_response.get_merchant_id() = "+ et_response.get_merchant_id()
						);
				
				TransactionDataResponse tdr = payment_iface.payForTicket(
						Double.parseDouble(et_response.getAmount()), et_response.get_order_id(), et_response.get_merchant_id());

				logger.info("payment call, response = "+ tdr);
				
				logger.info("srbija voz api call upisUPIT_KA_BANCI, params : tdr.is_pozitivan() = " + tdr.is_pozitivan() + ", " +
						"_user_id = " + _user_id + ", " +
						"et_response.get_order_id() = " + et_response.get_order_id() + ", " +
						"et_response.getAmount() = " + et_response.getAmount() + ", " +
						"tdr = " + tdr
						);
				
				_transaction_id = tdr.get_transactionNumber();
				
				
				EtKartaResponse et_karta = SrbijaVozIfaceFactory.getIface(MainUIController.SV_API_URL, MainUIController.SV_API_CONN_TIME, MainUIController.SV_API_READ_TIME).upisUPIT_KA_BANCI(tdr.is_pozitivan(), _user_id, et_response.get_order_id(), 
						Double.parseDouble(et_response.getAmount()), 
						tdr);
				logger.info("srbija voz api call upisUPIT_KA_BANCI response, et_karta = " + et_karta);
				if(tdr.is_pozitivan()){
					//onda cheers
					PrinterService printerService = new PrinterService();
					int i = 0;
					//preporuka japanaca da se ne bi papir zaglavljivao
//					printerService.back_feed_n();
//					printerService.print_init();
//					printerService.print_start();
					
					//prvo mali form feed
//					printerService.form_feed_n_small();
					
					//prvo da stampa slip
//					logger.info("print pay slipa " );
//					printerService.printCreditCardSlip(tdr.get_transactionDate(), tdr.get_transactionTime()/*sat_min.format(new Date())*/,
//							 et_response.getAmount(), tdr.get_tidNumber(),tdr.get_midNumber(), tdr.get_companyName(), "" + _id_kartomata,
//							 tdr.get_cardNumber(), tdr.get_expirationData(), _naziv_stanice, MainUIController.KARTOMAT_GRAD, 
//							 tdr.get_authorizationCode(), tdr.get_transactionNumber(), et_response.get_order_id());
					
//TODO obavezno skinuti ovo, samo za testiranje					
					String cridit_card_payment_slip = printerService.getCreditCardSlip(tdr.get_transactionDate(), tdr.get_transactionTime()/*sat_min.format(new Date())*/,
							 et_response.getAmount(), tdr.get_tidNumber(),tdr.get_midNumber(), tdr.get_companyName(), "" + _id_kartomata,
							 tdr.get_cardNumber(), tdr.get_expirationData(), _naziv_stanice, MainUIController.KARTOMAT_GRAD, 
							 tdr.get_authorizationCode(), tdr.get_transactionNumber(), et_response.get_order_id());
					logger.info("Slanje pay slipa na srbija voz api: cridit_card_payment_slip = " + cridit_card_payment_slip );
					
					try {
						String srb_voz_slip_return_info = SrbijaVozIfaceFactory.getIface(MainUIController.SV_API_URL, 
								MainUIController.SV_API_CONN_TIME, MainUIController.SV_API_READ_TIME).upisU_KARTOMAT_SLIP(
										tdr.get_cardNumber(), cridit_card_payment_slip);
						logger.info("srbija voz api: cridit_card_payment_slip returned = " + srb_voz_slip_return_info);
					}catch(Exception e) {
						logger.warn("unable to send request for upisU_KARTOMAT_SLIP to serbija voz api, details: " +
								e.getMessage() + ". Continue with logic");
						e.printStackTrace();
					}
					
					for(KartaStampaBean current : et_karta.getKarte()) {
						logger.info("printam : " + ++i + "kartu");
						_vk_amount.add(current.getBROJ_KARTE() + ":" + current.getCENA());
						//TODO - 
						//ovo nam nece trebati kad poprave
						vazi_do = _selected_voz.get_trajanje_datum().replace("-", ".");
						printerService.printKartu(_naziv_stanice, "" + current.getBROJ_KARTE(), 
								current.getDATUM_KUPOVINE(), 
								sat_min.format(new Date()), current.getVOZA(), "", current.getVOZR(), "", 
								current.getNAZIV_STANICE_ODA(), current.getNAZIV_STANICE_DOA(), current.getNAZIV_STANICE_DOA(), 
								current.getNAZIV_STANICE_DOR(), current.getVIA(), current.getVIAR(), current.getVAZI_OD(),
								_selected_voz.getRangNaziv(), current.getRAZREDA(), _selected_voz_povratak != null ? /*current.getVAZI_DO_R()*/vazi_do : current.getVAZI_DO(), 
								_selected_voz_povratak != null ? _selected_voz_povratak.getRangNaziv() : "",
										current.getRAZREDR(), "1", "", current.getPOVLASTICA_NAZIV(), "" + current.getCENA(),
										_selected_voz_povratak != null, current.getREZERVACIJA_A(), current.getREZERVACIJA_R(),
										current.getVREME_POLASKAA(), current.getVREME_POLASKAR());
						try {
							logger.info("poziv srbija voz api-ja upisKARTA_ODSTAMPANA, params: _user_id = " + _user_id + ", " +
									"current.getREDNI_BROJ() = " + current.getREDNI_BROJ() 
									);
							logger.info("poziv srbija voz api-ja upisKARTA_ODSTAMPANA povratak");
						SrbijaVozIfaceFactory.getIface(MainUIController.SV_API_URL, MainUIController.SV_API_CONN_TIME, MainUIController.SV_API_READ_TIME).upisKARTA_ODSTAMPANA("" + _user_id, "" + current.getREDNI_BROJ());
						}catch(Throwable e) {
							logger.error("Unknown exception when try to UPIS KARTA ODSTAMPONA call prema Srbija Voz API, details: " + 
									e.getMessage(), e);
						}
					}
					uspesna_kupovina = true;
					logger.info("kraj placanja, uspesna_kupovina = " + uspesna_kupovina);
//					String datum, String vreme, String iznos, String tID, String mID, String companyName, String terminalId,
//		    		String cardNumber, String expirationData, String kartomat_ime, String kartomat_grad, String authorizationCode, String transactionNumber

					
//					printerService.printCreditCardSlip(tdr.get_transactionDate(), tdr.get_transactionTime()/*sat_min.format(new Date())*/,
//							 et_response.getAmount(), tdr.get_tidNumber(),tdr.get_midNumber(), tdr.get_companyName(), "" + _id_kartomata,
//							 tdr.get_cardNumber(), tdr.get_expirationData(), _naziv_stanice, MainUIController.KARTOMAT_GRAD, 
//							 tdr.get_authorizationCode(), tdr.get_transactionNumber(), et_response.get_order_id());
					
					
//					//preporuka japanaca da se papir ne bi zaglavljivao
//					printerService.print_end();
//					printerService.full_cut();
//					printerService.form_feed_n();
					
					
				}else {
					uspesna_kupovina = false;
					logger.info("##kraj placanja, NEuspesna_kupovina = " + uspesna_kupovina);
					//onda poruka o gresci
					//payment_iface.cancelCurrentPayment(tdr.get_identifier());
				}

			}catch(CommunicationException ce) {
				//TODO pozvati panel sa greskom - pokusajte ponovo
				ce.printStackTrace();
				logger.error("CommunicationException when try to purchase a ticket, details: " + ce.getMessage(), ce); 
				_error_message = ce.getMessage();
			}catch(PaymentException pe) {
				//TODO pozvati panel sa greskom - pokusajte ponovo
				pe.printStackTrace();
				logger.info("PaymentException when try to purchase a ticket, details: " + pe.getMessage(), pe);
				//_error_message = "Pokušajte ponovo";
				_error_message = pe.getMessage();
			}catch(Throwable e) {
				//TODO pozvati panel sa greskom - pokusajte ponovo
				//ovde rollback payment-a
				e.printStackTrace();
				logger.info("Unknown Exception when try to purchase a ticket, details: " + e.getMessage(), e);
				_error_message = "Nepoznata greška, Pokušajte ponovo";
			}finally {
				if(payment_iface != null)payment_iface.close();


//				Platform.runLater(() -> {       	
//
//
//					Thread thread = new Thread(() -> {
//						try {
//							//only for testing try{Thread.sleep(5000);}catch(Exception e) {}
//							if(uspesna_kupovina) {
//								//showSlanjeZahteva("USPEŠNO plaćanje","Hvala što koristite kartomat");
//								placanje_result_pn.setStyle("-fx-background-image: url('"+resources.getString("karticq_za_placanje_uspesna_gif")+"')");
//								message_lbl_1.setText("");
//								message_lbl_2.setText("");
//								message_lbl_3.setText("");
//								message_lbl_4.setText("");
//								message_lbl_5.setText("");
//							}else {
//								//showSlanjeZahteva("NEUSPEŠNO plaćanje","Hvala što koristite kartomat");
//								placanje_result_pn.setStyle("-fx-background-image: url('"+resources.getString("karticq_za_placanje_neuspesna_gif")+"')");
//								String jednina_mnozina = broj_putnika > 1 ? "karata" : "karte";
//								message_lbl_1.setText("Vaša kupovina "+jednina_mnozina+" je NEUSPEŠNA");
//								if(_error_message.equals("")) {
//									message_lbl_2.setText("Plaćanje vašom kreditnom karticom je ODBIJENO");
//									message_lbl_3.setText("Hvala što koristite usluge kartomata Srbija Voza");
//									message_lbl_4.setText("");
//									message_lbl_5.setText("");
//								}else {
//									message_lbl_2.setText("Plaćanje vašom kreditnom karticom je ODBIJENO");
//									message_lbl_3.setText(_error_message);
//									message_lbl_4.setText("");
//									message_lbl_5.setText("");
//								}
//							}
//							Thread.sleep(10000);
//							placanje_pn.setVisible(false);
//							odrediste_pn.setVisible(true);
//							Platform.runLater(() -> { 
////								closeSession();
//								stop_recycling_session();
//							});
//
//						} catch (Exception exp) {
//							exp.printStackTrace();
//						} finally {
//							placanje_result_pn.setVisible(false);
//						}
//					});
//					thread.setDaemon(true);
//					thread.start();
//
//				});


			}

//		});//end platform start
			logger.info("#### kraj placanja, uspesna_kupovina = " + uspesna_kupovina);
			logger.info("#### kraj placanja, error_message = " + _error_message);
			return _error_message;

	}
	
	private String formirajDatumVreme(String datum, String vreme) {
		String[] datum_splt = datum.split("\\.");
		
		if(datum_splt.length < 3) {
			SimpleDateFormat sdf_send = new SimpleDateFormat("yyyy-MM-dd'T'");
			return sdf_send.format(new Date()) + vreme + ":00.727Z";
		}else {
			return datum_splt[2] +"-"+datum_splt[1] +"-"+datum_splt[0] +"T"+ vreme + ":00.727Z";
		}
		
	}
	
	private int getSifraPovlastice(int tip_karte) {

		int smer = _selected_voz_povratak == null ? 1: 2;
		switch(tip_karte) {
			case MainUIController.TIP_REDOVNA_CENA: return smer == 1 ? 1 : 20;
			case MainUIController.TIP_SRB_K13: return 37;
			case MainUIController.TIP_RAIL_K30: return 38;
			case MainUIController.TIP_DETE: return 50;
			case MainUIController.TIP_PAS: return 54;
			case MainUIController.TIP_POVRATNA: return 20;
			case MainUIController.TIP_PENZIONERI: return 31;
				default: return 1;
		}

	}
	
	private SrbijaVozInvoiceHandler fiskalizuj() {
		logger.info("fiskalizuj ");
		SrbijaVozInvoiceHandler fiskal_handler = new SrbijaVozInvoiceHandler(_vk_amount, 
				AbstractController.getProperties().getProperty("fiscal.esir.number", "1145/2.0"/*"601/1.0"*/), 
				AbstractController.getProperties());
		try {
			String result = fiskal_handler.handle_request();
			String[] journal_qr_code = result.split("&");
			_fiscal_journal = journal_qr_code[0];
			_fiscal_qr_code = journal_qr_code[1];
		}catch(IOException ioe) {
			logger.error("Unable to call fiskal service, details: = " + ioe.getMessage(), ioe);
		} 
		return fiskal_handler;
	}
	
	private void sendTotransactionReport(SrbijaVozInvoiceHandler fiskal_handler) {
		KupovinaTransaction to_report = new KupovinaTransaction();
		InvoiceRequestBean fiskalizacija_request = fiskal_handler.getBean();
		InvoiceResponse fiskalizacija_response = fiskal_handler.getInvoiceResponse();
		KartomatData kartomat = new KartomatData();
		kartomat.setId(_id_kartomata);
		kartomat.setName(_naziv_stanice);
		to_report.setKartomat(kartomat);
		List<FisklaizacijaTransactionKartaDto> fiskal_karte = new ArrayList<FisklaizacijaTransactionKartaDto>();
		for(ItemBean current : fiskalizacija_request.getItems()) {
			FisklaizacijaTransactionKartaDto fis_karta = new FisklaizacijaTransactionKartaDto();
			fis_karta.setAmount(current.getUnitPrice());
			fis_karta.setName(current.getName());
			fis_karta.setQuantity(current.getQuantity());
			fis_karta.setTotalPrice(current.getTotalAmount());
			fis_karta.setUnitPrice(current.getUnitPrice());
		}
		to_report.setFiskal_karte(fiskal_karte);

		FiskalizacijaTransactionItemDto fiskla_item = new FiskalizacijaTransactionItemDto();
		fiskla_item.setAmount(fiskalizacija_response.getTaxItems().get(0).getAmount());
		fiskla_item.setCategoryName(fiskalizacija_response.getTaxItems().get(0).getCategoryName());
		fiskla_item.setCategoryType("" + fiskalizacija_response.getTaxItems().get(0).getCategoryType());
		fiskla_item.setLabel(fiskalizacija_response.getTaxItems().get(0).getLabel());
		fiskla_item.setRate(fiskalizacija_response.getTaxItems().get(0).getRate());
		to_report.setFiskal_item(fiskla_item);
		
		FiskalizacijaTransactionDto fisk_tran = new FiskalizacijaTransactionDto();
		fisk_tran.setAddress(fiskalizacija_response.getAddress());
		fisk_tran.setAmount(Double.parseDouble(fiskalizacija_response.getTotalAmount()));
		fisk_tran.setBusinessName(fiskalizacija_response.getBusinessName());
		fisk_tran.setDistrict(fiskalizacija_response.getDistrict());
		fisk_tran.setInvoiceCounter(fiskalizacija_response.getInvoiceCounter());
		fisk_tran.setInvoiceCounterExtension(fiskalizacija_response.getInvoiceCounterExtension());
		fisk_tran.setInvoiceNumber(fiskalizacija_response.getInvoiceNumber());
		fisk_tran.setJournal(fiskalizacija_response.getJournal());
		fisk_tran.setLocationName(fiskalizacija_response.getLocationName());
		fisk_tran.setMrc(fiskalizacija_response.getMrc());
		fisk_tran.setPAC(fiskal_handler.getPAC());
		fisk_tran.setPayment(fiskalizacija_request.getPayment().get(0).getPaymentType());
		fisk_tran.setRequest_id(fiskal_handler.get_invoice_number());
		fisk_tran.setRequestedBy(fiskalizacija_response.getRequestedBy());
		fisk_tran.setSdcDateTime(fiskalizacija_response.getSdcDateTime());
		fisk_tran.setTin(fiskalizacija_response.getTin());
		fisk_tran.setTotalAmount(Integer.parseInt(fiskalizacija_response.getTotalAmount()));
		fisk_tran.setTransactionType(fiskalizacija_request.getTransactionType());
		fisk_tran.setVerificationQRCode(fiskalizacija_response.getVerificationQRCode());
		fisk_tran.setVerificationUrl(fiskalizacija_response.getVerificationUrl());
		to_report.setFiskalizacija_transakcija(fisk_tran);
		
		Iterator putniciIterator = putnici.iterator();
		List<KartaDto> karte = new ArrayList<KartaDto>();

		while(putniciIterator.hasNext()) {

			KartaDto karta_current = new KartaDto();
			Object current = putniciIterator.next();
		    if(current instanceof PutniciBean) {
		    	PutniciBean p_beee_current = (PutniciBean)current;
		    	karta_current.setCena(p_beee_current.getCenaa().get(0).getCenau());
		    	karta_current.setKarta_tip("" + p_beee_current.getSifrapovlastice());
		    	karta_current.setLegitimacija_id(p_beee_current.getIdleg());
		    	karta_current.setPovratna_cena(0);
		    	karta_current.setSelected_razred_polazak("" + _selected_razred_polazak);
		    	karta_current.setSelected_razred_povratak("");
		    }else {
		    	PutniciBeanPovratna p_beee_current = (PutniciBeanPovratna)current;
		    	karta_current.setCena(p_beee_current.getCenaa().get(0).getCenau());
		    	karta_current.setKarta_tip("" + p_beee_current.getSifrapovlastice());
		    	karta_current.setLegitimacija_id(p_beee_current.getIdleg());
		    	karta_current.setPovratna_cena((int)p_beee_current.getCenaa().get(0).getCenar());
		    	karta_current.setSelected_razred_polazak("" + _selected_razred_polazak);
		    	karta_current.setSelected_razred_povratak("" + _selected_razred_povratak);
		    }
		    karte.add(karta_current);
		}
		
		to_report.setKarte(karte);
		to_report.setSelected_train("" + _selected_voz.getBrvoz());
		to_report.setSelected_train_povrtaka("" + _selected_voz_povratak.getBrvoz());
		to_report.setBroj_putnika(_broj_putnika);
		to_report.setCena_ukupno(_ukupna_cena);
		to_report.setDestination(_selected_voz.getNazivdo());
		to_report.setId(_transaction_id);
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		to_report.setTransaction_time(sdf.format(new Date()));
		
		try {
			new Thread(new TransactionReportHandler(to_report, new URL(_transactionReportURL))).start();
		}catch(Exception e) {
			
		}
		
		
	}

}
