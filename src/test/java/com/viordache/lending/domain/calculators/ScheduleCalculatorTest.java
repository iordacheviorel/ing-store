package com.viordache.lending.domain.calculators;

import com.viordache.lending.domain.Installment;
import com.viordache.lending.domain.enums.InstallmentType;
import com.viordache.lending.domain.enums.InterestRateReviewUnit;
import com.viordache.lending.domain.utils.UtilsCLI;
import com.viordache.lending.entities.LoanAccount;
import com.viordache.lending.entities.LoanProduct;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.viordache.lending.domain.enums.InterestCalculationMethod.COMPOUND_INTEREST;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ScheduleCalculatorTest {

    /**
     * Simulates a loan with:
     *  - Declining Balance Equal Installments
     *  - Compound Interest
     *  - Principal = 1000
     *  - Interest = 10% per year
     *  - Period: 12 months, (payment monthly)
     *
     * Google Sheets calculations:
     * https://docs.google.com/spreadsheets/d/1SEgh4eNLqiNJk09aAK88ddgTskj-2Dfc05y_wAfeBcI/edit?usp=sharing
     */
    @ParameterizedTest
    @CsvSource({
            "15-09-2024, 87.92, 8.33, 79.58, 920.42",
            "15-10-2024, 87.92, 7.67, 80.25, 840.17",
            "15-11-2024, 87.92, 7.00, 80.91, 759.26",
            "15-12-2024, 87.92, 6.33, 81.59, 677.67",
            "15-01-2025, 87.92, 5.65, 82.27, 595.40",
            "15-02-2025, 87.92, 4.96, 82.95, 512.45",
            "15-03-2025, 87.92, 4.27, 83.65, 428.80",
            "15-04-2025, 87.92, 3.57, 84.34, 344.46",
            "15-05-2025, 87.92, 2.87, 85.05, 259.41",
            "15-06-2025, 87.92, 2.16, 85.75, 173.66",
            "15-07-2025, 87.92, 1.45, 86.47, 87.19",
            "15-08-2025, 87.92, 0.73, 87.19, 0.00"
    })
    void simulate_loan_comparison(String dueDateStr, String totalDueStr, String interestDueStr, String principalDueStr, String remainingPrincipalStr) {

        // setup
        BigDecimal totalDue = new BigDecimal(totalDueStr).setScale(2, RoundingMode.HALF_UP);
        BigDecimal interestDue = new BigDecimal(interestDueStr).setScale(2, RoundingMode.HALF_UP);
        BigDecimal principalDue = new BigDecimal(principalDueStr).setScale(2, RoundingMode.HALF_UP);
        BigDecimal remainingPrincipal = new BigDecimal(remainingPrincipalStr).setScale(2, RoundingMode.HALF_UP);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        LoanAccount loanAccount = getLoanAccount();
        ScheduleCalculator scheduleCalculator = new ScheduleCalculator();

        // execute
        List<Installment> schedule = scheduleCalculator.computeScheduleAtDisbursement(loanAccount);

        // verify
        Installment actualInstallment = schedule.stream()
                .filter(i -> dateFormat.format(i.getDueDate()).equals(dueDateStr))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Installment not found"));

        assertEquals(totalDue, actualInstallment.getTotalAmountDue().setScale(2, RoundingMode.HALF_UP));
        assertEquals(interestDue, actualInstallment.getInterestDue().setScale(2, RoundingMode.HALF_UP));
        assertEquals(principalDue, actualInstallment.getPrincipalDue().setScale(2, RoundingMode.HALF_UP));
        assertEquals(remainingPrincipal, actualInstallment.getRemainingPrincipal().setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    void simulate_loan() {

        // setup
        LoanAccount loanAccount = getLoanAccount();
        ScheduleCalculator scheduleCalculator = new ScheduleCalculator();

        // execute
        List<Installment> schedule = scheduleCalculator.computeScheduleAtDisbursement(loanAccount);

        // verify
        UtilsCLI.printScheduleHeader();
        System.out.printf(schedule.toString());
    }


    private LoanAccount getLoanAccount() {

        LoanProduct loanProduct = getLoanProduct();

        LoanAccount loanAccount = new LoanAccount();
        loanAccount.setLoanProduct(loanProduct);
        loanAccount.setLoanTerm(12);
        loanAccount.setPrincipalBalance(BigDecimal.valueOf(1000L));
        Calendar calendar = Calendar.getInstance();
        calendar.set(2024, Calendar.SEPTEMBER, 15);
        Date startDate = calendar.getTime();
        loanAccount.setStartDate(startDate);

        return loanAccount;
    }

    private static LoanProduct getLoanProduct() {
        LoanProduct loanProduct = new LoanProduct();
        loanProduct.setInstallmentType(InstallmentType.DECLINING_BALANCE_EQUAL_INSTALLMENTS);
        loanProduct.setInterestCalculationMethod(COMPOUND_INTEREST);
        loanProduct.setInterestRateReviewUnit(InterestRateReviewUnit.YEARLY);
        loanProduct.setInterestRate(BigDecimal.TEN);
        return loanProduct;
    }
}