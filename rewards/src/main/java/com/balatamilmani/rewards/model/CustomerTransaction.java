/*
 * Copyright (c) 2021. Balamurugan Tamilmani (balamurugan.leo@gmail.com). All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are not permitted.
 */

package com.balatamilmani.rewards.model;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author Balamurugan Tamilmani
 */

public class CustomerTransaction {

    @NotNull(message = "The CustomerId can't be empty")
    private Integer customerId;
    private Integer transactionAmount;
    private LocalDate transactionDate;

    public CustomerTransaction(Integer customerId, Integer transactionAmount, LocalDate transactionDate) {
        this.customerId = customerId;
        this.transactionAmount = transactionAmount;
        this.transactionDate = transactionDate;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(Integer transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getMonthDate(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-YYYY");
        return formatter.format(this.transactionDate);
    }

    @Override
    public String toString() {
        return "CustomerTransaction{" +
                "customerId=" + customerId +
                ", transactionAmount=" + transactionAmount +
                ", transactionDate=" + transactionDate +
                ", monthDate=" + this.getMonthDate() +
                '}';
    }
}
