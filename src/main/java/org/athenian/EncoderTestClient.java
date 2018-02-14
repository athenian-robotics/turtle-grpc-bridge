package org.athenian;

import com.google.protobuf.StringValue;
import org.athenian.grpc.EncoderData;
import org.athenian.grpc.TwistData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class EncoderTestClient {

    public static void main(String[] args) {
        new EncoderTestClient().run();
    }

    private void run() {
        EncoderClient client = new EncoderClient(
                RioBridgeConstants.hostname,
                RioBridgeConstants.port,
                this::onMessage);

        CountDownLatch finishLatch = client.startEncoderStream();
        System.out.println("Client started.");

        try {
            finishLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void onMessage(EncoderData encoderData) {
        System.out.printf("\nClient got: %s\n", encoderData.toString());
    }
}
