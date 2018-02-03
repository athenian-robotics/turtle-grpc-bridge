package org.athenian;

import io.grpc.Server;
import twist_service.TwistData;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

public class TwistTestServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server twistServer = TwistService.createServer(TwistTest.port, new Consumer<TwistData>() {
            @Override
            public void accept(TwistData twistData) {
                System.out.printf("Server got: %s", twistData.toString());
            }
        });

        twistServer.start();
        System.out.println("Server started.  Listening...");

        CountDownLatch latch = new CountDownLatch(1);
        latch.await();
    }
}
