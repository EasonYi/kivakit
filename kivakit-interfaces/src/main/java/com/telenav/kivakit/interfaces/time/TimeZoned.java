package com.telenav.kivakit.interfaces.time;

import java.time.ZoneId;
import java.util.TimeZone;

/**
 * Interface to an object that has a time zone.
 *
 * @author jonathanl (shibo)
 */
public interface TimeZoned<SubClass extends PointInTime<SubClass, ?>> extends Milliseconds
{
    static ZoneId localTimeZone()
    {
        return ZoneId.systemDefault();
    }

    static ZoneId utc()
    {
        return ZoneId.of("UTC");
    }

    /**
     * Returns this point in time in the given time zone
     */
    @SuppressWarnings("unchecked")
    default <ZonedSubClass extends TimeZoned<SubClass>> ZonedSubClass asLocalTime(ZoneId zone)
    {
        if (hasTimeZone())
        {
            return (ZonedSubClass) this;
        }
        else
        {
            return newLocalPointInTime(timeZone(), milliseconds());
        }
    }

    default <ZonedSubClass extends TimeZoned<SubClass>> ZonedSubClass asLocalTime()
    {
        return asLocalTime(localTimeZone());
    }

    /**
     * Returns this point in time in UTC time based on the given time zone
     */
    default SubClass asUtc(ZoneId zone)
    {
        var offset = TimeZone.getTimeZone(zone).getOffset(milliseconds());
        var milliseconds = milliseconds() - offset;
        return newPointInTime(milliseconds);
    }

    /**
     * @return This point in time, in UTC time
     */
    default SubClass asUtc()
    {
        return asUtc(timeZone());
    }

    /**
     * Returns true if this time has a time zone
     */
    default boolean hasTimeZone()
    {
        return !isUtc();
    }

    default boolean isUtc()
    {
        return timeZone().equals(utc());
    }

    default <ZonedSubClass extends TimeZoned<SubClass>> ZonedSubClass newLocalPointInTime(ZoneId zone,
                                                                                          long epochMilliseconds)
    {
        throw new UnsupportedOperationException();
    }

    SubClass newPointInTime(long milliseconds);

    default ZoneId timeZone()
    {
        return utc();
    }
}
