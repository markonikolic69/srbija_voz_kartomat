package test.application.cardpayment;

public class TransactionFlags {

    public static final String ERROR_IN_FORMAT_REQUEST = "00";
    public static final String TRANSACTION_ACCEPTED_WITHOUT_AUTHORIZATION_CODE = "01";
    public static final String TRANSACTION_ACCEPTED_WITH_AUTHORIZATION_CODE = "02";
    public static final String TRANSACTION_REFUSED = "04";
    public static final String TERMINAL_ID = "05";
    public static final String COMMUNICATION_ERROR = "06";
    public static final String SLEEPMODE = "07";
    
}
