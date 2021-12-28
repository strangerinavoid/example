/**
 * The {@code Type} is a special interface that used to define a data type and a way to convert given objects to a
 * defined data type. Also this interface can be used to specify expected class type in the case when compiler cannot
 * get information about class type from the left side of the expression.
 * <p>
 * A typical usage of the {@code Type} interface is when {@code var} is used on the left side or type casting is
 * needed in a stream processing. A few examples below:
 * <pre>
 * var map =
 *     asType(o, new <u>Type</u>&lt;Map&lt;String, List&lt;Map.Entry&lt;String, Boolean&gt;&gt;&gt;&gt;() {});
 *
 * ...
 *
 * try (Stream&lt;Optional&lt;...&gt;&gt; stream = ...) {
 *     stream.filter(Optional::isPresent)
 *       ...
 *       .map(o -&gt; asType(o, new <u>Type</u>&lt;List&lt;BusinessEntity&gt;&gt;() {}))
 *       ....
 * }</pre>
 *
 * @param   <T> the type of the result object.
 * @author  Andrey OCHIROV
 * @version 1.0
 */
public interface Type<T> extends Function<Object, T> {

    /**
     * Casts a specified object to a type <T> from the left side of the expression.
     * <p>
     * A typical usage when necessary cast a specified object to a type from the left side of the expression, like:
     * <pre>
     * Map&lt;String, Set&lt;Record&gt;&gt; assets = <u>asType</u>(o);
     *
     * ...
     *
     * private static &lt;S, T&gt; Object lookupService(final ServiceFactory&lt;S&gt; loader,
     *     final BiFunction&lt;S, T, Boolean&gt; condition, final Object k) {
     *         for (final S s : loader) {
     *             if (condition.apply(s, <u>asType</u>(k))) {
     *                 return s;
     *             }
     *         }
     *     return null;
     * }</pre>
     *
     * @param   <T> the type of the result object.
     * @param   o an object to cast.
     * @return  a result of type casting.
     * @throws  ClassCastException in case of incorrect type casting.
     */
    @SuppressWarnings("unchecked")
    static <T> T asType(final Object o) {
        return (T)o;
    }

    /**
     * Casts a specified object to a type specified type by {@link Type} instance.
     *
     * @param   <T> the type of the result object.
     * @param   o an object to cast.
     * @param   type a instance of the type definition.
     * @return  a result of type casting.
     * @throws  ClassCastException in case of incorrect type casting.
     */
    static <T> T asType(final Object o, final Type<T> type) {
        return requireNonNull(type, "Cannot cast to a null type").as(o);
    }

    /**
     * Creates an instance of {@link Type} from the given type class.
     *
     * @param   <T> the type of the result object.
     * @param   typeClass a class of the type.
     * @return  an instance of the {@link Type}.
     */
    static <T> Type<T> of(final Class<T> typeClass) {
        return ofType(typeClass);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default T apply(final Object o) {
        return as(o);
    }

    /**
     * Casts a specified object to a predefined type <T>.
     *
     * @param   o an object to cast.
     * @return  a result of type casting.
     * @throws  ClassCastException in case of incorrect type casting.
     */
    default T as(final Object o) {
        return Type.asType(o);
    }

}
