package heig.dai.pw03.monitor;
import heig.dai.pw03.metric.Metric;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.software.os.OSFileStore;

public class Monitor {
    static long[] prevTicks = new long[CentralProcessor.TickType.values().length];
    static SystemInfo si = new SystemInfo();
    static CentralProcessor processor = si.getHardware().getProcessor();
    static GlobalMemory memory = si.getHardware().getMemory();
    static double BYTES_IN_MB = 1024 * 1024;

    public static double valueOf(Metric metric) {
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
        double usage = (double) ((memory.getTotal() - memory.getAvailable()) / BYTES_IN_MB);
        return round(usage);
    }

    /**
     * Get the DSK usage in MB
     * @return DSK usage in MB
     */
    private static double getDSK() {
        OSFileStore store = si.getOperatingSystem().getFileSystem().getFileStores().get(0);
        double value = (double) ((store.getTotalSpace() - store.getUsableSpace()) / BYTES_IN_MB);
        return round(value);
    }

    private static double round(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}
