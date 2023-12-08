package heig.dai.pw03;

import static heig.dai.pw03.monitor.Monitor.*;

public class MonitorTest {
    public static void main(String[] args) {
        while (true) {
            System.out.println("CPU: " + getCPU() + "%");
            System.out.println("RAM: " + getRAM() + "MB");
            System.out.println("DSK: " + getDSK() + "MB");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
