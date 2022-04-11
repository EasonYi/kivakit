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
public class Minute extends BaseTime<Minute>
{
    private static final int millisecondsPerMinute = 60 * 1_000;

    @UmlMethodGroup("factory")
    public static Minute minute(long minute)
    {
        ensure(Longs.isBetweenInclusive(minute, 0, 59));
        return new Minute(minute);
    }

    @UmlExcludeMember
    protected Minute(long minute)
    {
        super(minute * millisecondsPerMinute);
    }

    @Override
    @UmlMethodGroup("arithmetic")
    public Minute maximum(Minute that)
    {
        return isGreaterThan(that) ? this : that;
    }

    @Override
    @UmlMethodGroup("arithmetic")
    public Minute maximum()
    {
        return minute(59);
    }

    @Override
    @UmlMethodGroup("units")
    public long millisecondsPerUnit()
    {
        return millisecondsPerMinute;
    }

    @Override
    @UmlMethodGroup("arithmetic")
    public Minute minimum(Minute that)
    {
        return isLessThan(that) ? this : that;
    }

    @Override
    @UmlMethodGroup("arithmetic")
    public Minute minimum()
    {
        return minute(0);
    }

    @UmlMethodGroup("units")
    public long minute()
    {
        return asUnits();
    }

    @Override
    @UmlExcludeMember
    public Duration newDuration(long milliseconds)
    {
        return Duration.milliseconds(milliseconds);
    }

    @Override
    public Minute newTime(long epochMilliseconds)
    {
        return Minute.minute(units(epochMilliseconds));
    }

    @Override
    public String toString()
    {
        return "minute " + minute();
    }
}
