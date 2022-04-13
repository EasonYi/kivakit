package com.telenav.kivakit.interfaces.time;

import com.telenav.kivakit.interfaces.lexakai.DiagramTimePoint;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.time.ZoneId;
import java.util.TimeZone;

/**
 * Interface to an object that has a time zone.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramTimePoint.class)
public interface TimeZoned<TimeSubclass extends PointInUnixEpoch<TimeSubclass, ?>> extends Milliseconds
{
    static ZoneId localTimeZone()
    {
        return ZoneId.systemDefault();
    }

    static ZoneId utc()
    {
        return ZoneId.of("UTC");
    }

    default <ZonedSubclass extends TimeZoned<TimeSubclass>> ZonedSubclass asLocalTime()
    {
        return asZonedTime(localTimeZone());
    }

    /**
     * Returns this point in time in UTC time based on the given time zone
     */
    default TimeSubclass asUtc(ZoneId zone)
    {
        var offset = TimeZone.getTimeZone(zone).getOffset(milliseconds());
        var milliseconds = milliseconds() - offset;
        return newTimeSubclass(milliseconds);
    }

    /**
     * @return This point in time, in UTC time
     */
    default TimeSubclass asUtc()
    {
        return asUtc(timeZone());
    }

    /**
     * Returns this point in time in the given time zone
     */
    @SuppressWarnings("unchecked")
    default <ZonedSubclass extends TimeZoned<TimeSubclass>> ZonedSubclass asZonedTime(ZoneId zone)
    {
        if (hasTimeZone())
        {
            return (ZonedSubclass) this;
        }
        else
        {
            return newZonedTime(timeZone(), milliseconds());
        }
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

    TimeSubclass newTimeSubclass(long milliseconds);

    default <ZonedSubclass extends TimeZoned<TimeSubclass>> ZonedSubclass newZonedTime(ZoneId zone,
                                                                                       long epochMilliseconds)
    {
        throw new UnsupportedOperationException();
    }

    default ZoneId timeZone()
    {
        return utc();
    }
}
