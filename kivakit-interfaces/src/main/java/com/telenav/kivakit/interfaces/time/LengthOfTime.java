package com.telenav.kivakit.interfaces.time;

import com.telenav.kivakit.interfaces.code.Callback;
import com.telenav.kivakit.interfaces.collection.NextValue;
import com.telenav.kivakit.interfaces.lexakai.DiagramTimeDuration;
import com.telenav.kivakit.interfaces.numeric.Arithmetic;
import com.telenav.kivakit.interfaces.numeric.Maximizable;
import com.telenav.kivakit.interfaces.numeric.Minimizable;
import com.telenav.kivakit.interfaces.numeric.Percentage;
import com.telenav.kivakit.interfaces.numeric.Quantizable;
import com.telenav.kivakit.interfaces.numeric.QuantumComparable;
import com.telenav.kivakit.interfaces.string.Stringable;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.UmlMethodGroup;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

/**
 * Interface to an object having a length of time, measured in milliseconds.
 *
 * <p><b>Creation</b></p>
 *
 * <p>
 * An object that implements this interface must provide implementations of:
 * </p>
 *
 * <ul>
 *     <li>{@link #milliseconds()} - The number of milliseconds for this length of time</li>
 *     <li>{@link #newDuration(long)}</li>
 *     <li>{@link #newTimeOrDuration(long)}</li>
 *     <li>{@link #millisecondsPerUnit()} - The number of milliseconds per unit of the implementing class</li>
 * </ul>
 *
 * <p><b>Conversion</b></p>
 *
 * <p>
 * A length of time can be converted to specific time units by calling one of the following methods:
 * </p>
 *
 * <ul>
 *     <li>{@link #asMilliseconds()}</li>
 *     <li>{@link #asSeconds()}</li>
 *     <li>{@link #asMinutes()}</li>
 *     <li>{@link #asHours()}</li>
 *     <li>{@link #asDays()}</li>
 *     <li>{@link #asWeeks()}</li>
 *     <li>{@link #asYears()}</li>
 * </ul>
 *
 * <p><b>Units</b></p>
 *
 * <ul>
 *     <li>{@link #asUnits()}</li>
 *     <li>{@link #minusUnits(long)}</li>
 *     <li>{@link #oneUnit()}</li>
 *     <li>{@link #plusUnits(long)}</li>
 *     <li>{@link #units(Milliseconds)}</li>
 * </ul>
 *
 *
 * <p><b>Arithmetic</b></p>
 *
 * <li>
 *     <li>{@link #decremented()}</li>
 *     <li>{@link #dividedBy(LengthOfTime)}</li>
 *     <li>{@link #dividedBy(double)}</li>
 *     <li>{@link #incremented()}</li>
 *     <li>{@link #minus(LengthOfTime)}</li>
 *     <li>{@link #plus(LengthOfTime)}</li>
 *     <li>{@link #times(double)}</li>
 * </li>
 *
 * <p><b>Math</b></p>
 *
 * <ul>
 *     <li>{@link #difference(LengthOfTime)}</li>
 *     <li>{@link #longerBy(Percentage)}</li>
 *     <li>{@link #modulo()}</li>
 *     <li>{@link #modulus(LengthOfTime)}</li>
 *     <li>{@link #nearest(LengthOfTime)}</li>
 *     <li>{@link #roundDown(LengthOfTime)}</li>
 *     <li>{@link #roundUp(LengthOfTime)}</li>
 *     <li>{@link #shorterBy(Percentage)}</li>
 *     <li>{@link #percentageOf(LengthOfTime)}</li>
 * </ul>
 *
 * <p><b>Comparison</b></p>
 *
 * <ul>
 *     <li>{@link #compareTo(LengthOfTime)}</li>
 *     <li>{@link #isLessThan(Quantizable)}</li>
 *     <li>{@link #isLessThanOrEqualTo(Quantizable)}</li>
 *     <li>{@link #isGreaterThan(Quantizable)}</li>
 *     <li>{@link #isLessThanOrEqualTo(Quantizable)}</li>
 *     <li>{@link #isApproximately(Quantizable, Quantizable)}</li>
 *     <li>{@link #isZero()}</li>
 *     <li>{@link #isNonZero()}</li>
 *     <li>{@link #quantum()}</li>
 * </ul>
 *
 * <p><b>Threading</b></p>
 *
 * <ul>
 *     <li>{@link #await(Condition)}</li>
 *     <li>{@link #sleep()}</li>
 *     <li>{@link #wait(Object)}</li>
 *     <li>{@link #waitThen(Callback)}</li>
 *     <li>{@link #every(Callback)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramTimeDuration.class)
public interface LengthOfTime<Duration extends LengthOfTime<Duration>> extends
        QuantumComparable<LengthOfTime<?>>,
        Comparable<LengthOfTime<?>>,
        Minimizable<Duration>,
        Arithmetic<Duration>,
        Maximizable<Duration>,
        TimeUnits<Duration>,
        NextValue<Duration>,
        Stringable
{
    /**
     * Retrieves the number of days for this length of time.
     *
     * @return The number of days for this length of time
     */
    @UmlMethodGroup("conversion")
    default double asDays()
    {
        return asHours() / 24.0;
    }

    /**
     * Retrieves the number of hours for this length of time
     *
     * @return The number of hours for this length of time
     */
    @UmlMethodGroup("conversion")
    default double asHours()
    {
        return asMinutes() / 60.0;
    }

    /**
     * Returns a human-readable string for this length of time
     */
    @NotNull
    @UmlMethodGroup("conversion")
    default String asHumanReadableString()
    {
        if (asMilliseconds() >= 0)
        {
            if (asYears() >= 1.0)
            {
                return unitString(asYears(), "year");
            }
            if (asWeeks() >= 1.0)
            {
                return unitString(asWeeks(), "week");
            }
            if (asDays() >= 1.0)
            {
                return unitString(asDays(), "day");
            }
            if (asHours() >= 1.0)
            {
                return unitString(asHours(), "hour");
            }
            if (asMinutes() >= 1.0)
            {
                return unitString(asMinutes(), "minute");
            }
            if (asSeconds() >= 1.0)
            {
                return unitString(asSeconds(), "second");
            }
            return asMilliseconds() + " millisecond" + (asMilliseconds() != 1 ? "s" : "");
        }
        else
        {
            return "N/A";
        }
    }

    /**
     * Returns a java.time.Duration object for the number of milliseconds
     */
    @UmlMethodGroup("conversion")
    default java.time.Duration asJavaDuration()
    {
        return java.time.Duration.ofMillis(milliseconds());
    }

    /**
     * Retrieves the number of milliseconds for this length of time
     *
     * @return The number of milliseconds for this length of time
     */
    @UmlMethodGroup("conversion")
    default double asMilliseconds()
    {
        return milliseconds();
    }

    /**
     * Retrieves the number of minutes for this length of time
     *
     * @return The number of minutes for this length of time
     */
    @UmlMethodGroup("conversion")
    default double asMinutes()
    {
        return asSeconds() / 60.0;
    }

    /**
     * Retrieves the number of seconds for this length of time
     *
     * @return The number of seconds for this length of time
     */
    @UmlMethodGroup("conversion")
    default double asSeconds()
    {
        return asMilliseconds() / 1000.0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("SpellCheckingInspection")
    @UmlMethodGroup("conversion")
    default String asString(Format format)
    {
        switch (format)
        {
            case FILESYSTEM:
                return asHumanReadableString().replace(' ', '_');

            case PROGRAMMATIC:
                return String.valueOf(milliseconds());

            case COMPACT:
            {

                String dateFormatPattern;
                if (asHours() > 1.0)
                {
                    dateFormatPattern = "H:mm:ss";
                }
                else if (asMinutes() > 1.0)
                {
                    dateFormatPattern = "m:ss'm'";
                }
                else if (asSeconds() > 1.0)
                {
                    dateFormatPattern = "s's'";
                }
                else
                {
                    dateFormatPattern = "S'ms'";
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatPattern);
                dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                Date date = new Date(milliseconds());
                return dateFormat.format(date);
            }

            case DEBUG:
                return asHumanReadableString() + " (" + milliseconds() + "ms)";

            case USER_SINGLE_LINE:
            case USER_LABEL:
            case HTML:
            case TO_STRING:
            case LOG:
            case USER_MULTILINE:
            case TEXT:
            default:
                return asHumanReadableString();
        }
    }

    @Override
    @UmlMethodGroup("conversion")
    default long asUnits()
    {
        return (int) (milliseconds() / millisecondsPerUnit());
    }

    /**
     * Retrieves the number of weeks for this length of time
     *
     * @return The number of weeks for this length of time
     */
    @UmlMethodGroup("conversion")
    default double asWeeks()
    {
        return asDays() / 7;
    }

    /**
     * Retrieves the approximate number of years for this length of time
     *
     * @return The approximate number of years for this length of time
     */
    @UmlMethodGroup("conversion")
    default double asYears()
    {
        return asWeeks() / 52.177457;
    }

    /**
     * Waits up to this length of time for the given {@link Condition} variable to be true
     *
     * @param condition The condition variable
     */
    @UmlMethodGroup("operations")
    default boolean await(Condition condition)
    {
        try
        {
            return condition.await(milliseconds(), TimeUnit.MILLISECONDS);
        }
        catch (InterruptedException ignored)
        {
        }
        return false;
    }

    @Override
    default int compareTo(LengthOfTime<?> that)
    {
        return Long.compare(quantum(), that.quantum());
    }

    @UmlMethodGroup("arithmetic")
    default Duration decremented()
    {
        return minus(oneUnit());
    }

    @UmlMethodGroup("arithmetic")
    default Duration difference(Duration that)
    {
        if (isGreaterThan(that))
        {
            return minus(that);
        }
        else
        {
            return that.minus(this);
        }
    }

    /**
     * Returns this length of time divided by the given value
     */
    @UmlMethodGroup("arithmetic")
    default Duration dividedBy(double value)
    {
        return newDuration((long) (milliseconds() / value));
    }

    @UmlMethodGroup("arithmetic")
    default double dividedBy(LengthOfTime<?> that)
    {
        return (double) milliseconds() / that.milliseconds();
    }

    /**
     * Uses a Java {@link Timer} to call the given callback at a rate of once per this-length-of-time
     */
    @UmlMethodGroup("operations")
    default void every(Callback<Timer> onTimer)
    {
        var timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run()
            {
                onTimer.callback(timer);
            }
        }, 0L, milliseconds());
    }

    @UmlMethodGroup("arithmetic")
    default Duration incremented()
    {
        return plus(oneUnit());
    }

    @UmlMethodGroup("arithmetic")
    default Duration longerBy(Percentage percentage)
    {
        return newDuration((long) (milliseconds() * (1.0 + percentage.unitValue())));
    }

    @SuppressWarnings("unchecked")
    @Override
    @UmlMethodGroup("arithmetic")
    default Duration maximum(Duration that)
    {
        return isGreaterThan(that) ? (Duration) this : that;
    }

    /**
     * @return Number of milliseconds in this duration
     */
    @Override
    @UmlMethodGroup("units")
    long milliseconds();

    /**
     * Returns the number of milliseconds per unit of time
     */
    @Override
    @UmlMethodGroup("units")
    long millisecondsPerUnit();

    @SuppressWarnings("unchecked")
    @Override
    @UmlMethodGroup("arithmetic")
    default Duration minimum(Duration that)
    {
        return isLessThan(that) ? (Duration) this : that;
    }

    /**
     * Returns this length of time minus the given length of time
     */
    @UmlMethodGroup("arithmetic")
    default Duration minus(LengthOfTime<?> that)
    {
        return newDuration(milliseconds() - that.milliseconds());
    }

    @Override
    @UmlMethodGroup("arithmetic")
    default Duration minusUnits(long units)
    {
        return plusUnits(-units);
    }

    /**
     * Returns the modulo size of this length of time in units. For example, the modulo for military time would be 24.
     */
    @Override
    @UmlMethodGroup("arithmetic")
    default long modulo()
    {
        var maximum = maximum().asUnits();
        var minimum = minimum().asUnits();
        return maximum - minimum + 1;
    }

    @UmlMethodGroup("arithmetic")
    default Duration modulus(LengthOfTime<?> that)
    {
        return newDuration(milliseconds() % that.milliseconds());
    }

    /**
     * @return The nearest length of time to the given length of time
     */
    @UmlMethodGroup("arithmetic")
    default Duration nearest(LengthOfTime<?> unit)
    {
        return plus(unit.dividedBy(2)).roundDown(unit);
    }

    /**
     * Returns an instance of the subclass of this length of time for the given number of milliseconds.
     *
     * @param milliseconds The number of milliseconds
     * @return The subclass instance
     */
    @UmlExcludeMember
    Duration newDuration(long milliseconds);

    @Override
    @UmlMethodGroup("arithmetic")
    default Duration next()
    {
        return plusUnits(1);
    }

    @UmlMethodGroup("arithmetic")
    default Duration oneUnit()
    {
        return newDuration(millisecondsPerUnit());
    }

    @UmlMethodGroup("arithmetic")
    Percentage percentageOf(LengthOfTime<?> that);

    /**
     * Returns this length of time plus the given length of time
     */
    @UmlMethodGroup("arithmetic")
    default Duration plus(LengthOfTime<?> that)
    {
        return newDuration(milliseconds() + that.milliseconds());
    }

    @Override
    @UmlMethodGroup("arithmetic")
    default Duration plusUnits(long units)
    {
        return newTimeOrDurationFromUnits(asUnits() + units);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @UmlExcludeMember
    default long quantum()
    {
        return milliseconds();
    }

    /**
     * Returns this length of time rounded down to the nearest whole unit
     */
    @UmlMethodGroup("arithmetic")
    default Duration roundDown(LengthOfTime<?> unit)
    {
        return newDuration(milliseconds() / unit.milliseconds() * unit.milliseconds());
    }

    /**
     * Returns this length of time rounded up to the nearest whole unit
     */
    @UmlMethodGroup("arithmetic")
    default Duration roundUp(LengthOfTime<?> unit)
    {
        return roundDown(unit).plus(unit);
    }

    @UmlMethodGroup("arithmetic")
    default Duration shorterBy(Percentage percentage)
    {
        return newDuration((long) (milliseconds() * (1.0 - percentage.unitValue())));
    }

    /**
     * Sleeps for this length of time, ignoring any interruptions
     */
    @UmlMethodGroup("operations")
    default void sleep()
    {
        if (milliseconds() > 0)
        {
            try
            {
                Thread.sleep(milliseconds());
            }
            catch (InterruptedException e)
            {
                // Ignored
            }
        }
    }

    /**
     * Returns this length of time times the given value
     */
    @UmlMethodGroup("arithmetic")
    default Duration times(double value)
    {
        return newDuration((long) (milliseconds() * value));
    }

    /**
     * Converts a value to a unit-suffixed value, taking care of English singular/plural suffix.
     *
     * @param value a double value to format
     * @param units the units to apply singular or plural suffix to
     * @return a String representation
     */
    @UmlMethodGroup("units")
    default String unitString(double value, String units)
    {
        var format = new DecimalFormat("###,###.##");
        return format.format(value) + " " + units + (value > 1.0 ? "s" : "");
    }

    /**
     * Wait for this length of time on the given monitor. Note that a duration of 0 milliseconds is considered to be a
     * wait time of zero milliseconds, whereas the underlying Java {@link #wait(long)} considers zero milliseconds to be
     * infinite wait time.
     *
     * @param monitor The monitor to wait on
     * @return True if the thread waited, false if it was interrupted
     */
    @SuppressWarnings({ "UnusedReturnValue", "SynchronizationOnLocalVariableOrMethodParameter" })
    @UmlMethodGroup("operations")
    default boolean wait(Object monitor)
    {
        synchronized (monitor)
        {
            try
            {
                var milliseconds = milliseconds();
                if (milliseconds > 0)
                {
                    monitor.wait(milliseconds);
                }
                return true;
            }
            catch (InterruptedException e)
            {
                return false;
            }
        }
    }

    /**
     * Waits this length of time and then calls the callback
     *
     * @param callback The callback
     */
    @UmlMethodGroup("operations")
    default void waitThen(Callback<Timer> callback)
    {
        var timer = new Timer();
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                callback.callback(timer);
            }
        }, milliseconds());
    }
}
