package com.telenav.kivakit.interfaces.time;

/**
 * An number of milliseconds. Note that this interface is different from {@link Epochal}, which provides milliseconds
 * since the start of the UNIX epoch.
 *
 * @author jonathanl (shibo)
 */
public interface Milliseconds
{
    /**
     * @return The number of milliseconds
     */
    long milliseconds();
}
