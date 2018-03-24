package org.athenian;

import io.grpc.StatusRuntimeException;

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
                RioBridgeConstants.port);

        CountDownLatch finishLatch = new CountDownLatch(1);
        System.out.println("Client started.");

        String healthCheck, response;
        boolean isConnected = false;
        while (finishLatch.getCount() > 0) {

            System.out.println("\nConnecting to server...");

            while (!isConnected) {
                try {
                    if (client.sendHealthCheck(RioBridgeConstants.START_MSG).equals(RioBridgeConstants.START_MSG)) {
                        System.out.println("Connected to server.");
                        isConnected = true;
                        break;  // Switch to connected loop
                    }
                } catch (StatusRuntimeException e) {
                    // Still disconnected
                    //System.out.println("Still trying");
                }

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    finishLatch.countDown(); // Quit completely
                    break;
                }
            }

            while (isConnected) {
                healthCheck = messages[message];
                System.out.printf("\nClient sent: %s\n", healthCheck);

                try {
                    response = client.sendHealthCheck(healthCheck);
                } catch (StatusRuntimeException e) {
                    System.out.printf("\nFailed to send: %s\n", healthCheck);
                    System.out.println("Disconnected from server, will try to reconnect.");
                    isConnected = false;
                    break;  // Switch to disconnected loop
                }
                System.out.printf("Client got: %s\n", response);

                message = (message + 1) % 3;

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    finishLatch.countDown(); // Quit completely
                    break;
                }
            }
        }
        System.out.println("Client ended successfully.");
    }
}
