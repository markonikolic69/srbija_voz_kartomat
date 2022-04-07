package test.application.cardpayment.models;

import test.application.cardpayment.MessageStringConstants;

public class ExtendedHoldDataResponse {
	
	
    private String _identifier = "";
    private String _displayMessage = "";
    private String _code = "";
	public String get_identifier() {
		return _identifier;
	}
	public void set_identifier(String _identifier) {
		this._identifier = _identifier;
	}
	public String get_displayMessage() {
		return _displayMessage;
	}
	public void set_displayMessage(String _displayMessage) {
		this._displayMessage = _displayMessage;
	}
	public String get_code() {
		return _code;
	}
	public void set_code(String _code) {
		this._code = _code;
	}

    public void fillObject(String posResponse)
    {
        posResponse = posResponse.replace(MessageStringConstants.STX, "").replace(MessageStringConstants.ETX, "");
        String[] posResponseSplit = posResponse.split( MessageStringConstants.FS );
        String splitItem = posResponseSplit[0];
        
        _identifier = splitItem.substring(0, 2);
        // 00 <- Range(2, 4)
        // 00 <- Range(4, 6)
        // 00 <- Range(6, 8)
        _displayMessage = splitItem.substring(8);
        // FS
        _code = posResponseSplit[1];
        // FS
    }

}
