package test.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class PowerShellPrinterStatus {
	
	public static final String PRINTER_OK = "0:3";
	public static final String PRINTER_OFFLINE = "128:1";
	public static final String PRINTER_NO_PAPER = "16:1";
	
	public void checkPrinterStatus() throws Exception{
		String printerName = "NPI Integration Driver";
		ProcessBuilder builder = new ProcessBuilder("powershell.exe", "get-wmiobject -class win32_printer | Select-Object Name, PrinterState, PrinterStatus | where {$_.Name -eq '"+printerName+"'}");

		String fullStatus = null;
		Process reg;
		builder.redirectErrorStream(true);
		try {
			reg = builder.start();
			
		    fullStatus = new BufferedReader(
		    	      new InputStreamReader(reg.getInputStream(), StandardCharsets.UTF_8))
		    	        .lines()
		    	        .collect(Collectors.joining("\n"));
			//fullStatus = getStringFromInputStream(reg.getInputStream());
			reg.destroy();
		} catch (IOException e1) {
			e1.printStackTrace();
			throw new Exception("Proveriti printer");
		}
		String full_status = parseFullStatus(fullStatus);
		System.out.print("full_status="+ full_status);
		if(full_status.startsWith("0")) {
			return;
		}else {
			if(full_status.equals(PRINTER_OFFLINE)) {
				throw new Exception("Printer u OFFLINE statusu");
			}else {
				if(full_status.equals(PRINTER_NO_PAPER)) {
					throw new Exception ( "Printer NEMA PAPIRA" );
				}
			}
		}
		
	}
	
	private String parseFullStatus (  String fullStatus ) throws Exception{
		String[] lines = fullStatus.split("\n");
		if(lines.length > 3) {
			String ocisceno = lines[3].substring("NPI Integration Driver".length());
			String state = ocisceno.substring(0,14).trim();
			String status = ocisceno.substring(14).trim();
			return state+":"+status;
		}else {
			throw new Exception( "Nepoznati status printera, Proveriti printer" );
		}
	}
	
	
	public static void main(String[] args) throws Exception{
		
		new PowerShellPrinterStatus().checkPrinterStatus();
		//kada je offline, pritner_state = 128, pritner status = 1
		//TODO treba sve kombinacije ispitati
	}

}
