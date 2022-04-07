package test.application.cardpayment.models;

import test.application.cardpayment.MessageStringConstants;

public class CardDataResponse {
	
    private String _identifier = "";
    private String _cardDataSource = "";
    private String _cardHash = "";
	public String get_identifier() {
		return _identifier;
	}
	public void set_identifier(String _identifier) {
		this._identifier = _identifier;
	}
	public String get_cardDataSource() {
		return _cardDataSource;
	}
	public void set_cardDataSource(String _cardDataSource) {
		this._cardDataSource = _cardDataSource;
	}
	public String get_cardHash() {
		return _cardHash;
	}
	public void set_cardHash(String _cardHash) {
		this._cardHash = _cardHash;
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
        _cardDataSource = splitItem.substring(8, 1);
        // FS
        _cardHash = posResponseSplit[1];
        // FS
    }

}
