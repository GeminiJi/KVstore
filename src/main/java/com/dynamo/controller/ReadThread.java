package com.dynamo.controller;

import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CountDownLatch;

class ReadThread extends Thread {

    private String URL;
    private CountDownLatch latch;
    private List<String> vectorClock;

    public ReadThread(String URL, CountDownLatch latch, List<String> vectorClock) {
        this.URL = URL;
        this.latch = latch;
        this.vectorClock = vectorClock;
    }

    @Override
    synchronized public void run() {
        try {
            System.out.println("Thread : " + URL);
            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(URL, String.class);
            latch.countDown();
            if (!result.equals("None")) {
                String[] list = result.split(";");
                // 0 is key, 1 is hashkey, 2 is value, 3 is IP:Port, 4 is counter
                String key = list[0];
                String updateVersionURL = "";
                if (vectorClock.size() == 0) {
                    vectorClock.add(list[2]);
                    vectorClock.add(list[3]);
                    vectorClock.add(list[4]);
                } else {
                    System.out.println("IP : " +  list[3]);
                    System.out.println("VCCounter : " +  vectorClock.get(2));
                    System.out.println("VALUECounter : " +  list[4]);
                    if (Integer.parseInt(vectorClock.get(2)) < Integer.parseInt(list[4])) {
                        // the latter one is a latest version, need to update the previous one
                        vectorClock.set(2, list[4]);
                        updateVersionURL = "http://" + vectorClock.get(1) + "/set?key=" + key
                                + "&val=" + list[1] + ";" + list[2] + ";" + vectorClock.get(1) + ";" + list[4];
                        restTemplate.getForObject(updateVersionURL, String.class);

                    } else if (Integer.parseInt(vectorClock.get(2)) > Integer.parseInt(list[4])) {
                        // the previous one is a latest version, need to update the current version
                        updateVersionURL = "http://" + list[3] + "/set?key=" + key
                                + "&val=" + list[1] + ";" + vectorClock.get(0) + ";" + list[3] + ";" + vectorClock.get(2);
                        restTemplate.getForObject(updateVersionURL, String.class);
                    }
                }
                System.out.println("Thread vectorClock : " + vectorClock);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}