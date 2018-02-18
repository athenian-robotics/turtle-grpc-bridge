package org.athenian;

import com.google.protobuf.StringValue;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.athenian.grpc.EncoderData;
import org.athenian.grpc.TwistData;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class AllServicesTestServer {
    private boolean servicesConnected = false;

    public static void main(String[] args) {
        new AllServicesTestServer().run();
    }

    private void run() {
        StrategyService strategyService = new StrategyService((TwistData twistData) ->
                onMessage("[StrategyService]","twist", twistData.toString()));
        CommandService commandService = new CommandService((String command) ->
                onMessage("[CommandService]","command", command));
        EncoderService encoderService = new EncoderService();
        HealthCheckService healthCheckService = new HealthCheckService((String message) ->
                onMessage("[HealthCheckService]","message", message));

        Server server = ServerBuilder.forPort(RioBridgeConstants.port)
                .addService(strategyService)
                .addService(commandService)
                .addService(encoderService)
                .addService(healthCheckService)
                .build();

        try {
            server.start();
            System.out.println("[Server] Started.  Waiting for connections...");
        } catch (IOException e) {
            System.out.println("[Server] Could not be started: ");
            e.printStackTrace();
            return;
        }

        int oldConnections, connections = 0;

        while (connections < 4) {
            oldConnections = connections;
            connections = (strategyService.isConnected() ? 1 : 0)
                        + (commandService.isConnected() ? 1 : 0)
                        + (encoderService.isConnected() ? 1 : 0)
                        + (healthCheckService.isConnected() ? 1 : 0);

            if (connections != oldConnections) {
                System.out.printf("[Server] %d services connected.\n", connections);
            }

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }

        servicesConnected = true;
        System.out.println("[Server] All services connected. Sending data...\n");



        CountDownLatch finishLatch = new CountDownLatch(1);

        int strategy = 0;
        int strategyTimeout = 0;

        String[] messages = {"Lorem ipsum", "dolor sit amet", "consectetur adipiscing elit"};
        int message = 0;

        while (finishLatch.getCount() > 0) {
            if (strategyTimeout <= 0) {
                strategyTimeout = 40;
                System.out.printf("[StrategyService] Sent strategy: %d\n", strategy);
                strategyService.sendStrategy(Integer.toString(strategy));
                strategy = (strategy + 1) % 4;
            }
            strategyTimeout--;

            String command = messages[message];
            System.out.printf("[CommandService] Sent command: %s\n", command);
            commandService.sendCommand(command);

            EncoderData encoderData = EncoderData.newBuilder()
                    .setLeft(Math.random() * 2 - 1)
                    .setRight(Math.random() * 2 - 1)
                    .build();
            System.out.printf("[EncoderService] Sent encoders: %s\n", encoderData.toString());
            encoderService.sendEncoderData(encoderData);

            String healthCheck = messages[message];
            System.out.printf("[HealthCheckService] Sent message: %s\n", healthCheck);
            healthCheckService.sendHealthCheck(healthCheck);

            message = (message + 1) % 3;

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
                finishLatch.countDown();
            }
        }
    }

    public void onMessage(String service, String dataType, String dataString) {
        if (servicesConnected)
            System.out.printf("[%s] Got %s: %s\n", service, dataType, dataString);
    }
}
