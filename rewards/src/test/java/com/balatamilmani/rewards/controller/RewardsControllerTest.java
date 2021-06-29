/*
 * Copyright (c) 2021. Balamurugan Tamilmani (balamurugan.leo@gmail.com). All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are not permitted.
 */

package com.balatamilmani.rewards.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Balamurugan Tamilmani
 */

@SpringBootTest
@AutoConfigureMockMvc
public class RewardsControllerTest {

    /**
     * There's no such a Customer, expect 404
     */
    @Test
    void testCustomerNotFound(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/rewards/100/01-2017")).andExpect(status().isNotFound());
    }

    /**
     * Customer has not done any Transaction on the queried month/year, expect 0 rewards points
     */
    @Test
    void testCustomerHasNoRewardsOnaMonthExpectZero(@Autowired MockMvc mvc) throws Exception {
        String payLoad = "[{\"customerId\":\"200\", \"transactionAmount\":\"55\", \"transactionDate\":\"2017-01-23\"}]";
        mvc.perform(post("/rewards").contentType(MediaType.APPLICATION_JSON).content(payLoad)).andExpect(status().isCreated());
        //Query for customerId 123 and expect
        mvc.perform(get("/rewards/200/02-2017")).andExpect(status().isOk()).andExpect(content().string("{\"customerId\":200,\"rewardsPoint\":0}"));
    }

    /**
     * Customer has not done transaction over $50, expect 0 rewards points
     */
    @Test
    void testCustomerNoTransOverFiftyExpectZeroPoints(@Autowired MockMvc mvc) throws Exception
    {
        String payLoad = "[{\"customerId\":\"101\", \"transactionAmount\":\"45\", \"transactionDate\":\"2017-01-23\"}]";
        mvc.perform(post("/rewards").contentType(MediaType.APPLICATION_JSON).content(payLoad)).andExpect(status().isCreated());
        //Query for customerId 123 and expect
        mvc.perform(get("/rewards/101/01-2017")).andExpect(status().isOk()).andExpect(content().string("{\"customerId\":101,\"rewardsPoint\":0}"));
    }

    /**
     * Customer has done single transaction over $50, that's $66, expect 16 rewards points
     */
    @Test
    void testCustomerSingleTransOverFifty(@Autowired MockMvc mvc) throws Exception
    {
        String payLoad = "[{\"customerId\":\"102\", \"transactionAmount\":\"66\", \"transactionDate\":\"2017-01-23\"}]";
        mvc.perform(post("/rewards").contentType(MediaType.APPLICATION_JSON).content(payLoad)).andExpect(status().isCreated());
        //Query for customerId 123 and expect
        mvc.perform(get("/rewards/102/01-2017")).andExpect(status().isOk()).andExpect(content().string("{\"customerId\":102,\"rewardsPoint\":16}"));
    }

    /**
     * A single Customer has done single transaction over $50 in a month and two transaction in a different month
     * $66 on Jan-2017 expect 16 rewards points
     * $125 & 75 on Feb-2017 expect 125 rewards points
     */
    @Test
    void testCustomerSingleTransAndTwoInDifferentMonth(@Autowired MockMvc mvc) throws Exception
    {
        String payLoad =
                "[{\"customerId\":\"103\", \"transactionAmount\":\"66\", \"transactionDate\":\"2017-01-23\"}, " +
                "{\"customerId\":\"103\", \"transactionAmount\":\"125\", \"transactionDate\":\"2017-02-22\"}, " +
                "{\"customerId\":\"103\", \"transactionAmount\":\"75\", \"transactionDate\":\"2017-02-23\"}]";
        mvc.perform(post("/rewards").contentType(MediaType.APPLICATION_JSON).content(payLoad)).andExpect(status().isCreated());
        mvc.perform(get("/rewards/103/01-2017")).andExpect(status().isOk()).andExpect(content().string("{\"customerId\":103,\"rewardsPoint\":16}"));
        mvc.perform(get("/rewards/103/02-2017")).andExpect(status().isOk()).andExpect(content().string("{\"customerId\":103,\"rewardsPoint\":125}"));
    }

