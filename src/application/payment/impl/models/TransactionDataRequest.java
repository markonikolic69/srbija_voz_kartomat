package application.payment.impl.models;

import application.payment.MessageStringConstants;
import application.payment.impl.helpers.MessageIdentifiers;

public class TransactionDataRequest implements IDataRequest {


	// always default 00
	private String _identifier = MessageIdentifiers.TRANSACTION_REQUEST;

	private String _terminalId = "00";

	private String _sourceId = "00";

	private String _sequentialNumber = "0000";

	//default 01        

	private String _transactionType  = "01";

	// 0 : Receipt printed by the ECR
	// 1 : Receipt printed by the POS 

	private String _printerFlag = "1";

	private String _cashierId = "00";

	//optional
	private String _transactionNumber = "";

	//FS
	//amount of transaction.The length of the field is 6
	private String _transactionAmount1 = "";

	//FS//FS
	// default : +0
	private String _amountExponent  = "+0";

	//FS
	//941
	private String _amountCurrency = "";

	//FS//FS//FS//FS
	//optional
	private String _authorizationCode = "";

	//FS//FS//FS
	//optional
	private String _inputLabel = "";

	//FS
	//optional
	private String _insurancePolicyNumber = "";

	//FS
	//optional
	private String _installmentsNumber = "";

	//FS
	//optional
	private String _minimumInputLength = "";

	//optional
	private String _maximumInputLength = "";

	//optional
	// 0: input data not masked
	// 1: input data masked
	private String _maskInputData = "";

	//FS
	//optional
	// default: 00        
	private String _languageId  = "00";

	//FS
	//optional
	private String _printData = "";

	//FS
	//optional
	private String _cashierId2 = "";

	//FS
	//optional
	private String _transactionAmount2 = "";

	//FS
	//optional
	private String _payservicesData = "";

	//FS
	//optional
	private String _transactionActivationCode ="";

	//FS
	//optional
	private String _instantPaymentReference = "";

	//FS
	//optional
	private String _qrCodeData = "";




	@Override
	public String getIdentifier() {
		return _identifier;
	}

	@Override
	public void setIdentifier(String identifier) {
		_identifier = identifier;

	}

	public String get_identifier() {
		return _identifier;
	}

	public void set_identifier(String _identifier) {
		this._identifier = _identifier;
	}

	public String get_terminalId() {
		return _terminalId;
	}

	public void set_terminalId(String _terminalId) {
		this._terminalId = _terminalId;
	}

	public String get_sourceId() {
		return _sourceId;
	}

	public void set_sourceId(String _sourceId) {
		this._sourceId = _sourceId;
	}

	public String get_sequentialNumber() {
		return _sequentialNumber;
	}

	public void set_sequentialNumber(String _sequentialNumber) {
		this._sequentialNumber = _sequentialNumber;
	}

	public String get_transactionType() {
		return _transactionType;
	}

	public void set_transactionType(String _transactionType) {
		this._transactionType = _transactionType;
	}

	public String get_printerFlag() {
		return _printerFlag;
	}

	public void set_printerFlag(String _printerFlag) {
		this._printerFlag = _printerFlag;
	}

	public String get_cashierId() {
		return _cashierId;
	}

	public void set_cashierId(String _cashierId) {
		this._cashierId = _cashierId;
	}

	public String get_transactionNumber() {
		return _transactionNumber;
	}

	public void set_transactionNumber(String _transactionNumber) {
		this._transactionNumber = _transactionNumber;
	}

	public String get_transactionAmount1() {
		return _transactionAmount1;
	}

	public void set_transactionAmount1(String _transactionAmount1) {
		this._transactionAmount1 = _transactionAmount1;
	}

	public String get_amountExponent() {
		return _amountExponent;
	}

	public void set_amountExponent(String _amountExponent) {
		this._amountExponent = _amountExponent;
	}

	public String get_amountCurrency() {
		return _amountCurrency;
	}

	public void set_amountCurrency(String _amountCurrency) {
		this._amountCurrency = _amountCurrency;
	}

	public String get_authorizationCode() {
		return _authorizationCode;
	}

	public void set_authorizationCode(String _authorizationCode) {
		this._authorizationCode = _authorizationCode;
	}

	public String get_inputLabel() {
		return _inputLabel;
	}

	public void set_inputLabel(String _inputLabel) {
		this._inputLabel = _inputLabel;
	}

	public String get_insurancePolicyNumber() {
		return _insurancePolicyNumber;
	}

	public void set_insurancePolicyNumber(String _insurancePolicyNumber) {
		this._insurancePolicyNumber = _insurancePolicyNumber;
	}

	public String get_installmentsNumber() {
		return _installmentsNumber;
	}

	public void set_installmentsNumber(String _installmentsNumber) {
		this._installmentsNumber = _installmentsNumber;
	}

