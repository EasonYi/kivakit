package com.telenav.kivakit.core.time;

import com.telenav.kivakit.core.test.NoTestRequired;
import com.telenav.kivakit.core.test.Tested;
import com.telenav.kivakit.core.value.count.BaseCount;
import com.telenav.kivakit.interfaces.lexakai.DiagramTimePoint;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.UmlMethodGroup;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import java.time.ZoneId;
import java.util.Objects;
import java.util.TimeZone;

import static com.telenav.kivakit.core.ensure.Ensure.ensureBetweenExclusive;
import static com.telenav.kivakit.core.time.DayOfWeek.isoDayOfWeek;

/**
 * Represents an hour of the week, for example Thursday at 1pm.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings({ "unused" })
@UmlClassDiagram(diagram = DiagramTimePoint.class)
public class HourOfWeek extends BaseTime<HourOfWeek>
{
    private static final long millisecondsPerHour = 60 * 60 * 1_000;

    public static final int hoursPerWeek = 7 * 24;

    /**
     * Returns an {@link HourOfWeek} for the given ordinal value
     *
     * @param militaryHourOfWeek The hour of the week from 0 to 7 * 24 (168), exclusive.
     */
    @Tested
    @UmlMethodGroup("factory")
    public static HourOfWeek hourOfWeek(long militaryHourOfWeek)
    {
        ensureBetweenExclusive(militaryHourOfWeek, 0, 24 * 7, "Hour of week $ is out of range", militaryHourOfWeek);

        var dayOfWeek = militaryHourOfWeek / 24;
        var hourOfDay = militaryHourOfWeek % 24;

        return new HourOfWeek(isoDayOfWeek(dayOfWeek), Hour.militaryHour(hourOfDay));
    }

    /**
     * Returns an {@link HourOfWeek} for the given ordinal value
     *
     * @param hourOfWeek The hour of the week from 0 to 7 * 24 (168), exclusive.
     */
    @Tested
    @UmlMethodGroup("factory")
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
    @UmlMethodGroup("factory")
    public static HourOfWeek hourOfWeek(DayOfWeek dayOfWeek, Hour hourOfDay)
    {
        return hourOfWeek((dayOfWeek.asIso() * 24 + hourOfDay.asUnits()));
    }

    /** The day of the week used to compute the hour of the week */
    private DayOfWeek dayOfWeek;

    /** The hour of the day used to compute the hour of the week */
    private Hour hourOfDay;

    @NoTestRequired
    @UmlExcludeMember
    protected HourOfWeek()
    {
    }

    @NoTestRequired
    @UmlExcludeMember
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

    public HourOfWeek asLocalTime()
    {
        return asZonedTime(ZonedTime.localTimeZone());
    }

    public HourOfWeek asUtc(ZoneId zone)
    {
        return HourOfWeek.hourOfWeek(asHours() - offsetInHours(zone));
    }

    public HourOfWeek asZonedTime(ZoneId zone)
    {
        var modulo = modulo();
        return HourOfWeek.hourOfWeek((asHours() + offsetInHours(zone) + modulo) % modulo);
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
        return hourOfWeek(hoursPerWeek - 1);
    }

    @Override
    public long millisecondsPerUnit()
    {
        return millisecondsPerHour;
    }

    @Override
    public HourOfWeek minimum()
    {
        return hourOfWeek(0);
    }

    @Override
    public HourOfWeek newTimeSubclass(long milliseconds)
    {
        return hourOfWeek(millisecondsToUnits(milliseconds));
    }

    @Override
    @NoTestRequired
    public String toString()
    {
        return dayOfWeek() + " at " + hourOfDay();
    }

    private long offsetInHours(ZoneId zone)
    {
        return TimeZone.getTimeZone(zone).getRawOffset() / millisecondsPerHour;
    }
}
