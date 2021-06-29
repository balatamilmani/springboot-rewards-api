/*
 * Copyright (c) 2021. Balamurugan Tamilmani (balamurugan.leo@gmail.com). All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are not permitted.
 */

package com.balatamilmani.rewards.controller;

import com.balatamilmani.rewards.exception.CustomerNotFoundException;
import com.balatamilmani.rewards.model.CustomerRewards;
import com.balatamilmani.rewards.model.CustomerTransaction;
import com.balatamilmani.rewards.service.RewardsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Balamurugan Tamilmani
 */

@RestController
@RequestMapping(path = "/rewards")
@Validated
public class RewardsController {

    private final RewardsService rewardsService;

    public RewardsController(RewardsService rewardsService) {
        this.rewardsService = rewardsService;
    }

    /**
     *
     * @param customerTransactions List of Transactions for which rewards to be calculated
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    void postTransaction(@Valid @RequestBody List<CustomerTransaction> customerTransactions){
        rewardsService.processRewards(customerTransactions);
    }

    /**
     *
     * @param customerId Customer ID for which the Rewards points queried
     * @param monthYear Month and Year for which the Rewards points queried
     * @return Customer Id and the Rewards points
     */
    @GetMapping
    @RequestMapping(path = "/{customerId}/{monthYear}")
    ResponseEntity<CustomerRewards> getRewardsByCustomerAndMonth (@PathVariable Integer customerId, @PathVariable String monthYear) {
        int rewardsPoint = 0;
        try {
            rewardsPoint = rewardsService.getRewardsByCustomerAndMonth(customerId, monthYear);
        } catch (CustomerNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return new ResponseEntity<CustomerRewards>(new CustomerRewards(customerId, rewardsPoint), HttpStatus.OK);
    }

}
