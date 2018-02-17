package org.athenian;

import com.google.protobuf.StringValue;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.athenian.grpc.HealthCheckServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class HealthCheckService extends HealthCheckServiceGrpc.HealthCheckServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(HealthCheckService.class);

    private final Consumer<String> onMessage;
    private final AtomicReference<StreamObserver<StringValue>> messageObserver = new AtomicReference<>();

    public HealthCheckService(Consumer<String> onMessage) {
        this.onMessage = onMessage;
    }

    public boolean isConnected() {
        return messageObserver.get() != null;
    }

    public void sendHealthCheck(String healthCheck) {
        StreamObserver<StringValue> observer = messageObserver.get();
        if (observer == null) {
            logger.info("Error in sendHealthCheck(): connection to client is not established");
            return;
        }

        observer.onNext(StringValue.newBuilder().setValue(healthCheck).build());
    }

    @Override
    public StreamObserver<StringValue> healthCheck(StreamObserver<StringValue> responseObserver) {
        messageObserver.set(responseObserver);

        return new StreamObserver<StringValue>() {
            @Override
            public void onNext(StringValue healthCheck) {
                onMessage.accept(healthCheck.getValue());
            }

            @Override
            public void onError(Throwable throwable) {
                try {
                    final Status status = Status.fromThrowable(throwable);
                    if (status != Status.CANCELLED)
                        logger.info("Error in startHealthCheckStream(): {}", status);
                    responseObserver.onNext(StringValue.getDefaultInstance());
                    responseObserver.onCompleted();
                }
                catch (StatusRuntimeException e) {
                    // Do nothing
                }
                messageObserver.set(null);  // No longer connected
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(StringValue.getDefaultInstance());
                responseObserver.onCompleted();

                messageObserver.set(null);  // No longer connected
            }
        };
    }
}
