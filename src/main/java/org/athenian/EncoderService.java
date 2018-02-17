package org.athenian;

import com.google.protobuf.Empty;
import io.grpc.Server;
import io.grpc.stub.StreamObserver;
import org.athenian.grpc.EncoderServiceGrpc;
import org.athenian.grpc.EncoderData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class EncoderService extends EncoderServiceGrpc.EncoderServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(EncoderService.class);

    private AtomicReference<StreamObserver<EncoderData>> encoderObserver = new AtomicReference<>();

    public EncoderService() {

    }

    public boolean isConnected() {
        return encoderObserver.get() != null;
    }

    public void sendEncoderData(EncoderData encoderData) {
        if (encoderObserver.get() == null) {
            logger.info("Error in runEncoder(): connection to client is not established");
            return;
        }
        
        encoderObserver.get().onNext(encoderData);
    }

    @Override
    public void startEncoderStream(Empty request, StreamObserver<EncoderData> responseObserver) {
        encoderObserver.set(responseObserver);
    }
}
