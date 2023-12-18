package heig.dai.pw03.aggregator;

import heig.dai.pw03.aggregator.MetricCollector;
import heig.dai.pw03.metric.MetricMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.nio.charset.StandardCharsets;

/**
 * Thread runnable that handles receiving metrics on a multicast socket.
 *
 * @author Loïc Herman
 * @author Massimo Stefani
 */
@Slf4j
@RequiredArgsConstructor
public final class MetricReceiver implements Runnable {

    private final MulticastSocket socket;
    private final MetricCollector collector;

    @Override
    public void run() {
        try {
            byte[] receiveData = new byte[1024];
            while (true) {
                DatagramPacket packet = new DatagramPacket(
                        receiveData,
                        receiveData.length
                );

                socket.receive(packet);

                String message = new String(
                        packet.getData(),
                        packet.getOffset(),
                        packet.getLength(),
                        StandardCharsets.UTF_8
                );

                log.debug("Received message: {}", message);
                collector.registerMetric(MetricMessage.from(message));
            }
        } catch (IOException e) {
            log.error("Error while receiving metrics", e);
            throw new UncheckedIOException(e);
        }
    }
}
