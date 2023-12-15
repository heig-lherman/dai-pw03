package heig.dai.pw03.command;

import heig.dai.pw03.metric.Metric;
import heig.dai.pw03.metric.MetricSender;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * Command to start a node unit, which is the unit
 * that will send monitoring data to the server.
 *
 * @author Loïc Herman
 * @author Massimo Stefani
 */
@Slf4j
@Command(
        name = "node",
        description = "Start a node unit to send metrics"
)
public class NodeCommand implements Runnable {

    @Option(
            names = {"-p", "--port"},
            description = "server port",
            defaultValue = "9378"
    )
    private int port;

    @Option(
            names = {"-i", "--iface", "--interface"},
            description = "interface to use",
            defaultValue = "lo"
    )
    private NetworkInterface iface;

    @Option(
            names = {"--delay"},
            description = "delay before sending metrics in seconds",
            defaultValue = "0"
    )
    private int delay;

    @Option(
            names = {"--frequency"},
            description = "frequency of metrics sending in seconds",
            defaultValue = "5"
    )
    private int frequency;

    @Option(
            names = {"-m", "--metric"},
            description = "metric to send",
            required = true
    )
    private Metric metric;

    @Override
    public void run() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        try (var socket = new MulticastSocket(port)) {
            log.info("Emitter started on port {}...", port);

            InetSocketAddress group = new InetSocketAddress(metric.getGroupAddress(), 6343);
            socket.joinGroup(group, iface);

            log.info("Scheduling sending {} metrics to {} on {} every {}s", metric, group, iface, frequency);
            executor.scheduleAtFixedRate(
                    new MetricSender(socket, group, metric),
                    delay,
                    frequency,
                    java.util.concurrent.TimeUnit.SECONDS
            );
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
            socket.leaveGroup(group, iface);
        } catch (Exception e) {
            log.error("Error while sending metrics", e);
        }
    }
}
