package com.telenav.kivakit.interfaces.time;

import com.telenav.kivakit.interfaces.language.Subclassed;
import com.telenav.kivakit.interfaces.lexakai.DiagramTimePoint;
import com.telenav.kivakit.interfaces.numeric.Maximizable;
import com.telenav.kivakit.interfaces.numeric.Minimizable;
import com.telenav.kivakit.interfaces.numeric.Quantizable;
import com.telenav.kivakit.interfaces.numeric.QuantumComparable;
import com.telenav.kivakit.interfaces.string.Stringable;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.UmlMethodGroup;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

/**
 * Interface to an object representing a point in time, measured in milliseconds.
 *
 * <p><b>Creation</b></p>
 *
 * <p>
 * An object that implements this interface must provide implementations of:
 * </p>
 *
 * <ul>
 *     <li>{@link #epochMilliseconds()} - The number of milliseconds since the start of the UNIX epoch for this point in time</li>
 *     <li>{@link #newTimeSubclass(long)} - Creates instances of the implementing class</li>
 *     <li>{@link #newDurationSubclass(long)} - Creates instances of a related {@link LengthOfTime} for use by methods such as {@link #minus(PointInTime)}</li>
 *     <li>{@link #newTimeUnitsSubclass(long)}</li>
 *     <li>{@link #subclass()}</li>
 * </ul>
 *
 * <p><b>Arithmetic</b></p>
 *
 * <ul>
 *     <li>{@link #incremented()} - This point in time plus one unit</li>
 *     <li>{@link #decremented()} - This point in time minus one unit</li>
 *     <li>{@link #minus(PointInTime)} - Returns this point in time minus the given point in time as a {@link LengthOfTime}</li>
 *     <li>{@link #plus(LengthOfTime)}  - Returns this point in time plus the given {@link LengthOfTime}</li>
 * </ul>
 *
 * <p><b>Math</b></p>
 *
 * <li>
 *     <li>{@link #durationBefore(PointInTime)}</li>
 *     <li>{@link #difference(PointInTime)} - Returns the {@link LengthOfTime} between this point in time and the given point in time</li>
 *     <li>{@link #until(LengthOfTime)} - Returns the amount of time left before the given amount of time has elapsed since this point in time</li>
 *     <li>{@link #until(PointInTime)}</li>
 *     <li>{@link #untilNow()}</li>
 *     <li>{@link #roundUp(LengthOfTime)}</li>
 *     <li>{@link #roundDown(LengthOfTime)}</li>
 * </li>
 *
 * <p><b>Conversion</b></p>
 *
 * <ul>
 *     <li>{@link #asMilliseconds()} - The number of milliseconds since the start of the UNIX epoch for this point in time</li>
 *     <li>{@link #asInstant()}</li>
 * </ul>
 *
 * <p><b>Age</b></p>
 *
 * <ul>
 *     <li>{@link #age()}</li>
 *     <li>{@link #elapsedSince()}</li>
 *     <li>{@link #durationBefore(PointInTime)}</li>
 *     <li>{@link #isAfter(PointInTime)}</li>
 *     <li>{@link #isAtOrAfter(PointInTime)}</li>
 *     <li>{@link #isBefore(PointInTime)}</li>
 *     <li>{@link #isAtOrBefore(PointInTime)}</li>
 *     <li>{@link #isOlderThan(LengthOfTime)}</li>
 *     <li>{@link #isOlderThanOrEqualTo(LengthOfTime)}</li>
 *     <li>{@link #isYoungerThan(LengthOfTime)}</li>
 *     <li>{@link #isYoungerThanOrEqualTo(LengthOfTime)}</li>
 * </ul>
 *
 * <p><b>Comparison</b></p>
 *
 * <ul>
 *     <li>{@link #compareTo(PointInTime)}</li>
 *     <li>{@link #isLessThan(Quantizable)}</li>
 *     <li>{@link #isLessThanOrEqualTo(Quantizable)}</li>
 *     <li>{@link #isGreaterThan(Quantizable)}</li>
 *     <li>{@link #isGreaterThanOrEqualTo(Quantizable)}</li>
 *     <li>{@link #isApproximately(Quantizable, Quantizable)}</li>
 *     <li>{@link #isNonZero()}</li>
 *     <li>{@link #isZero()}</li>
 *     <li>{@link #quantum()}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramTimePoint.class)
public interface PointInTime<TimeSubclass extends PointInTime<TimeSubclass, DurationSubclass>, DurationSubclass extends LengthOfTime<DurationSubclass>> extends
        QuantumComparable<PointInTime<?, ?>>,
        Comparable<PointInTime<?, ?>>,
        Minimizable<TimeSubclass>,
        Maximizable<TimeSubclass>,
        Subclassed<TimeSubclass>,
        Stringable,
        TimeZoned<TimeSubclass>,
        TimeUnits<TimeSubclass>,
        Milliseconds,
        Epochal,
        Clocked
{
    /**
     * Returns the length of time that has elapsed since this point in time. Same as {@link #elapsedSince()}.
     */
    @UmlMethodGroup("temporal")
    default DurationSubclass age()
    {
        return elapsedSince();
    }

    @UmlMethodGroup("conversion")
    default DurationSubclass asDuration()
    {
        return newDurationSubclass(milliseconds());
    }

    /**
     * @return A Java {@link Instant} for this time value
     */
    @UmlMethodGroup("conversion")
    default Instant asInstant()
    {
        return Instant.ofEpochMilli(epochMilliseconds());
    }

    /**
     * @return The number of milliseconds since the start of the UNIX epoch on January 1, 1970
     */
    @UmlMethodGroup("conversion")
    default long asMilliseconds()
    {
        return epochMilliseconds();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @UmlMethodGroup("conversion")
    default String asString(Format format)
    {
        switch (format)
        {
            case PROGRAMMATIC:
                return String.valueOf(epochMilliseconds());

            case COMPACT:
            case DEBUG:
            case USER_SINGLE_LINE:
            case USER_LABEL:
            case HTML:
            case TO_STRING:
            case LOG:
            case USER_MULTILINE:
            case TEXT:
            default:
                return new java.util.Date(epochMilliseconds()).toString();
        }
    }

    @Override
    default int compareTo(@NotNull PointInTime<?, ?> that)
    {
        return Long.compare(milliseconds(), that.milliseconds());
    }

    @UmlMethodGroup("arithmetic")
    default TimeSubclass decremented()
    {
        return minusUnits(1);
    }

    /**
     * Returns the length of time between this point in time and the given point in time
     */
    @UmlMethodGroup("arithmetic")
    default DurationSubclass difference(PointInTime<?, ?> that)
    {
        return newDurationSubclass(epochMilliseconds() - that.epochMilliseconds());
    }

    /**
     * Returns the length of time that has elapsed between this point in time and the given point in time in the future
     */
    @UmlMethodGroup("temporal")
    default DurationSubclass durationBefore(PointInTime<?, ?> future)
    {
        // If this time is before the given time,
        if (isLessThan(future))
        {
            // then we can subtract to get the duration.
            return newDurationSubclass(future.epochMilliseconds() - epochMilliseconds());
        }

        return newDurationSubclass(0);
    }

    /**
     * Returns the length of time that has elapsed since this point in time. If this point in time is in the future,
     * returns a zero length of time.
     */
    @UmlMethodGroup("temporal")
    default DurationSubclass elapsedSince()
    {
        var now = newTimeSubclass(System.currentTimeMillis());
        return durationBefore(now);
    }

    /**
     * @return Number of milliseconds since the start of the UNIX epoch for this point in time
     */
    @Override
    @UmlMethodGroup("units")
    long epochMilliseconds();

    @UmlMethodGroup("arithmetic")
    default TimeSubclass incremented()
    {
        return plusUnits(1);
    }

    /**
     * @return True if this point in time is after the given point in time
     */
    @UmlMethodGroup("temporal")
    default boolean isAfter(PointInTime<?, ?> that)
    {
        return isGreaterThan(that);
    }

    /**
     * @return True if this point in time is at or after the given point in time
     */
    @UmlMethodGroup("temporal")
    default boolean isAtOrAfter(PointInTime<?, ?> that)
    {
        return isGreaterThanOrEqualTo(that);
    }

    /**
     * @return True if this point in time is at or before the given point in time
     */
    @UmlMethodGroup("temporal")
    default boolean isAtOrBefore(TimeSubclass that)
    {
        return isLessThanOrEqualTo(that);
    }

    /**
     * @return True if this point in time is before the given point in time
     */
    @UmlMethodGroup("temporal")
    default boolean isBefore(TimeSubclass that)
    {
        return isLessThanOrEqualTo(that);
    }

    /**
     * @return True if this point in time is older than the given age
     */
    @UmlMethodGroup("temporal")
    default boolean isOlderThan(DurationSubclass age)
    {
        return age().isGreaterThan(age);
    }

    /**
     * @return True if this point in time is older or equal to than the given age
     */
    @UmlMethodGroup("temporal")
    default boolean isOlderThanOrEqualTo(DurationSubclass duration)
    {
        return age().isGreaterThanOrEqualTo(duration);
    }

    default boolean isPast()
    {
        return isBefore(currentTime());
    }

    /**
     * @return True if this point in time is younger than the given age
     */
    @UmlMethodGroup("temporal")
    default boolean isYoungerThan(DurationSubclass age)
    {
        return age().isLessThan(age);
    }

    /**
     * @return True if this point in time is younger than or equal to the given age
     */
    @UmlMethodGroup("temporal")
    default boolean isYoungerThanOrEqualTo(DurationSubclass age)
    {
        return age().isLessThanOrEqualTo(age);
    }

    @Override
    @UmlMethodGroup("arithmetic")
    default TimeSubclass maximum(TimeSubclass that)
    {
        return isAfter(that) ? newTimeSubclass(epochMilliseconds()) : that;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @UmlMethodGroup("arithmetic")
    default TimeSubclass maximum()
    {
        return newTimeSubclass(Integer.MAX_VALUE);
    }

    /**
     * Returns the number of milliseconds per unit of time
     */
    @Override
    @UmlMethodGroup("units")
    long millisecondsPerUnit();

    /**
     * {@inheritDoc}
     */
    @Override
    @UmlMethodGroup("arithmetic")
    default TimeSubclass minimum()
    {
        return newTimeSubclass(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @UmlMethodGroup("arithmetic")
    default TimeSubclass minimum(TimeSubclass that)
    {
        return isBefore(that) ? newTimeSubclass(epochMilliseconds()) : that;
    }

    /**
     * Returns this length of time minus the given length of time
     */
    @UmlMethodGroup("arithmetic")
    default DurationSubclass minus(PointInTime<?, ?> that)
    {
        return newDurationSubclass(epochMilliseconds() - that.epochMilliseconds());
    }

    /**
     * Returns this length of time minus the given length of time
     */
    @UmlMethodGroup("arithmetic")
    default TimeSubclass minus(LengthOfTime<?> that)
    {
        return newTimeSubclass(millisecondsInRange(epochMilliseconds() - that.milliseconds()));
    }

    /**
     * Returns an instance of a subclass implementing {@link LengthOfTime} for the given number of milliseconds.
     *
     * @param milliseconds The number of milliseconds
     * @return The {@link LengthOfTime} instance
     */
    @UmlExcludeMember
    DurationSubclass newDurationSubclass(long milliseconds);

    /**
     * Returns an instance of the subclass of this length of time for the given number of milliseconds.
     *
     * @param epochMilliseconds The number of milliseconds
     * @return The subclass instance
     */
    @Override
    @UmlExcludeMember
    TimeSubclass newTimeSubclass(long epochMilliseconds);

    /**
     * Returns this length of time plus the given length of time
     */
    @UmlMethodGroup("arithmetic")
    default TimeSubclass plus(LengthOfTime<?> that)
    {
        return newTimeSubclass(millisecondsInRange(epochMilliseconds() + that.milliseconds()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @UmlExcludeMember
    default long quantum()
    {
        return epochMilliseconds();
    }

    /**
     * Returns this length of time rounded down to the nearest whole unit
     */
    @UmlMethodGroup("arithmetic")
    default TimeSubclass roundDown(LengthOfTime<?> unit)
    {
        return newTimeSubclass(millisecondsInRange(epochMilliseconds() / unit.milliseconds() * unit.milliseconds()));
    }

    /**
     * Returns this length of time rounded up to the nearest whole unit
     */
    @UmlMethodGroup("arithmetic")
    default TimeSubclass roundUp(LengthOfTime<?> duration)
    {
        return roundDown(duration).incremented();
    }

    /**
     * Returns the amount of time between this point in time in the past and now. If this point in time is in the
     * future, returns a zero length of time.
     */
    @UmlMethodGroup("temporal")
    default DurationSubclass until()
    {
        if (isAfter(newTimeSubclass(System.currentTimeMillis())))
        {
            return newDurationSubclass(epochMilliseconds() - System.currentTimeMillis());
        }
        return newDurationSubclass(0);
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
     * The amount of time between now and the given point in time in the future. If this point in time is in the past,
     * returns a zero length of time.
     */
    @UmlMethodGroup("temporal")
    default DurationSubclass until(TimeSubclass that)
    {
        if (that.isAfter(subclass()))
        {
            return newDurationSubclass(that.epochMilliseconds() - epochMilliseconds());
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

    private TimeSubclass currentTime()
    {
        return newTimeSubclass(System.currentTimeMillis());
    }
}
