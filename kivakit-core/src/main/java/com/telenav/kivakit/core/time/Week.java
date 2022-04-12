package com.telenav.kivakit.core.time;

import com.telenav.kivakit.core.language.primitive.Longs;
import com.telenav.kivakit.interfaces.lexakai.DiagramTimeDuration;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;

/**
 * Represents the week of a point in time.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramTimeDuration.class)
public class Week extends BaseTime<Week>
{
    private static final long millisecondsPerWeek = 7 * 24 * 60 * 60 * 1_000;

    public static Week week(long weeks)
    {
        ensure(Longs.isBetweenInclusive(weeks, 0, 59));
        return new Week(weeks);
    }

    @UmlExcludeMember
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
        return week(Long.MAX_VALUE);
    }

    @Override
    public long millisecondsPerUnit()
    {
        return millisecondsPerWeek;
    }

    @Override
    public Week minimum()
    {
        return week(0);
    }

    @Override
    public Week minimum(Week that)
    {
        return isLessThan(that) ? this : that;
    }

    @Override
    public Week newTimeSubclass(long milliseconds)
    {
        return week(millisecondsToUnits(milliseconds));
    }

    @Override
    public String toString()
    {
        return week() + " weeks";
    }

    public long week()
    {
        return asUnits();
    }
}
