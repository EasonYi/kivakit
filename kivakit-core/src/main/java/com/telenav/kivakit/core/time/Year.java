package com.telenav.kivakit.core.time;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.time.LocalTime.utcTimeZone;

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
    public long millisecondsPerUnit()
    {
        return 31_557_600_000L;
    }

    @Override
    public Year newInstance(long year)
    {
        return year((int) year);
    }

    public Time withMonth(Month month)
    {
        return LocalTime.localTime(utcTimeZone(), this, month, Day.dayOfMonth(1), Hour.militaryHour(0), Minute.minutes(0), Second.seconds(0));
    }
}
