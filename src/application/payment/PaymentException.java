package application.payment;

public class PaymentException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PaymentException(String reason) {
		super(reason);
	}
}
