package heig.dai.pw03.reader;

import heig.dai.pw03.aggregator.MetricCollector;
import heig.dai.pw03.metric.MetricMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Thread runnable that handles receiving metrics request on a unicast socket.
 *
 * @author Loïc Herman
 * @author Massimo Stefani
 */
@Slf4j
@RequiredArgsConstructor
public final class ReaderHandler implements Runnable {

    private final DatagramSocket socket;
    private final MetricCollector metricCollector;

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
                        packet.getLength()
                );

                log.debug("Received message: {}", message);
                ReaderMessage readerMessage = ReaderMessage.from(message);

                if (readerMessage == null) {
                    log.error("Invalid message received: {}", message);
                    byte[] data = "ERROR 1".getBytes(StandardCharsets.UTF_8);
                    socket.send(new DatagramPacket(data, data.length, packet.getAddress(), packet.getPort()));
                    continue;
                }

                String response = switch (readerMessage.request()) {
                    case GET_EMITTERS -> metricCollector.getKnownHostnames().toString();
                    case GET_EMITTER -> mapMetrics(metricCollector.getHostnameMetrics(readerMessage.hostname()));
                };

                byte[] data = response.getBytes(StandardCharsets.UTF_8);
                socket.send(new DatagramPacket(data, data.length, packet.getAddress(), packet.getPort()));
            }
        } catch (IOException e) {
            log.error("Error while handling client", e);
            throw new UncheckedIOException(e);
        }
    }

    private String mapMetrics(List<MetricMessage> messages) {
        return "[%s]".formatted(
                messages.stream()
                        .map(message -> "%s: %s".formatted(message.metric(), message.value()))
                        .collect(Collectors.joining(", "))
        );
    }
}
