package org.athenian;

import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.Status;
import io.grpc.netty.NettyChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.athenian.grpc.EncoderServiceGrpc;
import org.athenian.grpc.EncoderData;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.athenian.grpc.EncoderServiceGrpc.newBlockingStub;
import static org.athenian.grpc.EncoderServiceGrpc.newStub;


public class EncoderClient {
    private final AtomicReference<EncoderServiceGrpc.EncoderServiceBlockingStub> blockingStubRef = new AtomicReference<>();
    private final AtomicReference<EncoderServiceGrpc.EncoderServiceStub> asyncStubRef = new AtomicReference<>();

    private final Consumer<EncoderData> onMessage;

    public EncoderClient(String hostname, int port, Consumer<EncoderData> onMessage) {
        AtomicReference<ManagedChannel> channelRef = new AtomicReference<>();
        channelRef.set(NettyChannelBuilder.forAddress(hostname, port)
                .usePlaintext(true)
                .build());
        this.blockingStubRef.set(newBlockingStub(channelRef.get()));
        this.asyncStubRef.set(newStub(channelRef.get()));

        this.onMessage = onMessage;
    }

    public CountDownLatch startEncoderStream() {
        final CountDownLatch finishLatch = new CountDownLatch(1);

        asyncStubRef.get().startEncoderStream(Empty.getDefaultInstance(), new StreamObserver<EncoderData>() {
            @Override
            public void onNext(EncoderData encoder) {
                onMessage.accept(encoder);
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
        });

        return finishLatch;
    }
}
