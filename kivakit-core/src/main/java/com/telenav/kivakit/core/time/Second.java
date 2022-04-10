package com.telenav.kivakit.core.time;

import com.telenav.kivakit.core.language.primitive.Longs;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;

/**
 * Represents a duration in the unit of minutes
 *
 * @author jonathanl (shibo)
 */
public class Second extends BaseDuration<Second>
{
    private static final int millisecondsPerSecond = 1_000;

    public static Second seconds(long second)
    {
        ensure(Longs.isBetweenInclusive(second, 0, 59));
        return new Second(second);
    }

    protected Second(long second)
    {
        super(second * millisecondsPerSecond);
    }

    @Override
    public Second maximum(Second that)
    {
        return isGreaterThan(that) ? this : that;
    }

    @Override
    public Second maximum()
    {
        return seconds(59);
    }

    @Override
    public long millisecondsPerUnit()
    {
        return millisecondsPerSecond;
    }

    @Override
    public Second minimum(Second that)
    {
        return isLessThan(that) ? this : that;
    }

    @Override
    public Second minimum()
    {
        return seconds(0);
    }

    @Override
    public Second newLengthOfTime(long seconds)
    {
        return seconds(seconds);
    }
}


