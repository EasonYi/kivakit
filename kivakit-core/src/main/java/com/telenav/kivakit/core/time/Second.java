package com.telenav.kivakit.core.time;

import com.telenav.kivakit.core.language.primitive.Longs;
import com.telenav.kivakit.interfaces.lexakai.DiagramTimeDuration;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.UmlMethodGroup;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;

/**
 * Represents a duration in the unit of minutes
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramTimeDuration.class)
public class Second extends BaseDuration<Second>
{
    private static final int millisecondsPerSecond = 1_000;

    @UmlMethodGroup("factory")
    public static Second seconds(long second)
    {
        ensure(Longs.isBetweenInclusive(second, 0, 59));
        return new Second(second);
    }

    @UmlExcludeMember
    protected Second(long second)
    {
        super(second * millisecondsPerSecond);
    }

    @Override
    @UmlMethodGroup("arithmetic")
    public Second maximum(Second that)
    {
        return isGreaterThan(that) ? this : that;
    }

    @Override
    @UmlMethodGroup("arithmetic")
    public Second maximum()
    {
        return seconds(59);
    }

    @Override
    @UmlMethodGroup("units")
    public long millisecondsPerUnit()
    {
        return millisecondsPerSecond;
    }

    @Override
    @UmlMethodGroup("arithmetic")
    public Second minimum(Second that)
    {
        return isLessThan(that) ? this : that;
    }

    @Override
    @UmlMethodGroup("arithmetic")
    public Second minimum()
    {
        return seconds(0);
    }

    @Override
    @UmlExcludeMember
    public Second newDuration(long seconds)
    {
        return seconds(seconds);
    }
}


