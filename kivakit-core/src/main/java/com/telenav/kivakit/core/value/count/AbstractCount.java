package com.telenav.kivakit.core.value.count;

import com.telenav.kivakit.core.language.primitive.Ints;
import com.telenav.kivakit.core.language.primitive.Longs;
import com.telenav.kivakit.core.language.primitive.Primes;
import com.telenav.kivakit.core.value.level.Percent;
import com.telenav.kivakit.interfaces.code.Loopable;
import com.telenav.kivakit.interfaces.collection.NextValue;
import com.telenav.kivakit.interfaces.numeric.Maximizable;
import com.telenav.kivakit.interfaces.numeric.Minimizable;
import com.telenav.kivakit.interfaces.numeric.Quantizable;
import com.telenav.kivakit.interfaces.string.Stringable;
import com.telenav.kivakit.interfaces.value.Source;

import java.util.function.Consumer;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.value.count.BitCount.bitCount;
import static com.telenav.kivakit.core.value.count.Estimate.estimate;

/**
 * Base class for classes that represent some kind of cardinal number. Each method in this class works for all subclass
 * types. Instances of the subclass are created with {@link #newInstance(long)}.
 *
 * <p>
 * Counts have useful properties that primitive values like <i>long</i> and <i>int</i> do not have:
 *
 * <ol>
 *     <li>Counts are guaranteed to be positive values, so it is not possible to have a {@link Count} of -1</li>
 *     <li>Counts have a variety of useful and convenient methods for:
 *     <ul>
 *         <li>Conversion</li>
 *         <li>Comparison</li>
 *         <li>Mathematics</li>
 *         <li>Looping</li>
 *         <li>Iteration</li>
 *         <li>Array allocation</li>
 *     </ul>
 *     </li>
 *     <li>Counts implement the {@link Quantizable} interface, which makes them interoperable with methods that consume {@link Quantizable}s.</li>
 *     <li>Counts provide a more readable, comma-separated String representation by default</li>
 * </ol>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Efficiency</b></p>
 *
 * <p>
 * {@link Count} objects are cheaper than they might seem for two reasons:
 * <ul>
 *     <li>(1) low count constants, and powers of two and ten can be accessed as constant objects in subclasses
 *     and values up to 65,536 are cached by subclass.
 *     </li>
 *
 *     <li>(2) allocation of count objects higher than 65,536 are cheap in Java due to the design of generational garbage
 *     collectors. This said, there will be occasions where {@link Count} objects are not desirable (for example, inside a
 *     doubly nested loop) and sometimes they may improve a public API method or constructor, while the internal representation
 *     is a primitive value for efficiency.
 *     </li>
 * </ul>
 *
 * <p>
 * Although {@link Count} objects are convenient and make method signatures clear and type-safe, as always, the
 * best approach is to simply use {@link Count} objects until a clear inefficiency shows up in a profiler like YourKit.</li>
 * </p>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Conversion</b></p>
 *
 * <ul>
 *     <li>{@link #asInt()} - This count cast to an <i>int</i> value</li>
 *     <li>{@link #asLong()} - This count as a <i>long</i></li>
 *     <li>{@link #get()} - This count as a <i>long</i></li>
 *     <li>{@link #count()} - This count</li>
 *     <li>{@link #asBitCount()} - This count as a {@link BitCount}</li>
 *     <li>{@link #asEstimate()} - This count as an {@link Estimate}</li>
 *     <li>{@link #asMaximum()} - This count as a {@link Maximum}</li>
 *     <li>{@link #asMinimum()} - This count as a {@link Minimum}</li>
 *     <li>{@link #quantum()} - This count as a quantum <i>long</i> value ({@link Quantizable#quantum()})</li>
 * </ul>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>String Representations</b></p>
 *
 * <ul>
 *     <li>{@link #asString(Format)} - This count formatted in the given format</li>
 *     <li>{@link #asCommaSeparatedString()} - This count as a comma-separated string, like 65,536</li>
 *     <li>{@link #asSimpleString()} - This count as a simple string, like 65536</li>
 * </ul>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Comparison</b></p>
 *
 * <ul>
 *     <li>{@link #compareTo(AbstractCount)} - {@link Comparable#compareTo(Object)} implementation</li>
 *     <li>{@link #isLessThan(Quantizable)} - True if this count is less than the given quantum</li>
 *     <li>{@link #isGreaterThan(Quantizable)} - True if this count is greater than the given quantum</li>
 *     <li>{@link #isLessThanOrEqualTo(Quantizable)} - True if this count is less than or equal to the given quantum</li>
 *     <li>{@link #isGreaterThanOrEqualTo(Quantizable) - True if this count is greater than or equal to the given quantum}</li>
 *     <li>{@link #isZero()} - True if this count is zero</li>
 *     <li>{@link #isNonZero()} - True if this count is not zero</li>
 * </ul>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Minima and Maxima</b></p>
 *
 * <ul>
 *     <li>{@link #isMaximum()} - True if this count is {@link #maximum()}</li>
 *     <li>{@link #isMinimum()} - True if this count is zero</li>
 *     <li>{@link #asMaximum()} - Converts this count to a {@link Maximum}</li>
 *     <li>{@link #asMinimum()} - Converts this count to a {@link Minimum}</li>
 *     <li>{@link #maximum(T)} - Returns the maximum of this count and the given count</li>
 *     <li>{@link #minimum(T)} - Returns the minimum of this count and the given count</li>
 * </ul>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Arithmetic</b></p>
 *
 * <ul>
 *     <li>{@link #decremented()} - This count minus one</li>
 *     <li>{@link #incremented()} - This count plus one</li>
 *     <li>{@link #plus(Quantizable)} - This count plus the given count</li>
 *     <li>{@link #plus(long)} - This count plus the given value</li>
 *     <li>{@link #minus(Quantizable)} - This count minus the given count</li>
 *     <li>{@link #minus(long)} - This count minus the given value</li>
 *     <li>{@link #dividedBy(Quantizable)} - This count divided by the given count, using integer division without rounding</li>
 *     <li>{@link #dividedBy(long)} - This count divided by the given value, using integer division without rounding</li>
 *     <li>{@link #times(Quantizable)} - This count times the given count</li>
 *     <li>{@link #times(long)} - This count times the given value</li>
 *     <li>{@link #times(double)} - This count times the given value, cast to a long value</li>
 *     <li>{@link #times(Percent)} - This count times the given percentage</li>
 * </ul>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Mathematics</b></p>
 *
 * <ul>
 *     <li>{@link #percent(Percent)} - The given percentage of this count</li>
 *     <li>{@link #percentOf(Quantizable)} - This count as a percentage of the given count</li>
 *     <li>{@link #dividesEvenlyBy(Quantizable)} - True if there is no remainder when dividing this count by the given count</li>
 *     <li>{@link #powerOfTenCeiling(int)} - The maximum value of this count taking on the given number of digits</li>
 *     <li>{@link #powerOfTenFloor(int)} - The minimum value of this count taking on the given number of digits</li>
 *     <li>{@link #nextPrime()} - The next prime value from a limited table of primes, useful in allocating linear hashmaps</li>
 *     <li>{@link #bitsToRepresent()} - The number of bits required to represent this count</li>
 *     <li>{@link #powerOfTwoCeiling()} - The next power of two above this count</li>
 * </ul>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Looping</b></p>
 *
 * <ul>
 *     <li>{@link #loop(Runnable)} - Runs the given code block {@link #count()} times</li>
 *     <li>{@link #loop(Loopable)} - Runs the given code block {@link #count()} times, passing the iteration number to the code</li>
 * </ul>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Iteration</b></p>
 *
 * <ul>
 *     <li>{@link #forEachByte(Consumer)} - Passes to the given consumer, each byte from 0 to the smaller of this count or {@link Byte#MAX_VALUE}, exclusive</li>
 *     <li>{@link #forEachInteger(Consumer)} - Passes to the given consumer, each byte from 0 to the smaller of this count or {@link Integer#MAX_VALUE}, exclusive</li>
 *     <li>{@link #forEachLong(Consumer)} - Passes each long from 0 to {@link #asLong()} to the given consumer, exclusive</li>
 *     <li>{@link #forEachShort(Consumer)} - Passes to the given consumer, each byte from 0 to the smaller of this count or {@link Short#MAX_VALUE}, exclusive</li>
 * </ul>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Array Allocation</b></p>
 *
 * <ul>
 *     <li>{@link #newByteArray()} - Allocates a byte array of {@link #count()} elements</li>
 *     <li>{@link #newCharArray()} - Allocates a char array of {@link #count()} elements</li>
 *     <li>{@link #newDoubleArray()} - Allocates a double array of {@link #count()} elements</li>
 *     <li>{@link #newFloatArray()} - Allocates a float array of {@link #count()} elements</li>
 *     <li>{@link #newIntArray()} - Allocates an int array of {@link #count()} elements</li>
 *     <li>{@link #newLongArray()} - Allocates a long array of {@link #count()} elements</li>
 *     <li>{@link #newObjectArray()} - Allocates an Object array of {@link #count()} elements</li>
 *     <li>{@link #newShortArray()} - Allocates a short array of {@link #count()} elements</li>
 *     <li>{@link #newStringArray()} - Allocates a String array of {@link #count()} elements</li>
 * </ul>
 * <p>
 * {@link Count} objects implement the {@link #hashCode()} / {@link #equals(Object)} contract and are {@link Comparable}.
 *
 * <p><br/><hr/><br/></p>
 *
 * @param <T> The subclass type
 * @author jonathanl (shibo)
 * @see Quantizable
 * @see Countable
 * @see Comparable
 * @see Estimate
 * @see Maximum
 * @see Minimum
 */
