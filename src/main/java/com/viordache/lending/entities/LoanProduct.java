package com.viordache.lending.entities;

import com.viordache.lending.domain.enums.InstallmentType;
import com.viordache.lending.domain.enums.InterestCalculationMethod;
import com.viordache.lending.domain.enums.InterestRateReviewUnit;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;

@Table(name = "loan_product")
@Entity
public class LoanProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal interestRate;

    @Column(nullable = false)
    private BigDecimal minLoanAmount;

    @Column(nullable = false)
    private BigDecimal maxLoanAmount;

    @Column(nullable = false)
    private Integer minTerm;

    @Column(nullable = false)
    private Integer maxTerm;

    @Enumerated(EnumType.STRING)
    private InstallmentType installmentType;

    @Enumerated(EnumType.STRING)
    private InterestCalculationMethod interestCalculationMethod;

    @Enumerated(EnumType.STRING)
    private InterestRateReviewUnit interestRateReviewUnit;

    @Column
    private BigDecimal penaltyRate;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public BigDecimal getMinLoanAmount() {
        return minLoanAmount;
    }

    public void setMinLoanAmount(BigDecimal minLoanAmount) {
        this.minLoanAmount = minLoanAmount;
    }

    public BigDecimal getMaxLoanAmount() {
        return maxLoanAmount;
    }

    public void setMaxLoanAmount(BigDecimal maxLoanAmount) {
        this.maxLoanAmount = maxLoanAmount;
    }

    public Integer getMinTerm() {
        return minTerm;
    }

    public void setMinTerm(Integer minTerm) {
        this.minTerm = minTerm;
    }

    public Integer getMaxTerm() {
        return maxTerm;
    }

    public void setMaxTerm(Integer maxTerm) {
        this.maxTerm = maxTerm;
    }

    public InstallmentType getInstallmentType() {
        return installmentType;
    }

    public void setInstallmentType(InstallmentType installmentType) {
        this.installmentType = installmentType;
    }

    public InterestCalculationMethod getInterestCalculationMethod() {
        return interestCalculationMethod;
    }

    public void setInterestCalculationMethod(InterestCalculationMethod interestCalculationMethod) {
        this.interestCalculationMethod = interestCalculationMethod;
    }

    public BigDecimal getPenaltyRate() {
        return penaltyRate;
    }

    public void setPenaltyRate(BigDecimal penaltyRate) {
        this.penaltyRate = penaltyRate;
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

    public InterestRateReviewUnit getInterestRateReviewUnit() {
        return interestRateReviewUnit;
    }

    public void setInterestRateReviewUnit(InterestRateReviewUnit interestRateReviewUnit) {
        this.interestRateReviewUnit = interestRateReviewUnit;
    }
}

