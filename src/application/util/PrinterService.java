package application.util;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.ArrayList;
import java.util.List;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;

import application.data.KartaBean;
import application.util.impl.PrintServiceImpl;

public class PrinterService implements Printable {

	public List<String> getPrinters(){

		DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
		PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();

		PrintService printServices[] = PrintServiceLookup.lookupPrintServices(
				flavor, pras);

		List<String> printerList = new ArrayList<String>();
		for(PrintService printerService: printServices){
			printerList.add( printerService.getName());
		}

		return printerList;
	}

	public void printKartu(String kartomat_ime, String ticketID, String datum, String vreme, 
			String a_voz_ID, String a_voz_br_sedista, String b_voz_ID, String b_voz_br_sedista,
			String a_stanica_od, String b_stanica_od, String a_stanica_do, String b_stanica_do,
			String a_via, String b_via, String vazi_od_datum, String a_rang, String a_razred,
			String vazi_do, String b_rang, String b_razred, String broj_putnika, String popust_procenat,
			String popust_osnova, String cena, boolean is_povratna, String rezervacija_a, String rezervacija_r, String vreme_o_od, String vreme_p_od) throws Exception{



		new PrintServiceImpl().print_ticket(kartomat_ime, ticketID, datum, vreme, a_voz_ID, a_voz_br_sedista, 
				b_voz_ID, b_voz_br_sedista, a_stanica_od, b_stanica_od, a_stanica_do, b_stanica_do, a_via, b_via, 
				vazi_od_datum, a_rang, a_razred, vazi_do, b_rang, b_razred, broj_putnika, popust_procenat, popust_osnova, cena, is_povratna,
				rezervacija_a, rezervacija_r, vreme_o_od, vreme_p_od);


	}


	//    public void printKartu(KartaBean karta) {
		//    	printString("NPI Integration Driver", karta.for_printing());
		//    	cut();
	//    	printCreditCardSlip(karta.getDATUM_KUPOVINE(), karta.get_vreme_kupovine(), "" + karta.get_ukupna_cena());
	//    	full_cut();
	//    }

	public void printCreditCardSlip(String datum, String vreme, String iznos, String tID, String mID, String companyName, String terminalId,
			String cardNumber, String expirationData, String kartomat_ime, String kartomat_grad, String authorizationCode, String transactionNumber
			) {
		String car_num_to_display = cardNumber.substring(cardNumber.length() - 4);
		StringBuffer buffer = new StringBuffer();
		buffer.append("\n\n\n");
		buffer.append("                  BANKA INTESA\n\n");

		buffer.append("            Kartomat: "+kartomat_ime+"\n");
		buffer.append("                  "+kartomat_grad+"\n\n");
		buffer.append("Terminal                               "+terminalId+"\n");
		buffer.append("Terminal ID                            "+tID+"\n\n");
		buffer.append("mID                                    "+mID+"\n\n");
		buffer.append(companyName + "\n");
		buffer.append("************"+car_num_to_display+"                  Vazi do "+expirationData+"\n");
		buffer.append("KUPOVINA KARTE ZA VOZ\n");
		buffer.append("Datum " + datum + "                      Vreme " + vreme + "\n");
		buffer.append("Br. Tr: "+transactionNumber+"            BR odobrenja: "+authorizationCode+"\n\n");
		buffer.append("IZNOS                                 "+ iznos +" RSD\n");
		buffer.append("                kopija za korisnika" + "\n\n");
		buffer.append("              Banka Intesa AD Beograd" + "\n");
		buffer.append("                  011 30 10 160" + "\n");
		buffer.append("                  kontant centar" + "\n\n\n\n\n");


		printString("NPI Integration Driver", buffer.toString());
//		full_cut();
//
//		//preporuka japanaca da se papir ne bi zaglavljivao
//		print_end();
//		full_cut();
//		form_feed_n();

		//    	StringBuffer buffer_prazan_slip = new StringBuffer();
		//    	buffer_prazan_slip.append("\n\n\n\n\n\n\n\n\n\n\n\n");
		//    	printString("NPI Integration Driver", buffer_prazan_slip.toString());
		//    	full_cut();

	}

	@Override
	public int print(Graphics g, PageFormat pf, int page)
			throws PrinterException {
		if (page > 0) { /* We have only one page, and 'page' is zero-based */
			return NO_SUCH_PAGE;
		}

		/*
		 * User (0,0) is typically outside the imageable area, so we must
		 * translate by the X and Y values in the PageFormat to avoid clipping
		 */
		Graphics2D g2d = (Graphics2D) g;
		g2d.translate(pf.getImageableX(), pf.getImageableY());
		/* Now we perform our rendering */

		g.setFont(new Font("Roman", 0, 8));
		g.drawString("Hello world !", 0, 10);

		return PAGE_EXISTS;
	}

