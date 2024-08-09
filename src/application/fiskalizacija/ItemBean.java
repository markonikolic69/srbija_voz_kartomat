package application.fiskalizacija;



import java.util.ArrayList;
import java.util.List;

public class ItemBean {
	
	private String name = "Београдска картица";
	private int quantity = 1;
	private double unitPrice = 0.0;
	private List<String> labels = new ArrayList<String>();
	private double totalAmount = 0.00;
	
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public ItemBean(PoreskaStopa poreska_stopa) {
		labels.add(poreska_stopa.get_oznaka());
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public List<String> getLabels() {
		return labels;
	}
	public void setLabels(List<String> labels) {
		this.labels = labels;
	}
	
	@Override
	public String toString() {
		return "ItemBean [name=" + name + ", quantity=" + quantity + ", unitPrice=" + unitPrice + ", labels=" + labels
				+ ", totalAmount=" + totalAmount + "]";
	}



	
	
	
	

}
