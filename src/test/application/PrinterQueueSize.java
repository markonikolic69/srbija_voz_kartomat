package test.application;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class PrinterQueueSize {
    public static void main(String[] args) {
        String printerName = "NPI Integration Driver"; // Replace with your actual printer name
        String command = "powershell.exe -Command \"(Get-PrintJob -PrinterName '" + printerName + "').Count\"";

        try {
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);
            builder.redirectErrorStream(true);
            Process process = builder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            int queueSize = 0;

            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    try {
                        queueSize = Integer.parseInt(line.trim()); // Convert output to integer
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing printer queue size: " + line);
                    }
                }
            }

            process.waitFor();
            System.out.println("Printer Queue Size: " + queueSize);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
