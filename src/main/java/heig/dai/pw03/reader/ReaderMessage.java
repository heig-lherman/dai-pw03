package heig.dai.pw03.reader;

/**
 * Class to represent a message sent by the reader to the aggregator.
 *
 * @param request   The request type
 * @param arguments The arguments of the request
 *
 * @author Loïc Herman
 * @author Massimo Stefani
 */
public record ReaderMessage(
        ReaderRequest request,
        String hostname
) {

    /**
     * Create a new reader message from a request and an optional hostname.
     * @param request The request
     */
    public ReaderMessage(ReaderRequest request) {
        this(request, null);
    }

    /**
     * Create a new reader message from a datagram. The datagram must be in the format: "request hostname".
     * @param datagram The datagram to parse
     * @return The reader message
     */
    public static ReaderMessage from(String datagram) {
        try {
            String[] parts = datagram.split("\s", 2);

            ReaderRequest request = ReaderRequest.valueOf(parts[0].toUpperCase());
            String hostname = parts.length > 1 ? parts[1] : null;

            return new ReaderMessage(request, hostname);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Get the reader message as a string. The string is in the format: "request hostname".
     * @return The reader message as a string
     */
    @Override
    public String toString() {
        return "%s %s".formatted(
                request.name(),
                hostname != null ? hostname : ""
        );
    }
}
