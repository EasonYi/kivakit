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

/**
 * Interface to an object representing a point in time, measured in milliseconds.
 *
 * <p>
 * A subclass implementing this interface is not necessarily in real time (for example "5 o'clock" is a point in time
 * relative to midnight, but it is not a point in real time). To represent points in real time, the interface {@link
 * PointInUnixEpoch} extends this interface to add methods relating to time in the Unix epoch.
 * </p>
 *
 * <p><b>Creation</b></p>
 *
 * <p>
 * An object that implements this interface must provide implementations of:
 * </p>
 *
 * <ul>
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
 *     <li>{@link #until(PointInTime)}</li>
 *     <li>{@link #roundUp(LengthOfTime)}</li>
 *     <li>{@link #roundDown(LengthOfTime)}</li>
 * </li>
 *
 * <p><b>Conversion</b></p>
 *
 * <ul>
 *     <li>{@link #asDuration()}</li>
 *     <li>{@link #asMilliseconds()} - The number of milliseconds since the start of the UNIX epoch for this point in time</li>
 * </ul>
 *
 * <p><b>Comparison</b></p>
 *
 * <ul>
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
        TimeComparable<TimeSubclass>,
        Minimizable<TimeSubclass>,
        Maximizable<TimeSubclass>,
        Subclassed<TimeSubclass>,
        TimeUnits<TimeSubclass>,
        Milliseconds,
        Stringable
{
    @UmlMethodGroup("conversion")
    default DurationSubclass asDuration()
    {
        return newDurationSubclass(milliseconds());
    }

    /**
     * @return The number of milliseconds since the start of the UNIX epoch on January 1, 1970
     */
    @UmlMethodGroup("conversion")
    default long asMilliseconds()
    {
        return milliseconds();
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
                return String.valueOf(milliseconds());

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
                return new java.util.Date(milliseconds()).toString();
        }
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
        return newDurationSubclass(Math.abs(milliseconds() - that.milliseconds()));
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
            return newDurationSubclass(future.milliseconds() - milliseconds());
        }

        return newDurationSubclass(0);
    }

    @UmlMethodGroup("arithmetic")
    default TimeSubclass incremented()
    {
        return plusUnits(1);
    }

    @Override
    @UmlMethodGroup("arithmetic")
    default TimeSubclass maximum(TimeSubclass that)
    {
        return isGreaterThan(that) ? newTimeSubclass(milliseconds()) : that;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @UmlMethodGroup("arithmetic")
    default TimeSubclass minimum(TimeSubclass that)
    {
        return isLessThan(that) ? newTimeSubclass(milliseconds()) : that;
    }

    /**
     * Returns this length of time minus the given length of time
     */
    @UmlMethodGroup("arithmetic")
    default DurationSubclass minus(PointInTime<?, ?> that)
    {
        return newDurationSubclass(milliseconds() - that.milliseconds());
    }

    /**
     * Returns this length of time minus the given length of time
     */
    @UmlMethodGroup("arithmetic")
    default TimeSubclass minus(LengthOfTime<?> that)
    {
        return newTimeSubclass(millisecondsInRange(milliseconds() - that.milliseconds()));
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
    @UmlExcludeMember
    TimeSubclass newTimeSubclass(long epochMilliseconds);

    /**
     * Returns this length of time plus the given length of time
     */
    @UmlMethodGroup("arithmetic")
    default TimeSubclass plus(LengthOfTime<?> that)
    {
        return newTimeSubclass(millisecondsInRange(milliseconds() + that.milliseconds()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @UmlExcludeMember
    default long quantum()
    {
        return milliseconds();
    }

    /**
     * Returns this length of time rounded down to the nearest whole unit
     */
    @UmlMethodGroup("arithmetic")
    default TimeSubclass roundDown(LengthOfTime<?> unit)
    {
        return newTimeSubclass(millisecondsInRange(milliseconds() / unit.milliseconds() * unit.milliseconds()));
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
     * The amount of time between now and the given point in time in the future. If this point in time is in the past,
     * returns a zero length of time.
     */
    @UmlMethodGroup("temporal")
    default DurationSubclass until(TimeSubclass that)
    {
        if (that.isGreaterThan(subclass()))
        {
            return newDurationSubclass(that.milliseconds() - milliseconds());
        }
        return newDurationSubclass(0);
    }
}
