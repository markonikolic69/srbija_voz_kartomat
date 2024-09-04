package application.https;

public class CommunicationException extends Exception {
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CommunicationException() {
		super("Došlo je do greške u komunikaciji sa serverom");
	}
	
	public CommunicationException(String message) {
		super(message);
	}
	
}
