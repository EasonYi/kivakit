package com.telenav.kivakit.core.time;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.interfaces.time.TimeZoned.utc;

@SuppressWarnings("unused")
public class Year extends BaseDuration<Year>
{
    public static Year year(int year)
    {
        ensure(year >= 1970 && year < 3000);
        return new Year(year);
    }

    protected Year(int year)
    {
        super(year);
    }

    @Override
    public Year maximum()
    {
        return year(2038);
    }

    @Override
    public long millisecondsPerUnit()
    {
        return 31_557_600_000L;
    }

    @Override
    public Year minimum()
    {
        return year(0);
    }

    @Override
    public Year newLengthOfTime(long year)
    {
        return year((int) year);
    }

    public LocalTime withMonth(Month month)
    {
        return LocalTime.localTime(utc(), this, month, Day.dayOfMonth(1));
    }
}
