package heig.dai.pw03.metric;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import heig.dai.pw03.monitor.Monitor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Metric {
    CPU("230.0.0.1"),
    RAM("230.0.0.2"),
    DSK("230.0.0.3");

    private final String groupAddr;

    public double value() {
        return switch (this) {
            case CPU -> Monitor.getCPU();
            case RAM -> Monitor.getRAM();
            case DSK -> Monitor.getDSK();
            default -> throw new IllegalStateException("Unexpected value: " + this);
        };
    }

    public InetAddress getGroupAddress() {
        try {
            return InetAddress.getByName(groupAddr);
        } catch (UnknownHostException e) {
            throw new IllegalStateException(e);
        }
    }
}
