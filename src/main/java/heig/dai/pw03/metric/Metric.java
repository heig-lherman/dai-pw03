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

    public InetAddress getGroupAddress() {
        try {
            return InetAddress.getByName(groupAddr);
        } catch (UnknownHostException e) {
            throw new IllegalStateException(e);
        }
    }
}
