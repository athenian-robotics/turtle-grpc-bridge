package org.athenian;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class StrategyTestServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        StrategyService service = new StrategyService(RioBridgeConstants.port, twistData ->
                System.out.printf("Server got: %s\n", twistData.toString())
        );

        service.start();
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
        int strategy = 0;

        while (finishLatch.getCount() > 0) {
            System.out.printf("\nStarting strategy %d\n", strategy);
            service.sendStrategy(Integer.toString(strategy));
            strategy = (strategy + 1) % 4;

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                finishLatch.countDown();
            }
        }
    }
}
