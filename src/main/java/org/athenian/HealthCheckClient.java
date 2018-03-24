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

    public HealthCheckClient(String hostname, int port) {
        AtomicReference<ManagedChannel> channelRef = new AtomicReference<>();
        channelRef.set(NettyChannelBuilder.forAddress(hostname, port)
                .usePlaintext(true)
                .build());
        this.blockingStubRef.set(newBlockingStub(channelRef.get()));
    }

    public String sendHealthCheck(String healthCheck) {
        return blockingStubRef.get().healthCheck(StringValue.newBuilder().setValue(healthCheck).build()).getValue();
    }
}
