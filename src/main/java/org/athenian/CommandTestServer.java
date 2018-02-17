package org.athenian;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class CommandTestServer {
    private static String[] messages = {"Lorem ipsum", "dolor sit amet", "consectetur adipiscing elit"};
    int message = 0;

    public static void main(String[] args) {
        new CommandTestServer().run();
    }

    private void run() {
        CommandService service = new CommandService(
                RioBridgeConstants.port,
                this::onMessage);

        try {
            service.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
        System.out.println("Connection established.");

        while (finishLatch.getCount() > 0) {
            String command = messages[message];
            System.out.printf("\nServer sent: %s\n", command);
            service.sendCommand(command);

            message = (message + 1) % 3;

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
                finishLatch.countDown();
            }
        }
    }

    private void onMessage(String command) {
        System.out.printf("Server got: \"%s\"\n", command);
    }
}
