package com.telenav.kivakit.interfaces.time;

import com.telenav.kivakit.interfaces.lexakai.DiagramTime;
import com.telenav.kivakit.interfaces.numeric.Maximizable;
import com.telenav.kivakit.interfaces.numeric.Minimizable;
import com.telenav.kivakit.interfaces.numeric.Quantizable;
import com.telenav.kivakit.interfaces.string.Stringable;
import com.telenav.lexakai.annotations.UmlClassDiagram;

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
 *     <li>{@link #newPointInTime(long)} - Creates instances of the implementing class</li>
 *     <li>{@link #newLengthOfTime(long)} - Creates instances of a related {@link LengthOfTime} for use by methods such as {@link #minus(PointInTime)}</li>
 * </ul>
 *
 * <p><b>Arithmetic</b></p>
 *
 * <ul>
 *     <li>{@link #difference(PointInTime)} - Returns the {@link LengthOfTime} between this point in time and the given point in time</li>
 *     <li>{@link #minus(PointInTime)} - Returns this point in time minus the given point in time as a {@link LengthOfTime}</li>
 *     <li>{@link #plus(LengthOfTime)} - Returns this point in time plus the given {@link LengthOfTime}</li>
 *     <li>{@link #leftUntil(LengthOfTime)} - Returns the amount of time left before the given amount of time has elapsed since this point in time</li>
 * </ul>
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
 *     <li>{@link #elapsedSince(PointInTime)}</li>
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
@UmlClassDiagram(diagram = DiagramTime.class)
public interface PointInTime<SubClass extends PointInTime<SubClass, LengthOfTimeSubClass>, LengthOfTimeSubClass extends LengthOfTime<LengthOfTimeSubClass>> extends
        Quantizable,
        Minimizable<SubClass>,
        Maximizable<SubClass>,
        Comparable<PointInTime<?, ?>>,
        Stringable,
        TimeZoned<SubClass>,
        TimeUnited<SubClass>
{
    /**
     * Returns the length of time that has elapsed since this point in time. Same as {@link #elapsedSince()}.
     */
    default LengthOfTime<?> age()
    {
        return elapsedSince();
    }

    /**
     * @return A Java {@link Instant} for this time value
     */
    default Instant asInstant()
    {
        return Instant.ofEpochMilli(epochMilliseconds());
    }

    /**
     * @return The number of milliseconds since the start of the UNIX epoch on January 1, 1970
     */
    default long asMilliseconds()
    {
        return epochMilliseconds();
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
    default long asUnits()
    {
        return epochMilliseconds() / millisecondsPerUnit();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default int compareTo(PointInTime<?, ?> that)
    {
        return Long.compare(epochMilliseconds(), that.epochMilliseconds());
    }

    default SubClass decremented()
    {
        return minus(newLengthOfTime(millisecondsPerUnit()));
    }

    /**
     * Returns the length of time between this point in time and the given point in time
     */
    default LengthOfTimeSubClass difference(PointInTime<?, ?> that)
    {
        return newLengthOfTime(epochMilliseconds() - that.epochMilliseconds());
    }

    /**
     * Returns the length of time that has elapsed since this point in time. If this point in time is in the future,
     * returns a zero length of time.
     */
    default LengthOfTimeSubClass elapsedSince()
    {
        return elapsedSince(newPointInTime(System.currentTimeMillis()));
    }

    /**
     * Returns the length of time that has elapsed between the given point in time and this point in time
     */
    default LengthOfTimeSubClass elapsedSince(PointInTime<?, ?> that)
    {
        // If this time is after the given time,
        if (isAtOrAfter(that))
        {
            // then we can subtract to get the duration.
            return newLengthOfTime(epochMilliseconds() - that.epochMilliseconds());
        }

        return newLengthOfTime(0);
    }

    /**
     * @return Number of milliseconds since the start of the UNIX epoch for this point in time
     */
    @Override
    long epochMilliseconds();

    default SubClass incremented()
    {
        return plus(newLengthOfTime(millisecondsPerUnit()));
    }

    /**
     * @return True if this point in time is after the given point in time
     */
    default boolean isAfter(PointInTime<?, ?> that)
    {
        return isGreaterThan(that);
    }

    /**
     * @return True if this point in time is at or after the given point in time
     */
    default boolean isAtOrAfter(PointInTime<?, ?> that)
    {
        return isGreaterThanOrEqualTo(that);
    }

    /**
     * @return True if this point in time is at or before the given point in time
     */
    default boolean isAtOrBefore(PointInTime<?, ?> that)
    {
        return isLessThan(that);
    }

    /**
     * @return True if this point in time is before the given point in time
     */
    default boolean isBefore(PointInTime<?, ?> that)
    {
        return isLessThanOrEqualTo(that);
    }

    /**
     * @return True if this point in time is older than the given age
     */
    default boolean isOlderThan(LengthOfTime<?> age)
    {
        return age().isGreaterThan(age);
    }

    /**
     * @return True if this point in time is older or equal to than the given age
     */
    default boolean isOlderThanOrEqualTo(LengthOfTime<?> duration)
    {
        return age().isGreaterThanOrEqualTo(duration);
    }

    /**
     * @return True if this point in time is younger than the given age
     */
    default boolean isYoungerThan(LengthOfTime<?> age)
    {
        return age().isLessThan(age);
    }

    /**
     * @return True if this point in time is younger than or equal to the given age
     */
    default boolean isYoungerThanOrEqualTo(LengthOfTime<?> age)
    {
        return age().isLessThanOrEqualTo(age);
    }

    /**
     * @return The amount of time left until the given amount of time has elapsed
     */
    default LengthOfTime<?> leftUntil(LengthOfTime<?> elapsed)
    {
        return elapsed.minus(elapsedSince());
    }

    @Override
    default SubClass maximum(SubClass that)
    {
        return isAfter(that) ? newPointInTime(epochMilliseconds()) : that;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default SubClass maximum()
    {
        return newPointInTime(Integer.MAX_VALUE);
    }

    /**
     * Returns the number of milliseconds per unit of time
     */
    @Override
    long millisecondsPerUnit();

    /**
     * {@inheritDoc}
     */
    @Override
    default SubClass minimum()
    {
        return newPointInTime(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default SubClass minimum(SubClass that)
    {
        return isBefore(that) ? newPointInTime(epochMilliseconds()) : that;
    }

    /**
     * Returns this length of time minus the given length of time
     */
    default LengthOfTimeSubClass minus(PointInTime<?, ?> that)
    {
        return newLengthOfTime(epochMilliseconds() - that.epochMilliseconds());
    }

    /**
     * Returns this length of time minus the given length of time
     */
    default SubClass minus(LengthOfTime<?> that)
    {
        return newPointInTime(epochMilliseconds() - that.milliseconds());
    }

    /**
     * Returns an instance of a subclass implementing {@link LengthOfTime} for the given number of milliseconds.
     *
     * @param milliseconds The number of milliseconds
     * @return The {@link LengthOfTime} instance
     */
    LengthOfTimeSubClass newLengthOfTime(long milliseconds);

    /**
     * Returns an instance of the subclass of this length of time for the given number of milliseconds.
     *
     * @param epochMilliseconds The number of milliseconds
     * @return The subclass instance
     */
    SubClass newPointInTime(long epochMilliseconds);

    /**
     * Returns this length of time plus the given length of time
     */
    default SubClass plus(LengthOfTime<?> that)
    {
        return newPointInTime(epochMilliseconds() + that.milliseconds());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default long quantum()
    {
        return epochMilliseconds();
    }

    /**
     * Returns this length of time rounded down to the nearest whole unit
     */
    default SubClass roundDown(LengthOfTime<?> unit)
    {
        return newPointInTime(epochMilliseconds() / unit.milliseconds() * unit.milliseconds());
    }

    /**
     * Returns this length of time rounded up to the nearest whole unit
     */
    default SubClass roundUp(LengthOfTime<?> unit)
    {
        return roundDown(unit).plus(unit);
    }

    /**
     * The amount of time between now and the given point in time. If this point in time is in the past, returns a zero
     * length of time.
     */
    default LengthOfTimeSubClass until(PointInTime<?, ?> that)
    {
        if (that.isAfter(this))
        {
            return newLengthOfTime(that.epochMilliseconds() - epochMilliseconds());
        }
        return newLengthOfTime(0);
    }

    /**
     * Returns the amount of time between this point in time and now. If this point in time is in the future, returns a
     * zero length of time.
     */
    default LengthOfTime<?> untilNow()
    {
        return until(newPointInTime(System.currentTimeMillis()));
    }
}
