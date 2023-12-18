package heig.dai.pw03.aggregator;

import heig.dai.pw03.metric.MetricMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class to collect metrics from the nodes, and store them in a map in a way that handles thread safety.
 *
 * @author Loïc Herman
 * @author Massimo Stefani
 */
@Slf4j
@RequiredArgsConstructor
public final class MetricCollector {

    private final Map<String, MetricList> emitters = new ConcurrentHashMap<>();

    /**
     * Register a metric message in the map.
     *
     * @param message The metric message to register
     */
    public void registerMetric(MetricMessage message) {
        emitters.computeIfAbsent(
                message.hostname(),
                k -> new MetricList()
        ).add(message);
    }

    /**
     * Returns a copy of the list of the last known metrics for a given hostname.
     * If the hostname is not known, an empty list is returned.
     *
     * @param hostname The hostname to get the metrics for
     * @return A copy of the list of the last known metrics for a given hostname
     */
    public List<MetricMessage> getHostnameMetrics(String hostname) {
        if (!emitters.containsKey(hostname)) {
            return Collections.emptyList();
        }

        return List.copyOf(emitters.get(hostname));
    }

    /**
     * Returns a copy of the list of the last known metrics for all known hostnames.
     * @return A copy of the list of the last known metrics for all known hostnames
     */
    public List<String> getKnownHostnames() {
        return List.copyOf(emitters.keySet());
    }
}
