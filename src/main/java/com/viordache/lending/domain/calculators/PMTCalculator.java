package com.viordache.lending.domain.calculators;

import com.viordache.lending.domain.enums.InterestRateReviewUnit;
import com.viordache.lending.entities.LoanAccount;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PMTCalculator {


    public static BigDecimal calculatePMT(LoanAccount loanAccount) {

        return switch (loanAccount.getLoanProduct().getInterestCalculationMethod()) {
            case COMPOUND_INTEREST -> calculateCompoundInterestPMT(loanAccount);
            case SIMPLE_INTEREST, CAPITALIZED_INTEREST -> BigDecimal.ZERO; // TODO
        };
    }


    /**
     * Compute the monthly expected payment using the PMT formula
     * PMT = PRINCIPAL * (MONTHLY_INTEREST * ((1 + MONTHLY_INTEREST)  ^12 / ((1 + MONTHLY_INTEREST)  ^12 - 1)))
     */
    public static BigDecimal calculateCompoundInterestPMT(LoanAccount loanAccount) {

//        D5        *(  D6               * ((1 + D6)  ^12 / ((1 + D6)  ^12 - 1)))
//        PRINCIPAL *(  monthly_interest * (growth_factor / (growth_factor - 1)))
        BigDecimal monthlyInterest = convertToMonthlyInterestDecimal(
                loanAccount.getLoanProduct().getInterestRate(),
                loanAccount.getLoanProduct().getInterestRateReviewUnit());

        BigDecimal growthFactor = BigDecimal.ONE.add(monthlyInterest).pow(12);

        return loanAccount.getPrincipalBalance().multiply(                          // PRINCIPAL *
                monthlyInterest.multiply(                                           // monthly_interest *
                        growthFactor.divide(                                        // growth_factor /
                                growthFactor.subtract(BigDecimal.ONE)               // growth_factor - ONE
                        , 20, RoundingMode.HALF_UP)));
    }

    /**
     * Convert to monthly interest rate and apply % to the value.
     */
    public static BigDecimal convertToMonthlyInterestDecimal(BigDecimal interest, InterestRateReviewUnit reviewUnit) {

        BigDecimal convertedInterest =
                switch (reviewUnit) {
                    case DAILY -> // TODO support 360, 365 and other DAILY convertedInterest options
                            interest.multiply(new BigDecimal("30"));
                    case MONTHLY -> interest;
                    case YEARLY -> interest.divide(new BigDecimal("12"), 20, RoundingMode.HALF_UP);
                };

        return convertedInterest.divide(new BigDecimal("100"), 20, RoundingMode.HALF_UP);
    }
}
