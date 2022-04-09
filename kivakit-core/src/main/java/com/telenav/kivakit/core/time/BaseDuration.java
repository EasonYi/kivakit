package com.telenav.kivakit.core.time;

import com.telenav.kivakit.interfaces.time.LengthOfTime;

import java.util.Objects;

/**
 * Base class for implementing durations
 *
 * @author jonathanl (shibo)
 * @see Duration
 * @see Second
 * @see Minute
 * @see Hour
 * @see Day
 * @see Week
 */
public abstract class BaseDuration<SubClass extends LengthOfTime<SubClass>> implements LengthOfTime<SubClass>
{
    private final long milliseconds;

    protected BaseDuration(long milliseconds)
    {
        this.milliseconds = milliseconds;
    }

    public Duration asDuration()
    {
        return Duration.milliseconds(milliseconds());
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof LengthOfTime<?>)
        {
            var that = (LengthOfTime<?>) object;
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
}
