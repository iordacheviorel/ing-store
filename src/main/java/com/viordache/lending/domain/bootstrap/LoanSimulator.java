package com.viordache.lending.domain.bootstrap;

import com.viordache.lending.domain.calculators.PMTCalculator;
import com.viordache.lending.domain.enums.InstallmentType;
import com.viordache.lending.domain.enums.InterestRateReviewUnit;
import com.viordache.lending.entities.LoanAccount;
import com.viordache.lending.entities.LoanProduct;

import java.math.BigDecimal;

import static com.viordache.lending.domain.enums.InterestCalculationMethod.COMPOUND_INTEREST;

public class LoanSimulator {

    public void simulateCompoundInterestLoan() {

        LoanAccount loanAccount = getLoanAccount();

        BigDecimal pmt = PMTCalculator.calculatePMT(loanAccount);
        System.out.println("pmt = " + pmt);
    }

    public LoanAccount getLoanAccount() {

        LoanProduct loanProduct = new LoanProduct();
        loanProduct.setInstallmentType(InstallmentType.DECLINING_BALANCE_EQUAL_INSTALLMENTS);
        loanProduct.setInterestCalculationMethod(COMPOUND_INTEREST);
        loanProduct.setInterestRateReviewUnit(InterestRateReviewUnit.YEARLY);
        loanProduct.setInterestRate(BigDecimal.TEN);

        LoanAccount loanAccount = new LoanAccount();
        loanAccount.setLoanProduct(loanProduct);
        loanAccount.setLoanTerm(12);
        loanAccount.setPrincipalBalance(BigDecimal.valueOf(1000L));

        return loanAccount;
    }
}
