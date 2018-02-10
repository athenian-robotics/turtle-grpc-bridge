package org.athenian;

import com.google.protobuf.Empty;
import com.google.protobuf.StringValue;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.athenian.grpc.EncoderServiceGrpc;
import org.athenian.grpc.EncoderData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class EncoderService extends EncoderServiceGrpc.EncoderServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(EncoderService.class);

    private final Consumer<EncoderData> onMessage;
    private StreamObserver<EncoderData> encoderObserver;

    private EncoderService(Consumer<EncoderData> onMessage) {
        this.onMessage = onMessage;
    }

    public static Server createServer(int port, Consumer<EncoderData> onMessage) {
        return ServerBuilder.forPort(port)
                .addService(new EncoderService(onMessage))
                .build();
    }
    
    public void sendEncoderData(double leftEncoder, double rightEncoder) {
        if (encoderObserver == null) {
            System.out.println("Error in runEncoder(): connection to client is not established");
            logger.info("Error in runEncoder(): connection to client is not established");
            return;
        }
        
        encoderObserver.onNext(EncoderData.newBuilder().setLeft(leftEncoder).setRight(rightEncoder).build());
        encoderObserver.onCompleted();
    }

    @Override
    public void startEncoderStream(Empty request, StreamObserver<EncoderData> responseObserver) {
        encoderObserver = responseObserver;
    }
}
