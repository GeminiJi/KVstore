package com.dynamo.controller;

import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CountDownLatch;

class WriteThread extends Thread {

    private String URL;
    private CountDownLatch latch;

    public WriteThread(String URL, CountDownLatch latch) {
        this.URL = URL;
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            Boolean result = restTemplate.getForObject(URL, Boolean.class);
            if (result) {
                latch.countDown();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}