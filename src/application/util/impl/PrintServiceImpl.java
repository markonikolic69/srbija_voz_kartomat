package application.util.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;

public class PrintServiceImpl {
	
	
	private String _blanc_ticket_location = "";
	
	public PrintServiceImpl(String blanc_ticket_location) {
		_blanc_ticket_location = blanc_ticket_location;
	}
	
	public PrintServiceImpl() {
		_blanc_ticket_location = "C:\\npi_printer_srbija_voz\\K_2b2.bmp";
	}
	
	
	
	
	public void print_ticket(String kartomat_ime, String ticketID, String datum, String vreme, 
    		String a_voz_ID, String a_voz_br_sedista, String b_voz_ID, String b_voz_br_sedista,
    		String a_stanica_od, String b_stanica_od, String a_stanica_do, String b_stanica_do,
    		String a_via, String b_via, String vazi_od_datum, String a_rang, String a_razred,
    		String vazi_do, String b_rang, String b_razred, String broj_putnika, String popust_procenat,
    		String popust_osnova, String cena, boolean is_povratna, String rezervacija_a, String rezervacija_r, String vreme_o_od, String vreme_p_od) throws Exception{
		
		
		String[] splt = popust_osnova.split("%");
		if(splt.length == 2) {
			popust_procenat = splt[0];
			popust_osnova = splt[1];
		}
		
		
		String rang_a_opis = "";
		String[] splt_a_voz_ID = a_voz_ID.split(":");
		if(splt_a_voz_ID.length > 1) {
			String[] splt_a_voz_ID_again = splt_a_voz_ID[1].split("-");
			//if(splt_a_voz_ID_again.length > 1) {
			a_voz_ID = splt_a_voz_ID_again[0];
			if(splt_a_voz_ID_again.length > 1) {
				rang_a_opis = splt_a_voz_ID_again[1].substring(1);
			}
			//}
		}
		
		String rang_b_opis = "";
		if(is_povratna) {
			String[] splt_b_voz_ID = b_voz_ID.split(":");
			if(splt_b_voz_ID.length > 1) {
				String[] splt_b_voz_ID_again = splt_b_voz_ID[1].split("-");
				//if(splt_a_voz_ID_again.length > 1) {
				b_voz_ID = splt_b_voz_ID_again[0];
				if(splt_b_voz_ID_again.length > 1) {
					rang_b_opis = splt_b_voz_ID_again[1].substring(1);
				}
				//}
			}
		}
		File add_text = new File(_blanc_ticket_location);
		
		BufferedImage image = ImageIO.read(add_text);
		
		Font font_mali = new Font("Montserrat", Font.PLAIN, 20);
		Font font_mali_bold = new Font("Montserrat", Font.BOLD, 20);
		Font font = new Font("Montserrat", Font.BOLD, 25);
		Font font_veci = new Font("Montserrat", Font.BOLD, 30);
		Graphics g = image.getGraphics();
		g.setColor(Color.BLACK);
		

		// A VOZ /
		//g.drawString("/", 210, 147);
		// A VOZ broj sedista
		//g.drawString(!_sedista, 250, 147);
		// B VOZ
		//g.drawString("VOZ", 530, 147);	

		// B VOZ /
		//g.drawString("/", 670, 147);
		// B VOZ broj sedista
		//g.drawString(b_voz_br_sedista, 710, 147);
		
		// A Od
		g.setFont(font_mali);
		//g.drawString("Од", 15, 200);
		// B Od		
		//g.drawString("Од", 490, 200);
		// A RAZRED
		g.drawString("Разред", 110, 363);
		// VAZI OD
		g.drawString("Од", 290, 363);
		// B RAZRED
		g.drawString("Разред", 690, 363);
		
		g.setFont(font_mali_bold);
		
		// A rezervacija
		g.drawString(rezervacija_a, 16, 316);
		
		// B rezervacija
		g.drawString(rezervacija_r, 410, 316);
		
		// popust osnova
		g.drawString(popust_osnova, 290, 470);
		
		// A VOZ
		g.drawString("VOZ", 70, 147);	

		

		if(!rezervacija_r.equals("")) {
			// B VOZ
			g.drawString("VOZ", 460, 147);

			
		}
		
		//ponovo normalni font
		
		g.setFont(font);

		// mesto gde je prodata karta
		g.drawString(kartomat_ime, 40, 80);
		// ID karte
		g.drawString(ticketID, 330, 80);
		// datum
		g.drawString(datum, 540, 80);
		// vreme
		g.drawString(vreme, 680, 80);	
		
		// A VOZ
		g.drawString(a_voz_ID, 150, 147);

		if(!rezervacija_a.equals("")) {
			// VREME A VOZ
			g.drawString("Pol: " + vreme_o_od, 215, 147);
		}
		
		if(!rezervacija_r.equals("")) {
			// B VOZ

			g.drawString( b_voz_ID, 540, 147);
			
			g.drawString("Pol: " + vreme_p_od, 610, 147);
			
		}

		// А полазиште
		g.drawString(a_stanica_od, 70, 187);
		

		// B Polazište
		if(is_povratna)g.drawString(b_stanica_od, 461, 187);
		
		// A Odredište
		g.drawString(a_stanica_do, 70, 227);
		// B Odredište
		if(is_povratna)g.drawString(b_stanica_do, 461, 227);
		
		

		
		

		
		
		// A VIA
		//g.drawString(a_via, 70, 312);
		// B VIA
		//g.drawString(b_via, 540, 312);
		

		// VAZI OD DATUM
		g.drawString(vazi_od_datum, 350, 363);

				
		// A RANG
		g.drawString(rang_a_opis/*a_rang*/, 15, 420);
		// A RAZRED вредност
		g.drawString(a_razred, 140, 420);
		// VAZI DO DATUM
		g.drawString(vazi_do, 350, 420);	
		// B RANG
		g.drawString(rang_b_opis/*b_rang*/, 605, 420);
		// B RAZRED вредност
		g.drawString(b_razred, 720, 420);
		
		// broj putnika
		g.drawString(broj_putnika, 50, 470);
		
		// popust procenat
		g.drawString(popust_procenat, 170, 470);
		

		
		// cena
		g.setFont(font_veci);
		g.drawString(cena + "0", 680, 530);
		g.setFont(font);
		
		g.drawImage(generateEAN13BarcodeImage(ticketID), 426, 490, null);
				
		File outputfile = new File("C:\\npi_printer_srbija_voz\\K_2b2_output.bmp");
		
		
		ImageIO.write(image, "bmp", outputfile);
		
		
		
		BufferedImage image_rotated = rotateClockwise90(ImageIO.read(outputfile));
		
		File outputfile_rotated = new File("C:\\npi_printer_srbija_voz\\K_2b2_output_rotated.bmp");
		ImageIO.write(image_rotated, "bmp", outputfile_rotated);
		
		/////////////////////////printing
		
		new PrintBMPFile("NPI Integration Driver").printImage(outputfile_rotated);
		
		
		outputfile.deleteOnExit();
		outputfile_rotated.deleteOnExit();
		
	}
	
	private static BufferedImage generateEAN13BarcodeImage(String barcodeText) throws Exception {
	    Barcode barcode = BarcodeFactory.createCode128(barcodeText);
	    //barcode.setFont(DefaultEnvironment.DEFAULT_FONT);
	    //barcode.setBarWidth(150);
	    barcode.setPreferredBarHeight(60);//bilo 50
	    return BarcodeImageHandler.getImage(barcode);
	}
	
	private static BufferedImage rotateClockwise90(BufferedImage src) {
	    int w = src.getWidth();
	    int h = src.getHeight();
	    BufferedImage dest = new BufferedImage(h, w, src.getType());
	    for (int y = 0; y < h; y++) 
	        for (int x = 0; x < w; x++) 
	            dest.setRGB(y, w - x - 1, src.getRGB(x, y));
	    return dest;
	}

}
