package com.viordache.lending.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Installment {

    BigDecimal totalAmountDue = BigDecimal.ZERO;
    BigDecimal totalAmountPaid = BigDecimal.ZERO;

    BigDecimal interestDue = BigDecimal.ZERO;
    BigDecimal interestPaid = BigDecimal.ZERO;
    BigDecimal accruedInterest = BigDecimal.ZERO;

    BigDecimal principalDue = BigDecimal.ZERO;
    BigDecimal principalPaid = BigDecimal.ZERO;

    BigDecimal feesDue = BigDecimal.ZERO;
    BigDecimal feesPaid = BigDecimal.ZERO;

    BigDecimal penaltiesDue = BigDecimal.ZERO;
    BigDecimal penaltiesPaid = BigDecimal.ZERO;

    Date dueDate;

    BigDecimal remainingPrincipal = BigDecimal.ZERO;

//    TODO some validations
//    totalAmountDue = principalDue + interestDue + feesDue + penaltiesDue
//    totalAmountPaid = principalPaid + interestPaid + feesPaid + penaltiesPaid
//    [...]Due <= [...]Paid

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        return String.format("\n %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s, %-15s",
                (dueDate != null) ? sdf.format(dueDate) : "N/A",
                formatBigDecimal(totalAmountDue),
                formatBigDecimal(totalAmountPaid),
                formatBigDecimal(interestDue),
                formatBigDecimal(interestPaid),
                formatBigDecimal(accruedInterest),
                formatBigDecimal(principalDue),
                formatBigDecimal(principalPaid),
                formatBigDecimal(feesDue),
                formatBigDecimal(feesPaid),
                formatBigDecimal(penaltiesDue),
                formatBigDecimal(penaltiesPaid),
                formatBigDecimal(remainingPrincipal)
        );
    }

    private String formatBigDecimal(BigDecimal value) {
        return value.setScale(4, RoundingMode.HALF_UP).toString();
    }

    public BigDecimal getTotalAmountDue() {
        return totalAmountDue;
    }

    public void setTotalAmountDue(BigDecimal totalAmountDue) {
        this.totalAmountDue = totalAmountDue;
    }

    public BigDecimal getTotalAmountPaid() {
        return totalAmountPaid;
    }

    public void setTotalAmountPaid(BigDecimal totalAmountPaid) {
        this.totalAmountPaid = totalAmountPaid;
    }

    public BigDecimal getInterestDue() {
        return interestDue;
    }

    public void setInterestDue(BigDecimal interestDue) {
        this.interestDue = interestDue;
    }

    public BigDecimal getInterestPaid() {
        return interestPaid;
    }

    public void setInterestPaid(BigDecimal interestPaid) {
        this.interestPaid = interestPaid;
    }

    public BigDecimal getAccruedInterest() {
        return accruedInterest;
    }

    public void setAccruedInterest(BigDecimal accruedInterest) {
        this.accruedInterest = accruedInterest;
    }

    public BigDecimal getPrincipalDue() {
        return principalDue;
    }

    public void setPrincipalDue(BigDecimal principalDue) {
        this.principalDue = principalDue;
    }

    public BigDecimal getPrincipalPaid() {
        return principalPaid;
    }

    public void setPrincipalPaid(BigDecimal principalPaid) {
        this.principalPaid = principalPaid;
    }

    public BigDecimal getFeesDue() {
        return feesDue;
    }

    public void setFeesDue(BigDecimal feesDue) {
        this.feesDue = feesDue;
    }

    public BigDecimal getFeesPaid() {
        return feesPaid;
    }

    public void setFeesPaid(BigDecimal feesPaid) {
        this.feesPaid = feesPaid;
    }

    public BigDecimal getPenaltiesDue() {
        return penaltiesDue;
    }

    public void setPenaltiesDue(BigDecimal penaltiesDue) {
        this.penaltiesDue = penaltiesDue;
    }

    public BigDecimal getPenaltiesPaid() {
        return penaltiesPaid;
    }

    public void setPenaltiesPaid(BigDecimal penaltiesPaid) {
        this.penaltiesPaid = penaltiesPaid;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimal getRemainingPrincipal() {
        return remainingPrincipal;
    }

    public void setRemainingPrincipal(BigDecimal remainingPrincipal) {
        this.remainingPrincipal = remainingPrincipal;
    }
}
