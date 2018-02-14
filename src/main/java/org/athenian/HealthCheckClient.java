package org.athenian;

import com.google.protobuf.StringValue;
import io.grpc.ManagedChannel;
import io.grpc.Status;
import io.grpc.netty.NettyChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.athenian.grpc.HealthCheckServiceGrpc;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.athenian.grpc.HealthCheckServiceGrpc.newBlockingStub;
import static org.athenian.grpc.HealthCheckServiceGrpc.newStub;


public class HealthCheckClient {
    private final AtomicReference<HealthCheckServiceGrpc.HealthCheckServiceBlockingStub> blockingStubRef = new AtomicReference<>();
    private final AtomicReference<HealthCheckServiceGrpc.HealthCheckServiceStub> asyncStubRef = new AtomicReference<>();

    private final Consumer<StringValue> onMessage;
    private final AtomicReference<StreamObserver<StringValue>> messageObserver = new AtomicReference<>();

    public HealthCheckClient(String hostname, int port, Consumer<StringValue> onMessage) {
        AtomicReference<ManagedChannel> channelRef = new AtomicReference<>();
        channelRef.set(NettyChannelBuilder.forAddress(hostname, port)
                .usePlaintext(true)
                .build());
        this.blockingStubRef.set(newBlockingStub(channelRef.get()));
        this.asyncStubRef.set(newStub(channelRef.get()));

        this.onMessage = onMessage;
    }

    public void sendStringValue(StringValue stringValue) {
        messageObserver.get().onNext(stringValue);
    }

    public CountDownLatch startHealthCheck() {
        final CountDownLatch finishLatch = new CountDownLatch(1);

        messageObserver.set(asyncStubRef.get().healthCheck(new StreamObserver<StringValue>() {
            @Override
            public void onNext(StringValue healthCheck) {
                onMessage.accept(healthCheck);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Error");
                final Status status = Status.fromThrowable(throwable);
                if (status != Status.CANCELLED)
                    System.out.printf("Error in startHealthCheck(): %s", status);
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
