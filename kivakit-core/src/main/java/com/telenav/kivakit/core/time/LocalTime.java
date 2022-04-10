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

import com.telenav.kivakit.core.lexakai.DiagramTime;
import com.telenav.kivakit.interfaces.time.PointInTime;
import com.telenav.kivakit.interfaces.time.TimeZoned;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Objects;

import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static com.telenav.kivakit.core.time.DayOfWeek.javaDayOfWeek;
import static com.telenav.kivakit.core.time.Duration.ONE_HOUR;
import static com.telenav.kivakit.core.time.Hour.militaryHour;
import static com.telenav.kivakit.core.time.Minute.minutes;
import static com.telenav.kivakit.core.time.Second.seconds;
import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.DAY_OF_WEEK;
import static java.time.temporal.ChronoField.DAY_OF_YEAR;
import static java.time.temporal.ChronoField.EPOCH_DAY;
import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;

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
@UmlClassDiagram(diagram = DiagramTime.class)
public class LocalTime extends BaseTime<LocalTime, Duration> implements TimeZoned<LocalTime>
{
    /**
     * Retrieves a <code>Time</code> instance based on the given milliseconds.
     *
     * @param milliseconds the <code>Time</code> value in milliseconds since START_OF_UNIX_TIME
     * @return a corresponding immutable <code>Time</code> object
     */
    public static LocalTime epochMilliseconds(ZoneId zone, long milliseconds)
    {
        return new LocalTime(zone, milliseconds);
    }

    /**
     * Retrieves a <code>Time</code> instance based on the given nanoseconds.
     *
     * @param nanoseconds the <code>Time</code> value in nanoseconds since START_OF_UNIX_TIME
     * @return a corresponding immutable <code>Time</code> object
     */
    public static LocalTime epochNanoseconds(ZoneId zone, long nanoseconds)
    {
        return new LocalTime(zone, nanoseconds / 1_000_000);
    }

    /**
     * @return A <code>Time</code> object representing the given number of seconds since START_OF_UNIX_TIME
     */
    public static LocalTime epochSeconds(ZoneId zone, double seconds)
    {
        return epochMilliseconds(zone, (long) (seconds * 1000));
    }

    public static LocalTime localTime(ZoneId zone, LocalDateTime dateTime)
    {
        return epochMilliseconds(zone, dateTime
                .atZone(zone)
                .toInstant()
                .toEpochMilli());
    }

    public static LocalTime localTime(ZoneId zone, Year year, Month month, Day dayOfMonth, Hour hour)
    {
        return localTime(zone, year, month, dayOfMonth, hour, minutes(0), seconds(0));
    }

    public static LocalTime localTime(ZoneId zone, Year year, Month month, Day dayOfMonth)
    {
        return localTime(zone, year, month, dayOfMonth, militaryHour(0));
    }

    public static LocalTime localTime(ZoneId zone, Year year, Month month)
    {
        return localTime(zone, year, month, Day.dayOfMonth(1), militaryHour(0));
    }

    public static LocalTime localTime(ZoneId zone,
                                      Year year,
                                      Month month,
                                      Day dayOfMonth,
                                      Hour hour,
                                      Minute minute,
                                      Second second)
    {
        return localTime(zone, LocalDateTime.of((int) year.asUnits(),
                month.monthOfYear(),
                (int) dayOfMonth.asUnits(),
                hour.asMilitaryHour(),
                (int) minute.asUnits(),
                (int) second.asUnits()));
    }

    /**
     * @return The local time zone, as defined by {@link ZoneId#systemDefault()}
     */
    public static ZoneId localTimeZone()
    {
        return ZoneId.systemDefault();
    }

    /**
     * Retrieves a <code>Time</code> instance based on the current time.
     *
     * @return the current <code>Time</code>
     */
    public static LocalTime now(ZoneId zone)
    {
        return epochMilliseconds(zone, System.currentTimeMillis());
    }

    /**
     * @return The current time in the local timezone, as defined by #localTimeZone
     */
    public static LocalTime nowInLocalTime()
    {
        return now(localTimeZone());
    }

    private ZoneId zone;

