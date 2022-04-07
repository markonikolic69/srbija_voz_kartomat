package application.https;

public class CommunicationException extends Exception {
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CommunicationException() {
		super("Doslo je do greske u kounikaciji sa serverom");
	}
	
	public CommunicationException(String message) {
		super(message);
	}
	
}
