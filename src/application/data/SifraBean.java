package application.data;

public class SifraBean {
	private int sifra = 0;
	private String naziv = "";
	public int getSifra() {
		return sifra;
	}
	public void setSifra(int sifra) {
		this.sifra = sifra;
	}
	public String getNaziv() {
		return naziv;
	}
	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	@Override
	public String toString() {
		return "SifraBean [sifra=" + sifra + ", naziv=" + naziv + "]";
	}
	
	
	
}
