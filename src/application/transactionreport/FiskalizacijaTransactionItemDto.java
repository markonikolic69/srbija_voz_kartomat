package application.transactionreport;



public class FiskalizacijaTransactionItemDto {
	

    private String categoryType;
    private String label;
    private Double amount;
    private Double rate;
    private String categoryName;

	public String getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
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
		return "FiskalizacijaTransactionItemDto [categoryType=" + categoryType + ", label=" + label + ", amount="
				+ amount + ", rate=" + rate + ", categoryName=" + categoryName + "]";
	}
    
    

}
