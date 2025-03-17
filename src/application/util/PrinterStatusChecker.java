package application.util;

import java.io.BufferedReader;
//import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;


public class PrinterStatusChecker {
	
	
	private static final String STATUS_NORMAL = "Normal";
	
	private static final Logger logger = Logger.getLogger("PrinterStatusChecker");

	public static void checkPrinterStatus(String printerName) throws Exception {
		// = "NPI Integration Driver"; // Replace with your actual printer name
		String command = "powershell.exe -Command \"(Get-Printer -Name '" + printerName + "').PrinterStatus\"";

		logger.info("--> checkPrinterStatus");

//		try {
			ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);
			builder.redirectErrorStream(true);
			Process process = builder.start();

			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;


			while ((line = reader.readLine()) != null) {
				System.out.println("powershell returned: " + line);
				if (!line.trim().isEmpty()) {
					logger.info("PRINTER STATUS: " + line.trim());
					if(line.trim().equalsIgnoreCase(STATUS_NORMAL)){
						return;
					}else {
						throw new Exception("PRINTER STATUS: " + line.trim());
					}
				}
			}

			process.waitFor();



//		} catch (IOException e) {
//			e.printStackTrace();
//		}

	}
	
	public static void checkPrinterQueueSize(String printerName) throws Exception {
		logger.info("--> checkPrinterQueueSize");
        String command = "powershell.exe -Command \"(Get-PrintJob -PrinterName '" + printerName + "').Count\"";
        //int queueSize = 0;
        try {
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);
            builder.redirectErrorStream(true);
            Process process = builder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            

            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    try {
                    	int queueSize =  Integer.parseInt(line.trim()); // Convert output to integer
                    	logger.info("queueSize = " + queueSize);
                    	if(queueSize > 0) {
                    		throw new Exception("PRINT QSIZE > 0");
                    	}
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing printer queue size: " + line);
                    }
                }
            }

            process.waitFor();
            //System.out.println("Printer Queue Size: " + queueSize);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //return queueSize;
	}
	
	public static void main(String[] args) throws Exception{
		checkPrinterQueueSize("NPI Integration Driver");
		checkPrinterStatus("NPI Integration Driver");
		
	}

}
