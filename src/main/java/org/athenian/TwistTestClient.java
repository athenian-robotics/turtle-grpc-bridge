package org.athenian;

import io.grpc.stub.StreamObserver;
import org.athenian.grpc.TwistData;


public class TwistTestClient {
    public static void main(String[] args) {
        TwistClient twistClient = new TwistClient(TwistTest.hostname, TwistTest.port);
        System.out.println("Client started.");

        System.out.println("\nWrite test started");
        long timer = System.currentTimeMillis();
        for (double speed = 0; speed < 0.5; speed += 0.01) {
            writeTwist(twistClient, speed, 0.25);
        }
        System.out.printf("Write test finished in %f seconds",
                (System.currentTimeMillis() - timer) / 1000000d);


        System.out.println("\nStream test started.");
        timer = System.currentTimeMillis();
        StreamObserver<TwistData> observer = twistClient.streamTwistData();
        for (double angular = 0.25; angular > -0.25; angular -= 0.01) {
            streamTwist(observer, 0.5, angular);
        }
        observer.onCompleted();
        System.out.printf("Stream test finished in %f seconds",
                (System.currentTimeMillis() - timer) / 1000000d);
    }

    private static void writeTwist(TwistClient twistClient, double speed, double angular) {
        TwistData twistData = TwistData.newBuilder()
                .setLinearX(speed)
                .setAngularZ(angular)
                .build();
        System.out.printf("Client sent: %s", twistData);
        twistClient.writeTwistData(twistData);
    }

    public static void streamTwist(StreamObserver<TwistData> observer, double speed, double angular) {
        TwistData twistData = TwistData.newBuilder()
                .setLinearX(speed)
                .setAngularZ(angular)
                .build();
        System.out.printf("Client sent: %s", twistData);
        observer.onNext(twistData);
    }
}
