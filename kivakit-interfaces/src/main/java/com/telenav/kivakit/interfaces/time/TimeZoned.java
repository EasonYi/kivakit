package com.telenav.kivakit.interfaces.time;

import java.time.ZoneId;

/**
 * Interface to an object that has a time zone.
 *
 * @author jonathanl (shibo)
 */
public interface TimeZoned<SubClass extends PointInTime<SubClass, ?>> extends Epoched
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
    default SubClass asLocalTime(ZoneId zone)
    {
        if (hasTimeZone())
        {
            return (SubClass) this;
        }
        else
        {
            return newZonedPointInTime(timeZone(), epochMilliseconds());
        }
    }

    /**
     * @return This point in time, in UTC time
     */
    default SubClass asUtc()
    {
        return asLocalTime(utc());
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

    <ZonedSubClass extends TimeZoned<SubClass>> ZonedSubClass newZonedPointInTime(ZoneId zone, long epochMilliseconds);

    default ZoneId timeZone()
    {
        return utc();
    }
}
