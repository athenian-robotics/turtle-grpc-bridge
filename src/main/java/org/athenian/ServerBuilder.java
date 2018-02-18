package org.athenian;

public abstract class ServerBuilder {
    public io.grpc.ServerBuilder<?> forPort(int port) {
        return io.grpc.ServerBuilder.forPort(port);
    }
}