    /**
     * Two different Customers has done single transaction, that's $66 & $45 expect 16 & 0 rewards points
     */
    @Test
    void testTwoCustomerSingleTrans(@Autowired MockMvc mvc) throws Exception
    {
        String payLoad =
                "[{\"customerId\":\"104\", \"transactionAmount\":\"66\", \"transactionDate\":\"2017-01-23\"}, " +
                 "{\"customerId\":\"105\", \"transactionAmount\":\"45\", \"transactionDate\":\"2019-10-23\"}]";
        mvc.perform(post("/rewards").contentType(MediaType.APPLICATION_JSON).content(payLoad)).andExpect(status().isCreated());
        mvc.perform(get("/rewards/104/01-2017")).andExpect(status().isOk()).andExpect(content().string("{\"customerId\":104,\"rewardsPoint\":16}"));
        mvc.perform(get("/rewards/105/10-2019")).andExpect(status().isOk()).andExpect(content().string("{\"customerId\":105,\"rewardsPoint\":0}"));
    }


    /**
     * More than one Customer has done more than one transaction
     *
     * CustomerId 105, five transaction, 2 in each month Jan & Feb and 1 in Mar
     * Jan - 202+50+100=352
     * Feb - 52+0=52
     * Feb-21 - 1 points
     *
     * CustomerId 106, 2 transaction in Mar-2021 and 1 in Jun-2021
     * Mar-2021 - 150+50=200
     * Jun-2021 - 850 points
     */
    @Test
    void testMoreThanOneCustomerMoreThanSingleTrans(@Autowired MockMvc mvc) throws Exception
    {
        String payLoad =
                "[{\"customerId\":\"105\", \"transactionAmount\":\"201\", \"transactionDate\":\"2019-01-23\"}, " +
                        "{\"customerId\":\"105\", \"transactionAmount\":\"125\", \"transactionDate\":\"2019-01-20\"}, " +
                        "{\"customerId\":\"105\", \"transactionAmount\":\"101\", \"transactionDate\":\"2017-02-23\"}, " +
                        "{\"customerId\":\"105\", \"transactionAmount\":\"45\", \"transactionDate\":\"2017-02-10\"}, " +
                        "{\"customerId\":\"105\", \"transactionAmount\":\"51\", \"transactionDate\":\"2021-02-10\"}, " +
                        "{\"customerId\":\"106\", \"transactionAmount\":\"175\", \"transactionDate\":\"2021-03-23\"}, " +
                        "{\"customerId\":\"106\", \"transactionAmount\":\"5\", \"transactionDate\":\"2021-03-15\"}, " +
                        "{\"customerId\":\"106\", \"transactionAmount\":\"500\", \"transactionDate\":\"2021-06-23\"}]";
        mvc.perform(post("/rewards").contentType(MediaType.APPLICATION_JSON).content(payLoad)).andExpect(status().isCreated());
        mvc.perform(get("/rewards/105/01-2019")).andExpect(status().isOk()).andExpect(content().string("{\"customerId\":105,\"rewardsPoint\":352}"));
        mvc.perform(get("/rewards/105/02-2017")).andExpect(status().isOk()).andExpect(content().string("{\"customerId\":105,\"rewardsPoint\":52}"));
        mvc.perform(get("/rewards/106/03-2021")).andExpect(status().isOk()).andExpect(content().string("{\"customerId\":106,\"rewardsPoint\":200}"));
        mvc.perform(get("/rewards/106/06-2021")).andExpect(status().isOk()).andExpect(content().string("{\"customerId\":106,\"rewardsPoint\":850}"));
    }


    /**
     * The CustomerId being passed is empty, just to show the validation
     */
    @Test
    void testInvalidDataExpect4xx(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(post("/rewards").contentType(MediaType.APPLICATION_JSON).content("[{\"customerId\":\"\", \"transactionAmount\":\"120\", \"transactionDate\":\"2017-01-23\"}, {\"customerId\":\"123\", \"transactionAmount\":\"120\", \"transactionDate\":\"2017-01-24\"}]")).andExpect(status().is4xxClientError());
    }
}
