package com.telenav.kivakit.core.time;
import com.telenav.kivakit.core.test.CoreUnitTest;
import org.junit.Test;

import java.time.ZoneId;

import static com.telenav.kivakit.core.time.DayOfWeek.FRIDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.MONDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.SATURDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.SUNDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.TUESDAY;
import static com.telenav.kivakit.core.time.DayOfWeek.WEDNESDAY;
import static com.telenav.kivakit.core.time.HourOfDay.hourOfDay;
import static com.telenav.kivakit.core.time.HourOfWeek.hourOfWeek;
import static com.telenav.kivakit.core.value.count.Count._100;
import static com.telenav.kivakit.core.value.count.Count._2;

/**
 * Unit test for {@link HourOfWeek}
 *
 * @author jonathanl (shibo)
 */
public class HourOfWeekTest extends CoreUnitTest
{
    @Test
    public void testConversions()
    {
        // For all days of the week,
        random().rangeInclusive(MONDAY, SUNDAY, 0).forEach(dayOfWeek ->
        {
            // and all hours of the day,
            random().rangeExclusive(hourOfDay(0), hourOfDay(24)).forEach(hourOfDay ->
            {
                // create an HourOfWeek,
                var hourOfWeek = hourOfWeek(dayOfWeek, hourOfDay);

                // and ensure that it can be converted back to each original value.
                ensure(hourOfWeek.dayOfWeek().equals(dayOfWeek));
                ensure(hourOfWeek.hourOfDay().equals(hourOfDay));
            });
        });

        // For all hours of the week from 0 to 167,
        random().rangeExclusive(0, 7 * 24).forEach(at ->
        {
            // create an HourOfWeek for that hour,
            var hourOfWeek = hourOfWeek(at);

            // and ensure that it can be converted to the right day of the week, and hour of the day.
            ensure(hourOfWeek.hourOfDay().asInt() == at.asInt() % 24);
            ensure(hourOfWeek.dayOfWeek().asIso() == at.asInt() / 24);
        });
    }

    @Test
    public void testCreate()
    {
        // Check valid construction
        ensure(hourOfWeek(5).asInt() == 5);
        ensure(hourOfWeek(_2).asInt() == 2);

        _100.loop(() ->
        {
            var range = random().rangeExclusive(0, 7 * 24);
            ensure(range.isExclusive());
            ensure(range.size() <= 168);
            ensure(range.exclusiveMaximum().asInt() <= 168);
            range.forEach(HourOfWeek::hourOfWeek);
            range.forEach(at -> hourOfWeek(at.asInt()));
        });

        // Check invalid construction
        ensureThrows(() -> hourOfWeek(-1));
        ensureThrows(() -> hourOfWeek(7 * 24));
    }

    @Test
    public void testLocalToUtc()
    {
        {
            // Tuesday at 9am in zone +04:00 is Tuesday at 5am UTC.
            var utc = hourOfWeek(TUESDAY, hourOfDay(9))
                    .asUtc(ZoneId.of("+04:00"));
            ensureEqual(utc.dayOfWeek(), TUESDAY);
            ensureEqual(utc.hourOfDay(), hourOfDay(5));
        }

        {
            // Tuesday at 9pm in zone -04:00 is Wednesday at 1am UTC.
            var utc = hourOfWeek(TUESDAY, hourOfDay(12 + 9))
                    .asUtc(ZoneId.of("-04:00"));
            ensureEqual(utc.dayOfWeek(), WEDNESDAY);
            ensureEqual(utc.hourOfDay(), hourOfDay(1));
        }
    }

    @Test
    public void testLocalToUtcWrapAround()
    {
        {
            // Wednesday at 9am in zone +10:00 is Tuesday at 11pm UTC.
            var utc = hourOfWeek(WEDNESDAY, hourOfDay(9))
                    .asUtc(ZoneId.of("+10:00"));
            ensureEqual(utc.dayOfWeek(), TUESDAY);
            ensureEqual(utc.hourOfDay(), hourOfDay(12 + 11));
        }

        {
            // Tuesday at 6pm in zone -10:00 is Wednesday at 4am UTC.
            var utc = hourOfWeek(TUESDAY, hourOfDay(12 + 6))
                    .asUtc(ZoneId.of("-10:00"));
            ensureEqual(utc.dayOfWeek(), WEDNESDAY);
            ensureEqual(utc.hourOfDay(), hourOfDay(4));
        }
    }

    @Test
    public void testMaximum()
    {
        ensure(hourOfWeek(0).maximum().asInt() == 167);
    }

    @Test
    public void testMinimum()
    {
        ensure(hourOfWeek(0).minimum().asInt() == 0);
    }

    @Test
    public void testMinus()
    {
        ensureEqual(hourOfWeek(7).minus(24).dayOfWeek(), SUNDAY);
        ensureEqual(hourOfWeek(24 * 6).minus(7).dayOfWeek(), SATURDAY);
        ensureEqual(hourOfWeek(24 * 6).minus(24 + 7).dayOfWeek(), FRIDAY);
    }

    @Test
    public void testPlus()
    {
        ensureEqual(hourOfWeek(7).plus(24).dayOfWeek(), TUESDAY);
        ensureEqual(hourOfWeek(24 * 6).plus(7).dayOfWeek(), SUNDAY);
        ensureEqual(hourOfWeek(24 * 6).plus(24 + 7).dayOfWeek(), MONDAY);
    }

    @Test
    public void testUtcToLocal()
    {
        {
            //  Tuesday at 9am in time zone -07:00, is TUESDAY at 2am,
            var local = hourOfWeek(TUESDAY, hourOfDay(9))
                    .asLocalTime(ZoneId.of("-07:00"));
            ensureEqual(local.dayOfWeek(), TUESDAY);
            ensureEqual(local.hourOfDay(), hourOfDay(2));
        }

        {
            // Tuesday at 9am in time zone -10:00, is Monday at 11pm.
            var local = hourOfWeek(TUESDAY, hourOfDay(9))
                    .asLocalTime(ZoneId.of("-10:00"));
            ensureEqual(local.dayOfWeek(), MONDAY);
            ensureEqual(local.hourOfDay(), hourOfDay(12 + 11));
        }
    }

    @Test
    public void testUtcToLocalWrapAround()
    {
        {
            // Monday at 9am in time zone -10:00, is Sunday at 11pm.
            var local = hourOfWeek(MONDAY, hourOfDay(9))
                    .asLocalTime(ZoneId.of("-10:00"));
            ensureEqual(local.dayOfWeek(), SUNDAY);
            ensureEqual(local.hourOfDay(), hourOfDay(23));
        }

        {
            // Sunday at 9pm in time zone 6:00, is MONDAY at 3am.
            var local = hourOfWeek(SUNDAY, hourOfDay(12 + 9))
                    .asLocalTime(ZoneId.of("+06:00"));
            ensureEqual(local.dayOfWeek(), MONDAY);
            ensureEqual(local.hourOfDay(), hourOfDay(3));
        }
    }
}
