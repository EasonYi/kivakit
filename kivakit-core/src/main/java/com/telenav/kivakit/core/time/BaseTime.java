package com.telenav.kivakit.core.time;

import com.telenav.kivakit.interfaces.time.LengthOfTime;
import com.telenav.kivakit.interfaces.time.PointInTime;

import java.util.Objects;

import static com.telenav.kivakit.core.time.Duration.days;

public abstract class BaseTime<SubClass extends PointInTime<SubClass, LengthOfTimeSubClass>, LengthOfTimeSubClass extends LengthOfTime<LengthOfTimeSubClass>>
        implements PointInTime<SubClass, LengthOfTimeSubClass>
{
    /** This point in time in milliseconds since the start of the UNIX epoch */
    private long epochMilliseconds;

    public BaseTime()
    {
    }

    protected BaseTime(long epochMilliseconds)
    {
        this.epochMilliseconds = epochMilliseconds;
    }

    public SubClass endOfDay()
    {
        return roundUp(days(1));
    }

    @Override
    public long epochMilliseconds()
    {
        return epochMilliseconds;
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof PointInTime<?, ?>)
        {
            var that = (PointInTime<?, ?>) object;
            return this.epochMilliseconds() == that.epochMilliseconds();
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(epochMilliseconds());
    }

    @Override
    public long milliseconds()
    {
        return epochMilliseconds();
    }

    @Override
    public SubClass newInstance(long milliseconds)
    {
        return newPointInTime(milliseconds);
    }

    public SubClass startOfDay()
    {
        return roundDown(days(1));
    }
}
