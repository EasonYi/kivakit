package com.telenav.kivakit.interfaces.time;

import com.telenav.kivakit.interfaces.numeric.Quantizable;

/**
 * An number of milliseconds. Note that this interface is different from {@link UnixEpochal}, which provides
 * milliseconds since the start of the UNIX epoch.
 *
 * @author jonathanl (shibo)
 */
public interface Milliseconds extends Quantizable
{
    /**
     * @return The number of milliseconds
     */
    long milliseconds();

    @Override
    default long quantum()
    {
        return milliseconds();
    }
}
