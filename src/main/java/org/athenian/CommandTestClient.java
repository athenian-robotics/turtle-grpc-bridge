package org.athenian;

import java.util.concurrent.CountDownLatch;

public class CommandTestClient {
    private static String[] messages = {"The quick brown fox", "jumped over", "the lazy dog"};
    int message = 0;

    public static void main(String[] args) {
        new CommandTestClient().run();
    }

    private void run() {
        CommandClient client = new CommandClient(
                RioBridgeConstants.hostname,
                RioBridgeConstants.port,
                this::onMessage);

        CountDownLatch finishLatch = client.startCommandStream();
        System.out.println("Client started.");

        while (finishLatch.getCount() > 0) {
            String command = messages[message];
            System.out.printf("Client sent: %s\n", command);
            client.sendCommand(command);

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
        System.out.printf("Client got: \"%s\"\n", command);
    }
}
