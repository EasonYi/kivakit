package com.telenav.kivakit.core.time;

import com.telenav.kivakit.core.language.primitive.Longs;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;

@SuppressWarnings("unused")
public class Week extends BaseDuration<Week>
{
    private static final long millisecondsPerUnit = 7 * 24 * 60 * 60 * 1_000;

    public static Week weeks(long weeks)
    {
        ensure(Longs.isBetweenInclusive(weeks, 0, 59));
        return new Week(weeks);
    }

    protected Week(long weeks)
    {
        super(weeks * millisecondsPerUnit);
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
        return millisecondsPerUnit;
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
    public Week newLengthOfTime(long milliseconds)
    {
        return weeks(milliseconds / millisecondsPerUnit());
    }
}
