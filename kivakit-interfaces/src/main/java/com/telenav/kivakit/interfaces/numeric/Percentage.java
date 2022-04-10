package com.telenav.kivakit.interfaces.numeric;

/**
 * Interface to a class that represents a value from 0 to 100, or more.
 *
 * @author jonathanl (shibo)
 */
public interface Percentage
{
    /**
     * A value from 0 to 100, exclusive. A percentage can also be more than 100%, such as 115%.
     */
    double percent();
}
