package heig.dai.pw03.metric;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class MetricSender implements Runnable {

    private final DatagramSocket socket;
    private final InetSocketAddress group;
    private final Metric metric;

    @Override
    public void run() {
        try {
            log.debug("Sending metrics to server");
            var value = metric.value();
            String message = "metric{value=%.2f, type=%s, host=%s}".formatted(value, metric.name(), InetAddress.getLocalHost().getHostName());
            byte[] payload = message.getBytes(StandardCharsets.UTF_8);
            DatagramPacket datagram = new DatagramPacket(payload, payload.length, group);
            socket.send(datagram);
        } catch (IOException e) {
            log.error("Error while sending metrics", e);
            throw new UncheckedIOException(e);
        }
    }
}
