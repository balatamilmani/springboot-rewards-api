/*
 * Copyright (c) 2021. Balamurugan Tamilmani (balamurugan.leo@gmail.com). All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are not permitted.
 */

package com.balatamilmani.rewards.service;

import com.balatamilmani.rewards.exception.CustomerNotFoundException;
import com.balatamilmani.rewards.model.CustomerTransaction;

import java.util.List;

/**
 * @author Balamurugan Tamilmani
 */

public interface RewardsService {
    void processRewards(List<CustomerTransaction> customerTransactions);

    Integer getRewardsByCustomerAndMonth(Integer customerId, String monthYear) throws CustomerNotFoundException;
}
