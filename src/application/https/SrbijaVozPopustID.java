package application.https;

public enum SrbijaVozPopustID {
	
	
	REDOVNA_CENA(new Integer(1)),
	POVRATNA(new Integer(2)),
	SRB_PLUS_K_13(new Integer(3)),
	RAIL_PLUS_K_30(new Integer(4)),
	DETE( new Integer(5)),
	PAS( new Integer(6));
	
	
	
	
	public static final int ID_REDOVNA_CENA = 1;
	public static final int ID_POVRATNA = 2;
	public static final int ID_SRB_PLUS_K_13 = 3;
	public static final int ID_RAIL_PLUS_K_30 = 4;
	public static final int ID_DETE = 5;
	public static final int ID_PAS = 6;
	
	
	public Integer _app_id = 0;
	SrbijaVozPopustID( Integer app_id){
		_app_id = app_id;
	}

}
