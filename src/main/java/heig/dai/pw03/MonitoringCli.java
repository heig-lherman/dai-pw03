package heig.dai.pw03;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import heig.dai.pw03.command.AggregatorCommand;
import heig.dai.pw03.command.NodeCommand;
import heig.dai.pw03.command.ReaderCommand;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.HelpCommand;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParseResult;
import picocli.CommandLine.ScopeType;

/**
 * Main CLI for the Chess client and server.
 *
 * @author Loïc Herman
 * @author Massimo Stefani
 */
@Command(
        name = "DistributedMonitoring",
        description = "Aggregate monitoring data from multiple sources.",
        version = "DistributedMonitoring 1.0.0",
        mixinStandardHelpOptions = true,
        subcommands = {
                HelpCommand.class,
                AggregatorCommand.class,
                NodeCommand.class,
                ReaderCommand.class
        }
)
public class MonitoringCli implements Runnable {

    boolean[] verbosity = new boolean[0];

    @Option(
            names = "-v",
            description = "Change log verbosity. Use -vvv for maximum verbosity.",
            scope = ScopeType.INHERIT
    )
    public void setVerbosity(boolean[] verbosity) {
        this.verbosity = verbosity;
    }

    /**
     * Print basic usage information when the user doesn't use
     * one of the built-in subcommands.
     */
    @Override
    public void run() {
        CommandLine.usage(this, System.out);
    }

    /**
     * Proxy the default execution strategy, after setting the logger level correctly.
     */
    private int executionStrategy(ParseResult parseResult) {
        Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(
                switch (verbosity.length) {
                    case 0 -> Level.WARN;
                    case 1 -> Level.INFO;
                    case 2 -> Level.DEBUG;
                    default -> Level.TRACE;
                }
        );
        return new CommandLine.RunLast().execute(parseResult); // default execution strategy
    }

    /**
     * Main entry point for the CLI.
     *
     * @param args arguments that will be parsed by picocli
     */
    public static void main(String[] args) {
        MonitoringCli cli = new MonitoringCli();
        new CommandLine(cli)
                .setExecutionStrategy(cli::executionStrategy)
                .execute(args);
    }
}
