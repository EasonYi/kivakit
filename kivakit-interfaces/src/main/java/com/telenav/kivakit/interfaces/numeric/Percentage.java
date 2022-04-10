package com.telenav.kivakit.interfaces.numeric;

/**
 * Interface to a class that represents a value from 0 to 100, or more.
 *
 * @author jonathanl (shibo)
 */
public interface Percentage extends UnitValue
{
    /**
     * A value from 0 to 100, exclusive. A percentage can also be more than 100%, such as 115%.
     */
    double percent();

    /**
     * Returns this percentage as a unit value
     */
    @Override
    default double unitValue()
    {
        return percent() / 100.0;
    }
}
