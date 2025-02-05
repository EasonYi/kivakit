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

package com.telenav.kivakit.core.test;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.language.Hash;
import com.telenav.kivakit.core.lexakai.DiagramTest;
import com.telenav.kivakit.core.logging.Logger;
import com.telenav.kivakit.core.logging.LoggerFactory;
import com.telenav.kivakit.core.messaging.Debug;
import com.telenav.kivakit.core.test.CoreUnitTest.Repeats;
import com.telenav.kivakit.core.value.count.BaseCount;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.value.count.Range;
import com.telenav.kivakit.core.value.identifier.Identifier;
import com.telenav.kivakit.core.value.level.Confidence;
import com.telenav.kivakit.interfaces.comparison.Filter;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.interfaces.numeric.RandomNumeric;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.test.CoreUnitTest.Repeats.ALLOW_REPEATS;
import static com.telenav.kivakit.core.test.CoreUnitTest.Repeats.NO_REPEATS;
import static com.telenav.kivakit.core.value.count.Count._256;
import static com.telenav.kivakit.core.value.count.Count._65_536;
import static com.telenav.kivakit.core.value.count.Count.count;
import static com.telenav.kivakit.interfaces.code.FilteredLoopBody.FilterAction.ACCEPT;
import static com.telenav.kivakit.interfaces.code.FilteredLoopBody.FilterAction.REJECT;

/**
 * Utility class for tests used to create random values. {@link CoreUnitTest} has a variety of methods for random testing
 * that use this class. Projects can subclass this to provide additional random values relevant to the project.
 *
 * <p><b>Seeding</b></p>
 *
 * <p>
 * The seed used for random values can be set with {@link #seed(long)}. KivaKit tests will emit a seed value when a test
 * fails. This value can be fed to {@link #seed(long)} to reproduce the failure.
 * </p>
 *
 * <p><b>Values</b></p>
 * <p>
 * Values of the given types can be randomly produced:
 * </p>
 *
 * <ul>
 *     <li>Characters</li>
 *     <li>Strings</li>
 *     <li>Booleans</li>
 *     <li>Bytes</li>
 *     <li>Confidence</li>
 *     <li>Count</li>
 *     <li>Double</li>
 *     <li>Identifier</li>
 *     <li>Ints</li>
 *     <li>Longs</li>
 *     <li>Shorts</li>
 *     <li>Unsigned Doubles</li>
 *     <li>Unsigned Ints</li>
 *     <li>Unsigned Longs</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see CoreUnitTest
 */
