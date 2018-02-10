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

import java.util.function.Consumer;

public class StrategyService extends StrategyServiceGrpc.StrategyServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(StrategyService.class);

    private final Consumer<TwistData> onMessage;
    private StreamObserver<StringValue> strategyObserver;

    private StrategyService(Consumer<TwistData> onMessage) {
        this.onMessage = onMessage;
    }

    public static Server createServer(int port, Consumer<TwistData> onMessage) {
        return ServerBuilder.forPort(port)
                .addService(new StrategyService(onMessage))
                .build();
    }

    public void runStrategy(String strategy) {
        if (strategyObserver == null) {
            System.out.println("Error in runStrategy(): connection to client is not established");
            logger.info("Error in runStrategy(): connection to client is not established");
            return;
        }

        strategyObserver.onNext(StringValue.newBuilder().setValue(strategy).build());
        strategyObserver.onCompleted();
    }

    @Override
    public StreamObserver<TwistData> startStrategyStream(StreamObserver<StringValue> responseObserver) {
        strategyObserver = responseObserver;

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
                strategyObserver = null;  // No longer connected
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(StringValue.getDefaultInstance());
                responseObserver.onCompleted();
            }
        };
    }
}
