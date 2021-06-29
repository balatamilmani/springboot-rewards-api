/*
 * Copyright (c) 2021. Balamurugan Tamilmani (balamurugan.leo@gmail.com). All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are not permitted.
 */

package com.balatamilmani.rewards.model;

/**
 * @author Balamurugan Tamilmani
 */

public class CustomerRewards {

    private Integer customerId;
    private Integer rewardsPoint;

    public CustomerRewards(Integer customerId, Integer rewardsPoint) {
        this.customerId = customerId;
        this.rewardsPoint = rewardsPoint;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getRewardsPoint() {
        return rewardsPoint;
    }

    public void setRewardsPoint(Integer rewardsPoint) {
        this.rewardsPoint = rewardsPoint;
    }

    @Override
    public String toString() {
        return "CustomerRewards{" +
                "customerId=" + customerId +
                ", rewardsPoint=" + rewardsPoint +
                '}';
    }
}
