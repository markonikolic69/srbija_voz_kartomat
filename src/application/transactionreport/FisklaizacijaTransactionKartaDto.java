package application.transactionreport;


public class FisklaizacijaTransactionKartaDto {
	

	    private String name;

	    private int quantity;

	    private Double amount;

	    private Double unitPrice;

	    private Double totalPrice;

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

		public Double getAmount() {
			return amount;
		}

		public void setAmount(Double amount) {
			this.amount = amount;
		}

		public Double getUnitPrice() {
			return unitPrice;
		}

		public void setUnitPrice(Double unitPrice) {
			this.unitPrice = unitPrice;
		}

		public Double getTotalPrice() {
			return totalPrice;
		}

		public void setTotalPrice(Double totalPrice) {
			this.totalPrice = totalPrice;
		}

		@Override
		public String toString() {
			return "FisklaizacijaTransactionKartaDto [name=" + name + ", quantity=" + quantity + ", amount=" + amount
					+ ", unitPrice=" + unitPrice + ", totalPrice=" + totalPrice + "]";
		}
	    
	    

}
