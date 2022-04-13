package com.telenav.kivakit.core.time;

import com.telenav.kivakit.core.test.CoreUnitTest;
import org.junit.Test;

import static com.telenav.kivakit.core.time.Duration.minutes;
import static com.telenav.kivakit.core.time.Duration.seconds;
import static com.telenav.kivakit.core.time.Minute.minute;
import static com.telenav.kivakit.core.time.Second.second;
import static com.telenav.kivakit.core.time.Time.time;

/**
 * Tests for {@link Minute}
 *
 * @author jonathanl (shibo)
 */
public class MinuteTest extends CoreUnitTest
{
    @Test
    public void testArithmetic()
    {
        ensureEqual(minute(0).decremented(), minute(59));
        ensureEqual(minute(5).decremented(), minute(4));
        ensureEqual(minute(5).incremented(), minute(6));
        ensureEqual(minute(59).incremented(), minute(0));
        ensureEqual(minute(5).plus(seconds(60)), time(minute(6), second(0)));
        ensureEqual(minute(5).minus(seconds(60)), minute(4));
    }

    @Test
    public void testConversion()
    {
        ensureEqual(minute(5), minute(5));
        ensureEqual(minute(5).asMilliseconds(), 60 * 5 * 1000);
        ensureEqual(minute(5).asUnits(), 5);
        ensureEqual(minute(5).difference(minute(2)), minutes(3));
        ensureEqual(minute(5).minute(), 5);
        ensureEqual(minute(5).asDuration(), minutes(5));
    }

    @Test
    public void testCreation()
    {
        ensureThrows(() -> minute(-1));
        ensureEqual(minute(5).minute(), 5);
        ensureEqual(minute(5).milliseconds(), 60 * 5 * 1000L);
    }

    @Test
    public void testDifference()
    {
        ensureEqual(minute(3).difference(minute(7)), minutes(4));
        ensureEqual(minute(7).difference(minute(3)), minutes(4));
    }

    @Test
    public void testEquals()
    {
        ensureEqual(minute(59), minute(58).incremented());
        ensureEqual(minute(10), minute(5).plus(minutes(5)));
    }

    @Test
    public void testMath()
    {
        ensureEqual(minute(4).durationBefore(minute(5)), minutes(1));
        ensureThrows(() -> minute(4).roundUp(minutes(1)));
        ensureThrows(() -> minute(4).roundDown(minutes(1)));
        ensureEqual(minute(4).until(minute(12)), minutes(8));
    }

    @Test
    public void testQuantum()
    {
        ensureEqual(minute(4).quantum(), 240_000);
        ensure(minute(4).isNonZero());
        ensure(!minute(4).isZero());
        ensure(minute(0).isZero());
        ensure(!minute(0).isNonZero());
    }

    @Test
    public void testQuantumComparable()
    {
        ensure(minute(5).isApproximately(minute(4), minute(2)));
        ensure(minute(5).isBetweenExclusive(minute(0), minute(6)));
        ensure(minute(5).isBetweenInclusive(minute(0), minute(5)));
        ensure(!minute(7).isBetweenExclusive(minute(0), minute(6)));
        ensure(!minute(7).isBetweenInclusive(minute(0), minute(5)));
        ensure(minute(5).isGreaterThan(minute(4)));
        ensure(minute(5).isGreaterThanOrEqualTo(minute(5)));
        ensure(minute(5).isLessThan(minute(6)));
        ensure(minute(6).isLessThanOrEqualTo(minute(6)));
        ensure(minute(5).isNonZero());
        ensure(minute(0).isZero());
    }

    @Test
    public void testRange()
    {
        ensure(!minute(5).isMaximum());
        ensure(minute(59).isMaximum());
        ensure(minute(0).isMinimum());
        ensure(!minute(1).isMinimum());

        ensureEqual(minute(5).minimum(), minute(0));
        ensureEqual(minute(5).minimum(minute(6)), minute(5));
        ensureEqual(minute(5).maximum(), minute(59));
        ensureEqual(minute(5).maximum(minute(6)), minute(6));
        ensureEqual(minute(5).maximum(), minute(59));
    }

    @Test
    public void testTemporalComparison()
    {
        ensure(minute(3).isBefore(minute(6)));
        ensure(minute(6).isAfter(minute(3)));
        ensure(minute(6).isAtOrAfter(minute(3)));
        ensure(minute(6).isAtOrAfter(minute(6)));
        ensure(minute(6).isAtOrBefore(minute(6)));
        ensure(minute(6).isAtOrBefore(minute(7)));
    }

    @Test
    public void testUnits()
    {
        ensureEqual(minute(5).millisecondsPerUnit(), 60 * 1_000);
        ensureEqual(minute(5).asUnits(), 5);
        ensureEqual(minute(5).minusUnits(1), minute(4));
        ensureEqual(minute(5).plusUnits(1), minute(6));
        ensureEqual(minute(5).next(), minute(6));
        ensureEqual(minute(59).next(), minute(0));
    }
}
