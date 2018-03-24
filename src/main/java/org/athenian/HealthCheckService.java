package org.athenian;

import com.google.protobuf.StringValue;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.athenian.grpc.HealthCheckServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class HealthCheckService extends HealthCheckServiceGrpc.HealthCheckServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(HealthCheckService.class);

    private final Function<String, String> onMessage;

    public HealthCheckService(Function<String, String> onMessage) {
        this.onMessage = onMessage;
    }

    @Override
    public void healthCheck(StringValue request, StreamObserver<StringValue> responseObserver) {
        responseObserver.onNext(StringValue.newBuilder()
                .setValue(onMessage.apply(request.getValue()))
                .build());
        responseObserver.onCompleted();
    }
}
