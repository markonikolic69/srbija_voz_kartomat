package application.https;

import java.util.List;

import application.data.*;
import application.payment.impl.models.TransactionDataResponse;

public interface SrbijaVozIface {
	
	public List<StanicaIDBean> getStanicaIDs() throws CommunicationException;
	
	public List<VozBean> getListaVozovaNaTrasi(int sifra_stanice_od, int sifra_stanice_do, String datum, int broj_putnika, int razred) throws CommunicationException;
	
	public CenaBean getCenaPovratna(int broj_putnika,  int kilometraza, int razred_odlazak,  int rang_odlazak, int razred_povratak,  int rang_povratak) throws CommunicationException;
	
	public CenaBean getCenaJedanSmer(int broj_putnika, int razred, int kilometraza,  int rang) throws CommunicationException;
	
	public CenaBean getCenaPovratnaPSE(int broj_putnika,  int kilometraza, int razred_odlazak,  int rang_odlazak, int razred_povratak,  int rang_povratak) throws CommunicationException;
	
	public CenaBean getCenaPovratnaDETE(int broj_putnika,  int kilometraza, int razred_odlazak,  int rang_odlazak, 
			int razred_povratak,  int rang_povratak) throws CommunicationException;

	public CenaBean getCenaPovratnaSRB_PLUS_K_13(int broj_putnika,  int kilometraza, int razred_odlazak,  int rang_odlazak, 
			int razred_povratak,  int rang_povratak) throws CommunicationException;
	
	public CenaBean getCenaPovratnaRAIL_PLUS_K_30(int broj_putnika,  int kilometraza, int razred_odlazak,  int rang_odlazak, 
			int razred_povratak,  int rang_povratak) throws CommunicationException;
	
	public CenaBean getCenaJedanSmerPSE(int broj_putnika, int razred, int kilometraza,  int rang) throws CommunicationException;
	

	public CenaBean getCenaJedanSmerDETE(int broj_putnika, int razred, int kilometraza,  int rang) throws CommunicationException;
	
	public CenaBean getCenaJedanSmerSRB_PLUS_K_13(int broj_putnika, int razred, int kilometraza,  int rang) throws CommunicationException;
	
	public CenaBean getCenaJedanSmerRAIL_PLUS_K_30(int broj_putnika, int razred, int kilometraza,  int rang) throws CommunicationException;
	
	public List<PovlasticaBean> getPovlastice(int smer/*1 ili 2*/) throws CommunicationException;
	
	public boolean provera_legitimacije(int broj_legitimacije, int povlastica_id) throws CommunicationException;//https://ekarta.srbvoz.rs/api/povlastice?brleg=52910&pov=37
	
	public TokenBean getToken(String username, String password) throws CommunicationException;
	
	public List<FrekventneStaniceBean> getFrekventneStanice(String mak_adresa) throws CommunicationException;
	
	public KartomatBean getKartomat(String mac_address) throws CommunicationException;
	
	public ETKartaResponseBean upisET_ORDER(int userID, String datum_kupovine, int ID_RELACIJE, int stanica_od, int stanica_do,
			int smer, String via, String vazi_od, String vazi_do, int voz_a, int razred_a, String vreme_polaska_a, String vreme_dolaska_a,
			String voz_r, String razred_r, String vreme_polaska_r, String vreme_dolaska_r, int broj_putnika, double ukupna_cena, 
			EtKartaBean et_karta_data) throws CommunicationException;
	
	public EtKartaResponse upisUPIT_KA_BANCI(boolean is_pozitivan, int userID, String orderID, double cena, TransactionDataResponse payment_transaction_data) throws CommunicationException;

	public void upisKARTA_ODSTAMPANA(String userID, String broj_karte) throws CommunicationException;

}
