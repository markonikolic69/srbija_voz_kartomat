package test.application;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;
import net.sourceforge.barbecue.env.DefaultEnvironment;

public class BmpWriteText {

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		
		
		File add_text = new File("C:\\npi_printer_srbija_voz\\K_2b2.bmp");
		
		BufferedImage image = ImageIO.read(add_text);
		
		Font font = new Font("Montserrat", Font.BOLD, 20);

		Graphics g = image.getGraphics();
		g.setFont(font);
		g.setColor(Color.BLACK);
		// mesto gde je prodata karta
		g.drawString("PANČEVAČKI MOST", 40, 80);
		// ID karte
		g.drawString("16013 009200344", 320, 80);
		// datum
		g.drawString("13.01.2022", 570, 80);
		// vreme
		g.drawString("13:39:08", 680, 80);	
		// A VOZ
		g.drawString("VOZ", 70, 147);	
		// A VOZ
		g.drawString("791", 150, 147);
		// A VOZ /
		g.drawString("/", 210, 147);
		// A VOZ broj sedista
		g.drawString("123", 250, 147);
		// B VOZ
		g.drawString("VOZ", 530, 147);	
		// B VOZ
		g.drawString("791", 610, 147);
		// B VOZ /
		g.drawString("/", 670, 147);
		// B VOZ broj sedista
		g.drawString("123", 710, 147);
		
		// A Od
		g.drawString("Од", 15, 200);
		// А полазиште
		g.drawString("PANČEVAČKI MOST", 70, 200);
		
		// B Od
		g.drawString("Од", 490, 200);
		// B Polazište
		g.drawString("LAPOVO", 540, 200);
		
		// A Odredište
		g.drawString("LAPOVO", 70, 255);
		// B Odredište
		g.drawString("PANČEVAČKI MOST", 540, 255);
		
		
		// A VIA
		g.drawString("MALA KRSNA", 70, 312);
		// B VIA
		g.drawString("MALA KRSNA", 540, 312);
		
		// A RAZRED
		g.drawString("Разред", 110, 363);
		// VAZI OD
		g.drawString("Од", 290, 363);
		// VAZI OD DATUM
		g.drawString("05.01.2022", 350, 363);
		// B RAZRED
		g.drawString("Разред", 690, 363);
				
		// A RANG
		g.drawString("REGIO", 15, 420);
		// A RAZRED вредност
		g.drawString("2", 110, 420);
		// VAZI DO DATUM
		g.drawString("19.01.2022", 350, 420);	
		// B RANG
		g.drawString("REGIO", 605, 420);
		// B RAZRED вредност
		g.drawString("2", 690, 420);
		
		// broj putnika
		g.drawString("01", 50, 470);
		
		// popust procenat
		g.drawString("20", 170, 470);
		
		// popust osnova
		g.drawString("POVRATNA", 290, 470);
		
		// cena
		g.drawString("12546.00", 690, 530);
		
		
		g.drawImage(generateEAN13BarcodeImage("16013009200344"), 436, 510, null);
				
		File outputfile = new File("C:\\npi_printer_srbija_voz\\K_2b2_output.bmp");
		
		
		ImageIO.write(image, "bmp", outputfile);
		
		
		
		BufferedImage image_rotated = rotateClockwise90(ImageIO.read(outputfile));
		
		File outputfile_rotated = new File("C:\\npi_printer_srbija_voz\\K_2b2_output_rotated.bmp");
		ImageIO.write(image_rotated, "bmp", outputfile_rotated);
		
//		BufferedImage image_for_barcode = ImageIO.read(outputfile);
//		Graphics g_barcode = image_for_barcode.getGraphics();
//		g_barcode.drawImage(generateEAN13BarcodeImage("160130092003"), 436, 529, null);
//		File outputfile_barcode = new File("C:\\npi_printer_srbija_voz\\K_2b2_output_barcode.bmp");
//		ImageIO.write(image_for_barcode, "bmp", outputfile_barcode);
	}
	
	public static void generateBarCode(String barcodeText) {
		
	}
	
	public static BufferedImage generateEAN13BarcodeImage(String barcodeText) throws Exception {
	    Barcode barcode = BarcodeFactory.createCode128(barcodeText);
	    //barcode.setFont(DefaultEnvironment.DEFAULT_FONT);
	    //barcode.setBarWidth(150);
	    barcode.setPreferredBarHeight(50);
	    return BarcodeImageHandler.getImage(barcode);
	}
	
	public static BufferedImage rotateClockwise90(BufferedImage src) {
	    int w = src.getWidth();
	    int h = src.getHeight();
	    BufferedImage dest = new BufferedImage(h, w, src.getType());
	    for (int y = 0; y < h; y++) 
	        for (int x = 0; x < w; x++) 
	            dest.setRGB(y, w - x - 1, src.getRGB(x, y));
	    return dest;
	}

}
