package org.athenian;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class HealthCheckTestServer {
    private static String[] messages = {"Lorem ipsum", "dolor sit amet", "consectetur adipiscing elit"};
    private static boolean isConnected = false;
    int message = 0;

    public static void main(String[] args) {
        new HealthCheckTestServer().run();
    }

    private void run() {
        HealthCheckService service = new HealthCheckService(this::onMessage);
        Server server = ServerBuilder.forPort(RioBridgeConstants.port)
                .addService(service)
                .build();

        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Server started.  Waiting for connection...");
        while (!this.isConnected) {
            try {

                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }

        CountDownLatch finishLatch = new CountDownLatch(1);
        System.out.println("Connection established.");

        try {
            finishLatch.await();
        } catch(InterruptedException e) {
            System.out.println("Server interrupted");
        }

        System.out.println("Server ended successfully.");
    }

    private String onMessage(String healthCheck) {
        this.isConnected = true;

        System.out.printf("\nServer got: %s\n", healthCheck);

        System.out.printf("Server sent: %s\n", healthCheck);
        return healthCheck;
    }
}
