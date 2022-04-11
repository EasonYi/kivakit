package com.telenav.kivakit.core.time;

import com.telenav.kivakit.interfaces.lexakai.DiagramTimeDuration;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.time.Day.dayOfMonth;
import static com.telenav.kivakit.interfaces.time.TimeZoned.utc;

@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramTimeDuration.class)
public class Year extends BaseDuration<Year>
{
    private static final long millisecondsPerYear = 31_557_600_000L;

    public static Year year(int year)
    {
        ensure(year >= 1970 && year < 3000);
        return new Year(year);
    }

    protected Year(int year)
    {
        super(year * millisecondsPerYear);
    }

    @Override
    public Year maximum()
    {
        return year(2038);
    }

    @Override
    public long millisecondsPerUnit()
    {
        return millisecondsPerYear;
    }

    @Override
    public Year minimum()
    {
        return year(0);
    }

    @Override
    public Year newDuration(long year)
    {
        return year((int) year);
    }

    public ZonedTime withMonth(Month month)
    {
        return ZonedTime.zonedTime(utc(), this, month, dayOfMonth(1));
    }
}
