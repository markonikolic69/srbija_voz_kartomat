package application;

import org.apache.log4j.Logger;

import application.https.SrbijaVozIfaceFactory;

public class GetListaVozovaPolazakThread implements Runnable{
	
	private String _url;
	private int _conn_time;
	private int _read_time;
	private int _sifra_stanice_od;
	private int _sifra_stanice_do;
	private String _datum;
	private int _broj_putnika;
	private int _razred;
	private IGetListaVozovaPolasci _callback;
	
	private static final Logger logger = Logger.getLogger("GetListaVozovaPolazakThread");
	
	public GetListaVozovaPolazakThread(IGetListaVozovaPolasci callback, String url, int conn_time, int read_time, 
			int sifra_stanice_od, int sifra_stanice_do, String datum, int broj_putnika, int razred) {
		_url = url;
		_conn_time = conn_time;
		_read_time = read_time;
		_sifra_stanice_od = sifra_stanice_od;
		_sifra_stanice_do = sifra_stanice_do;
		_datum = datum;
		_broj_putnika = broj_putnika;
		_razred = razred;
		_callback = callback;
	}

	
	public void run() {
		try {
			logger.info("run, params: _url = " + _url + ", _sifra_stanice_od = " + _sifra_stanice_od + ", _sifra_stanice_do = " + _sifra_stanice_do + 
					", _datum = " + _datum + ", _broj_putnika = " + _broj_putnika + ", _razred = " + _razred   );
			_callback.setListaVozovaPolasciCallbackSuccess(SrbijaVozIfaceFactory.getIface(_url, _conn_time, _read_time).getListaVozovaNaTrasiNew(_sifra_stanice_od, 
				_sifra_stanice_do, _datum, 
				_broj_putnika, _razred));
		}catch(Exception e) {
			_callback.setListaVozovaPolasciCallbackNOTSuccess("Error 1");
		}
	}
}
