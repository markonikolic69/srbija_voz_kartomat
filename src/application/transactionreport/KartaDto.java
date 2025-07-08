package application.transactionreport;



public class KartaDto {

	

	private Double cena;
	

	private String selected_razred_polazak;
	

	private String selected_razred_povratak;
	

	private int povratna_cena;
	

	private String karta_tip;


	private String legitimacija_id;


	public Double getCena() {
		return cena;
	}


	public void setCena(Double cena) {
		this.cena = cena;
	}


	public String getSelected_razred_polazak() {
		return selected_razred_polazak;
	}


	public void setSelected_razred_polazak(String selected_razred_polazak) {
		this.selected_razred_polazak = selected_razred_polazak;
	}


	public String getSelected_razred_povratak() {
		return selected_razred_povratak;
	}


	public void setSelected_razred_povratak(String selected_razred_povratak) {
		this.selected_razred_povratak = selected_razred_povratak;
	}


	public int getPovratna_cena() {
		return povratna_cena;
	}


	public void setPovratna_cena(int povratna_cena) {
		this.povratna_cena = povratna_cena;
	}


	public String getKarta_tip() {
		return karta_tip;
	}


	public void setKarta_tip(String karta_tip) {
		this.karta_tip = karta_tip;
	}


	public String getLegitimacija_id() {
		return legitimacija_id;
	}


	public void setLegitimacija_id(String legitimacija_id) {
		this.legitimacija_id = legitimacija_id;
	}


	@Override
	public String toString() {
		return "KartaDto [cena=" + cena + ", selected_razred_polazak=" + selected_razred_polazak
				+ ", selected_razred_povratak=" + selected_razred_povratak + ", povratna_cena=" + povratna_cena
				+ ", karta_tip=" + karta_tip + ", legitimacija_id=" + legitimacija_id + "]";
	}
	
	

}
