package com.telenav.kivakit.interfaces.time;

import com.telenav.kivakit.interfaces.collection.NextValue;
import com.telenav.kivakit.interfaces.lexakai.DiagramTimeDuration;
import com.telenav.kivakit.interfaces.lexakai.DiagramTimePoint;
import com.telenav.kivakit.interfaces.numeric.Maximizable;
import com.telenav.kivakit.interfaces.numeric.Minimizable;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

/**
 * An object with time units.
 *
 * <p>
 * A subclass of TimeUnits such as {@link PointInUnixEpoch} or {@link LengthOfTime} must implement:
 * </p>
 *
 * <ul>
 *      <li>{@link #millisecondsPerUnit()}</li>
 *      <li>{@link #newTimeUnitsSubclass(long)}</li>
 * </ul>
 *
 *
 *
 * <ul>
 *      <li>{@link #asUnits()}</li>
 *      <li>{@link #milliseconds()}</li>
 *      <li>{@link #millisecondsPerUnit()}</li>
 *      <li>{@link #minusUnits(long)}</li>
 *      <li>{@link #modulo()}</li>
 *      <li>{@link #next()}</li>
 *      <li>{@link #plusUnits(long)}</li>
 *      <li>{@link #millisecondsToUnits(Milliseconds)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see PointInUnixEpoch
 * @see LengthOfTime
 */
@UmlClassDiagram(diagram = DiagramTimePoint.class)
@UmlClassDiagram(diagram = DiagramTimeDuration.class)
public interface TimeUnits<TimeUnitSubclass extends TimeUnits<TimeUnitSubclass>> extends
        Minimizable<TimeUnitSubclass>,
        Maximizable<TimeUnitSubclass>,
        NextValue<TimeUnitSubclass>,
        Milliseconds
{
    /**
     * This time value in units
     */
    default long asUnits()
    {
        return milliseconds() / millisecondsPerUnit();
    }

    /**
     * Returns an instance of a general point in time, restricted to the range for this time unit.
     *
     * @param milliseconds The number of milliseconds
     * @return The subclass instance
     */
    @UmlExcludeMember
    default long millisecondsInRange(long milliseconds)
    {
        var modulo = modulo() * millisecondsPerUnit();
        if (milliseconds < 0)
        {
            milliseconds = -milliseconds;
            milliseconds = (modulo - (milliseconds % modulo));
        }
        else
        {
            milliseconds = milliseconds % modulo;
        }
        return minimum().milliseconds() + milliseconds;
    }

    /**
     * Returns the number of milliseconds per unit of time
     */
    long millisecondsPerUnit();

    /**
     * Converts from milliseconds to units
     */
    default long millisecondsToUnits(long milliseconds)
    {
        return milliseconds / millisecondsPerUnit();
    }

    /**
     * Converts from milliseconds to units
     */
    default long millisecondsToUnits(Milliseconds value)
    {
        return millisecondsToUnits(value.milliseconds());
    }

    default long millisecondsToUnitsInRange(long milliseconds)
    {
        return millisecondsToUnits(millisecondsInRange(milliseconds));
    }

    /**
     * Returns this object minus the given number of units
     */
    default TimeUnitSubclass minusUnits(long units)
    {
        return plusUnits(-units);
    }

    /**
     * Returns the modulo size of this number of time units. For example, the modulo for military time would be 24.
     */
    default long modulo()
    {
        return millisecondsToUnits(maximum()) - millisecondsToUnits(minimum()) + 1;
    }

    /**
     * Returns a new instance of the subclass of {@link TimeUnits} for the given number of milliseconds
     */
    TimeUnitSubclass newTimeUnitsSubclass(long milliseconds);

    /**
     * Returns this object plus one unit
     */
    @Override
    default TimeUnitSubclass next()
    {
        return plusUnits(1);
    }

    /**
     * Returns this object plus the given number of units
     */
    default TimeUnitSubclass plusUnits(long units)
    {
        return newTimeUnitsSubclass(millisecondsInRange(unitsToMilliseconds(asUnits() + units)));
    }

    default long unitsToMilliseconds(long units)
    {
        return units * millisecondsPerUnit();
    }
}
