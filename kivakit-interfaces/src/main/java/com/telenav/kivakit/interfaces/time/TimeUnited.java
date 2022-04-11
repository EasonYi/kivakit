package com.telenav.kivakit.interfaces.time;

import com.telenav.kivakit.interfaces.lexakai.DiagramTimeDuration;
import com.telenav.kivakit.interfaces.lexakai.DiagramTimePoint;
import com.telenav.kivakit.interfaces.numeric.Maximizable;
import com.telenav.kivakit.interfaces.numeric.Minimizable;
import com.telenav.lexakai.annotations.UmlClassDiagram;

@UmlClassDiagram(diagram = DiagramTimePoint.class)
@UmlClassDiagram(diagram = DiagramTimeDuration.class)
public interface TimeUnited<TimeOrDuration extends TimeUnited<TimeOrDuration>> extends
        Minimizable<TimeOrDuration>,
        Maximizable<TimeOrDuration>,
        Milliseconds
{
    default long asUnits()
    {
        return milliseconds() / millisecondsPerUnit();
    }

    @Override
    long milliseconds();

    /**
     * Returns the number of milliseconds per unit of time
     */
    long millisecondsPerUnit();

    default TimeOrDuration minusUnits(long units)
    {
        return plusUnits(-units);
    }

    /**
     * Returns the modulo size of this length of time in units. For example, the modulo for military time would be 24.
     */
    default long modulo()
    {
        return units(maximum()) - units(minimum()) + 1;
    }

    /**
     * Returns an instance of the subclass of this length or point in time.
     *
     * @param milliseconds The number of milliseconds
     * @return The subclass instance
     */
    TimeOrDuration newTimeOrDuration(long milliseconds);

    /**
     * Forces the given units to be within the range between {@link #minimum()} and {@link #maximum()}, inclusive.
     */
    default TimeOrDuration newTimeOrDurationFromUnits(long units)
    {
        return newTimeOrDurationInRange(units * millisecondsPerUnit());
    }

    default TimeOrDuration newTimeOrDurationInRange(long milliseconds)
    {
        var modulo = modulo();
        var units = milliseconds / millisecondsPerUnit();
        if (units < 0)
        {
            units = -units;
            units = (modulo - (units % modulo));
        }
        else
        {
            units = units % modulo;
        }
        return newTimeOrDuration(minimum().milliseconds() + (units * millisecondsPerUnit()));
    }

    default TimeOrDuration next()
    {
        return plusUnits(1);
    }

    default TimeOrDuration plusUnits(long units)
    {
        return newTimeOrDurationFromUnits(asUnits() + units);
    }

    default long units(Milliseconds value)
    {
        return value.milliseconds() / millisecondsPerUnit();
    }
}