    /**
     * Private constructor forces use of static factory methods.
     *
     * @param milliseconds the <code>Time</code> value in milliseconds since START_OF_UNIX_TIME
     */
    protected LocalTime(final ZoneId zone, long milliseconds)
    {
        super(milliseconds);

        this.zone = zone;
    }

    protected LocalTime()
    {
    }

    public String asDateString()
    {
        return asDateString(timeZone());
    }

    public String asDateString(ZoneId zone)
    {
        return TimeFormats.KIVAKIT_DATE.format(asInstant()) + "_" + TimeZones.shortDisplayName(zone);
    }

    public String asDateTimeString()
    {
        return asDateTimeString(timeZone());
    }

    public String asDateTimeString(ZoneId zone)
    {
        return TimeFormats.KIVAKIT_DATE_TIME
                .withZone(zone)
                .format(asInstant()) + "_" + TimeZones.shortDisplayName(zone);
    }

    public Time asTime()
    {
        return Time.epochMilliseconds(asUtc().epochMilliseconds());
    }

    public String asTimeString()
    {
        return asTimeString(timeZone());
    }

    public String asTimeString(ZoneId zone)
    {
        return TimeFormats.KIVAKIT_TIME.format(asInstant()) + "_" + TimeZones.shortDisplayName(zone);
    }

    @Override
    public LocalTime asUtc()
    {
        return super.asUtc();
    }

    public Quarter calendarQuarter()
    {
        return month().calendarQuarter();
    }

    /**
     * @return The day of year from 0-365 (or 366 in leap years)
     */
    public Day dayOfMonth()
    {
        return Day.dayOfMonth(javaLocalDateTime().getDayOfMonth());
    }

    public Day dayOfUnixEpoch()
    {
        return Day.dayOfUnixEpoch((int) javaLocalDateTime().getLong(EPOCH_DAY));
    }

    /**
     * @return The day of week from 0-6
     */
    public DayOfWeek dayOfWeek()
    {
        return javaDayOfWeek(javaLocalDateTime().getDayOfWeek());
    }

    /**
     * @return The day of year from 0-365 (or 366 in leap years)
     */
    public Day dayOfYear()
    {
        return Day.dayOfYear(javaLocalDateTime().getDayOfYear());
    }

