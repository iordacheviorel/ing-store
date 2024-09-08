package com.viordache.lending.entities;

import com.viordache.lending.domain.enums.LoanStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;

@Table(name = "loan_product")
@Entity
public class LoanAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "loan_product_id", nullable = false)
    private LoanProduct loanProduct;

    @Column(nullable = false)
    private BigDecimal principalBalance;

    // TODO add the rest of the balances

    //    interestBalance
    //    interestDue
    //    interestPaid

    //    ADDED principalBalance
    //    principalDue
    //    principalPaid

    //    penalty, fees?

    @Column(nullable = false)
    private BigDecimal interestRate;

    @Column(nullable = false)
    private Integer loanTerm;  // Term in months

    @Column(nullable = false)
    private BigDecimal installmentAmount;

    @Column(nullable = false)
    // map to User
    private Long customerId;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    @Column(nullable = false)
    private Date startDate;

    @Column
    private Date endDate;

    @Column
    private Date nextInstallmentDate;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LoanProduct getLoanProduct() {
        return loanProduct;
    }

    public void setLoanProduct(LoanProduct loanProduct) {
        this.loanProduct = loanProduct;
    }

    public BigDecimal getPrincipalBalance() {
        return principalBalance;
    }

    public void setPrincipalBalance(BigDecimal principalAmount) {
        this.principalBalance = principalAmount;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public Integer getLoanTerm() {
        return loanTerm;
    }

    public void setLoanTerm(Integer loanTerm) {
        this.loanTerm = loanTerm;
    }

    public BigDecimal getInstallmentAmount() {
        return installmentAmount;
    }

    public void setInstallmentAmount(BigDecimal installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getNextInstallmentDate() {
        return nextInstallmentDate;
    }

    public void setNextInstallmentDate(Date nextInstallmentDate) {
        this.nextInstallmentDate = nextInstallmentDate;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
