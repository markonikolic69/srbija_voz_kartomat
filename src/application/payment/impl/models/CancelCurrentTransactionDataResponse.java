package application.payment.impl.models;

import application.payment.MessageStringConstants;


public class CancelCurrentTransactionDataResponse {
	
	
	private String _identifier = "";
	private String _responseMessage = "";
	
	public String get_identifier() {
		return _identifier;
	}
	public void set_identifier(String _identifier) {
		this._identifier = _identifier;
	}
	public String get_responseMessage() {
		return _responseMessage;
	}
	public void set_responseMessage(String _responseMessage) {
		this._responseMessage = _responseMessage;
	}
	
	
    public void fillObject(String posResponse)
    {
        posResponse = posResponse.replace(MessageStringConstants.STX, "").replace(MessageStringConstants.ETX, "");
        String[] posResponseSplit = posResponse.split(MessageStringConstants.FS);
        String splitItem = posResponseSplit[0];

        _identifier = splitItem.substring(0, 2);
        // 00 <- Range(2, 4)
        // 00 <- Range(4, 6)
        // 00 <- Range(6, 8)
        _responseMessage = splitItem.substring(8);
    }

}
