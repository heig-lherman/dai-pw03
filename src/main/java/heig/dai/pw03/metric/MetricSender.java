package heig.dai.pw03.metric;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import heig.dai.pw03.monitor.Monitor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class MetricSender implements Runnable {

    private final DatagramSocket socket;
    private final InetSocketAddress group;
    private final Metric metric;
    private final String hostname;

    @Override
    public void run() {
        try {
            log.debug("Sending metrics to server");
            MetricMessage message = new MetricMessage(metric, hostname, Monitor.valueOf(metric));
            byte[] payload = message.toString().getBytes(StandardCharsets.UTF_8);
            DatagramPacket datagram = new DatagramPacket(payload, payload.length, group);
            socket.send(datagram);
        } catch (IOException e) {
            log.error("Error while sending metrics", e);
            throw new UncheckedIOException(e);
        }
    }
}
