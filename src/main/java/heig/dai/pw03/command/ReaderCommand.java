package heig.dai.pw03.command;

import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * Command to start a reader client, which allows to request
 * monitoring metrics from the aggregator server.
 *
 * @author Loïc Herman
 * @author Massimo Stefani
 */
@Slf4j
@Command(
        name = "reader",
        description = "Start a reader client to fetch metrics"
)
public class ReaderCommand implements Runnable {

    @Option(
            names = {"-H", "--host"},
            description = "server host IP address",
            defaultValue = "127.0.0.1"
    )
    private String ipAddress;

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