    @Override
    public Duration elapsedSince(PointInTime<?, ?> thatTime)
    {
        return (Duration) thatTime.asUtc().minus(asUtc());
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof PointInTime)
        {
            var that = (PointInTime<?, ?>) object;
            return asUtc().epochMilliseconds() == that.asUtc().epochMilliseconds();
        }
        return false;
    }

    public Quarter fiscalQuarter()
    {
        return month().fiscalQuarter();
    }

    @Override
    public boolean hasTimeZone()
    {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return Objects.hashCode(asUtc().epochMilliseconds());
    }

    /**
     * @return The hour of day from 0 to 23
     */
    public Hour hourOfDay()
    {
        return militaryHour(javaLocalDateTime().get(HOUR_OF_DAY));
    }

    public HourOfWeek hourOfWeek()
    {
        return HourOfWeek.hourOfWeek(dayOfWeek().asIso() * 24 + hourOfDay().asMilitaryHour());
    }

    public LocalDateTime javaLocalDateTime()
    {
        return LocalDateTime.ofInstant(asInstant(), timeZone());
    }

    public java.time.LocalTime javaLocalTime()
    {
        return javaLocalDateTime().toLocalTime();
    }

    public ZonedDateTime javaZonedDate()
    {
        return ZonedDateTime.ofInstant(asInstant(), timeZone());
    }

    public ZonedDateTime javaZonedDateTime(ZoneOffset offset)
    {
        return ZonedDateTime.ofInstant(asInstant(), ZoneId.ofOffset("", offset));
    }

    @Override
    public LocalTime maximum()
    {
        return newPointInTime(Integer.MAX_VALUE);
    }

    public Meridiem meridiem()
    {
        return hourOfDay().meridiem();
    }

    @Override
    public long millisecondsPerUnit()
    {
        return 1;
    }

    @Override
    public LocalTime minimum()
    {
        return newPointInTime(0);
    }

    /**
     * @return The minute from 0-59
     */
    public Minute minute()
    {
        return minuteOfHour();
    }

    /**
     * @return The minute of the day from 0-1439
     */
    public int minuteOfDay()
    {
        return javaLocalDateTime().get(MINUTE_OF_DAY);
    }

    public Minute minuteOfHour()
    {
        return minutes(javaLocalDateTime().get(MINUTE_OF_HOUR));
    }

    public Month month()
    {
        return Month.monthOfYear(javaLocalDateTime().getMonthValue());
    }

    @Override
    public Duration newLengthOfTime(long epochMilliseconds)
    {
        return Duration.duration(epochMilliseconds);
    }

    @Override
    public LocalTime newPointInTime(long epochMilliseconds)
    {
        return epochMilliseconds(timeZone(), epochMilliseconds);
    }

    @Override
    public LocalTime newTimeUnitedInstance(long milliseconds)
    {
        return epochMilliseconds(timeZone(), milliseconds);
    }

    @Override
    public LocalTime newZonedPointInTime(ZoneId zone, long epochMilliseconds)
    {
        return epochMilliseconds(zone, epochMilliseconds);
    }

    @Override
    public LocalTime startOfDay()
    {
        return epochSeconds(timeZone(), javaZonedDate()
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0)
                .toEpochSecond());
    }

    public LocalTime startOfHour()
    {
        return epochSeconds(timeZone(), javaZonedDate()
                .withMinute(0)
                .withSecond(0)
                .withNano(0)
                .toEpochSecond());
    }

    public LocalTime startOfNextHour()
    {
        return nowInLocalTime().startOfHour().plus(ONE_HOUR);
    }

    public LocalTime startOfTomorrow()
    {
        return startOfDay().plus(Duration.ONE_DAY);
    }

    @Override
    public ZoneId timeZone()
    {
        return zone;
    }

    @Override
    public String toString()
    {
        return year()
                + "." + String.format("%02d", month().monthOfYear())
                + "." + String.format("%02d", dayOfMonth().asUnits())
                + "_" + String.format("%02d", hourOfDay().asMeridiemHour())
                + "." + String.format("%02d", minute().asUnits())
                + meridiem()
                + "_" + TimeZones.shortDisplayName(timeZone());
    }

    /**
     * @return The week of year in 0-51-52 format. NOTE: Java week of year starts at 1.
     */
    public int weekOfYear()
    {
        return javaLocalDateTime().getDayOfYear() / 7;
    }

    /**
     * @return This local time on the given day
     */
    public LocalTime withDay(Day day)
    {
        switch (day.type())
        {
            case DAY_OF_WEEK:
                return withDayOfWeek(day.asDayOfWeek());

            case DAY_OF_MONTH:
                return localTime(timeZone(), javaLocalDateTime().with(DAY_OF_MONTH, day.asUnits()));

            case DAY_OF_YEAR:
                return localTime(timeZone(), javaLocalDateTime().with(DAY_OF_YEAR, day.asUnits()));

            case DAY_OF_UNIX_EPOCH:
                return localTime(timeZone(), javaLocalDateTime().with(EPOCH_DAY, day.asUnits()));

            case DAY:
            default:
                return unsupported();
        }
    }

    public LocalTime withDayOfWeek(DayOfWeek day)
    {
        return localTime(timeZone(), javaLocalDateTime().with(DAY_OF_WEEK, day.asJava()));
    }

    public LocalTime withHourOfDay(Hour hour)
    {
        return localTime(timeZone(), javaLocalDateTime().with(HOUR_OF_DAY, hour.asMilitaryHour()));
    }

    public LocalTime withMinute(Minute minute)
    {
        return localTime(timeZone(), javaLocalDateTime().with(MINUTE_OF_HOUR, minute.asUnits()));
    }

    public LocalTime withUnixEpochDay(Day day)
    {
        return localTime(timeZone(), javaLocalDateTime().with(EPOCH_DAY, day.asUnits()));
    }

    public Year year()
    {
        return Year.year(javaLocalDateTime().getYear());
    }
}
