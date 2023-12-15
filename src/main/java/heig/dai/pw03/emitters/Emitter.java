package heig.dai.pw03.emitters;

import heig.dai.pw03.metric.MetricMessage;

import java.util.ArrayList;
import java.util.List;

public class Emitter {
    private final String hostname;
    private final List<MetricMessage> messages = new ArrayList<>();

    public Emitter(String hostname) {
        this.hostname = hostname;
    }

    public void addMetricMessage(MetricMessage message) {
        messages.add(message);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof String) {
            return hostname.equals(obj);
        }
        return super.equals(obj);
    }
}
