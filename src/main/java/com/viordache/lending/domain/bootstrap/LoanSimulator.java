package com.viordache.lending.domain.bootstrap;

import com.viordache.lending.domain.Installment;
import com.viordache.lending.domain.calculators.PMTCalculator;
import com.viordache.lending.domain.enums.InstallmentType;
import com.viordache.lending.domain.enums.InterestRateReviewUnit;
import com.viordache.lending.entities.LoanAccount;
import com.viordache.lending.entities.LoanProduct;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.viordache.lending.domain.enums.InterestCalculationMethod.COMPOUND_INTEREST;

public class LoanSimulator {

    public void simulateCompoundInterestLoan() {

        LoanAccount loanAccount = getLoanAccount();

        BigDecimal pmt = PMTCalculator.calculatePMT(loanAccount);
        System.out.println("pmt = " + pmt);

        // TODO extract to utils
        System.out.println(String.format("%-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-20s",
                "Due Date", "Total Due", "Total Paid", "Interest Due", "Interest Paid", "Accrued Int", "Principal Due",
                "Principal Paid", "Fees Due", "Fees Paid", "Penalties Due", "Penalties Paid", "Remaining Principal"
        ));
        System.out.println(computeScheduleAtDisbursement(loanAccount));
    }

    public List<Installment> computeScheduleAtDisbursement(LoanAccount loanAccount) {

        BigDecimal pmt = PMTCalculator.calculatePMT(loanAccount);
        // TODO
        // converting monthly, should check Loan Account period settings
        BigDecimal interestDecimal = loanAccount.getLoanProduct().getInterestRate().divide(BigDecimal.valueOf(100), 20, RoundingMode.HALF_UP).divide(BigDecimal.valueOf(12), 20, RoundingMode.HALF_UP);

        List<Installment> installments = getEmptyInstallments(loanAccount, pmt);

        BigDecimal principalBalance = loanAccount.getPrincipalBalance();
        Installment first = installments.getFirst();
        first.setInterestDue(loanAccount.getPrincipalBalance().multiply(interestDecimal));
        first.setPrincipalDue(first.getTotalAmountDue().subtract(first.getInterestDue()));
        first.setRemainingPrincipal(principalBalance.subtract(first.getPrincipalDue()));

        for (int i = 1; i < installments.size(); i++) {
            Installment currentInstallment = installments.get(i);
            Installment previousInstallment = installments.get(i - 1);

            currentInstallment.setInterestDue(previousInstallment.getRemainingPrincipal().multiply(interestDecimal));
            currentInstallment.setPrincipalDue(currentInstallment.getTotalAmountDue().subtract(currentInstallment.getInterestDue()));
            currentInstallment.setRemainingPrincipal(previousInstallment.getRemainingPrincipal().subtract(currentInstallment.getPrincipalDue()));
        }

        return installments;
    }

    private List<Installment> getEmptyInstallments(LoanAccount loanAccount, BigDecimal pmt) {

        List<Installment> installments = new ArrayList<>();

        for(int i = 0; i < loanAccount.getLoanTerm(); i++) {

            Installment installment = new Installment();

            installment.setTotalAmountDue(pmt);
            installment.setDueDate(calculateDueDate(loanAccount.getStartDate(), i));

            installments.add(installment);
        }
        return installments;
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
        Calendar calendar = Calendar.getInstance();
        calendar.set(2024, Calendar.SEPTEMBER, 15); // Month is 0-based (January is 0)
        Date startDate = calendar.getTime();
        loanAccount.setStartDate(startDate);

        return loanAccount;
    }

    // TODO
    // for now support only MONTHLY payment period
    // add paymentPeriod at loanAccount level and use it from there
    // WEEKLY, FOUR_WEEK, MONTHLY, QUARTERLY, YEARLY
    private Date calculateDueDate(Date startDate, int installmentIndex) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        // Add one month for each installment index
        calendar.add(Calendar.MONTH, installmentIndex);

        return calendar.getTime();
    }
}
