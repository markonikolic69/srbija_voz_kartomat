package test.application;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class PrinterStatusChecker {
    public static void main(String[] args) {
        String printerName = "NPI Integration Driver"; // Replace with your actual printer name
        String command = "powershell.exe -Command \"(Get-Printer -Name '" + printerName + "').PrinterStatus\"";

        // Mapping PowerShell PrinterStatus codes to readable status
        Map<Integer, String> printerStatusMap = new HashMap<>();
        printerStatusMap.put(0, "Other");
        printerStatusMap.put(1, "Unknown");
        printerStatusMap.put(2, "Idle");
        printerStatusMap.put(3, "Printing");
        printerStatusMap.put(4, "Warming Up");
        printerStatusMap.put(5, "Stopped Printing");
        printerStatusMap.put(6, "Offline");
        printerStatusMap.put(7, "Paused");
        printerStatusMap.put(8, "Error");
        printerStatusMap.put(9, "Busy");
        printerStatusMap.put(10, "Not Available");
        printerStatusMap.put(11, "Waiting");
        printerStatusMap.put(12, "Processing");
        printerStatusMap.put(13, "Initialization");
        printerStatusMap.put(14, "Power Save");
        printerStatusMap.put(15, "Pending Deletion");
        printerStatusMap.put(16, "I/O Active");
        printerStatusMap.put(17, "Manual Feed Required");

        try {
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);
            builder.redirectErrorStream(true);
            Process process = builder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            int statusCode = -1;

            while ((line = reader.readLine()) != null) {
            	System.out.println("powershell returned: " + line);
                if (!line.trim().isEmpty()) {
                    try {
                        statusCode = Integer.parseInt(line.trim());
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing printer status: " + line);
                    }
                }
            }

            process.waitFor();

            String statusMessage = printerStatusMap.getOrDefault(statusCode, "Unknown Status");
            System.out.println("Printer Status: " + statusMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
