package application.fiskalizacija;


public class PaymentBean {
	
	private double amount = 0.00;
	private String paymentType = "Cash";
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	@Override
	public String toString() {
		return "PaymentBean [amount=" + amount + ", paymentType=" + paymentType + "]";
	}
	
	

}
