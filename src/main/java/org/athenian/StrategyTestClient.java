package org.athenian;

import com.google.protobuf.StringValue;
import io.grpc.stub.StreamObserver;
import org.athenian.grpc.TwistData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class StrategyTestClient {
    private static List<Supplier<TwistData>> strategies = new ArrayList<>();
    private static AtomicInteger strategy = new AtomicInteger(0);
    private CountDownLatch finishLatch;

    public static void main(String[] args) {
        new StrategyTestClient().run();
    }

    private StrategyTestClient() {
        strategies.add(TwistData.newBuilder()::build);
        strategies.add(TwistData.newBuilder().setLinearX(0.1)::build);
        strategies.add(TwistData.newBuilder().setAngularZ(0.1)::build);
    }

    private void run() {
        StrategyClient client = new StrategyClient(
                RioBridgeConstants.hostname,
                RioBridgeConstants.port,
                this::onMessage);

        finishLatch = client.startStrategyStream();
        System.out.println("Client started.");

        while (finishLatch.getCount() > 0) {
            TwistData twistData = strategies.get(strategy.get()).get();
            System.out.printf("Client sent: %s\n", twistData.toString());
            client.sendTwistData(twistData);

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
                finishLatch.countDown();
            }
        }
    }

    private void onMessage(StringValue strategyName) {
        try {
            int strat = Integer.parseInt(strategyName.getValue());
            if (strat < 0 || strat >= strategies.size()) {
                onInvalid(strategyName.getValue());
                return;
            }

            strategy.set(strat);
            System.out.printf("\nStarted strategy %d\n", strat);
        }
        catch (NumberFormatException e) {
            onInvalid(strategyName.getValue());
        }
    }

    private void onInvalid(String message) {
        System.out.printf("\nIgnored invalid strategy: %s\n", message);
    }
}
