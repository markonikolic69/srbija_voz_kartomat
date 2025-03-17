package test.application;

import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;

public class PrintStatusCheckedAlternative {
    public static void main(String[] args) {
        // Find the printer
        PrintService printer = findPrinter("NPI Integration Driver"); // Change to your printer name
        if (printer == null) {
            System.out.println("Printer not found!");
            return;
        }

        // Get printer attributes
        PrintServiceAttributeSet attributes = printer.getAttributes();
        for (Attribute attr : attributes.toArray()) {
            System.out.println(attr.getName() + ": " + attributes.get(attr.getClass()));
        }

        // Check if paper is out
        PrinterStateReasons reasons = (PrinterStateReasons) printer.getAttribute(PrinterStateReasons.class);
        if (reasons != null && reasons.containsKey(PrinterStateReason.MEDIA_EMPTY)) {
            System.out.println("Paper is OUT!");
        } else {
            System.out.println("Paper is OK.");
        }
    }

    private static PrintService findPrinter(String printerName) {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        for (PrintService service : services) {
            if (service.getName().equalsIgnoreCase(printerName)) {
                return service;
            }
        }
        return null;
    }
}

