package com.telenav.kivakit.interfaces.time;

/**
 * An object with a time since the beginning of the UNIX epoch, in milliseconds.
 *
 * @author jonathanl (shibo)
 */
public interface Epoched
{
    /**
     * @return The number of milliseconds since the beginning of the UNIX epoch
     */
    long epochMilliseconds();
}
