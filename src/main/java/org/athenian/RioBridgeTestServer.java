package org.athenian;

import io.grpc.Server;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class RioBridgeTestServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server rioBridgeServer = RioBridgeService.createServer(RioBridgeTest.port, twistData ->
                System.out.printf("Server got: %s", twistData.toString())
        );

        rioBridgeServer.start();
        System.out.println("Server started.  Listening...");

        CountDownLatch latch = new CountDownLatch(1);
        latch.await();
    }
}
