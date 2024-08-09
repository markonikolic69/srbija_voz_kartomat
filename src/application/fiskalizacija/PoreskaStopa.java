package application.fiskalizacija;


public enum PoreskaStopa {
	
	DVADESET_POSTO(20.00, "Ђ", 1.20),
	BEZ_PDV(00.00, "Г", 1.00),
	DEVET_TEST_POSTO_TEST(9.00, "A", 1.09),
	DESET_POSTO(10.00, "Е", 1.10);
	
	
	
	private double _postotak = 0.00;
	private String _oznaka = "";
	private double _za_izrac_osnov = 0.00;

	PoreskaStopa(double postotak, String oznaka, double za_izracunavanje_oznovice){
		_postotak = postotak;
		_oznaka = oznaka;
		_za_izrac_osnov = za_izracunavanje_oznovice;
	}
	public double get_postotak() {
		return _postotak;
	}
	public void set_postotak(double _postotak) {
		this._postotak = _postotak;
	}
	public String get_oznaka() {
		return _oznaka;
	}
	public void set_oznaka(String _oznaka) {
		this._oznaka = _oznaka;
	}
	
	public double get_za_izrac_osnov() {
		return _za_izrac_osnov;
	}
	
}
