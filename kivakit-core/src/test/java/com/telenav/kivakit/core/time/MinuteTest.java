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
        ensureEqual(minute(5).plus(seconds(60)), time(minute(6), second(0)));
        ensureEqual(minute(5).minus(seconds(60)), minute(4));
    }

    @Test
    public void testComparison()
    {
        ensureEqual(minute(5).minimum(), minute(0));
        ensureEqual(minute(5).minimum(minute(6)), minute(5));
        ensureEqual(minute(5).maximum(), minute(59));
        ensureEqual(minute(5).maximum(minute(6)), minute(6));
        ensureEqual(minute(5).maximum(), minute(59));

        ensure(minute(5).isApproximately(minute(4), minute(2)));
        ensure(minute(5).isBetweenExclusive(minute(0), minute(6)));
        ensure(minute(5).isBetweenInclusive(minute(0), minute(5)));
        ensure(!minute(7).isBetweenExclusive(minute(0), minute(6)));
        ensure(!minute(7).isBetweenInclusive(minute(0), minute(5)));
        ensure(minute(5).isGreaterThan(minute(4)));
        ensure(minute(5).isGreaterThanOrEqualTo(minute(5)));
        ensure(minute(5).isLessThan(minute(6)));
        ensure(minute(6).isLessThanOrEqualTo(minute(6)));
        ensure(!minute(5).isMaximum());
        ensure(minute(59).isMaximum());
        ensure(minute(0).isMinimum());
        ensure(!minute(1).isMinimum());
        ensure(minute(5).isNonZero());
        ensure(minute(0).isZero());
    }

    @Test
    public void testConversion()
    {
        ensureEqual(minute(5), minute(5));
        ensureEqual(minute(5).asMilliseconds(), 60 * 5 * 1000);
        ensureEqual(minute(5).asUnits(), 5);
        ensureEqual(minute(5).difference(minute(2)), minutes(3));
        ensureEqual(minute(5).minute(), 5);
    }

    @Test
    public void testCreation()
    {
        ensureThrows(() -> minute(-1));
        ensureEqual(minute(5).minute(), 5);
        ensureEqual(minute(5).milliseconds(), 60 * 5 * 1000L);
    }

    @Test
    public void testEquals()
    {
        ensureEqual(minute(59), minute(58).incremented());
        ensureEqual(minute(10), minute(5).plus(minutes(5)));
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
