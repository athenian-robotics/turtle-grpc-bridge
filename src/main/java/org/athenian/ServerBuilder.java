package org.athenian;

public abstract class ServerBuilder {
    // Proxy for io.grpc.ServerBuilder.forPort()
    public io.grpc.ServerBuilder<?> forPort(int port) {
        return io.grpc.ServerBuilder.forPort(port);
    }
}
