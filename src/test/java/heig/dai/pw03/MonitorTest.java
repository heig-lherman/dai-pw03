package heig.dai.pw03;

import heig.dai.pw03.metric.Metric;
import heig.dai.pw03.monitor.Monitor;

import static heig.dai.pw03.monitor.Monitor.*;

public class MonitorTest {
    public static void main(String[] args) {
        while (true) {
            System.out.println("CPU: " + Monitor.getMetricValue(Metric.CPU) + "%");
            System.out.println("RAM: " + Monitor.getMetricValue(Metric.RAM) + "MB");
            System.out.println("DSK: " + Monitor.getMetricValue(Metric.DSK) + "MB");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
