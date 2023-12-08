package heig.dai.pw03.monitor;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.software.os.OSFileStore;

public class Monitor {
    static long[] prevTicks = new long[CentralProcessor.TickType.values().length];
    static SystemInfo si = new SystemInfo();
    static CentralProcessor processor = si.getHardware().getProcessor();
    static GlobalMemory memory = si.getHardware().getMemory();

    static int BYTES_IN_MB = 1024 * 1024;

    /**
     * Get the CPU usage in percentage
     * @return CPU usage in percentage
     */
    public static double getCPU() {
        double usage = processor.getSystemCpuLoadBetweenTicks(prevTicks) * 100;
        prevTicks = processor.getSystemCpuLoadTicks();
        return Math.round(usage * 10.0) / 10.0;
    }

    /**
     * Get the RAM usage in MB
     * @return RAM usage in MB
     */
    public static int getRAM() {
        return (int) ((memory.getTotal() - memory.getAvailable()) / BYTES_IN_MB);
    }

    /**
     * Get the DSK usage in MB
     * @return DSK usage in MB
     */
    public static int getDSK() {
        OSFileStore store = si.getOperatingSystem().getFileSystem().getFileStores().get(0);
        return (int) ((store.getTotalSpace() - store.getUsableSpace()) / BYTES_IN_MB);
    }
}