	public String get_minimumInputLength() {
		return _minimumInputLength;
	}

	public void set_minimumInputLength(String _minimumInputLength) {
		this._minimumInputLength = _minimumInputLength;
	}

	public String get_maximumInputLength() {
		return _maximumInputLength;
	}

	public void set_maximumInputLength(String _maximumInputLength) {
		this._maximumInputLength = _maximumInputLength;
	}

	public String get_maskInputData() {
		return _maskInputData;
	}

	public void set_maskInputData(String _maskInputData) {
		this._maskInputData = _maskInputData;
	}

	public String get_languageId() {
		return _languageId;
	}

	public void set_languageId(String _languageId) {
		this._languageId = _languageId;
	}

	public String get_printData() {
		return _printData;
	}

	public void set_printData(String _printData) {
		this._printData = _printData;
	}

	public String get_cashierId2() {
		return _cashierId2;
	}

	public void set_cashierId2(String _cashierId2) {
		this._cashierId2 = _cashierId2;
	}

	public String get_transactionAmount2() {
		return _transactionAmount2;
	}

	public void set_transactionAmount2(String _transactionAmount2) {
		this._transactionAmount2 = _transactionAmount2;
	}

	public String get_payservicesData() {
		return _payservicesData;
	}

	public void set_payservicesData(String _payservicesData) {
		this._payservicesData = _payservicesData;
	}

	public String get_transactionActivationCode() {
		return _transactionActivationCode;
	}

	public void set_transactionActivationCode(String _transactionActivationCode) {
		this._transactionActivationCode = _transactionActivationCode;
	}

	public String get_instantPaymentReference() {
		return _instantPaymentReference;
	}

	public void set_instantPaymentReference(String _instantPaymentReference) {
		this._instantPaymentReference = _instantPaymentReference;
	}

	public String get_qrCodeData() {
		return _qrCodeData;
	}

	public void set_qrCodeData(String _qrCodeData) {
		this._qrCodeData = _qrCodeData;
	}


	
	public String create()
    {
		StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(MessageStringConstants.STX);
        stringBuilder.append(MessageStringConstants.STX);
        stringBuilder.append(_identifier);
        stringBuilder.append(_terminalId);
        stringBuilder.append(_sourceId);
        stringBuilder.append(_sequentialNumber);
        stringBuilder.append(_transactionType);
        stringBuilder.append(_printerFlag);
        stringBuilder.append(_cashierId);
        stringBuilder.append(_transactionNumber);
        stringBuilder.append(MessageStringConstants.FS);
        stringBuilder.append(_transactionAmount1);
        stringBuilder.append(MessageStringConstants.FS);
        stringBuilder.append(MessageStringConstants.FS);
        stringBuilder.append(_amountExponent);
        stringBuilder.append(MessageStringConstants.FS);
        stringBuilder.append(_amountCurrency);
        stringBuilder.append(MessageStringConstants.FS);
        stringBuilder.append(MessageStringConstants.FS);
        stringBuilder.append(MessageStringConstants.FS);
        stringBuilder.append(MessageStringConstants.FS);
        stringBuilder.append(_authorizationCode);
        stringBuilder.append(MessageStringConstants.FS);
        stringBuilder.append(MessageStringConstants.FS);
        stringBuilder.append(MessageStringConstants.FS);
        stringBuilder.append(_inputLabel);
        stringBuilder.append(MessageStringConstants.FS);
        stringBuilder.append(_insurancePolicyNumber);
        stringBuilder.append(MessageStringConstants.FS);
        stringBuilder.append(_installmentsNumber);
        stringBuilder.append(MessageStringConstants.FS);
        stringBuilder.append(_minimumInputLength);
        stringBuilder.append(_maximumInputLength);
        stringBuilder.append(_maskInputData);
        stringBuilder.append(MessageStringConstants.FS);
        stringBuilder.append(_languageId);
        stringBuilder.append(MessageStringConstants.FS);
        stringBuilder.append(_printData);
        stringBuilder.append(MessageStringConstants.FS);
        stringBuilder.append(_cashierId2);
        stringBuilder.append(MessageStringConstants.FS);
        stringBuilder.append(_transactionAmount2);
        stringBuilder.append(MessageStringConstants.FS);
        stringBuilder.append(_payservicesData);
        stringBuilder.append(MessageStringConstants.FS);
        stringBuilder.append(_transactionActivationCode);
        stringBuilder.append(MessageStringConstants.FS);
        stringBuilder.append(_instantPaymentReference);
        stringBuilder.append(MessageStringConstants.FS);
        stringBuilder.append(_qrCodeData);
        stringBuilder.append(MessageStringConstants.FS);
        stringBuilder.append(MessageStringConstants.ETX);
        return stringBuilder.toString();
    }

}

