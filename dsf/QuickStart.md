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

        default Builder withHeader(final String header, final Object value) {
            final var h = requireNonNull(header).trim().toLowerCase();

            switch (h) {
                case "x-channel-a": // "x-id", "x-uuid",
                    log().warning("MESSAGE: Illegal operation: Trying to overwrite `%s` header: [`%s` -> `%s`]. "
                        + "Illegal operation ignored.", h, "old", "new");
                    break;
                default:
                    // do default assignment
                    ;
            }
            return this;
        }

        default Builder withHeaders(final Map<String, Object> headers) {
            requireNonNull(headers).forEach(this::withHeader);
            return this;
        }

        default Builder withPayload(final Object payload) {
            requireNonNull(payload);
            return this;
        }

    }

    AtomicLong COUNTER = new AtomicLong();

    static Builder newBuilder() {
        return () -> new Message() {

            private final long id = COUNTER.incrementAndGet();
            private final UUID uuid = UUID.randomUUID();

            @Override
            public <T> T header(final String header) {
                return null;
            }

            @Override
            public long id() {
                return this.id;
            }

            @Override
            public UUID uuid() {
                return this.uuid;
            }};
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

    default Duration timeToLive() {
        return header("x-ttl", Duration.ZERO);
    }

    default UUID uuid() {
        return header("x-uuid");
    }

}

```
