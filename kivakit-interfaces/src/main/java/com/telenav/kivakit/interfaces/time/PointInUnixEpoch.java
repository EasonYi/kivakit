package com.telenav.kivakit.interfaces.time;

import com.telenav.lexakai.annotations.UmlMethodGroup;
import org.jetbrains.annotations.NotNull;

import java.time.Clock;

/**
 * A point in time within the UNIX epoch.
 *
 * <p>
 * This interface extends {@link PointInTime} to add methods related to real time in the Unix epoch (which started on
 * January 1, 1970 at midnight.
 * </p>
 *
 * <p><b>Real Time in the Unix Epoch</b></p>
 *
 * <p>
 * This point in the Unix epoch is uniquely identified by {@link #epochMilliseconds()}, which is the number of
 * milliseconds since January 1, 1970 at midnight. The current time can be retrieved with {@link #currentTime()}. When
 * compared against the current time, a time will returns true for one of: {@link #isPast()}, {@link #isNow()}, or
 * {@link #isFuture()}. The method #until
 * </p>
 *
 * <ul>
 *     <li>{@link #epochMilliseconds()} - Milliseconds since start of Unix epoch</li>
 *     <li>{@link #asInstant()} - Java time object for this point in time</li>
 *     <li>{@link #clock()} - Java {@link Clock} object for this point in time</li>
 *     <li>{@link #currentTime()} - The current time </li>
 *     <li>{@link #isPast()} - True if this time is in the past</li>
 *     <li>{@link #isNow()} - True if this time is the same as {@link #currentTime()}</li>
 *     <li>{@link #isFuture()} - True if this time is in the future</li>
 * </ul>
 *
 * <p><b>Age</b></p>
 *
 * <p>
 * The method {@link #age()} has two synonyms to make code easier to read: {@link #elapsedSince()}, and {@link #untilNow()}.
 *
 * </p>
 *
 * <ul>
 *     <li>{@link #age()} - The amount of time that has elapsed since this point in time</li>
 *     <li>{@link #elapsedSince()} - The amount of time that has elapsed since this point in time</li>
 *     <li>{@link #untilNow()}The amount of time that has elapsed since this point in time</li>
 *     <li>{@link #until()} - The amount of time before this point in time in the future, or zero if the time is in the past</li>
 *     <li>{@link #until(LengthOfTime)} - Returns the amount of time left before the given amount of time has elapsed since this point in time</li>
 *     <li>{@link #compareTo(PointInUnixEpoch)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
public interface PointInUnixEpoch<TimeSubclass extends PointInUnixEpoch<TimeSubclass, DurationSubclass>, DurationSubclass extends LengthOfTime<DurationSubclass>> extends
        PointInTime<TimeSubclass, DurationSubclass>,
        Comparable<PointInUnixEpoch<?, ?>>,
        Aged<DurationSubclass>,
        UnixEpochal,
        TimeZoned<TimeSubclass>,
        Clocked
{

    /**
     * Returns the length of time that has elapsed since this point in time. Same as {@link #elapsedSince()}.
     */
    @Override
    @UmlMethodGroup("temporal")
    default DurationSubclass age()
    {
        return elapsedSince();
    }

    @Override
    default int compareTo(@NotNull PointInUnixEpoch<?, ?> that)
    {
        return Long.compare(milliseconds(), that.milliseconds());
    }

    /**
     * The current time
     */
    default TimeSubclass currentTime()
    {
        return newTimeSubclass(System.currentTimeMillis());
    }

    /**
     * Returns the length of time that has elapsed since this point in time. If this point in time is in the future,
     * returns a zero length of time.
     */
    @UmlMethodGroup("temporal")
    default DurationSubclass elapsedSince()
    {
        return untilNow();
    }

    /**
     * Returns true if this point in time is in the future
     */
    default boolean isFuture()
    {
        return isAfter(currentTime());
    }

    /**
     * True if this point in time is the current time
     */
    default boolean isNow()
    {
        return equals(currentTime());
    }

    /**
     * Returns true if this point in time is in the past
     */
    default boolean isPast()
    {
        return isBefore(currentTime());
    }

    /**
     * @return The amount of time left until the given amount of time has elapsed
     */
    @UmlMethodGroup("temporal")
    default LengthOfTime<?> until(LengthOfTime<?> elapsed)
    {
        return elapsed.minus(elapsedSince());
    }

    /**
     * Returns the amount of time between this point in time in the past and now. If this point in time is in the
     * future, returns a zero length of time.
     */
    @UmlMethodGroup("temporal")
    default DurationSubclass until()
    {
        var now = currentTime();
        if (isGreaterThan(now))
        {
            return newDurationSubclass(milliseconds() - now.asMilliseconds());
        }
        return newDurationSubclass(0);
    }

    /**
     * Returns the amount of time between the given point of time in the past and now. If the given point in time is in
     * the future, returns a zero length of time.
     */
    @UmlMethodGroup("temporal")
    default DurationSubclass untilNow()
    {
        return until(currentTime());
    }
}
