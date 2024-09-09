package com.viordache.lending.domain.utils;

public class UtilsCLI {

    public static void printScheduleHeader() {

        System.out.println(String.format("%-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-20s",
                "Due Date", "Total Due", "Total Paid", "Interest Due", "Interest Paid", "Accrued Int", "Principal Due",
                "Principal Paid", "Fees Due", "Fees Paid", "Penalties Due", "Penalties Paid", "Remaining Principal"
        ));
    }
}