public abstract class AbstractCount<T extends AbstractCount<T>> implements
        Countable,
        Comparable<AbstractCount<T>>,
        Quantizable,
        Maximizable<T>,
        Minimizable<T>,
        Stringable,
        NextValue<T>,
        Source<Long>
{
    /** The underlying primitive cardinal number */
    private long count;

    public AbstractCount()
    {
    }

    protected AbstractCount(long count)
    {
        ensure(count >= 0, "Count of " + count + " is negative");

        this.count = count;
    }

    public BitCount asBitCount()
    {
        return bitCount(count);
    }

    public Count asCount()
    {
        return Count.count(count);
    }

    public Estimate asEstimate()
    {
        return estimate(count);
    }

    public int asInt()
    {
        return count > Integer.MAX_VALUE
                ? Integer.MAX_VALUE
                : (int) count;
    }

    public long asLong()
    {
        return count;
    }

    public Maximum asMaximum()
    {
        return Maximum.maximum(count);
    }

    public Minimum asMinimum()
    {
        return Minimum.minimum(count);
    }

    public BitCount bitsToRepresent()
    {
        return BitCount.bitsToRepresent(count);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(AbstractCount<T> that)
    {
        return Long.compare(count, that.count);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Count count()
    {
        return Count.count(count);
    }

    public T decremented()
    {
        return offset(-1);
    }

    public T dividedBy(Quantizable divisor)
    {
        return dividedBy(divisor.quantum());
    }

    public T dividedBy(long divisor)
    {
        return inRange(count / divisor);
    }

    public boolean dividesEvenlyBy(Quantizable value)
    {
        return get() % value.quantum() == 0;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof AbstractCount)
        {
            var that = (AbstractCount<?>) object;
            return count == that.count;
        }
        return false;
    }

    public void forEach(Consumer<T> consumer)
    {
        for (var value = minimum(); value.isLessThan(maximum()); value = value.next())
        {
            consumer.accept(value);
        }
    }

    public void forEachByte(Consumer<Byte> consumer)
    {
        for (byte i = 0; i < maximum().asInt(); i++)
        {
            consumer.accept(i);
        }
    }

    public void forEachInteger(Consumer<Integer> consumer)
    {
        for (var i = 0; i < maximum().asInt(); i++)
        {
            consumer.accept(i);
        }
    }

    public void forEachLong(Consumer<Long> consumer)
    {
        for (long i = 0; i < asLong(); i++)
        {
            consumer.accept(i);
        }
    }

    public void forEachShort(Consumer<Short> consumer)
    {
        for (short i = 0; i < maximum().asInt(); i++)
        {
            consumer.accept(i);
        }
    }

    /**
     * Implements {@link Source#get()} to return this count as a Long
     */
    @Override
    public Long get()
    {
        return count;
    }

    @Override
    public int hashCode()
    {
        return Long.hashCode(count);
    }

    public T inRange(long value)
    {
        if (value > maximum().asLong())
        {
            return null;
        }
        if (value < 0)
        {
            return null;
        }
        return newInstance(value);
    }

    public T incremented()
    {
        return offset(1);
    }

    public boolean isBetweenExclusive(AbstractCount<?> minimum, AbstractCount<?> maximum)
    {
        return Longs.isBetweenExclusive(asLong(), minimum.asLong(), maximum.asLong());
    }

    public boolean isBetweenInclusive(AbstractCount<?> minimum, AbstractCount<?> maximum)
    {
        return Longs.isBetweenInclusive(asLong(), minimum.asLong(), maximum.asLong());
    }

    public boolean isGreaterThan(Quantizable that)
    {
        return count > that.quantum();
    }

    public boolean isGreaterThanOrEqualTo(Quantizable that)
    {
        return count >= that.quantum();
    }

    public boolean isLessThan(Quantizable that)
    {
        return count < that.quantum();
    }

    public boolean isLessThanOrEqualTo(Quantizable that)
    {
        return count <= that.quantum();
    }

    public boolean isMaximum()
    {
        return count == maximum().asLong();
    }

    public boolean isMinimum()
    {
        return count == 0;
    }

    @Override
    public boolean isNonZero()
    {
        return count != 0;
    }

    @Override
    public boolean isZero()
    {
        return count == 0;
    }

    public void loop(Loopable<T> code)
    {
        for (var at = minimum(); at.isLessThan(maximum()); at = at.next())
        {
            code.at(at);
        }
    }

    public void loop(Runnable code)
    {
        for (var iteration = 0; iteration < asLong(); iteration++)
        {
            code.run();
        }
    }

    public void loopInclusive(Loopable<T> code)
    {
        for (var at = minimum(); at.isLessThanOrEqualTo(maximum()); at = at.next())
        {
            code.at(at);
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public T maximum(T that)
    {
        if (count > that.asLong())
        {
            return (T) this;
        }
        else
        {
            return that;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T maximum()
    {
        return inRange(Long.MAX_VALUE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public T minimum(T that)
    {
        if (count < that.asLong())
        {
            return (T) this;
        }
        else
        {
            return that;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T minimum()
    {
        return inRange(0);
    }

    public T minus(Quantizable count)
    {
        return minus(count.quantum());
    }

    public T minus(long count)
    {
        return inRange(this.count - count);
    }

    public byte[] newByteArray()
    {
        return new byte[asInt()];
    }

    public char[] newCharArray()
    {
        return new char[asInt()];
    }

    public double[] newDoubleArray()
    {
        return new double[asInt()];
    }

    public float[] newFloatArray()
    {
        return new float[asInt()];
    }

    public int[] newIntArray()
    {
        return new int[asInt()];
    }

    public long[] newLongArray()
    {
        return new long[asInt()];
    }

    @SuppressWarnings({ "unchecked" })
    public <Element> Element[] newObjectArray()
    {
        return (Element[]) new Object[asInt()];
    }

    public short[] newShortArray()
    {
        return new short[asInt()];
    }

    public String[] newStringArray()
    {
        return new String[asInt()];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T next()
    {
        return offset(1);
    }

    public T nextPrime()
    {
        return newInstance(Primes.primeAllocationSize(asInt()));
    }

    public T offset(long offset)
    {
        return inRange(count + offset);
    }

    public T percent(Percent percentage)
    {
        return inRange((long) (count * percentage.asUnitValue()));
    }

    public Percent percentOf(Quantizable total)
    {
        if (total.isZero())
        {
            return Percent._0;
        }
        return Percent.of(count * 100.0 / total.quantum());
    }

    public T plus(Quantizable count)
    {
        return plus(count.quantum());
    }

    public T plus(long count)
    {
        return inRange(this.count + count);
    }

    /**
     * Returns the next higher power of ten with the given number of digits. For example, if this is a count of 6700,
     * and digits is 5, then the result will be 6700 + 10_000 = 16,700 / 10,000 = 1 * 10,000 = 10,000.
     *
     * @param digits The number of digits
     */
    public T powerOfTenCeiling(int digits)
    {
        return inRange((get() + Ints.powerOfTen(digits))
                / Ints.powerOfTen(digits)
                * Ints.powerOfTen(digits));
    }

    /**
     * Returns the next lower power of ten with the given number of digits. For example, if this is a count of 16,700,
     * and digits is 5, then the result will be 16,700 / 10,000 = 1 * 10,000 = 10,000.
     *
     * @param digits The number of digits
     */
    public T powerOfTenFloor(int digits)
    {
        return inRange(get() / Ints.powerOfTen(digits) * Ints.powerOfTen(digits));
    }

    /**
     * Rounds this count up to the next higher por of two
     */
    public T powerOfTwoCeiling()
    {
        var rounded = 1L;
        while (rounded < asLong())
        {
            rounded <<= 1;
        }
        return newInstance(rounded);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long quantum()
    {
        return count;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size()
    {
        return asInt();
    }

    public T times(Quantizable count)
    {
        return times(count.quantum());
    }

    public T times(double multiplier)
    {
        return inRange((long) (get() * multiplier));
    }

    public T times(long multiplier)
    {
        return inRange(count * multiplier);
    }

    public T times(Percent percentage)
    {
        return times(percentage.asUnitValue());
    }

    public Range<T> toExclusive(T maximum)
    {
        return Range.exclusive(asSubclassType(), maximum);
    }

    public Range<T> toInclusive(T maximum)
    {
        return Range.inclusive(asSubclassType(), maximum);
    }

    @Override
    public String toString()
    {
        return asCommaSeparatedString();
    }

    /**
     * Returns a new instance of the concrete subclass of this abstract class.
     *
     * @param count The count value
     * @return The new instance
     */
    protected abstract T newInstance(long count);

    @SuppressWarnings("unchecked")
    private T asSubclassType()
    {
        return (T) this;
    }
}
