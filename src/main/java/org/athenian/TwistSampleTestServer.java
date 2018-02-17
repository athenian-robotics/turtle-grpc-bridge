package org.athenian;

import io.grpc.Server;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class TwistSampleTestServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server twistSampleServer = TwistSampleService.createServer(RioBridgeConstants.port, twistData ->
                System.out.printf("Server got: %s", twistData.toString())
        );

        twistSampleServer.start();
        System.out.println("Server started.  Listening...");

        CountDownLatch latch = new CountDownLatch(1);
        latch.await();

        System.out.println("Connection completed successfully.");
    }
}
