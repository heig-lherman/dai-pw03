package heig.dai.pw03;

import heig.dai.pw03.metric.Metric;
import heig.dai.pw03.monitor.Monitor;

import static heig.dai.pw03.monitor.Monitor.*;

public class MonitorTest {
    public static void main(String[] args) {
        while (true) {
            System.out.println("CPU: " + Monitor.valueOf(Metric.CPU) + "%");
            System.out.println("RAM: " + Monitor.valueOf(Metric.RAM) + "MB");
            System.out.println("DSK: " + Monitor.valueOf(Metric.DSK) + "MB");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
