package heig.dai.pw03.aggregator;

import heig.dai.pw03.metric.MetricMessage;

import java.util.LinkedList;

/**
 * A list of metric messages that keeps only the last N messages.
 *
 * @author Loïc Herman
 * @author Massimo Stefani
 */
public final class MetricList extends LinkedList<MetricMessage> {

    private final int maxSize;

    public MetricList() {
        this(10);
    }

    public MetricList(int maxSize) {
        this.maxSize = maxSize;
    }

    /**
     * Add a metric message to the list. If the list is full, the oldest message is removed.
     * @param metricMessage The metric message to add
     * @return true as per Collection.add
     */
    @Override
    public boolean add(MetricMessage metricMessage) {
        if (size() >= maxSize) {
            removeFirst();
        }

        return super.add(metricMessage);
    }
}
