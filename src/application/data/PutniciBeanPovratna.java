package application.data;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class PutniciBeanPovratna implements PutnikIface{
	
		private List<CenaBean> cenap = new ArrayList<CenaBean>();
		
		private String imeprezime = "";
		private String datumrodjenja = "";
		private int sifrapovlastice = 1;
		private String idleg = "";
		public List<CenaBean> getCenaa() {
			return cenap;
		}
		public void setCenaa(List<CenaBean> cenap) {
			this.cenap = cenap;
		}
		public String getImeprezime() {
			return imeprezime;
		}
		public void setImeprezime(String imeprezime) {
			this.imeprezime = imeprezime;
		}
		public String getDatumrodjenja() {
			return datumrodjenja;
		}
		public void setDatumrodjenja(String datumrodjenja) {
			this.datumrodjenja = datumrodjenja;
		}
		public int getSifrapovlastice() {
			return sifrapovlastice;
		}
		public void setSifrapovlastice(int sifrapovlastice) {
			this.sifrapovlastice = sifrapovlastice;
		}
		public String getIdleg() {
			return idleg;
		}
		public void setIdleg(String idleg) {
			this.idleg = idleg;
		}
		@Override
		public String toString() {
			return "PutniciBean [cenap=" + cenap + ", imeprezime=" + imeprezime + ", datumrodjenja=" + datumrodjenja
					+ ", sifrapovlastice=" + sifrapovlastice + ", idleg=" + idleg + "]";
		}
		
		
		public String toJson() {
			Gson gson = new Gson();
			return gson.toJson(this);
		}

}
