////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.time;

import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.interfaces.lexakai.DiagramTimePoint;
import com.telenav.kivakit.interfaces.time.PointInTime;
import com.telenav.kivakit.interfaces.time.TimeZoned;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.UmlMethodGroup;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static com.telenav.kivakit.core.time.Hour.militaryHour;
import static com.telenav.kivakit.core.time.TimeFormats.KIVAKIT_DATE;
import static com.telenav.kivakit.core.time.TimeFormats.KIVAKIT_DATE_TIME;
import static com.telenav.kivakit.core.time.TimeFormats.KIVAKIT_TIME;
import static com.telenav.kivakit.interfaces.time.TimeZoned.localTimeZone;

/**
 * An immutable <code>Time</code> class that represents a specific point in UNIX time. The underlying representation is
 * a <code>long</code> value which holds a number of milliseconds since January 1, 1970, 0:00 GMT. To represent a
 * duration of time, such as "6 seconds", use the
 * <code>Duration</code> class. To represent a time period with a start and end time, use the
 * <code>TimeSpan</code> class.
 *
 * @author Jonathan Locke
 * @since 1.2.6
 */
@SuppressWarnings({ "unused", "unchecked" })
@UmlClassDiagram(diagram = DiagramTimePoint.class)
public class Time extends BaseTime<Time, Duration> implements TimeZoned<Time>, Comparable<PointInTime<?, ?>>
{
    /** The beginning of UNIX time: January 1, 1970, 0:00 GMT. */
    public static final Time START_OF_UNIX_EPOCH = epochMilliseconds(0);

    /** The minimum time value is the start of UNIX time */
    public static final Time MINIMUM = START_OF_UNIX_EPOCH;

    /** The end of time */
    public static final Time MAXIMUM = epochMilliseconds(Long.MAX_VALUE);

    /**
     * Retrieves a <code>Time</code> instance based on the given milliseconds.
     *
     * @param milliseconds the <code>Time</code> value in milliseconds since START_OF_UNIX_TIME
     * @return a corresponding immutable <code>Time</code> object
     */
    @UmlMethodGroup("factory")
    public static Time epochMilliseconds(long milliseconds)
    {
        return new Time(milliseconds);
    }

    /**
     * Retrieves a <code>Time</code> instance based on the given nanoseconds.
     *
     * @param nanoseconds the <code>Time</code> value in nanoseconds since START_OF_UNIX_TIME
     * @return a corresponding immutable <code>Time</code> object
     */
    @UmlMethodGroup("factory")
    public static Time epochNanoseconds(long nanoseconds)
    {
        return new Time(nanoseconds / 1_000_000);
    }

    /**
     * @return A <code>Time</code> object representing the given number of seconds since START_OF_UNIX_TIME
     */
    @UmlMethodGroup("factory")
    public static Time epochSeconds(double seconds)
    {
        return epochMilliseconds((long) (seconds * 1000));
    }

    /**
     * Retrieves the current time
     *
     * @return The current time
     */
    @UmlMethodGroup("factory")
    public static Time now()
    {
        return epochMilliseconds(systemClock().millis());
    }

    @UmlMethodGroup("factory")
    public static Time parseKivaKitDate(Listener listener, String text)
    {
        return parseTime(listener, KIVAKIT_DATE, text);
    }

    @UmlMethodGroup("factory")
    public static Time parseKivaKitDateTime(Listener listener, String text)
    {
        return parseTime(listener, KIVAKIT_DATE_TIME, text);
    }

    @UmlMethodGroup("factory")
    public static Time parseKivaKitTime(Listener listener, String text)
    {
        return parseTime(listener, KIVAKIT_TIME, text);
    }

    @UmlMethodGroup("factory")
    public static Time parseTime(Listener listener, DateTimeFormatter formatter, String text)
    {
        try
        {
            return epochMilliseconds(LocalDateTime.parse(text, formatter)
                    .atZone(TimeZoned.utc())
                    .toInstant()
                    .toEpochMilli());
        }
        catch (Exception e)
        {
            listener.problem("Unable to parse time: $", text);
            return null;
        }
    }

    @UmlMethodGroup("factory")
    public static Time time(Year year, Month month, Day dayOfMonth, Hour hour)
    {
        return time(year, month, dayOfMonth, hour, Minute.minutes(0), Second.seconds(0));
    }

    @UmlMethodGroup("factory")
    public static Time time(Year year, Month month, Day dayOfMonth)
    {
        return time(year, month, dayOfMonth, militaryHour(0));
    }

    @UmlMethodGroup("factory")
    public static Time time(Year year, Month month)
    {
        return time(year, month, Day.dayOfMonth(1), militaryHour(0));
    }

    @UmlMethodGroup("factory")
    public static Time time(Year year,
                            Month month,
                            Day dayOfMonth,
                            Hour hour,
                            Minute minute,
                            Second second)
    {
        return ZonedTime.zonedTime(TimeZoned.utc(), year, month, dayOfMonth, hour, minute, second).asTime();
    }

    /**
     * Private constructor forces use of static factory methods.
     *
     * @param milliseconds the <code>Time</code> value in milliseconds since START_OF_UNIX_TIME
     */
    @UmlExcludeMember
    protected Time(long milliseconds)
    {
        super(milliseconds);
    }

    @UmlExcludeMember
    protected Time()
    {
    }

    @UmlMethodGroup("conversion")
    public Duration asDuration()
    {
        return Duration.milliseconds(milliseconds());
    }

    @Override
    @UmlMethodGroup("conversion")
    public ZonedTime asLocalTime()
    {
        return asZonedTime(localTimeZone());
    }

    @Override
    @UmlMethodGroup("conversion")
    public String asString(Format format)
    {
        switch (format)
        {
            case PROGRAMMATIC:
                return Long.toString(milliseconds());

            case DEBUG:
                return asString() + " (" + milliseconds() + ")";

            case TEXT:
            default:
                return asLocalTime().toString();
        }
    }

    @Override
    @UmlMethodGroup("conversion")
    public ZonedTime asZonedTime(ZoneId zone)
    {
        return ZonedTime.epochMilliseconds(zone, epochMilliseconds());
    }

    @Override
    public int compareTo(@NotNull PointInTime<?, ?> that)
    {
        return Long.compare(epochMilliseconds(), that.epochMilliseconds());
    }

    @Override
    public Time maximum()
    {
        return MAXIMUM;
    }

    @Override
    public long millisecondsPerUnit()
    {
        return 1;
    }

    @Override
    public Time minimum()
    {
        return START_OF_UNIX_EPOCH;
    }

    @Override
    @UmlExcludeMember
    public Duration newDuration(long milliseconds)
    {
        return Duration.milliseconds(milliseconds);
    }

    @Override
    @UmlExcludeMember
    public Time newTime(long epochMilliseconds)
    {
        return epochMilliseconds(epochMilliseconds);
    }

    @Override
    @SuppressWarnings("unchecked")
    @UmlExcludeMember
    public ZonedTime newZonedTime(ZoneId zone, long epochMilliseconds)
    {
        return ZonedTime.epochMilliseconds(zone, epochMilliseconds);
    }

    @Override
    public String toString()
    {
        return asString();
    }
}
