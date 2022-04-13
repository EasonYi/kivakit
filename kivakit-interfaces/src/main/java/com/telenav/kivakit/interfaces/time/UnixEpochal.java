package com.telenav.kivakit.interfaces.time;

import com.telenav.kivakit.interfaces.lexakai.DiagramTimePoint;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.UmlMethodGroup;

import java.time.Instant;

/**
 * An object with a time since the beginning of the Unix epoch, in milliseconds. The Unix epoch started on January 1,
 * 1970 at midnight.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramTimePoint.class)
public interface UnixEpochal
{
    /**
     * @return A Java {@link Instant} for this time value
     */
    @UmlMethodGroup("conversion")
    default Instant asInstant()
    {
        return Instant.ofEpochMilli(epochMilliseconds());
    }

    /**
     * @return The number of milliseconds since the beginning of the UNIX epoch
     */
    long epochMilliseconds();
}
