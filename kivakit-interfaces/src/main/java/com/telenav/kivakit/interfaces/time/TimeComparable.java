package com.telenav.kivakit.interfaces.time;

import com.telenav.lexakai.annotations.UmlMethodGroup;

/**
 * Compares two objects that implement {@link Milliseconds}.
 * <p>
 * T
 * <li>{@link #isAfter(Time)}</li>
 * <li>{@link #isAtOrAfter(Time)}</li>
 * <li>{@link #isBefore(Time)}</li>
 * <li>{@link #isAtOrBefore(Time)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
public interface TimeComparable<Time extends Milliseconds> extends Milliseconds
{
    /**
     * @return True if this point in time is after the given point in time
     */
    @UmlMethodGroup("temporal")
    default boolean isAfter(Time that)
    {
        return milliseconds() > that.milliseconds();
    }

    /**
     * @return True if this point in time is at or after the given point in time
     */
    @UmlMethodGroup("temporal")
    default boolean isAtOrAfter(Time that)
    {
        return milliseconds() >= that.milliseconds();
    }

    /**
     * @return True if this point in time is at or before the given point in time
     */
    @UmlMethodGroup("temporal")
    default boolean isAtOrBefore(Time that)
    {
        return milliseconds() <= that.milliseconds();
    }

    /**
     * @return True if this point in time is before the given point in time
     */
    @UmlMethodGroup("temporal")
    default boolean isBefore(Time that)
    {
        return milliseconds() < that.milliseconds();
    }
}
