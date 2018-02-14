package org.athenian;

import com.google.protobuf.Empty;
import com.google.protobuf.StringValue;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.athenian.grpc.StrategyServiceGrpc;
import org.athenian.grpc.TwistData;
import org.athenian.grpc.TwistSampleServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class StrategyService extends StrategyServiceGrpc.StrategyServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(StrategyService.class);
    private Server server;

    private final Consumer<TwistData> onMessage;
    private final AtomicReference<StreamObserver<StringValue>> strategyObserver = new AtomicReference<>();

    public StrategyService(int port, Consumer<TwistData> onMessage) {
        this.onMessage = onMessage;
        server = ServerBuilder.forPort(port)
                .addService(this)
                .build();
    }

    public void start() throws IOException {
        server.start();
    }

    public boolean isConnected() {
        return strategyObserver.get() != null;
    }

    public void sendStrategy(String strategy) {
        StreamObserver<StringValue> observer = strategyObserver.get();
        if (observer == null) {
            logger.info("Error in sendStrategy(): connection to client is not established");
            return;
        }

        observer.onNext(StringValue.newBuilder().setValue(strategy).build());
    }

    @Override
    public StreamObserver<TwistData> startStrategyStream(StreamObserver<StringValue> responseObserver) {
        strategyObserver.set(responseObserver);

        return new StreamObserver<TwistData>() {
            @Override
            public void onNext(TwistData TwistData) {
                onMessage.accept(TwistData);
            }

            @Override
            public void onError(Throwable throwable) {
                try {
                    final Status status = Status.fromThrowable(throwable);
                    if (status != Status.CANCELLED)
                        logger.info("Error in startStrategyStream(): {}", status);
                    responseObserver.onNext(StringValue.getDefaultInstance());
                    responseObserver.onCompleted();
                }
                catch (StatusRuntimeException e) {
                    // Do nothing
                }
                strategyObserver.set(null);  // No longer connected
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(StringValue.getDefaultInstance());
                responseObserver.onCompleted();

                strategyObserver.set(null);  // No longer connected
            }
        };
    }
}
