package application.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import application.data.FrekventneStaniceBean;
import application.data.StanicaIDBean;

public class StanicaNames {
	
	
	private static List<String> _names = null;
	
	public static void loadCirLatinFS(List<FrekventneStaniceBean> frekventne_stanice) {
		Map<String, String> key_latin_names = getStanicaNamesLatin();
		Map<String, String> key_cirilic_names = getStanicaNamesCirilic();
		for(FrekventneStaniceBean current_stanica :  frekventne_stanice) {
			current_stanica.setNaziV_UPUTNE_STANICE_latin(
					key_latin_names.get(current_stanica.getNaziV_UPUTNE_STANICE()) == null ? 
							current_stanica.getNaziV_UPUTNE_STANICE() : key_latin_names.get(current_stanica.getNaziV_UPUTNE_STANICE()));
			current_stanica.setNaziV_UPUTNE_STANICE_cir(
					key_cirilic_names.get(current_stanica.getNaziV_UPUTNE_STANICE()) == null ? 
							current_stanica.getNaziV_UPUTNE_STANICE() : key_cirilic_names.get(current_stanica.getNaziV_UPUTNE_STANICE()));
		}
	}
	
	public static void loadCirLatinOS(List<StanicaIDBean> ostale_stanice) {
		Map<String, String> key_latin_names = getStanicaNamesLatin();
		Map<String, String> key_cirilic_names = getStanicaNamesCirilic();
		for(StanicaIDBean current_stanica :  ostale_stanice) {
			current_stanica.setNaziv_latin(
					key_latin_names.get(current_stanica.getNaziv()) == null ? 
							current_stanica.getNaziv() : key_latin_names.get(current_stanica.getNaziv()));
			current_stanica.setNaziv_cirilic(
					key_cirilic_names.get(current_stanica.getNaziv()) == null ? 
							current_stanica.getNaziv() : key_cirilic_names.get(current_stanica.getNaziv()));
		}
	}
	
	
	private static Map<String, String> getStanicaNamesLatin(){
		Map<String, String> key_latin_names = new HashMap<String, String>();
		if(_names == null) {
			loadNames();
		}
		for(String current_line : _names) {
			String[] splt = current_line.split(",");
			key_latin_names.put(splt[0], splt[1]);
		}
		return key_latin_names;
	}
	
	private static Map<String, String> getStanicaNamesCirilic(){
		Map<String, String> key_cirilic_names = new HashMap<String, String>();
		if(_names == null) {
			loadNames();
		}
		for(String current_line : _names) {
			String[] splt = current_line.split(",");
			key_cirilic_names.put(splt[0], splt[2]);
		}
		return key_cirilic_names;
	}
	
	
	private static void loadNames() {


		_names = new ArrayList<String>();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(
					"srbija_voz_stanice_final.csv"));
			String line = reader.readLine();
			while (line != null) {
				_names.add(line);
				// read next line
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
