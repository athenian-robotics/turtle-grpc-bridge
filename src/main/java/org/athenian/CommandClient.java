package org.athenian;

import com.google.protobuf.StringValue;
import io.grpc.ManagedChannel;
import io.grpc.Status;
import io.grpc.netty.NettyChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.athenian.grpc.CommandServiceGrpc;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.athenian.grpc.CommandServiceGrpc.newBlockingStub;
import static org.athenian.grpc.CommandServiceGrpc.newStub;


public class CommandClient {
    private final AtomicReference<CommandServiceGrpc.CommandServiceBlockingStub> blockingStubRef = new AtomicReference<>();
    private final AtomicReference<CommandServiceGrpc.CommandServiceStub> asyncStubRef = new AtomicReference<>();

    private final Consumer<String> onMessage;
    private final AtomicReference<StreamObserver<StringValue>> messageObserver = new AtomicReference<>();

    public CommandClient(String hostname, int port, Consumer<String> onMessage) {
        AtomicReference<ManagedChannel> channelRef = new AtomicReference<>();
        channelRef.set(NettyChannelBuilder.forAddress(hostname, port)
                .usePlaintext(true)
                .build());
        this.blockingStubRef.set(newBlockingStub(channelRef.get()));
        this.asyncStubRef.set(newStub(channelRef.get()));

        this.onMessage = onMessage;
    }

    public void sendCommand(String command) {
        messageObserver.get().onNext(StringValue.newBuilder().setValue(command).build());
    }

    public CountDownLatch startCommandStream() {
        final CountDownLatch finishLatch = new CountDownLatch(1);

        messageObserver.set(asyncStubRef.get().startCommandStream(new StreamObserver<StringValue>() {
            @Override
            public void onNext(StringValue command) {
                onMessage.accept(command.getValue());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Error");
                final Status status = Status.fromThrowable(throwable);
                if (status != Status.CANCELLED)
                    System.out.printf("Error in startCommand(): %s", status);
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
