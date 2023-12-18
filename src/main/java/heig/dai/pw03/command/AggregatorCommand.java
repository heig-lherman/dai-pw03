package heig.dai.pw03.command;

import heig.dai.pw03.metric.Metric;
import heig.dai.pw03.aggregator.MetricCollector;
import heig.dai.pw03.aggregator.MetricReceiver;
import heig.dai.pw03.reader.ReaderHandler;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.net.*;
import java.util.concurrent.*;

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
        description = "Start an aggregator server to receive and store metrics",
        mixinStandardHelpOptions = true
)
public class AggregatorCommand implements Runnable {

    @Option(
            names = {"-P", "--metrics-port"},
            description = "server port for metrics",
            defaultValue = "9378"
    )
    private int metricsPort;

    @Option(
            names = {"-p", "--server-port"},
            description = "server port for request",
            defaultValue = "6343"
    )
    private int serverPort;

    @Option(
            names = {"-i", "--iface", "--interface"},
            description = "interface to use for metrics collection",
            defaultValue = "eth0"
    )
    private NetworkInterface iface;

    @Override
    public void run() {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        try (
                MulticastSocket metricsSocket = new MulticastSocket(metricsPort);
                DatagramSocket serverSocket = new DatagramSocket(serverPort);
        ) {
            String myself = InetAddress.getLocalHost().getHostAddress();
            log.info("Reader server started ({}:{})", myself, serverPort);

            // join each metric multicast group
            for (Metric metric : Metric.values()) {
                log.info(
                        "Joining multicast group for metric {} on {}:{}",
                        metric,
                        metric.getGroupAddress(),
                        metricsPort
                );

                metricsSocket.joinGroup(
                        new InetSocketAddress(metric.getGroupAddress(), metricsPort),
                        iface
                );
            }

            // init metric collector
            MetricCollector metricCollector = new MetricCollector();

            // start collection threads
            executor.submit(new MetricReceiver(metricsSocket, metricCollector));
            executor.submit(new ReaderHandler(serverSocket, metricCollector));

            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        } catch (Exception e) {
            log.error("Error while receiving metrics", e);
        }
    }
}
