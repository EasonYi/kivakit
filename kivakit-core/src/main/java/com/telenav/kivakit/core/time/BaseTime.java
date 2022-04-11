package com.telenav.kivakit.core.time;

import com.telenav.kivakit.interfaces.lexakai.DiagramTimePoint;
import com.telenav.kivakit.interfaces.numeric.Maximizable;
import com.telenav.kivakit.interfaces.numeric.Minimizable;
import com.telenav.kivakit.interfaces.numeric.Quantizable;
import com.telenav.kivakit.interfaces.numeric.QuantumComparable;
import com.telenav.kivakit.interfaces.string.Stringable;
import com.telenav.kivakit.interfaces.time.Clocked;
import com.telenav.kivakit.interfaces.time.Epochal;
import com.telenav.kivakit.interfaces.time.Milliseconds;
import com.telenav.kivakit.interfaces.time.PointInTime;
import com.telenav.kivakit.interfaces.time.TimeUnits;
import com.telenav.kivakit.interfaces.time.TimeZoned;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Objects;

import static com.telenav.kivakit.core.time.Duration.days;

/**
 * Base class for implementations of {@link PointInTime}.
 *
 * <p>
 * This base class inherits many methods from {@link PointInTime} and its interfaces:
 * </p>
 *
 * <ul>
 *     <li>{@link Milliseconds} - get milliseconds</li>
 *     <li>{@link Epochal} - get milliseconds since the start of the UNIX epoch</li>
 *     <li>{@link TimeZoned} - a time object with a time zone</li>
 *     <li>{@link TimeUnits} - methods for working with units</li>
 *     <li>{@link Clocked} - the clock to use, enabling testing</li>
 *     <li>{@link Minimizable} - time minimum</li>
 *     <li>{@link Maximizable} - time maximum</li>
 *     <li>{@link Stringable} - convert to string representations</li>
 *     <li>{@link Comparable} - compare to another object</li>
 *     <li>{@link QuantumComparable} - compare based on quanta</li>
 *     <li>{@link Quantizable} - a general purpose discrete value</li>
 * </ul>
 *
 * <p><b>The System Clock</b></p>
 *
 * <p>
 * To enable testing, the system clock used by KivaKit can be set, including to a fixed value.
 * </p>
 *
 * <ul>
 *     <li>{@link #systemClock(Clock)} - sets the system clock to use</li>
 *     <li>{@link #systemClock()} - the system clock being used</li>
 *     <li>{@link #systemClock(PointInTime)} - sets the system clock to the given value</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see PointInTime
 * @see Epochal
 * @see TimeZoned
 * @see TimeUnits
 * @see Clocked
 * @see Minimizable
 * @see Maximizable
 * @see Stringable
 * @see Comparable
 * @see QuantumComparable
 * @see Quantizable
 * @see Clock
 */
@UmlClassDiagram(diagram = DiagramTimePoint.class)
public abstract class BaseTime<SubClass extends PointInTime<SubClass, Duration>> implements PointInTime<SubClass, Duration>
{
    /**
     * The system clock consulted for system measurements, like the current epoch time in milliseconds or system time
     * zone. By default the actual system clock is used, but calling {@link #systemClock(Clock)} or {@link
     * #systemClock(PointInTime)} substitutes a different clock. {@link #systemClock(PointInTime)} is useful in testing,
     * as it allows the system clock to be set to any desired time.
     */
    private static Clock systemClock = Clock.systemUTC();

    /**
     * Sets the system clock to the given point in time, including time zone. This method is useful in testing because
     * it allows the system clock to be set to a fixed time.
     */
    public static void systemClock(PointInTime<?, ?> time)
    {
        systemClock = Clock.fixed(time.asInstant(), time.timeZone());
    }

    public static Clock systemClock()
    {
        return systemClock;
    }

    /**
     * Sets the system clock to use when getting system milliseconds, time zone, etc. This allows a test clock
     * implementation to be used, such as the one created by {@link Clock#fixed(Instant, ZoneId)}.
     */
    public static void systemClock(Clock systemClock)
    {
        BaseTime.systemClock = systemClock;
    }

    /** Clock for testing */
    private transient Clock clock = systemClock;

    /** This point in time in milliseconds since the start of the UNIX epoch */
    private long epochMilliseconds;

    @UmlExcludeMember
    protected BaseTime()
    {
    }

    @UmlExcludeMember
    protected BaseTime(long epochMilliseconds)
    {
        this.epochMilliseconds = epochMilliseconds;
    }

    @Override
    public Clock clock()
    {
        return clock;
    }

    public void clock(Clock clock)
    {
        this.clock = clock;
    }

    public SubClass endOfDay()
    {
        return roundUp(days(1));
    }

    @Override
    public long epochMilliseconds()
    {
        return epochMilliseconds;
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof PointInTime<?, ?>)
        {
            var that = (PointInTime<?, ?>) object;
            return this.epochMilliseconds() == that.epochMilliseconds();
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(epochMilliseconds());
    }

    @Override
    public long milliseconds()
    {
        return epochMilliseconds();
    }

    @Override
    public Duration newDuration(long milliseconds)
    {
        return Duration.milliseconds(milliseconds);
    }

    @Override
    public SubClass newTimeOrDuration(long milliseconds)
    {
        return newTime(milliseconds);
    }

    public SubClass startOfDay()
    {
        return roundDown(days(1));
    }
}
