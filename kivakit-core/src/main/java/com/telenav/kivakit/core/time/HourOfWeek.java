package com.telenav.kivakit.core.time;

import com.telenav.kivakit.core.test.NoTestRequired;
import com.telenav.kivakit.core.test.Tested;
import com.telenav.kivakit.core.value.count.BaseCount;

import java.time.ZoneId;
import java.util.Objects;

import static com.telenav.kivakit.core.ensure.Ensure.ensureBetweenExclusive;
import static com.telenav.kivakit.core.time.DayOfWeek.isoDayOfWeek;

/**
 * Represents an hour of the week, for example Thursday at 1pm.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
public class HourOfWeek extends BaseTime<HourOfWeek, Duration>
{
    private static final long millisecondsPerHour = 60 * 60 * 1_000;

    /**
     * Returns an {@link HourOfWeek} for the given ordinal value
     *
     * @param hourOfWeek The hour of the week from 0 to 7 * 24 (168), exclusive.
     */
    @Tested
    public static HourOfWeek hourOfWeek(long hourOfWeek)
    {
        ensureBetweenExclusive(hourOfWeek, 0, 24 * 7, "Hour of week $ is out of range", hourOfWeek);

        var dayOfWeek = hourOfWeek / 24;
        var hourOfDay = hourOfWeek % 24;

        return new HourOfWeek(isoDayOfWeek(dayOfWeek), Hour.militaryHour(hourOfDay));
    }

    /**
     * Returns an {@link HourOfWeek} for the given ordinal value
     *
     * @param hourOfWeek The hour of the week from 0 to 7 * 24 (168), exclusive.
     */
    @Tested
    public static HourOfWeek hourOfWeek(BaseCount<?> hourOfWeek)
    {
        return hourOfWeek(hourOfWeek.asInt());
    }

    /**
     * Returns an {@link HourOfWeek} for the given {@link DayOfWeek} and {@link Hour}.
     *
     * @param dayOfWeek The day of the week
     * @param hourOfDay The hour of the day
     * @return The hour of the week
     */
    @Tested
    public static HourOfWeek hourOfWeek(DayOfWeek dayOfWeek, Hour hourOfDay)
    {
        return hourOfWeek((dayOfWeek.asIso() * 24 + hourOfDay.asUnits()));
    }

    /** The day of the week used to compute the hour of the week */
    private DayOfWeek dayOfWeek;

    /** The hour of the day used to compute the hour of the week */
    private Hour hourOfDay;

    @NoTestRequired
    protected HourOfWeek()
    {
    }

    @NoTestRequired
    protected HourOfWeek(DayOfWeek dayOfWeek, Hour hourOfDay)
    {
        super(dayOfWeek.milliseconds() + hourOfDay.milliseconds());

        this.dayOfWeek = dayOfWeek;
        this.hourOfDay = hourOfDay;
    }

    public Time asEpochTime()
    {
        return Time.epochMilliseconds(asMilliseconds());
    }

    public long asHours()
    {
        return asUnits();
    }

    @Override
    @SuppressWarnings("unchecked")
    public HourOfWeek asLocalTime()
    {
        return asLocalTime(LocalTime.localTimeZone());
    }

    @Override
    @SuppressWarnings("unchecked")
    public HourOfWeek asLocalTime(ZoneId zone)
    {
        return LocalTime.epochMilliseconds(zone, milliseconds()).hourOfWeek();
    }

    /**
     * @return The day of the week
     */
    @Tested
    public DayOfWeek dayOfWeek()
    {
        return dayOfWeek;
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof HourOfWeek)
        {
            HourOfWeek that = (HourOfWeek) object;
            return this.dayOfWeek().equals(that.dayOfWeek())
                    && this.hourOfDay().equals(that.hourOfDay());
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(dayOfWeek(), hourOfDay());
    }

    /**
     * @return The hour of the day
     */
    @Tested
    public Hour hourOfDay()
    {
        return hourOfDay;
    }

    @Override
    @Tested
    public HourOfWeek maximum()
    {
        return hourOfWeek(7 * 24 - 1);
    }

    @Override
    public long millisecondsPerUnit()
    {
        return millisecondsPerHour;
    }

    @Override
    public Duration newLengthOfTime(long milliseconds)
    {
        return Duration.duration(milliseconds);
    }

    @Override
    public HourOfWeek newPointInTime(long epochMilliseconds)
    {
        return hourOfWeek(epochMilliseconds);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NoTestRequired
    public HourOfWeek newTimeUnitedInstance(long count)
    {
        return hourOfWeek((int) count);
    }

    @Override
    @NoTestRequired
    public String toString()
    {
        return dayOfWeek() + " at " + hourOfDay();
    }
}
