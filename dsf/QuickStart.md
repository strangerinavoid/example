=== BCF Quick Start Guides ===

[[framework/bcf/QuickStart/Modelling_BCF_Entities|Part I: Modelling BCF Entities In X Steps]]
\\[[framework/bcf/QuickStart/Modelling_BCF_Entity_Sessions|Part II: Modelling BCF Entity Sessions In X Steps]]
\\[[framework/bcf/QuickStart/BCF_Within_EJB_3_0_Environment|Part III: BCF Within EJB 3.0 Environment In X Steps]]
\\[[framework/bcf/QuickStart/JPA_With_BCF_Entities|Part IV: JPA With BCF Entities  In X Steps]]

Have fun and nice drive!


```java

public interface Channel {

    String channel();

    default boolean offer(final Object payload) {
        return offer(payload, Map.of());
    }

    default boolean offer(final Object payload, final Map<String, Object> headers) {

        final var message = Message.newBuilder()
            .withHeaders(headers)
            .withHeader("x-channel-a", channel())
            .withPayload(payload)
            .build();

        // offer to publisher

        return false;
    }

}

public interface Gateway {

    boolean broadcast(Message message);

}

public interface Message {

    interface Builder extends LogAware {

        Message build();

        Builder withHeader(final String header, final Object value);

        default Builder withHeaders(final Map<String, Object> headers) {
            requireNonNull(headers).forEach(this::withHeader);
            return this;
        }

        Builder withPayload(final Object payload);

    }

    static Builder newBuilder() {
        return new DefaultMessageBuilder();
    }

    default String channelA() {
        return header("x-channel-a");
    }

    default String channelB() {
        return header("x-channel-b", this::channelA);
    }

    default String contentType() {
        return header("x-content-type", "application/object");
    }

    <T> T header(String header);

    default <T> T header(final String header, final Supplier<T> defaultValueSupplier) {
        return requireNonNullElseGet(header(header), defaultValueSupplier);
    }
    default <T> T header(final String header, final T defaultValue) {
        return requireNonNullElse(header(header), defaultValue);
    }

    default long id() {
        return header("x-id", 0L);
    }

    <T> T payload();

    default Duration timeToLive() {
        return header("x-ttl", Duration.ZERO);
    }

    default UUID uuid() {
        return header("x-uuid");
    }

}

final class DefaultMessage implements Message {

    private final Map<String, Object> headers;
    private final Object payload;

    DefaultMessage(final Map<String, Object> headers, final Object payload) {
        this.headers = headers;
        this.payload = payload;
    }

    @Override
    public <T> T header(final String header) {
        return asType(this.headers.get(header));
    }

    @Override
    public <T> T payload() {
        return asType(this.payload);
    }

}


public class DefaultMessageBuilder implements Message.Builder {

    private static final AtomicLong COUNTER = new AtomicLong();

    private final Map<String, Object> headers;
    private Object payload;

    public DefaultMessageBuilder() {
        this.headers = new HashMap<>();
    }

    @Override
    public Message build() {
        this.headers.put("x-id", COUNTER.incrementAndGet());

        return new DefaultMessage(copyOf(this.headers),
            requireNonNull(this.payload, "Unable to create message with null payload"));
    }

    @Override
    public Builder withHeader(final String header, final Object value) {
        final var h = requireNonNull(header).trim().toLowerCase();

        switch (h) {
            case "x-id":
            case "x-uuid":
                break;
            case "x-channel-a":
                log().warning("MESSAGE: Illegal operation: Trying to overwrite `%s` header: [`%s` -> `%s`]. "
                    + "Illegal operation ignored.", h, "old", "new");
                break;
            default:
                final var v = null == value ? this.headers.remove(header) : this.headers.put(header, value);

                if (nonNull(v)) {
                    log().warning("MESSAGE: A previously assigned header `%s` value `%s` has been replaced with `%s`.",
                        h, v, value);
                }
        }
        return this;
    }

    @Override
    public Builder withPayload(final Object payload) {
        this.payload = requireNonNull(payload);
        return this;
    }

}
```
