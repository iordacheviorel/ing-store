package com.viordache.lending.domain.bootstrap;

import org.junit.jupiter.api.Test;

class LoanSimulatorTest {

    @Test
    void simulate_loan() {

        LoanSimulator loanSimulator = new LoanSimulator();

        loanSimulator.simulateCompoundInterestLoan();

        // GOOGLE SHEETS formula result
        System.out.println("pmt = " + 87.91588723);
    }
}