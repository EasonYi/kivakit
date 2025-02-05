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
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Objects;

import static com.telenav.kivakit.core.time.DayOfWeek.javaDayOfWeek;
import static java.time.temporal.ChronoField.AMPM_OF_DAY;
import static java.time.temporal.ChronoField.DAY_OF_WEEK;
import static java.time.temporal.ChronoField.DAY_OF_YEAR;
import static java.time.temporal.ChronoField.EPOCH_DAY;
import static java.time.temporal.ChronoField.HOUR_OF_AMPM;
import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;

/**
 * Snapshot of local time at a specific timezone
 */
@UmlClassDiagram(diagram = DiagramTime.class)
public class LocalTime extends Time
{
    public static LocalTime from(ZoneId zone, LocalDateTime dateTime)
    {
        var milliseconds = dateTime.atZone(zone).toInstant().toEpochMilli();
        return of(zone, milliseconds(milliseconds));
    }

    public static ZoneId localTimeZone()
    {
        return ZoneId.systemDefault();
    }

    public static LocalTime milliseconds(ZoneId zone, long milliseconds)
    {
        return new LocalTime(zone, milliseconds);
    }

    public static LocalTime now()
    {
        return Time.now().localTime();
    }

    public static LocalTime of(ZoneId zone, LocalDateTime time)
    {
        var zoned = time.atZone(zone);
        return milliseconds(zone, zoned.toInstant().toEpochMilli());
    }

    public static LocalTime of(ZoneId zone, Time time)
    {
        return milliseconds(zone, time.asMilliseconds());
    }

    public static LocalTime of(ZoneId zone, int year, int month, int dayOfMonth,
                               int hour, int minute, int second, Meridiem meridiem)
    {
        return of(zone, LocalDateTime.of(year, month, dayOfMonth,
                meridiem == Meridiem.AM ? hour : hour + 12, minute, second));
    }

    public static LocalTime seconds(ZoneId zone, long seconds)
    {
        return new LocalTime(zone, seconds * 1_000);
    }

    public static ZoneId utcTimeZone()
    {
        return ZoneId.of("UTC");
    }

    private ZoneId timeZone;

    protected LocalTime()
    {
    }

    protected LocalTime(ZoneId zone, long milliseconds)
    {
        super(milliseconds);
        timeZone = zone;
    }