	public void printString(String printerName, String text) {

		// find the printService of name printerName
		DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
		PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();

		PrintService printService[] = PrintServiceLookup.lookupPrintServices(
				flavor, pras);
		PrintService service = findPrintService(printerName, printService);

		DocPrintJob job = service.createPrintJob();

		try {

			byte[] bytes;

			// important for umlaut chars
			bytes = text.getBytes("CP437");

			Doc doc = new SimpleDoc(bytes, flavor, null);


			job.print(doc, null);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void printBytes(String printerName, byte[] bytes) {

		DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
		PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();

		PrintService printService[] = PrintServiceLookup.lookupPrintServices(
				flavor, pras);
		PrintService service = findPrintService(printerName, printService);

		DocPrintJob job = service.createPrintJob();

		try {

			Doc doc = new SimpleDoc(bytes, flavor, null);

			job.print(doc, null);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private PrintService findPrintService(String printerName,
			PrintService[] services) {
		for (PrintService service : services) {
			if (service.getName().equalsIgnoreCase(printerName)) {
				return service;
			}
		}

		return null;
	}

	private void cut() {
		byte[] cutP = new byte[] { 0x1b, 'm' };
		printBytes("NPI Integration Driver", cutP);
	}

	public void full_cut() {
		byte[] cutP = new byte[] { 0x1b, 'i' };
		printBytes("NPI Integration Driver", cutP);
	}

	public void back_feed_n() {
		//back feed 31mm
		byte[] backf = new byte[] { 0x1b, 'B', (byte)0xFA };
		printBytes("NPI Integration Driver", backf);
	}

	public void form_feed_n() {
		//form feed 25 cm
//		byte[] formf = new byte[] { 0x1b, 'j', (byte)0xF8 };
//
//		printBytes("NPI Integration Driver", formf);
		printString("NPI Integration Driver", "\n\n\n\n\n\n\n\n\n\n\n");
	}


	public void print_init() {
		byte[] pr_init = new byte[] { 0x1b, '@', 0x1C, 0x43, 0x01 };
		printBytes("NPI Integration Driver", pr_init);
	}

	public void print_start() {
		byte[] pr_start = new byte[] { 0x1D, 'G', 0x01 };
		printBytes("NPI Integration Driver", pr_start);
	}

	public void print_end() {
		byte[] pr_end = new byte[] { 0x1D, 'G', 0x00 };
		printBytes("NPI Integration Driver", pr_end);
	}


	public static void main(String[] args) throws Exception{

		PrinterService printerService = new PrinterService();

		System.out.println(printerService.getPrinters());


		//        printerService.print_init();
		//        printerService.print_start();
		//        printerService.back_feed_n();
		//        
		//        printerService.printKartu("kartomat_ime", "ticketID", "datum", "vreme", "a_voz_ID", "a_voz_br_sedista", "b_voz_ID", 
		//        		"b_voz_br_sedista", "a_stanica_od", "b_stanica_od", "a_stanica_do", "b_stanica_do", "a_via", "b_via", "vazi_od_datum", 
		//        		"a_rang", "a_razred", "vazi_do", "b_rang", "b_razred", "broj_putnika", "popust_procenat", "popust_osnova", "cena", 
		//        		false, "rezervacija_a", "rezervacija_r", "vreme_o_od", "vreme_p_od");
		//        
		//        
		//        printerService.printCreditCardSlip("datum", "vreme", "iznos", "tID", "mID", "companyName", "terminalId", 
		//        		"cardNumber", "expirationData", "kartomat_ime", "kartomat_grad", "authorizationCode", "transactionNumber");
		//
		////        printerService.print_end();
		//        printerService.full_cut();
//		printerService.back_feed_n();
//		printerService.print_init();
//		printerService.print_start();



		printerService.printKartu("kartomat_ime", "ticketID", "datum", "vreme", "a_voz_ID", "a_voz_br_sedista", "b_voz_ID", 
				"b_voz_br_sedista", "a_stanica_od", "b_stanica_od", "a_stanica_do", "b_stanica_do", "a_via", "b_via", "vazi_od_datum", 
				"a_rang", "a_razred", "vazi_do", "b_rang", "b_razred", "broj_putnika", "popust_procenat", "popust_osnova", "cena", 
				false, "rezervacija_a", "rezervacija_r", "vreme_o_od", "vreme_p_od");


		printerService.printCreditCardSlip("datum", "vreme", "iznos", "tID", "mID", "companyName", "terminalId", 
				"cardNumber", "expirationData", "kartomat_ime", "kartomat_grad", "authorizationCode", "transactionNumber");



//		printerService.print_end();
//		printerService.full_cut();
//		printerService.form_feed_n();
		//printerService.printString("NPI Integration Driver", "\n\n\n\n\n\n\n\n\n\n\n");

		
	}



}
