package heig.dai.pw03.command;

import heig.dai.pw03.metric.Metric;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.net.*;
import java.nio.charset.StandardCharsets;

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

                System.out.println("Multicast receiver received message: " + message);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
