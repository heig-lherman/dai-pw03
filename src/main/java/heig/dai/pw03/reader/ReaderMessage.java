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

    public ReaderMessage(ReaderRequest request) {
        this(request, null);
    }

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

    @Override
    public String toString() {
        return "%s %s".formatted(
                request.name(),
                hostname != null ? hostname : ""
        );
    }
}
