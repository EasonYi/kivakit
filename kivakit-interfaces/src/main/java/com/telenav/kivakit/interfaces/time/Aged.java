package com.telenav.kivakit.interfaces.time;

import com.telenav.lexakai.annotations.UmlMethodGroup;

/**
 * Computes and compares age.
 *
 * <ul>
 *     <li>{@link #age()}</li>
 *     <li>{@link #isOlderThan(Duration)}</li>
 *     <li>{@link #isOlderThanOrEqualTo(Duration)}</li>
 *     <li>{@link #isYoungerThan(Duration)}</li>
 *     <li>{@link #isYoungerThanOrEqualTo(Duration)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
public interface Aged<Duration extends LengthOfTime<Duration>>
{
    /**
     * Returns the length of time that has elapsed since this point in time.
     */
    @UmlMethodGroup("age")
    Duration age();

    /**
     * @return True if this point in time is older than the given age
     */
    @UmlMethodGroup("age")
    default boolean isOlderThan(Duration age)
    {
        return age().isGreaterThan(age);
    }

    /**
     * @return True if this point in time is older or equal to than the given age
     */
    @UmlMethodGroup("age")
    default boolean isOlderThanOrEqualTo(Duration age)
    {
        return age().isGreaterThanOrEqualTo(age);
    }

    /**
     * @return True if this point in time is younger than the given age
     */
    @UmlMethodGroup("age")
    default boolean isYoungerThan(Duration age)
    {
        return age().isLessThan(age);
    }

    /**
     * @return True if this point in time is younger than or equal to the given age
     */
    @UmlMethodGroup("age")
    default boolean isYoungerThanOrEqualTo(Duration age)
    {
        return age().isLessThanOrEqualTo(age);
    }
}
