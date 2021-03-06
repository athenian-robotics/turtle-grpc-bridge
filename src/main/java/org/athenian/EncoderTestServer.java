package org.athenian;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.athenian.grpc.EncoderData;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class EncoderTestServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        EncoderService service = new EncoderService();
        Server server = ServerBuilder.forPort(RioBridgeConstants.port)
                .addService(service)
                .build();

        server.start();

        System.out.println("Server started.  Waiting for connection...");
        while (!service.isConnected()) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }

        CountDownLatch finishLatch = new CountDownLatch(1);

        while (finishLatch.getCount() > 0) {
            EncoderData encoderData = EncoderData.newBuilder()
                    .setLeft(Math.random() * 2 - 1)
                    .setRight(Math.random() * 2 - 1)
                    .build();
            System.out.printf("\nServer sent: %s\n", encoderData.toString());
            service.sendEncoderData(encoderData);

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
                finishLatch.countDown();
            }
        }
    }
}
