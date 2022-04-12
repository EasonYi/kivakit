package com.telenav.kivakit.core.time;

import com.telenav.kivakit.interfaces.lexakai.DiagramTimeDuration;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import static com.telenav.kivakit.core.time.Day.dayOfMonth;
import static com.telenav.kivakit.core.time.ZonedTime.zonedTime;
import static com.telenav.kivakit.interfaces.time.TimeZoned.utc;

/**
 * Represents the year of a point in time.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramTimeDuration.class)
public class Year extends BaseTime<Year>
{
    private static final long millisecondsPerYear = 31_557_600_000L;

    public static Year unixEpochYear()
    {
        return year(1970);
    }

    public static Year year(long year)
    {
        return new Year(year);
    }

    @UmlExcludeMember
    protected Year(long year)
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
    public Year newTimeSubclass(long milliseconds)
    {
        return year(millisecondsToUnits(milliseconds));
    }

    @Override
    public String toString()
    {
        return "year " + year();
    }

    public Time withMonth(Month month)
    {
        return zonedTime(utc(), this, month, dayOfMonth(1)).asTime();
    }

    public long year()
    {
        return asUnits();
    }
}
