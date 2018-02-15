package org.athenian;

import java.util.concurrent.CountDownLatch;

public class HealthCheckTestClient {
    private static String[] messages = {"The quick brown fox", "jumped over", "the lazy dog"};
    int message = 0;

    public static void main(String[] args) {
        new HealthCheckTestClient().run();
    }

    private void run() {
        HealthCheckClient client = new HealthCheckClient(
                RioBridgeConstants.hostname,
                RioBridgeConstants.port,
                this::onMessage);

        CountDownLatch finishLatch = client.startHealthCheck();
        System.out.println("Client started.");

        while (finishLatch.getCount() > 0) {
            String healthCheck = messages[message];
            System.out.printf("Client sent: %s\n", healthCheck);
            client.sendHealthCheck(healthCheck);

            message = (message + 1) % 3;

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
                finishLatch.countDown();
            }
        }
    }

    private void onMessage(String healthCheck) {
        System.out.printf("Client got: \"%s\"\n", healthCheck);
    }
}
