package org.athenian;

import com.google.protobuf.Empty;
import io.grpc.ConnectivityState;
import io.grpc.ManagedChannel;
import io.grpc.Status;
import io.grpc.netty.NettyChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.athenian.grpc.TwistData;
import org.athenian.grpc.TwistSampleServiceGrpc;

import java.util.concurrent.atomic.AtomicReference;

import static org.athenian.grpc.TwistSampleServiceGrpc.newBlockingStub;
import static org.athenian.grpc.TwistSampleServiceGrpc.newStub;


public class TwistSampleClient {
    // DEPRECATED blockingStubRef and writeTwistData()
    // private final AtomicReference<TwistSampleServiceGrpc.TwistSampleServiceBlockingStub> blockingStubRef = new AtomicReference<>();
    private final AtomicReference<TwistSampleServiceGrpc.TwistSampleServiceStub> asyncStubRef = new AtomicReference<>();
    private final AtomicReference<ManagedChannel> channelRef = new AtomicReference<>();

    public TwistSampleClient(String hostname, int port) {
        channelRef.set(NettyChannelBuilder.forAddress(hostname, port)
                .usePlaintext(true)
                .build());
        // DEPRECATED blockingStubRef and writeTwistData()
        // this.blockingStubRef.set(newBlockingStub(channelRef.get()));
        this.asyncStubRef.set(newStub(channelRef.get()));
        //channelRef.get().notifyWhenStateChanged();
    }

    // DEPRECATED blockingStubRef and writeTwistData()
    // public void writeTwistData(TwistData twistData) { this.blockingStubRef.get().writeTwistData(twistData); }

    public StreamObserver<TwistData> streamTwistData() {
        //for (int i = 0; i < 5; i++)
            //System.out.println(channelRef.get().getState(true));
        /*try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        return newStub(channelRef.get()).streamTwistData(new StreamObserver<Empty>() {
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
