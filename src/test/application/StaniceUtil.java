package test.application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;


public class StaniceUtil {
	
	public static void writeFile1(List<String> nazivi_stanica) throws IOException {
		File fout = new File("C:\\projekti\\srbija_voz\\srbija_voz_stanice_final.csv");
		FileOutputStream fos = new FileOutputStream(fout);
	 
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
	 
		for (String current_nuziv : nazivi_stanica) {
			bw.write(current_nuziv );
			bw.newLine();
		}
	 
		bw.close();
	}
	
	public static String convertCyrilic(String message){
	    char[] abcCyr =   {' ','а','б','в','г','д','ђ','е', 'ж','з','ѕ','и','ј','к','л','љ','м','н','њ','о','п','р','с','т', 'ћ','у', 'ф','х','ц','ч','џ','ш', 'А','Б','В','Г','Д','Ђ','Е', 'Ж','З','Ѕ','И','Ј','К','Л','Љ','М','Н','Њ','О','П','Р','С','Т', 'Ћ', 'У','Ф', 'Х','Ц','Ч','Џ','Ш'};
	    String[] abcLat = {" ","a","b","v","g","d","đ","e","ž","z","y","i","j","k","l","lj","m","n","nj","o","p","r","s","t","ć","u","f","h", "c","č", "dž","š","A","B","V","G","D","Đ","E","Ž","Z","Y","I","J","K","L","Lj","M","N","Nj","O","P","R","S","T","Ć","U","F","H", "C","Č", "Dž","Š"};
	    StringBuilder builder = new StringBuilder();
	    for (int i = 0; i < message.length(); i++) {
	        for (int x = 0; x < abcLat.length; x++ ) {
	            if (message.substring(i,i+1).equals(abcLat[x])) {
	                builder.append(abcCyr[x]);
	            }
	        }
	    }
	    return builder.toString();
	}


	public static void main(String[] args) throws Exception{
		
//		List<String> naziv_stanica = new ArrayList<String>();
//		BufferedReader reader;
//		try {
//			reader = new BufferedReader(new FileReader(
//					"C:\\projekti\\srbija_voz\\srbija_voz_stanice.csv"));
//			String line = reader.readLine();
//			while (line != null) {
//				String ime_stanice = line.substring(24, line.length() - 3);
//				System.out.println(ime_stanice);
//				naziv_stanica.add(ime_stanice);
//				// read next line
//				line = reader.readLine();
//			}
//			reader.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		writeFile1(naziv_stanica);
		
		
		List<String> naziv_stanica_latinica = new ArrayList<String>();
		BufferedReader reader_output;
		try {
			reader_output = new BufferedReader(new FileReader(
					"C:\\projekti\\srbija_voz\\srbija_voz_stanice_output.csv"));
			String line = reader_output.readLine();
			while (line != null) {
				String[] splt = line.split(",");
				String ime_stanice_latinica = splt[1];
				System.out.println("latinica = " + ime_stanice_latinica);
				String cirilica_converted = convertCyrilic(ime_stanice_latinica);
				String cirilica_converted_1 = cirilica_converted.replace("лј", "љ");
				String cirilica_converted_2 = cirilica_converted_1.replace("Лј", "Љ");
				String cirilica_converted_3 = cirilica_converted_2.replace("нј", "њ");
				String cirilica_converted_4 = cirilica_converted_3.replace("Нј", "Њ");
				String cirilica_converted_5 = cirilica_converted_4.replace("дж", "џ");
				String cirilica_converted_6 = cirilica_converted_5.replace("Дж", "Џ");
				System.out.println("citilica = " + cirilica_converted_6);
				naziv_stanica_latinica.add(line + "," + cirilica_converted_6);
				//naziv_stanica.add(ime_stanice);
				// read next line
				line = reader_output.readLine();
			}
			reader_output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		writeFile1(naziv_stanica_latinica);
	}
}
