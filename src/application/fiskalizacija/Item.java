package application.fiskalizacija;
import com.google.gson.internal.LinkedTreeMap;

public class Item {
	
	private int categoryType = 0;
	private String label = "";
	private double amount = 0.0;
	private double rate = 0.0;
	private String categoryName = "";
	
	
	public Item (LinkedTreeMap gson_container) {
		categoryType = Integer.parseInt(gson_container.getOrDefault("categoryType", "0").toString());
		label = gson_container.getOrDefault("label", "").toString();
		amount = Double.parseDouble(gson_container.getOrDefault("amount", "0.0").toString());
		rate = Double.parseDouble(gson_container.getOrDefault("rate", "0.0").toString());
		categoryName = gson_container.getOrDefault("categoryName", "").toString();
	}
	
	public Item () {

	}


	public int getCategoryType() {
		return categoryType;
	}


	public void setCategoryType(int categoryType) {
		this.categoryType = categoryType;
	}


	public String getLabel() {
		return label;
	}


	public void setLabel(String label) {
		this.label = label;
	}


	public double getAmount() {
		return amount;
	}


	public void setAmount(double amount) {
		this.amount = amount;
	}


	public double getRate() {
		return rate;
	}


	public void setRate(double rate) {
		this.rate = rate;
	}


	public String getCategoryName() {
		return categoryName;
	}


	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}


	@Override
	public String toString() {
		return "Item [categoryType=" + categoryType + ", label=" + label + ", amount=" + amount + ", rate=" + rate
				+ ", categoryName=" + categoryName + "]";
	}
	
	

}
