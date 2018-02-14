package org.athenian;

import com.google.protobuf.StringValue;
import io.grpc.ManagedChannel;
import io.grpc.Status;
import io.grpc.netty.NettyChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.athenian.grpc.TwistData;
import org.athenian.grpc.StrategyServiceGrpc;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.athenian.grpc.StrategyServiceGrpc.newBlockingStub;
import static org.athenian.grpc.StrategyServiceGrpc.newStub;


public class StrategyClient {
    private final AtomicReference<StrategyServiceGrpc.StrategyServiceBlockingStub> blockingStubRef = new AtomicReference<>();
    private final AtomicReference<StrategyServiceGrpc.StrategyServiceStub> asyncStubRef = new AtomicReference<>();

    private final Consumer<StringValue> onMessage;
    private final AtomicReference<StreamObserver<TwistData>> twistObserver = new AtomicReference<>();

    public StrategyClient(String hostname, int port, Consumer<StringValue> onMessage) {
        AtomicReference<ManagedChannel> channelRef = new AtomicReference<>();
        channelRef.set(NettyChannelBuilder.forAddress(hostname, port)
                .usePlaintext(true)
                .build());
        this.blockingStubRef.set(newBlockingStub(channelRef.get()));
        this.asyncStubRef.set(newStub(channelRef.get()));

        this.onMessage = onMessage;
    }

    public void sendTwistData(TwistData twistData) {
        twistObserver.get().onNext(twistData);
    }

    public CountDownLatch startStrategyStream() {
        final CountDownLatch finishLatch = new CountDownLatch(1);

        twistObserver.set(asyncStubRef.get().startStrategyStream(new StreamObserver<StringValue>() {
            @Override
            public void onNext(StringValue strategy) {
                onMessage.accept(strategy);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Error");
                final Status status = Status.fromThrowable(throwable);
                if (status != Status.CANCELLED)
                    System.out.printf("Error in streamTwistData(): %s", status);
                finishLatch.countDown();
            }

            @Override
            public void onCompleted() {
                finishLatch.countDown();
            }
        }));

        return finishLatch;
    }
}
