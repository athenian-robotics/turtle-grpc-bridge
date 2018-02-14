package org.athenian;

import com.google.protobuf.StringValue;
import org.athenian.grpc.TwistData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class HealthCheckTestClient {
    private static String[] messages = {"The quick brown fox", "jumped over", "the lazy dog"};
    int message = 0;
    private CountDownLatch finishLatch;

    public static void main(String[] args) {
        new HealthCheckTestClient().run();
    }

    private void run() {
        StrategyClient client = new StrategyClient(
                RioBridgeConstants.hostname,
                RioBridgeConstants.port,
                this::onMessage);

        finishLatch = client.startStrategyStream();
        System.out.println("Client started.");

        while (finishLatch.getCount() > 0) {
            StringValue healthData = StringValue.newBuilder().setValue(messages[message]).build();
            System.out.printf("Client sent: %s\n", healthData.toString());
            client.sendTwistData(healthData);

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
                finishLatch.countDown();
            }
        }
    }

    private void onMessage(StringValue message) {
        System.out.printf("Client got: \"%s\"\n", message.toString());
    }

    private void onInvalid(String message) {
        System.out.printf("\nIgnored invalid strategy: %s\n", message);
    }
}
