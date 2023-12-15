package heig.dai.pw03.emitters;

import heig.dai.pw03.metric.MetricMessage;
import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Emitter {
    private final String hostname;
    private final List<MetricMessage> messages = new ArrayList<>();

    public Emitter(String hostname) {
        this.hostname = hostname;
    }

    public void addMetricMessage(MetricMessage message) {
        messages.add(message);
    }

    public boolean equals(String obj) {
        if (obj != null) {
            return hostname.equals(obj);
        }
        return false;
    }
}
