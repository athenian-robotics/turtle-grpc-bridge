package org.athenian;

import com.google.protobuf.StringValue;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.athenian.grpc.CommandServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class CommandService extends CommandServiceGrpc.CommandServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(CommandService.class);

    private final Consumer<String> onMessage;
    private final AtomicReference<StreamObserver<StringValue>> messageObserver = new AtomicReference<>();

    public CommandService(Consumer<String> onMessage) {
        this.onMessage = onMessage;
    }

    public boolean isConnected() {
        return messageObserver.get() != null;
    }

    public void sendCommand(String command) {
        StreamObserver<StringValue> observer = messageObserver.get();
        if (observer == null) {
            logger.info("Error in sendCommand(): connection to client is not established");
            return;
        }

        observer.onNext(StringValue.newBuilder().setValue(command).build());
    }

    @Override
    public StreamObserver<StringValue> startCommandStream(StreamObserver<StringValue> responseObserver) {
        messageObserver.set(responseObserver);

        return new StreamObserver<StringValue>() {
            @Override
            public void onNext(StringValue command) {
                onMessage.accept(command.getValue());
            }

            @Override
            public void onError(Throwable throwable) {
                try {
                    final Status status = Status.fromThrowable(throwable);
                    if (status != Status.CANCELLED)
                        logger.info("Error in startCommandStream(): {}", status);
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
