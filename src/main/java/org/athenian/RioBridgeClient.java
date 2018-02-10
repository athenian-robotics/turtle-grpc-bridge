package org.athenian;

import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.Status;
import io.grpc.netty.NettyChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.athenian.grpc.TwistData;
import org.athenian.grpc.TwistServiceGrpc;

import java.util.concurrent.atomic.AtomicReference;

import static org.athenian.grpc.TwistServiceGrpc.newBlockingStub;
import static org.athenian.grpc.TwistServiceGrpc.newStub;


public class RioBridgeClient {
    private final AtomicReference<TwistServiceGrpc.TwistServiceBlockingStub> blockingStubRef = new AtomicReference<>();
    private final AtomicReference<TwistServiceGrpc.TwistServiceStub> asyncStubRef = new AtomicReference<>();


    public RioBridgeClient(String hostname, int port) {
        AtomicReference<ManagedChannel> channelRef = new AtomicReference<>();
        channelRef.set(NettyChannelBuilder.forAddress(hostname, port)
                .usePlaintext(true)
                .build());
        this.blockingStubRef.set(newBlockingStub(channelRef.get()));
        this.asyncStubRef.set(newStub(channelRef.get()));
    }

    public void writeTwistData(TwistData twistData) {
        this.blockingStubRef.get().writeTwistData(twistData);
    }

    public StreamObserver<TwistData> streamTwistData() {
        return asyncStubRef.get().streamTwistData(new StreamObserver<Empty>() {
            @Override
            public void onNext(Empty empty) {

            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Error");
                final Status status = Status.fromThrowable(throwable);
                if (status != Status.CANCELLED)
                    System.out.printf("Error in streamTwistData(): %s", status);
            }

            @Override
            public void onCompleted() {

            }
        });
    }
}