package heig.dai.pw03.monitor;
import heig.dai.pw03.metric.Metric;
import lombok.experimental.UtilityClass;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.software.os.OSFileStore;

/**
 * Utility class that can fetch the metrics from the system
 *
 * @author Loïc Herman
 * @author Massimo Stefani
 */
@UtilityClass
public final class Monitor {

    private static final double BYTES_IN_MB = 1024 * 1024;

    private static final SystemInfo si = new SystemInfo();
    private static final CentralProcessor processor = si.getHardware().getProcessor();
    private static final GlobalMemory memory = si.getHardware().getMemory();

    private static long[] prevTicks = new long[CentralProcessor.TickType.values().length];

    public static double getMetricValue(Metric metric) {
        return switch (metric) {
            case CPU -> getCPU();
            case RAM -> getRAM();
            case DSK -> getDSK();
        };
    }

    /**
     * Get the CPU usage in percentage
     * @return CPU usage in percentage
     */
    private static double getCPU() {
        double usage = processor.getSystemCpuLoadBetweenTicks(prevTicks) * 100;
        prevTicks = processor.getSystemCpuLoadTicks();
        return round(usage);
    }

    /**
     * Get the RAM usage in MB
     * @return RAM usage in MB
     */
    private static double getRAM() {
        double usage = (memory.getTotal() - memory.getAvailable()) / BYTES_IN_MB;
        return round(usage);
    }

    /**
     * Get the DSK usage in MB
     * @return DSK usage in MB
     */
    private static double getDSK() {
        OSFileStore store = si.getOperatingSystem().getFileSystem().getFileStores().get(0);
        double value = (store.getTotalSpace() - store.getUsableSpace()) / BYTES_IN_MB;
        return round(value);
    }

    private static double round(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}
