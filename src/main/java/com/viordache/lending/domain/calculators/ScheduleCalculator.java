package com.viordache.lending.domain.calculators;

import com.viordache.lending.domain.Installment;
import com.viordache.lending.entities.LoanAccount;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ScheduleCalculator {

    public List<Installment> computeScheduleAtDisbursement(LoanAccount loanAccount) {

        BigDecimal interestDecimal = getMonthlyInterest(loanAccount.getLoanProduct().getInterestRate());
        List<Installment> installments = getEmptyInstallments(loanAccount);

        BigDecimal principalBalance = loanAccount.getPrincipalBalance();
        Installment first = installments.getFirst();
        first.setInterestDue(loanAccount.getPrincipalBalance().multiply(interestDecimal));
        first.setPrincipalDue(first.getTotalAmountDue().subtract(first.getInterestDue()));
        first.setRemainingPrincipal(principalBalance.subtract(first.getPrincipalDue()));

        for (int i = 1; i < installments.size(); i++) {
            Installment currentInstallment = installments.get(i);
            Installment previousInstallment = installments.get(i - 1);

            currentInstallment.setInterestDue(
                    previousInstallment.getRemainingPrincipal()
                            .multiply(interestDecimal));

            currentInstallment.setPrincipalDue(
                    currentInstallment.getTotalAmountDue()
                            .subtract(currentInstallment.getInterestDue()));

            currentInstallment.setRemainingPrincipal(
                    previousInstallment.getRemainingPrincipal()
                            .subtract(currentInstallment.getPrincipalDue()));
        }

        return installments;
    }

    // TODO
    // exctract in InterestCalculator
    private static BigDecimal getMonthlyInterest(BigDecimal interestRate) {
        return interestRate.divide(BigDecimal.valueOf(100), 20, RoundingMode.HALF_UP).divide(BigDecimal.valueOf(12), 20, RoundingMode.HALF_UP);
    }

    private List<Installment> getEmptyInstallments(LoanAccount loanAccount) {

        List<Installment> installments = new ArrayList<>();
        BigDecimal pmt = PMTCalculator.calculatePMT(loanAccount);

        for(int i = 0; i < loanAccount.getLoanTerm(); i++) {

            Installment installment = new Installment();

            installment.setTotalAmountDue(pmt);
            installment.setDueDate(calculateDueDate(loanAccount.getStartDate(), i));
            installments.add(installment);
        }
        return installments;
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