    /**
     * Constructor that takes a number of milliseconds since January 1st 1970, and a TimeZone to represent a snapshot of
     * local time at a specific timezone.
     */
    protected LocalTime(ZoneId zone, Time time)
    {
        this(zone, time.asMilliseconds());
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

    public String asTimeString()
    {
        return asTimeString(timeZone());
    }

    public String asTimeString(ZoneId zone)
    {
        return TimeFormats.KIVAKIT_TIME.format(asInstant()) + "_" + TimeZones.shortDisplayName(zone);
    }

    public long asZonedMilliseconds()
    {
        return javaLocalDateTime().atZone(timeZone()).toInstant().toEpochMilli();
    }

    /**
     * @return The day of year from 0-365 (or 366 in leap years)
     */
    public int day()
    {
        return javaLocalDateTime().getDayOfMonth();
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
    public int dayOfYear()
    {
        return javaLocalDateTime().getDayOfYear();
    }

    public int epochDay()
    {
        return (int) javaLocalDateTime().getLong(EPOCH_DAY);
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof LocalTime)
        {
            var that = (LocalTime) object;
            return asZonedMilliseconds() == that.asZonedMilliseconds();
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return Objects.hashCode(asZonedMilliseconds());
    }

    /**
     * @return The hour of day from 0 to 11
     */
    public int hour()
    {
        return javaLocalDateTime().get(HOUR_OF_AMPM);
    }

    /**
     * @return The hour of day from 0 to 23
     */
    public int hourOfDay()
    {
        return javaLocalDateTime().get(HOUR_OF_DAY);
    }

    public int hourOfWeek()
    {
        return dayOfWeek().asIso() * 24 + hourOfDay();
    }

    public LocalDateTime javaLocalDateTime()
    {
        return LocalDateTime.ofInstant(asInstant(), timeZone);
    }

    public java.time.LocalTime javaLocalTime()
    {
        return javaLocalDateTime().toLocalTime();
    }

    public ZonedDateTime javaZonedDate()
    {
        return ZonedDateTime.ofInstant(asInstant(), timeZone);
    }

    public ZonedDateTime javaZonedDateTime(ZoneOffset offset)
    {
        return ZonedDateTime.ofInstant(asInstant(), ZoneId.ofOffset("", offset));
    }

    public Meridiem meridiem()
    {
        return javaLocalDateTime().getHour() > 12 ? Meridiem.PM : Meridiem.AM;
    }

    /**
     * @return The hour of day from 0-12
     */
    public int meridiemHour()
    {
        var hour = hour();
        return hour > 12 ? hour - 12 : hour;
    }

    @Override
    public LocalTime minus(Duration duration)
    {
        return of(timeZone(), super.minus(duration));
    }

    @Override
    public Duration minus(Time time)
    {
        var localTime = milliseconds(timeZone(), super.minus(time).milliseconds());
        return Duration.milliseconds(localTime.asMilliseconds());
    }

    /**
     * @return The minute from 0-59
     */
    public int minute()
    {
        return javaLocalDateTime().getMinute();
    }

    /**
     * @return The minute of the day from 0-1439
     */
    public int minuteOfDay()
    {
        return javaLocalDateTime().get(MINUTE_OF_DAY);
    }

    public int minuteOfHour()
    {
        return javaLocalDateTime().get(MINUTE_OF_HOUR);
    }

    public int month()
    {
        return javaLocalDateTime().getMonthValue();
    }

    @Override
    public LocalTime plus(Duration duration)
    {
        return of(timeZone(), super.plus(duration));
    }

    public int quarter()
    {
        return (month() - 1) / 3 + 1;
    }

    public LocalTime startOfDay()
    {
        return seconds(timeZone(), javaZonedDate()
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0)
                .toEpochSecond());
    }

    public LocalTime startOfHour()
    {
        return seconds(timeZone(), javaZonedDate()
                .withMinute(0)
                .withSecond(0)
                .withNano(0)
                .toEpochSecond());
    }

    public LocalTime startOfNextHour()
    {
        return of(timeZone(), Time.now()).startOfHour().plus(Duration.ONE_HOUR);
    }

    public LocalTime startOfTomorrow()
    {
        return startOfDay().plus(Duration.ONE_DAY);
    }

    public ZoneId timeZone()
    {
        return timeZone;
    }

    @Override
    public String toString()
    {
        return year()
                + "." + String.format("%02d", month())
                + "." + String.format("%02d", day())
                + "_" + String.format("%02d", hour())
                + "." + String.format("%02d", minute())
                + meridiem()
                + "_" + TimeZones.shortDisplayName(timeZone);
    }

    @Override
    public LocalTime utc()
    {
        return milliseconds(ZoneId.of("UTC"), asMilliseconds());
    }

    /**
     * @return The week of year in 0-51-52 format. NOTE: Java week of year starts at 1.
     */
    public int weekOfYear()
    {
        return javaLocalDateTime().getDayOfYear() / 7;
    }

    public LocalTime withDayOfWeek(int day)
    {
        return from(timeZone(), javaLocalDateTime().with(DAY_OF_WEEK, day));
    }

    public LocalTime withDayOfYear(int dayOfYear)
    {
        return from(timeZone(), javaLocalDateTime().with(DAY_OF_YEAR, dayOfYear));
    }

    public LocalTime withEpochDay(int day)
    {
        return from(timeZone(), javaLocalDateTime().with(EPOCH_DAY, day));
    }

    public LocalTime withHourOfDay(int hour)
    {
        return from(timeZone(), javaLocalDateTime().with(HOUR_OF_DAY, hour));
    }

    public LocalTime withHourOfMeridiem(int hour, Meridiem meridiem)
    {
        return from(timeZone(), javaLocalDateTime()
                .with(HOUR_OF_AMPM, hour)
                .with(AMPM_OF_DAY, meridiem.ordinal()));
    }

    public LocalTime withMinuteOfHour(int minute)
    {
        return from(timeZone(), javaLocalDateTime().with(MINUTE_OF_HOUR, minute));
    }

    public int year()
    {
        return javaLocalDateTime().getYear();
    }
}
