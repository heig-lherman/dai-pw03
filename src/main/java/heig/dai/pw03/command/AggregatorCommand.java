package heig.dai.pw03.command;

import heig.dai.pw03.emitters.Emitter;
import heig.dai.pw03.metric.Metric;
import heig.dai.pw03.metric.MetricMessage;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Command to start an aggregator server, which stores monitoring
 * metrics sent by the node units, and listens to read requests
 * from reader clients.
 *
 * @author Loïc Herman
 * @author Massimo Stefani
 */
@Slf4j
@Command(
        name = "aggregator",
        description = "Start an aggregator server to receive and store metrics"
)
public class AggregatorCommand implements Runnable {

    private static final HashMap<String, Emitter> emitters =  new HashMap<>();

    @Option(
            names = {"-p", "--port"},
            description = "server port",
            defaultValue = "6343"
    )
    private int port;

    @Override
    public void run() {
        try (MulticastSocket socket = new MulticastSocket(port)) {
            for (Metric metric : Metric.values()) {
                socket.joinGroup(new InetSocketAddress(metric.getGroupAddress(), port), NetworkInterface.getByName("lo"));
            }
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
                MetricMessage metricMessage = MetricMessage.from(message);
                if (!emitters.containsKey(metricMessage.hostname())) {
                    emitters.put(metricMessage.hostname(), new Emitter(metricMessage.hostname()));
                    log.info("New emitter: " + metricMessage.hostname());
                }
                emitters.get(metricMessage.hostname()).addMetricMessage(metricMessage);
                log.info("Multicast receiver received message: " + message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
