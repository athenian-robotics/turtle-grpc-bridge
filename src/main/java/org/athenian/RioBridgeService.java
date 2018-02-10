package org.athenian;

import com.google.protobuf.Empty;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.athenian.grpc.TwistData;
import org.athenian.grpc.TwistServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class RioBridgeService extends TwistServiceGrpc.TwistServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(RioBridgeService.class);

    private final Consumer<TwistData> onMessage;

    private RioBridgeService(Consumer<TwistData> onMessage) {
        this.onMessage = onMessage;
    }

    public static Server createServer(int port, Consumer<TwistData> onMessage) {
        return ServerBuilder.forPort(port)
                .addService(new RioBridgeService(onMessage))
                .build();
    }

    @Override
    public void writeTwistData(TwistData request, StreamObserver<Empty> responseObserver) {
        onMessage.accept(request);
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<TwistData> streamTwistData(StreamObserver<Empty> responseObserver) {
        // TODO write something here
        return new StreamObserver<TwistData>() {
            @Override
            public void onNext(TwistData twistData) {
                onMessage.accept(twistData);
            }

            @Override
            public void onError(Throwable throwable) {
                try {
                    final Status status = Status.fromThrowable(throwable);
                    if (status != Status.CANCELLED)
                        logger.info("Error in streamTwistData(): {}", status);
                    responseObserver.onNext(Empty.getDefaultInstance());
                    responseObserver.onCompleted();
                }
                catch (StatusRuntimeException e) {
                    // Do nothing
                }

            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(Empty.getDefaultInstance());
                responseObserver.onCompleted();
            }
        };
    }
}
