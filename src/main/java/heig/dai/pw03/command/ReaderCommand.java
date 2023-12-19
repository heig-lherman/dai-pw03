package heig.dai.pw03.command;

import heig.dai.pw03.reader.ReaderMessage;
import heig.dai.pw03.reader.ReaderRequest;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
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
        description = "Start a reader client to fetch metrics",
        mixinStandardHelpOptions = true
)
public class ReaderCommand implements Runnable {

    @Option(
            names = {"-H", "--host"},
            description = "server host IP address",
            defaultValue = "127.0.0.1"
    )
    private InetAddress ipAddress;

    @Option(
            names = {"-p", "--port"},
            description = "server port",
            defaultValue = "6343"
    )
    private int port;

    @Override
    public void run() {
        try (
                DatagramSocket socket = new DatagramSocket();
                Scanner scanner = new Scanner(System.in);
        ) {
            System.out.println("Welcome to the reader client");

            while (true) {
                ReaderMessage message = pollMessage(scanner);

                byte[] payload = message.toString().getBytes(StandardCharsets.UTF_8);
                DatagramPacket datagram = new DatagramPacket(payload, payload.length, ipAddress, port);

                socket.send(datagram);

                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);

                String response = new String(
                        receivePacket.getData(),
                        receivePacket.getOffset(),
                        receivePacket.getLength(),
                        StandardCharsets.UTF_8
                );

                System.out.println("-".repeat(50));
                System.out.printf("Response: %s%n", response);
                System.out.println("-".repeat(50));
            }
        } catch (Exception e) {
            log.error("Error while running reader", e);
        }
    }

    /**
     * Polls the user for a message to send to the server.
     * @param scanner the scanner to use
     * @return the message to send
     */
    public ReaderMessage pollMessage(Scanner scanner) {
        while (true) {
            System.out.println("Please choose an option:\n1. Get known emitters\n2. Get metrics from emitter\n3. Exit");
            System.out.print("> ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    return new ReaderMessage(ReaderRequest.GET_EMITTERS);
                case 2:
                    System.out.println("Enter the hostname of the emitter:");
                    System.out.print("> ");
                    String emitter = scanner.next();

                    return new ReaderMessage(ReaderRequest.GET_EMITTER, emitter);
                case 3:
                    System.exit(0);
                default:
                    log.error("Invalid choice");
            }
        }
    }
}
