package com.telenav.kivakit.core.time;

import com.telenav.kivakit.core.language.primitive.Longs;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;

/**
 * Represents a duration in the unit of minutes
 *
 * @author jonathanl (shibo)
 */
public class Minute extends BaseDuration<Minute>
{
    private static final int millisecondsPerMinute = 60 * 1_000;

    public static Minute minutes(long minute)
    {
        ensure(Longs.isBetweenInclusive(minute, 0, 59));
        return new Minute(minute);
    }

    protected Minute(long minute)
    {
        super(minute * millisecondsPerMinute);
    }

    @Override
    public Minute maximum(Minute that)
    {
        return isGreaterThan(that) ? this : that;
    }

    @Override
    public Minute maximum()
    {
        return minutes(59);
    }

    @Override
    public long millisecondsPerUnit()
    {
        return millisecondsPerMinute;
    }

    @Override
    public Minute minimum(Minute that)
    {
        return isLessThan(that) ? this : that;
    }

    @Override
    public Minute minimum()
    {
        return minutes(0);
    }

    public long minutes()
    {
        return asUnits();
    }

    @Override
    public Minute newLengthOfTime(long minute)
    {
        return minutes(minute);
    }
}
