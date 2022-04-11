package com.telenav.kivakit.core.time;

import com.telenav.kivakit.core.language.primitive.Longs;
import com.telenav.kivakit.interfaces.lexakai.DiagramTimeDuration;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;

@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramTimeDuration.class)
public class Week extends BaseDuration<Week>
{
    private static final long millisecondsPerWeek = 7 * 24 * 60 * 60 * 1_000;

    public static Week weeks(long weeks)
    {
        ensure(Longs.isBetweenInclusive(weeks, 0, 59));
        return new Week(weeks);
    }

    protected Week(long weeks)
    {
        super(weeks * millisecondsPerWeek);
    }

    @Override
    public Week maximum(Week that)
    {
        return isGreaterThan(that) ? this : that;
    }

    @Override
    public Week maximum()
    {
        return weeks(Long.MAX_VALUE);
    }

    @Override
    public long millisecondsPerUnit()
    {
        return millisecondsPerWeek;
    }

    @Override
    public Week minimum()
    {
        return weeks(0);
    }

    @Override
    public Week minimum(Week that)
    {
        return isLessThan(that) ? this : that;
    }

    @Override
    public Week newDuration(long milliseconds)
    {
        return weeks(milliseconds / millisecondsPerUnit());
    }
}
