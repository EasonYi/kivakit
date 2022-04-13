package com.telenav.kivakit.core.time;

import com.telenav.kivakit.interfaces.time.Milliseconds;
import com.telenav.kivakit.interfaces.time.PointInTime;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import java.util.Objects;

/**
 * Base class for points in time, as measured in {@link Milliseconds}.
 *
 * @author jonathanl (shibo)
 */
public abstract class BaseTime<TimeSubclass extends PointInTime<TimeSubclass, Duration>> implements PointInTime<TimeSubclass, Duration>
{
    /** This point in time in milliseconds */
    private long milliseconds;

    public BaseTime()
    {
    }

    @UmlExcludeMember
    protected BaseTime(long milliseconds)
    {
        this.milliseconds = milliseconds;
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof PointInTime<?, ?>)
        {
            var that = (PointInTime<?, ?>) object;
            return this.milliseconds() == that.milliseconds();
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(milliseconds());
    }

    @Override
    public long milliseconds()
    {
        return milliseconds;
    }

    @Override
    public Duration newDurationSubclass(long milliseconds)
    {
        return Duration.milliseconds(milliseconds);
    }

    @Override
    @SuppressWarnings("unchecked")
    public TimeSubclass newTimeSubclass(long epochMilliseconds)
    {
        return (TimeSubclass) Time.epochMilliseconds(epochMilliseconds);
    }

    @Override
    public TimeSubclass newTimeUnitsSubclass(long milliseconds)
    {
        return newTimeSubclass(milliseconds);
    }
}
