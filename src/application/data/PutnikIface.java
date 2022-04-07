package application.data;

import java.util.List;

public interface PutnikIface {
	public List<CenaBean> getCenaa();
	public void setCenaa(List<CenaBean> cenaa);
	public String getImeprezime();
	public void setImeprezime(String imeprezime);
	public String getDatumrodjenja();
	public void setDatumrodjenja(String datumrodjenja);
	public int getSifrapovlastice();
	public void setSifrapovlastice(int sifrapovlastice);
	public String getIdleg();
	public void setIdleg(String idleg);
}
