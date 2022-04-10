package com.telenav.kivakit.interfaces.time;

import com.telenav.kivakit.interfaces.numeric.Maximizable;
import com.telenav.kivakit.interfaces.numeric.Minimizable;

public interface TimeUnited<SubClass extends TimeUnited<SubClass>> extends
        Minimizable<SubClass>,
        Maximizable<SubClass>,
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

    default SubClass minusUnits(long units)
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
     * Forces the given units to be within the range between {@link #minimum()} and {@link #maximum()}, inclusive.
     */
    default SubClass newInstanceFromUnits(long units)
    {
        var modulo = modulo();
        var rounded = (units + modulo) % modulo;
        var offset = units(minimum()) + rounded;
        return newTimeUnitedInstance(offset * millisecondsPerUnit());
    }

    /**
     * Returns an instance of the subclass of this length of time for the given number of milliseconds.
     *
     * @param milliseconds The number of milliseconds
     * @return The subclass instance
     */
    SubClass newTimeUnitedInstance(long milliseconds);

    default SubClass next()
    {
        return plusUnits(1);
    }

    default SubClass plusUnits(long units)
    {
        return newInstanceFromUnits(asUnits() + units);
    }

    default long units(Milliseconds value)
    {
        return value.milliseconds() / millisecondsPerUnit();
    }
}
