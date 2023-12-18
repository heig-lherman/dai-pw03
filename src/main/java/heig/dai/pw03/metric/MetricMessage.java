package heig.dai.pw03.metric;

import lombok.Getter;

import java.util.Locale;

/**
 * Represents a message sent by an emitter.
 *
 * @param metric   The metric type
 * @param hostname The hostname of the emitter
 * @param value    The value of the metric
 *
 * @author Loïc Herman
 * @author Massimo Stefani
 */
public record MetricMessage(
        Metric metric,
        String hostname,
        double value
) {

    public static MetricMessage from(String datagram) {
        String[] parts = datagram.split("\\{|\\}");
        String[] values = parts[1].split(", ");
        Metric metric = Metric.valueOf(parts[0].toUpperCase());
        String metricValue = values[0].split("=")[1];
        String host = values[1].split("=")[1];
        return new MetricMessage(metric, host, Double.parseDouble(metricValue));
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "%s{value=%.2f, host=%s}", metric.name().toLowerCase(), value, hostname);
    }
}
