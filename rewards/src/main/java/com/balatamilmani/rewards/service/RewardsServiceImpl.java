/*
 * Copyright (c) 2021. Balamurugan Tamilmani (balamurugan.leo@gmail.com). All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are not permitted.
 */

package com.balatamilmani.rewards.service;

import com.balatamilmani.rewards.exception.CustomerNotFoundException;
import com.balatamilmani.rewards.model.CustomerTransaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Balamurugan Tamilmani
 */

@Service
public class RewardsServiceImpl implements RewardsService {


    @Value("${points.for.spending.over.hundred}")
    private Integer pointsForSpendingOverHundred;

    @Value("${points.for.spending.over.fifty}")
    private Integer pointsForSpendingOverFifty;

    /**
     * Map that holds the Rewards points per Month/Year per Customer
     */
    private Map<Integer, Map<String, Integer>> rewardsDataMap = new HashMap<>();

    /**
     * Business logic to calculate the Rewards points
     */
    private Function<Integer, Integer> rewardsCalculator = transactionAmount->{
      int rewardsPoint = 0;
      int amountSpentOverHundred = 0;
      //Calculate points for speing over $100
      if(transactionAmount > 100){
          amountSpentOverHundred = transactionAmount - 100;
          rewardsPoint += amountSpentOverHundred * pointsForSpendingOverHundred;
      }

      //Calculate points for spending over $50
      if(transactionAmount > 50){
           int spentOverFifty = transactionAmount - amountSpentOverHundred - 50;
           rewardsPoint += spentOverFifty * pointsForSpendingOverFifty;
      }
      return rewardsPoint;
    };

    /**
     * Receive Customer transactions and calculate Rewards per Customer/Month-Year
     * @param customerTransactions Customer's transaction which spans for months
     */
    public void processRewards(List<CustomerTransaction> customerTransactions){
        //Process each Transaction and calculate the rewards
        //A Map with Customer id as Key and another Map as value wherein key is Transaction Month & Year (MM-YYYY) and Value is rewards points for that month/year is created
        //E.g <123, <01-2021, 23> - The Customer with id 123 has 23 rewards points for the Month of Jan 2021
        rewardsDataMap = customerTransactions.stream().collect(
                Collectors.groupingBy(
                        CustomerTransaction::getCustomerId, Collectors.groupingBy(CustomerTransaction::getMonthDate, Collectors.summingInt(customerTransaction->rewardsCalculator.apply(customerTransaction.getTransactionAmount())))
                )
        );

    }

    /**
     *
     * @param customerId CustomerId for which rewards is looked up
     * @param monthYear Month-year for which rewards is looked up
     * @return The rewards points the Customer has earned for the month/year
     * @throws CustomerNotFoundException When no customer found for the given CustomerId
     */
    public Integer getRewardsByCustomerAndMonth(Integer customerId, String monthYear) throws CustomerNotFoundException {
        int rewardsPoint = 0;
        Map<String, Integer> customerRewardsByMonth = rewardsDataMap.get(customerId);
        if(customerRewardsByMonth != null){
            Integer customerRewardsPointForTheMonth = customerRewardsByMonth.get(monthYear);
            if(customerRewardsPointForTheMonth != null){
                rewardsPoint = customerRewardsPointForTheMonth;
            }
        } else {
            throw new CustomerNotFoundException(String.format("Customer with Customer ID %s is not found", customerId));

        }
        return rewardsPoint;
    }
}