@UmlClassDiagram(diagram = DiagramTest.class)
@LexakaiJavadoc(complete = true)
@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public class RandomValueFactory implements RandomNumeric
{
    private static volatile long SALT = 8682522807148012L;

    private static final Logger LOGGER = LoggerFactory.newLogger();

    private static final Debug DEBUG = new Debug(LOGGER);

    /** Seed value when reproducing test failures */
    private long seed;

    public RandomValueFactory(long seed)
    {
        seed(seed);
    }

    public RandomValueFactory()
    {
        seed(newSeed());
    }

    public void byteSequence(Consumer<Byte> consumer)
    {
        sequence(ALLOW_REPEATS, iterations(), minimum(Byte.class), maximum(Byte.class), Byte.class, Filter.all(), consumer);
    }

    public void byteSequence(Repeats repeats,
                             Matcher<Byte> include,
                             Consumer<Byte> consumer)
    {
        sequence(repeats, iterations(), minimum(Byte.class), maximum(Byte.class), Byte.class, include, consumer);
    }

    public void byteSequence(Repeats repeats,
                             Consumer<Byte> consumer)
    {
        sequence(repeats, iterations(), minimum(Byte.class), maximum(Byte.class), Byte.class, Filter.all(), consumer);
    }

    public void byteSequence(Matcher<Byte> include,
                             Consumer<Byte> consumer)
    {
        sequence(ALLOW_REPEATS, iterations(), minimum(Byte.class), maximum(Byte.class), Byte.class, include, consumer);
    }

    public Confidence confidence()
    {
        return Confidence.confidence(randomDoubleZeroToOne());
    }

    public <T> T from(Collection<T> values)
    {
        if (values.isEmpty())
        {
            return null;
        }
        else
        {
            List<T> list = new ArrayList<>(values);
            return list.get(randomIndex(list.size()));
        }
    }

    public <T> T from(T[] values)
    {
        if (values.length == 0)
        {
            return null;
        }
        else
        {
            return values[randomIndex(values.length)];
        }
    }

    public Identifier identifierExclusive(long maximum)
    {
        return new Identifier(randomLongExclusive(1, maximum));
    }

    public void indexes(Repeats repeats,
                        long maximum,
                        Consumer<Integer> consumer)
    {
        sequence(repeats, iterations(), 0, maximum, Integer.class, consumer);
    }

    public void indexes(long maximum, Consumer<Integer> consumer)
    {
        sequence(ALLOW_REPEATS, iterations().minimum(randomCount(maximum - 1)), 0, maximum - 1, Integer.class, consumer);
    }

    public void intSequence(Consumer<Integer> consumer)
    {
        sequence(ALLOW_REPEATS, iterations(), minimum(Integer.class), maximum(Integer.class), Integer.class, Filter.all(), consumer);
    }

    public void intSequence(Repeats repeats,
                            Matcher<Integer> include,
                            Consumer<Integer> consumer)
    {
        sequence(repeats, iterations(), minimum(Integer.class), maximum(Integer.class), Integer.class, include, consumer);
    }

    public void intSequence(Repeats repeats,
                            Consumer<Integer> consumer)
    {
        sequence(repeats, iterations(), minimum(Integer.class), maximum(Integer.class), Integer.class, Filter.all(), consumer);
    }

    public void intSequence(Matcher<Integer> include,
                            Consumer<Integer> consumer)
    {
        sequence(ALLOW_REPEATS, iterations(), minimum(Integer.class), maximum(Integer.class), Integer.class, include, consumer);
    }

    public Count iterations()
    {
        return _256;
    }

    public char letter()
    {
        var alphabet = "ABCDEFGHIJKLMONPQRSTUVWXYZabcdefghijklmonpqrstuvwxyz";
        return alphabet.charAt(randomIntExclusive(0, alphabet.length()));
    }

    public String letters()
    {
        return letters(1, 32);
    }

    public String letters(int minLength, int maxLength)
    {
        var builder = new StringBuilder();
        for (var i = 0; i < randomIntInclusive(minLength, maxLength); i++)
        {
            builder.append(letter());
        }
        return builder.toString();
    }

    public <T extends Number> List<T> list(Repeats repeats,
                                           Count count,
                                           Class<T> type)
    {
        var list = new ArrayList<T>(count.asInt());
        sequence(repeats, count, type, list::add);
        return list;
    }

    public <T extends Number> List<T> list(Repeats repeats,
                                           Class<T> type)
    {
        var list = new ArrayList<T>(iterations().asInt());
        sequence(repeats, iterations(), type, list::add);
        return list;
    }

    public <T extends Number> List<T> list(Class<T> type)
    {
        var list = new ArrayList<T>(iterations().asInt());
        sequence(ALLOW_REPEATS, iterations(), type, list::add);
        return list;
    }

    public <T extends Number> List<T> list(Repeats repeats,
                                           Class<T> type,
                                           Matcher<T> include)
    {
        return list(repeats, iterations(), type, include);
    }

    public <T extends Number> List<T> list(Repeats repeats,
                                           Count count,
                                           long minimum,
                                           long maximum,
                                           Class<T> type,
                                           Matcher<T> include)
    {
        var list = new ArrayList<T>(count.asInt());
        sequence(repeats, count, minimum, maximum, type, include, list::add);
        return list;
    }

    public <T extends Number> List<T> list(Repeats repeats,
                                           Count count,
                                           long minimum,
                                           long maximum,
                                           Class<T> type)
    {
        return list(repeats, count, minimum, maximum, type, Filter.all());
    }

    public <T extends Number> List<T> list(Repeats repeats,
                                           Count count,
                                           Class<T> type,
                                           Matcher<T> include)
    {
        return list(repeats, count, minimum(type), maximum(type), type, include);
    }

    public void longSequence(Consumer<Long> consumer)
    {
        sequence(ALLOW_REPEATS, iterations(), minimum(Long.class), maximum(Long.class), Long.class, Filter.all(), consumer);
    }

    public void longSequence(Repeats repeats,
                             Matcher<Long> include,
                             Consumer<Long> consumer)
    {
        sequence(repeats, iterations(), minimum(Long.class), maximum(Long.class), Long.class, include, consumer);
    }

    public void longSequence(Repeats repeats,
                             Consumer<Long> consumer)
    {
        sequence(repeats, iterations(), minimum(Long.class), maximum(Long.class), Long.class, Filter.all(), consumer);
    }

    public void longSequence(Matcher<Long> include,
                             Consumer<Long> consumer)
    {
        sequence(ALLOW_REPEATS, iterations(), minimum(Long.class), maximum(Long.class), Long.class, include, consumer);
    }

    public void loop(Runnable code)
    {
        iterations().loop(code);
    }

    public Count randomCount(long maximum)
    {
        return randomCount(0, maximum);
    }

    public Count randomCount(long minimum, long maximum)
    {
        return count(randomLongInclusive(minimum, maximum));
    }

    @Override
    public double randomDoubleZeroToOne()
    {
        return random.nextDouble();
    }

    @Override
    public final long randomLongExclusive(long minimum, long exclusiveMaximum)
    {
        return internalNextLong(minimum, exclusiveMaximum);
    }

    public Range<Count> rangeExclusive(long minimum, long exclusiveMaximum)
    {
        return rangeExclusive(minimum, exclusiveMaximum, 1);
    }

    public Range<Count> rangeExclusive(long minimum, long exclusiveMaximum, long minimumWidth)
    {
        return rangeExclusive(count(minimum), count(exclusiveMaximum), minimumWidth);
    }

    public <T extends BaseCount<T>> Range<T> rangeExclusive(T minimum, T exclusiveMaximum, long minimumWidth)
    {
        ensure(minimum.isLessThan(exclusiveMaximum));

        var width = exclusiveMaximum.minus(minimum).asLong();

        var randomWidth = randomLongExclusive(minimumWidth, width);
        var randomMinimum = randomLongExclusive(minimum.asLong(), exclusiveMaximum.asLong() - randomWidth);
        var randomExclusiveMaximum = randomMinimum + randomWidth;

        return Range.exclusive(minimum.newInstance(randomMinimum), minimum.newInstance(randomExclusiveMaximum));
    }

    public <T extends BaseCount<T>> Range<T> rangeExclusive(T minimum, T exclusiveMaximum)
    {
        return rangeInclusive(minimum, exclusiveMaximum.incremented(), 0);
    }

    public Range<Count> rangeInclusive(long minimum, long inclusiveMaximum)
    {
        return rangeInclusive(minimum, inclusiveMaximum, 0);
    }

    public <T extends BaseCount<T>> Range<T> rangeInclusive(T minimum, T inclusiveMaximum, long minimumWidth)
    {
        ensure(minimum.isLessThanOrEqualTo(inclusiveMaximum));

        var width = inclusiveMaximum.minus(minimum).asLong() + 1;

        var randomWidth = randomLongInclusive(minimumWidth, width - 1);
        var randomMinimum = randomLongInclusive(minimum.asLong(), minimum.asLong() + randomWidth);
        var randomInclusiveMaximum = randomMinimum + randomWidth;

        return Range.inclusive(minimum.newInstance(randomMinimum), minimum.newInstance(randomInclusiveMaximum));
    }

    public Range<Count> rangeInclusive(long minimum, long inclusiveMaximum, long minimumWidth)
    {
        return rangeInclusive(count(minimum), count(inclusiveMaximum), minimumWidth);
    }

    public long seed()
    {
        return seed;
    }

    public void seed(long seed)
    {
        random = new Random(seed);
        this.seed = seed;
    }

    public <T extends Number> void sequence(Class<T> type,
                                            Consumer<T> consumer)
    {
        sequence(ALLOW_REPEATS, iterations(), minimum(type), maximum(type), type, Filter.all(), consumer);
    }

    public <T extends Number> void sequence(Repeats repeats,
                                            Class<T> type,
                                            Consumer<T> consumer)
    {
        sequence(repeats, iterations(), minimum(type), maximum(type), type, Filter.all(), consumer);
    }

    public <T extends Number> void sequence(Repeats repeats,
                                            Count count,
                                            Class<T> type,
                                            Consumer<T> consumer)
    {
        sequence(repeats, count, minimum(type), maximum(type), type, Filter.all(), consumer);
    }

    public <T extends Number> void sequence(Repeats repeats,
                                            long minimum,
                                            long maximum,
                                            Class<T> type,
                                            Consumer<T> consumer)
    {
        sequence(repeats, iterations(), minimum, maximum, type, Filter.all(), consumer);
    }

    public <T extends Number> void sequence(Repeats repeats,
                                            long maximum,
                                            Class<T> type,
                                            Consumer<T> consumer)
    {
        sequence(repeats, iterations(), 0, maximum, type, Filter.all(), consumer);
    }

    public <T extends Number> void sequence(Repeats repeats,
                                            Count count,
                                            long minimum,
                                            long maximum,
                                            Class<T> type,
                                            Consumer<T> consumer)
    {
        sequence(repeats, count, minimum, maximum, type, Filter.all(), consumer);
    }

    public <T extends Number> void sequence(Repeats repeats,
                                            Count count,
                                            Class<T> type,
                                            Matcher<T> include,
                                            Consumer<T> consumer)
    {
        sequence(repeats, count, minimum(type), maximum(type), type, include, consumer);
    }

    public <T extends Number> void sequence(Repeats repeats,
                                            Class<T> type,
                                            Matcher<T> include,
                                            Consumer<T> consumer)
    {
        sequence(repeats, iterations(), minimum(type), maximum(type), type, include, consumer);
    }

    public <T extends Number> void sequence(Repeats repeats,
                                            Count count,
                                            long minimum,
                                            long exclusiveMaximum,
                                            Class<T> type,
                                            Matcher<T> include,
                                            Consumer<T> consumer)
    {
        // Computed the range, handling overflow (well enough for our tests)
        double range = (double) exclusiveMaximum - (double) minimum;
        if (repeats == NO_REPEATS && count.asInt() > range)
        {
            count = randomCount((long) range);
        }

        ensure(repeats == ALLOW_REPEATS || count.get() <= range, "Count $ is larger than range $", count, range);

        // If we're allowing repeats,
        if (repeats == NO_REPEATS)
        {
            var values = new ObjectSet<T>();
            if (count.isLessThanOrEqualTo(_65_536))
            {
                count.loop(at ->
                {
                    var value = cast(at, type);
                    if (include.matches(value))
                    {
                        values.add(value);
                        return ACCEPT;
                    }
                    return REJECT;
                });
            }
            else
            {
                count.loop(() ->
                {
                    var included = false;
                    do
                    {
                        var randomValue = cast(randomLongExclusive(minimum, exclusiveMaximum), type);
                        included = !values.contains(randomValue) && include.matches(randomValue);
                        if (included)
                        {
                            values.add(randomValue);
                        }
                    }
                    while (!included);
                });
            }

            var list = new ObjectList<>(values);
            list.shuffle(random);

            for (var value : list)
            {
                consumer.accept(value);
            }
        }
        else
        {
            // otherwise, we are allowing repeats, so things are simple.
            Range.exclusive(count(0), count).forCount(count, at ->
            {
                var value = randomExclusive(minimum, exclusiveMaximum, type, include);
                if (include.matches(value))
                {
                    consumer.accept(value);
                    return ACCEPT;
                }
                return REJECT;
            });
        }
    }

    public void shortSequence(Consumer<Short> consumer)
    {
        sequence(ALLOW_REPEATS, iterations(), minimum(Short.class), maximum(Short.class), Short.class, Filter.all(), consumer);
    }

    public void shortSequence(Repeats repeats,
                              Matcher<Short> include,
                              Consumer<Short> consumer)
    {
        sequence(repeats, iterations(), minimum(Short.class), maximum(Short.class), Short.class, include, consumer);
    }

    public void shortSequence(Repeats repeats,
                              Consumer<Short> consumer)
    {
        sequence(repeats, iterations(), minimum(Short.class), maximum(Short.class), Short.class, Filter.all(), consumer);
    }

    public void shortSequence(Matcher<Short> include,
                              Consumer<Short> consumer)
    {
        sequence(ALLOW_REPEATS, iterations(), minimum(Short.class), maximum(Short.class), Short.class, include, consumer);
    }

    public String string()
    {
        return string(1024);
    }

    public String string(int maximum)
    {
        return string(1, 1024);
    }

    public String string(int minimum, int maximum)
    {
        var builder = new StringBuilder();
        for (var i = 0; i < randomIntInclusive(minimum, maximum); i++)
        {
            builder.append(randomChar());
        }
        return builder.toString();
    }

    /**
     * NOTE: This (poorly documented) method is verbatim from java.util.Random.java. It is being used because the task
     * of finding a random long between two longs involves complex overflow issues and this code has been tested.
     * <p>
     * The form of nextLong used by LongStream Spliterators.  If origin is greater than bound, acts as unbounded form of
     * nextLong, else as bounded form.
     *
     * @param origin the least value, unless greater than bound
     * @param bound the upper bound (exclusive), must not equal origin
     * @return a pseudorandom value
     */
    @SuppressWarnings("StatementWithEmptyBody")
    private long internalNextLong(long origin, long bound)
    {
        if (origin == bound)
        {
            return origin;
        }
        var r = random.nextLong();
        if (origin < bound)
        {
            var n = bound - origin;
            var m = n - 1;
            if ((n & m) == 0L)  // power of two
            {
                r = (r & m) + origin;
            }
            else if (n > 0L)
            {  // reject over-represented candidates
                for (var u = r >>> 1;            // ensure non-negative
                     u + m - (r = u % n) < 0L;    // rejection check
                     u = random.nextLong() >>> 1) // retry
                {
                }
                r += origin;
            }
            else
            {              // range not representable as long
                while (r < origin || r >= bound)
                {
                    r = random.nextLong();
                }
            }
        }
        return r;
    }

    private synchronized long newSeed()
    {
        // NOTE: this code comes from java.util.Random, since it does not expose the way it creates
        // seed values
        return Hash.knuth(++SALT + System.nanoTime());
    }

    /** Random number generator */
    protected Random random;
}
