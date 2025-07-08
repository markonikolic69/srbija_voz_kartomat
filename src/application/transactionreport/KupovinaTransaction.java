package application.transactionreport;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;





public class KupovinaTransaction {
	private String id;
	private KartomatData kartomat;
	private double cena_ukupno;
	private String destination;
	private String selected_train;
	private String selected_train_povrtaka;
	private int broj_putnika;
	private String transaction_time;
	private FiskalizacijaTransactionDto fiskalizacija_transakcija;
	private List<KartaDto> karte;
	private List<FisklaizacijaTransactionKartaDto> fiskal_karte;
	private FiskalizacijaTransactionItemDto fiskal_item;
	

	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}

	public KartomatData getKartomat() {
		return kartomat;
	}

	public void setKartomat(KartomatData kartomat) {
		this.kartomat = kartomat;
	}

	public double getCena_ukupno() {
		return cena_ukupno;
	}

	public void setCena_ukupno(double cena_ukupno) {
		this.cena_ukupno = cena_ukupno;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getSelected_train() {
		return selected_train;
	}

	public void setSelected_train(String selected_train) {
		this.selected_train = selected_train;
	}

	public String getSelected_train_povrtaka() {
		return selected_train_povrtaka;
	}

	public void setSelected_train_povrtaka(String selected_train_povrtaka) {
		this.selected_train_povrtaka = selected_train_povrtaka;
	}

	public int getBroj_putnika() {
		return broj_putnika;
	}

	public void setBroj_putnika(int broj_putnika) {
		this.broj_putnika = broj_putnika;
	}

	public String getTransaction_time() {
		return transaction_time;
	}

	public void setTransaction_time(String transaction_time) {
		this.transaction_time = transaction_time;
	}

	public FiskalizacijaTransactionDto getFiskalizacija_transakcija() {
		return fiskalizacija_transakcija;
	}

	public void setFiskalizacija_transakcija(FiskalizacijaTransactionDto fiskalizacija_transakcija) {
		this.fiskalizacija_transakcija = fiskalizacija_transakcija;
	}

	public List<KartaDto> getKarte() {
		return karte;
	}

	public void setKarte(List<KartaDto> karte) {
		this.karte = karte;
	}

	public List<FisklaizacijaTransactionKartaDto> getFiskal_karte() {
		return fiskal_karte;
	}

	public void setFiskal_karte(List<FisklaizacijaTransactionKartaDto> fiskal_karte) {
		this.fiskal_karte = fiskal_karte;
	}

	public FiskalizacijaTransactionItemDto getFiskal_item() {
		return fiskal_item;
	}


	public void setFiskal_item(FiskalizacijaTransactionItemDto fiskal_item) {
		this.fiskal_item = fiskal_item;
	}



	public static void main(String[] args) {
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

		System.out.println(new Gson().toJson(karta_tran));


	}
}


