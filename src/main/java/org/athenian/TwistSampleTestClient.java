package org.athenian;

import io.grpc.stub.StreamObserver;
import org.athenian.grpc.TwistData;


public class TwistSampleTestClient {
    public static void main(String[] args) {
        TwistSampleClient twistSampleClient = new TwistSampleClient(RioBridgeConstants.hostname, RioBridgeConstants.port);
        System.out.println("Client started.");

        long timer = System.currentTimeMillis();
        StreamObserver<TwistData> observer = twistSampleClient.streamTwistData();
        for (double angular = 0.25; angular > -0.25; angular -= 0.0001) {
            sendTwist(observer, 0.5, angular);
        }
        observer.onCompleted();
        System.out.printf("Streamed 50 Twist values in %f seconds",
                (System.currentTimeMillis() - timer) / 1000d);
    }

    public static void sendTwist(StreamObserver<TwistData> observer, double speed, double angular) {
        TwistData twistData = TwistData.newBuilder()
                .setLinearX(speed)
                .setAngularZ(angular)
                .build();
        System.out.printf("Client sent: %s", twistData.toString());
        observer.onNext(twistData);
    }
}
