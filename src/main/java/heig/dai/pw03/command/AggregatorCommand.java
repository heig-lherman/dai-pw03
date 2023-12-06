package heig.dai.pw03.command;

import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

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
//        Socket socket = openSocket(ipAddress, port);
//        log.info("Connected to server, waiting for game to start...");
    }
}
