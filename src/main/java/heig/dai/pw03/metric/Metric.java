package heig.dai.pw03.metric;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import heig.dai.pw03.monitor.Monitor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Represents a metric. Each metric has a multicast group address.
 */
@RequiredArgsConstructor
public enum Metric {
    CPU("230.0.0.1"),
    RAM("230.0.0.2"),
    DSK("230.0.0.3");

    private final String groupAddr;

    /**
     * Get the multicast socket address for this metric.
     * @return The multicast socket address
     */
    public InetAddress getGroupAddress() {
        try {
            return InetAddress.getByName(groupAddr);
        } catch (UnknownHostException e) {
            throw new IllegalStateException(e);
        }
    }
}
