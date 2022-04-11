package com.telenav.kivakit.interfaces.time;

import java.time.Clock;

/**
 * Interface to an object with a Java {@link Clock}
 *
 * @author jonathanl (shibo)
 */
public interface Clocked
{
    /**
     * @return The clock
     */
    Clock clock();
}
