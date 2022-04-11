package com.telenav.kivakit.core.time;

import com.telenav.kivakit.core.language.primitive.Longs;
import com.telenav.kivakit.interfaces.lexakai.DiagramTimeDuration;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.UmlMethodGroup;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;

/**
 * Represents the second of a point in time.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramTimeDuration.class)
public class Second extends BaseTime<Second>
{
    private static final int millisecondsPerSecond = 1_000;

    @UmlMethodGroup("factory")
    public static Second second(long second)
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
        return second(59);
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
        return second(0);
    }

    @Override
    @UmlExcludeMember
    public Duration newDuration(long seconds)
    {
        return Duration.seconds(seconds);
    }

    @Override
    public Second newTime(long epochMilliseconds)
    {
        return Second.second(units(epochMilliseconds));
    }

    public long second()
    {
        return asUnits();
    }

    @Override
    public String toString()
    {
        return "second " + second();
    }
}


