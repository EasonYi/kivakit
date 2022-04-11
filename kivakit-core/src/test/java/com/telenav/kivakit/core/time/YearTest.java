package com.telenav.kivakit.core.time;

import com.telenav.kivakit.core.test.CoreUnitTest;
import org.junit.Test;

import java.util.HashMap;

import static com.telenav.kivakit.core.time.Day.dayOfWeek;
import static com.telenav.kivakit.core.time.Day.days;
import static com.telenav.kivakit.core.time.DayOfWeek.Standard.ISO;
import static com.telenav.kivakit.core.time.DayOfWeek.Standard.JAVA;
import static com.telenav.kivakit.core.value.count.Count._0;
import static com.telenav.kivakit.core.value.count.Count._1;
import static com.telenav.kivakit.core.value.count.Count._6;
import static com.telenav.kivakit.core.value.count.Count._7;
import static com.telenav.kivakit.core.value.count.Range.rangeInclusive;

public class YearTest extends CoreUnitTest
{
    @Test
    public void test()
    {
    }

    @Test
    public void testEquals()
    {
        ensureEqual(days(49), (days(49)));
        ensureEqual(dayOfWeek(4, JAVA), (dayOfWeek(4, JAVA)));
        ensureNotEqual(dayOfWeek(4, JAVA), (days(4)));
        ensureNotEqual(dayOfWeek(4, ISO), (days(4)));

        var map = new HashMap<Day, Integer>();

        rangeInclusive(_1, _7).forEachInt(at -> map.put(dayOfWeek(at, JAVA), at));
        rangeInclusive(_1, _7).forEachInt(at -> ensureEqual(map.get(dayOfWeek(at, JAVA)), at));

        rangeInclusive(_0, _6).forEachInt(at -> map.put(dayOfWeek(at, ISO), at));
        rangeInclusive(_0, _6).forEachInt(at -> ensureEqual(map.get(dayOfWeek(at, ISO)), at));
    }
}
