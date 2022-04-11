package com.telenav.kivakit.interfaces.time;

import com.telenav.kivakit.interfaces.lexakai.DiagramTimePoint;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * An object with a time since the beginning of the UNIX epoch, in milliseconds.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramTimePoint.class)
public interface Epochal
{
    /**
     * @return The number of milliseconds since the beginning of the UNIX epoch
     */
    long epochMilliseconds();
}
