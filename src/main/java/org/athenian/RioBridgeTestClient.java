package org.athenian;

import io.grpc.stub.StreamObserver;
import org.athenian.grpc.TwistData;


public class RioBridgeTestClient {
    public static void main(String[] args) {
        RioBridgeClient rioBridgeClient = new RioBridgeClient(RioBridgeTest.hostname, RioBridgeTest.port);
        System.out.println("Client started.");

        System.out.println("\nWrite test started");
        long timer = System.currentTimeMillis();
        for (double speed = 0; speed < 0.5; speed += 0.01) {
            writeTwist(rioBridgeClient, speed, 0.25);
        }
        System.out.printf("Write test finished in %f seconds",
                (System.currentTimeMillis() - timer) / 1000d);


        System.out.println("\nStream test started.");
        timer = System.currentTimeMillis();
        StreamObserver<TwistData> observer = rioBridgeClient.streamTwistData();
        for (double angular = 0.25; angular > -0.25; angular -= 0.01) {
            streamTwist(observer, 0.5, angular);
        }
        observer.onCompleted();
        System.out.printf("Stream test finished in %f seconds",
                (System.currentTimeMillis() - timer) / 1000d);
    }

    private static void writeTwist(RioBridgeClient rioBridgeClient, double speed, double angular) {
        TwistData twistData = TwistData.newBuilder()
                .setLinearX(speed)
                .setAngularZ(angular)
                .build();
        System.out.printf("Client sent: %s", twistData.toString());
        rioBridgeClient.writeTwistData(twistData);
    }

    public static void streamTwist(StreamObserver<TwistData> observer, double speed, double angular) {
        TwistData twistData = TwistData.newBuilder()
                .setLinearX(speed)
                .setAngularZ(angular)
                .build();
        System.out.printf("Client sent: %s", twistData.toString());
        observer.onNext(twistData);
    }
}
