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

package com.telenav.kivakit.kernel.language.values.count;

import com.telenav.kivakit.kernel.language.strings.Strings;
import com.telenav.kivakit.kernel.language.strings.conversion.AsString;
import com.telenav.kivakit.kernel.language.values.level.Percent;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageValue;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Collection;
import java.util.Iterator;

/**
 * A count value that is an estimate. This class mainly exists to clarify APIs by giving a meaning to the count value.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageValue.class)
public class Estimate extends Count implements AsString
{
    public static final Estimate _0 = new Estimate(0);

    public static final Estimate _1 = new Estimate(1);

    public static final Estimate _2 = new Estimate(2);

    public static final Estimate _3 = new Estimate(3);

    public static final Estimate _4 = new Estimate(4);

    public static final Estimate _5 = new Estimate(5);

    public static final Estimate _6 = new Estimate(6);

    public static final Estimate _7 = new Estimate(7);

    public static final Estimate _8 = new Estimate(8);

    public static final Estimate _9 = new Estimate(9);

    public static final Estimate _10 = new Estimate(10);

    public static final Estimate _11 = new Estimate(11);

    public static final Estimate _12 = new Estimate(12);

    public static final Estimate _13 = new Estimate(13);

    public static final Estimate _14 = new Estimate(14);

    public static final Estimate _15 = new Estimate(15);

    public static final Estimate _16 = new Estimate(16);

    public static final Estimate _17 = new Estimate(17);

    public static final Estimate _18 = new Estimate(18);

    public static final Estimate _19 = new Estimate(19);

    public static final Estimate _20 = new Estimate(20);

    public static final Estimate _21 = new Estimate(21);

    public static final Estimate _22 = new Estimate(22);

    public static final Estimate _23 = new Estimate(23);

    public static final Estimate _24 = new Estimate(24);

    public static final Estimate _25 = new Estimate(25);

    public static final Estimate _26 = new Estimate(26);

    public static final Estimate _27 = new Estimate(27);

    public static final Estimate _28 = new Estimate(28);

    public static final Estimate _29 = new Estimate(29);

    public static final Estimate _30 = new Estimate(30);

    public static final Estimate _31 = new Estimate(31);

    public static final Estimate _32 = new Estimate(32);

    public static final Estimate _64 = new Estimate(64);

    public static final Estimate _128 = new Estimate(128);

    public static final Estimate _256 = new Estimate(256);

    public static final Estimate _512 = new Estimate(512);

    public static final Estimate _1024 = new Estimate(1024);

    public static final Estimate _2048 = new Estimate(2048);

    public static final Estimate _4096 = new Estimate(4096);

    public static final Estimate _8192 = new Estimate(8192);

    public static final Estimate _16384 = new Estimate(16384);

    public static final Estimate _32768 = new Estimate(32678);

    public static final Estimate _65536 = new Estimate(65536);

    public static final Estimate _131_072 = new Estimate(131_072);

    public static final Estimate _262_144 = new Estimate(262_144);

    public static final Estimate _524_288 = new Estimate(524_288);

    public static final Estimate _1_048_576 = new Estimate(1_048_576);

    public static final Estimate _100 = new Estimate(100);

    public static final Estimate _1_000 = new Estimate(1_000);

    public static final Estimate _10_000 = new Estimate(10_000);

    public static final Estimate _100_000 = new Estimate(100_000);

    public static final Estimate _1_000_000 = new Estimate(1_000_000);

    public static final Estimate _10_000_000 = new Estimate(10_000_000);

    public static final Estimate _100_000_000 = new Estimate(100_000_000);

    public static final Estimate _1_000_000_000 = new Estimate(1_000_000_000);

    public static final Estimate MAXIMUM = new Estimate(Long.MAX_VALUE);

    private static final int CACHE_SIZE = 8_192;

    private static final Estimate[] CACHED;

    private static final Estimate[] CACHED_POWERS_OF_TWO;

    static
    {
        CACHED = new Estimate[CACHE_SIZE];
        for (var i = 0; i < CACHED.length; i++)
        {
            CACHED[i] = new Estimate(i);
        }

        CACHED_POWERS_OF_TWO = new Estimate[63];
        for (var i = 0; i < CACHED_POWERS_OF_TWO.length; i++)
        {
            CACHED_POWERS_OF_TWO[i] = new Estimate(1L << i);
        }
    }

    public static Estimate estimate(final Collection<?> collection)
    {
        return estimate(collection.size());
    }

    public static Estimate estimate(final Iterable<?> iterable)
    {
        return estimate(iterable.iterator());
    }

    public static Estimate estimate(final Iterator<?> iterator)
    {
        var count = 0L;
        while (iterator.hasNext())
        {
            iterator.next();
            count++;
        }
        return estimate(count);
    }

    public static Estimate estimate(final long value)
    {
        // If we have a cached value,
        if (value < CACHE_SIZE)
        {
            // return it
            return CACHED[(int) value];
        }

        // If our value is a power of two,
        if ((value & value - 1) == 0)
        {
            // return the cached value
            return CACHED_POWERS_OF_TWO[Long.numberOfTrailingZeros(value)];
        }

        // If the value isn't < CACHE_SIZE (65,536) and it's not a power of two we have to allocate
        return new Estimate(value);
    }

    public static <T> Estimate estimate(final T[] values)
    {
        return estimate(values.length);
    }

    public static Estimate parse(String value)
    {
        if (value.indexOf(',') > 0)
        {
            value = Strings.removeAll(value, ',');
        }
        return estimate(Long.parseLong(value));
    }

    protected Estimate(final long estimate)
    {
        super(estimate);
    }

    protected Estimate()
    {
        super(0);
    }

    public Estimate add(final Estimate that)
    {
        return plus(that.get());
    }

    @Override
    public String asString()
    {
        return super.toString() + " (estimate)";
    }

    @Override
    public Estimate ceiling(final int digits)
    {
        return (Estimate) super.ceiling(digits);
    }

    @Override
    public Estimate decremented()
    {
        return (Estimate) super.decremented();
    }

    @Override
    public Estimate dividedBy(final Count divisor)
    {
        return dividedBy(divisor.get());
    }

    @Override
    public Estimate dividedBy(final long divisor)
    {
        return estimate(get() / divisor);
    }

    @Override
    public Estimate floor(final int digits)
    {
        return (Estimate) super.floor(digits);
    }

    @Override
    public Estimate incremented()
    {
        return (Estimate) super.incremented();
    }

    @Override
    public Estimate maximum(final Count that)
    {
        if (isGreaterThan(that))
        {
            return this;
        }
        else
        {
            return that.asEstimate();
        }
    }

    @Override
    public Estimate minimum(final Count that)
    {
        if (isLessThan(that))
        {
            return this;
        }
        else
        {
            return that.asEstimate();
        }
    }

    @Override
    public Estimate minus(final Count count)
    {
        return (Estimate) super.minus(count);
    }

    @Override
    public Estimate minus(final long count)
    {
        return (Estimate) super.minus(count);
    }

    @Override
    public Estimate minusOne()
    {
        return (Estimate) super.minusOne();
    }

    @Override
    public Estimate nextPrime()
    {
        return (Estimate) super.nextPrime();
    }

    @Override
    public Estimate percent(final Percent percentage)
    {
        return (Estimate) super.percent(percentage);
    }

    @Override
    public Estimate plus(final Count count)
    {
        return (Estimate) super.plus(count);
    }

    @Override
    public Estimate plus(final long count)
    {
        return estimate(get() + count);
    }

    @Override
    public Estimate plusOne()
    {
        return (Estimate) super.plusOne();
    }

    @Override
    public Estimate roundUpToPowerOfTwo()
    {
        return (Estimate) super.roundUpToPowerOfTwo();
    }

    @Override
    public Estimate times(final long count)
    {
        return (Estimate) super.times(count);
    }

    @Override
    public Estimate times(final Count count)
    {
        return estimate(get() * count.get());
    }

    @Override
    public Estimate times(final double multiplier)
    {
        return estimate((long) (get() * multiplier));
    }

    @Override
    public Estimate times(final Percent percentage)
    {
        return times(percentage.asUnitValue());
    }

    @Override
    protected Estimate onNewInstance(final long value)
    {
        return new Estimate(value);
    }
}
