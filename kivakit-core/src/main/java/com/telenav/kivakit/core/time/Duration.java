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

package com.telenav.kivakit.core.time;

import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.test.NoTestRequired;
import com.telenav.kivakit.core.test.Tested;
import com.telenav.kivakit.interfaces.lexakai.DiagramTimeDuration;
import com.telenav.kivakit.interfaces.time.LengthOfTime;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.UmlMethodGroup;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import java.util.regex.Pattern;

import static com.telenav.kivakit.core.string.Strings.isOneOf;
import static com.telenav.kivakit.core.time.DayOfWeek.isoDayOfWeek;
import static com.telenav.kivakit.core.time.Duration.Restriction.POSITIVE_ONLY;
import static java.util.regex.Pattern.CASE_INSENSITIVE;

/**
 * A <code>Duration</code> is an immutable length of time stored as a number of milliseconds. Various factory and
 * conversion methods are available for convenience.
 * <p>
 * These static factory methods allow easy construction of value objects using either long values like
 * <code>seconds(30)</code> or <code>hours(3)</code>:
 * </p>
 *
 * <ul>
 *     <li>{@link #milliseconds(long)}</li>
 *     <li>{@link #seconds(long)}</li>
 *     <li>{@link #minutes(long)}</li>
 *     <li>{@link #hours(long)}</li>
 *     <li>{@link #days(long)}</li>
 * </ul>
 *
 * <p>
 * ...or double-precision floating point values like <code>days(3.2)</code>:
 * </p>
 *
 * <ul>
 *     <li>{@link #milliseconds(double)}</li>
 *     <li>{@link #seconds(double)}</li>
 *     <li>{@link #minutes(double)}</li>
 *     <li>{@link #hours(double)}</li>
 *     <li>{@link #days(double)}</li>
 * </ul>
 *
 * <p>
 * The precise number of milliseconds represented by a <code>Duration</code> object can be retrieved
 * by calling the <code>milliseconds</code> method. The value of a <code>Duration</code> object
 * in a given unit like days or hours can be retrieved by calling one of the following unit methods,
 * each of which returns a double-precision floating point number:
 * </p>
 *
 * <ul>
 *     <li>{@link #asSeconds()}</li>
 *     <li>{@link #asMinutes()}</li>
 *     <li>{@link #asHours()}</li>
 *     <li>{@link #asDays()}</li>
 *     <li>{@link #asWeeks()}</li>
 *     <li>{@link #asYears()}</li>
 * </ul>
 *
 * <p>
 * Values can be added and subtracted using the {@link #plus(LengthOfTime)} and
 * <code>{@link #minus(Duration)}</code> methods, each of which returns a new immutable
 * <code>Duration</code> object.
 * <p>
 * Finally, the <code>sleep</code> method will sleep for the value of a <code>Duration</code>.
 *
 * @author Jonathan Locke
 * @see Time
 */
@SuppressWarnings("unused")
@Tested
@UmlClassDiagram(diagram = DiagramTimeDuration.class)
public class Duration extends BaseDuration<Duration>
{
    /** Constant for maximum duration. */
    public static final Duration FOREVER = milliseconds(Long.MAX_VALUE);

    /** Constant for no duration. */
    public static final Duration INSTANTANEOUS = milliseconds(0);

    /** Constant for one day. */
    public static final Duration ONE_DAY = days(1);

    /** Constant for one half hour. */
    public static final Duration ONE_HALF_HOUR = hours(0.5);

    /** Constant for 15 minutes. */
    public static final Duration ONE_QUARTER_HOUR = hours(.25);

    /** Constant for one hour. */
    public static final Duration ONE_HOUR = hours(1);

    /** Constant for on minute. */
    public static final Duration ONE_MINUTE = minutes(1);

    /** Constant for one second. */
    public static final Duration ONE_SECOND = seconds(1);

    /** Constant for one week. */
    public static final Duration ONE_WEEK = days(7);

    /** Constant for one year. */
    public static final Duration ONE_YEAR = years(1);

    /** Pattern to match strings */
    private static final Pattern PATTERN = Pattern.compile(
            "(?x) (?<quantity> [0-9]+ ([.,] [0-9]+)?) "
                    + "(\\s+ | - | _)?"
                    + "(?<units> d | h | m | s | ms | ((millisecond | second | minute | hour | day | week | year) s?))", CASE_INSENSITIVE);

    @Tested
    @UmlMethodGroup("factory")
    public static Duration days(double days)
    {
        return hours(24.0 * days);
    }

    @Tested
    @UmlMethodGroup("factory")
    public static Duration days(long days)
    {
        return hours(24 * days);
    }

    @Tested
    @UmlMethodGroup("factory")
    public static Duration hours(double hours)
    {
        return minutes(60.0 * hours);
    }

    @Tested
    @UmlMethodGroup("factory")
    public static Duration hours(long hours)
    {
        return minutes(60 * hours);
    }

    @Tested
    @UmlMethodGroup("factory")
    public static Duration milliseconds(double milliseconds)
    {
        return milliseconds((long) (milliseconds + 0.5));
    }

    @Tested
    @UmlMethodGroup("factory")
    public static Duration milliseconds(long milliseconds)
    {
        return new Duration(milliseconds, POSITIVE_ONLY);
    }

    @Tested
    @UmlMethodGroup("factory")
    public static Duration minutes(double minutes)
    {
        return seconds(60.0 * minutes);
    }

    @Tested
    @UmlMethodGroup("factory")
    public static Duration minutes(long minutes)
    {
        return seconds(60 * minutes);
    }

    @Tested
    @UmlMethodGroup("factory")
    public static Duration nanoseconds(long nanoseconds)
    {
        return milliseconds(nanoseconds / 1_000_000.0);
    }

    @Tested
    @UmlMethodGroup("parsing")
    public static Duration parseDuration(String value)
    {
        return parseDuration(Listener.throwingListener(), value);
    }

    @Tested
    @UmlMethodGroup("parsing")
    public static Duration parseDuration(Listener listener, String value)
    {
        var matcher = PATTERN.matcher(value);
        if (matcher.matches())
        {
            var quantity = Double.parseDouble(matcher.group("quantity"));
            var units = matcher.group("units");
            if (isOneOf(units, "milliseconds", "millisecond", "ms"))
            {
                return Duration.milliseconds(quantity);
            }
            else if (isOneOf(units, "seconds", "second", "s"))
            {
                return Duration.seconds(quantity);
            }
            else if (isOneOf(units, "minutes", "minute", "m"))
            {
                return Duration.minutes(quantity);
            }
            else if (isOneOf(units, "hours", "hour", "h"))
            {
                return Duration.hours(quantity);
            }
            else if (isOneOf(units, "days", "day", "d"))
            {
                return Duration.days(quantity);
            }
            else if (isOneOf(units, "weeks", "week"))
            {
                return Duration.weeks(quantity);
            }
            else if (isOneOf(units, "years", "year"))
            {
                return Duration.years(quantity);
            }
            else
            {
                listener.problem("Unrecognized units: ${debug}", value);
                return null;
            }
        }
        else
        {
            listener.problem("Unable to parse: ${debug}", value);
            return null;
        }
    }

    @Tested
    @UmlMethodGroup("factory")
    public static Duration seconds(double seconds)
    {
        return milliseconds(seconds * 1000.0);
    }

    @Tested
    @UmlMethodGroup("factory")
    public static Duration seconds(long seconds)
    {
        return milliseconds(seconds * 1000L);
    }

    @Tested
    @UmlMethodGroup("arithmetic")
    public static Duration untilNextSecond()
    {
        var now = Time.now();
        var then = now.roundUp(ONE_SECOND);
        return now.until(then);
    }

    @Tested
    @UmlMethodGroup("factory")
    public static Duration weeks(double scalar)
    {
        return days(7 * scalar);
    }

    @Tested
    @UmlMethodGroup("factory")
    public static Duration years(double scalar)
    {
        return weeks(52 * scalar);
    }

    /**
     * The range of values allowed for a given duration.
     * <p>
     * Negative durations cannot be directly constructed, but they can result from a subtract operation if that
     * operation explicitly allows negative values. This permits a sequence of operations to produce a valid positive
     * duration at the end, while steps in the computation along the way may temporarily result in negative durations.
     */
    public enum Restriction
    {
        POSITIVE_ONLY,
        ALLOW_NEGATIVE
    }

    /**
     * For reflective construction
     */
    @NoTestRequired
    @UmlExcludeMember
    protected Duration()
    {
    }

    /**
     * Protected constructor forces use of static factory methods.
     *
     * @param milliseconds Number of milliseconds in this <code>Duration</code>
     */
    @NoTestRequired
    @UmlExcludeMember
    protected Duration(long milliseconds, Restriction restriction)
    {
        super(milliseconds);

        if (restriction == POSITIVE_ONLY && milliseconds < 0)
        {
            throw new IllegalArgumentException("Negative time not allowed");
        }
    }

    @NoTestRequired
    @UmlMethodGroup("conversion")
    public Frequency asFrequency()
    {
        return Frequency.every(this);
    }

    /**
     * @return A String representation of the time of week represented by this duration, assuming it starts on Monday,
     * 00:00, modulo the length of a week.
     */
    @Tested
    @UmlMethodGroup("arithmetic")
    public String fromStartOfWeekModuloWeekLength()
    {
        // There are 10080 minutes in a week
        var duration = (int) Math.floor(asMinutes() % 10080);
        // There are 1440 minutes in a day
        var days = duration / (1440);
        var hoursRest = duration % 1440;
        // There are 60 minutes in an hour
        var hours = hoursRest / 60;
        var minutes = hoursRest % 60;
        // Ex. "TUESDAY, 15:32"
        return isoDayOfWeek(days) + ", "
                + (String.valueOf(hours).length() == 2 ? hours : ("0" + hours)) + ":"
                + (String.valueOf(minutes).length() == 2 ? minutes : ("0" + minutes));
    }

    @Override
    @UmlMethodGroup("arithmetic")
    public Duration maximum()
    {
        return FOREVER;
    }

    @Override
    @UmlMethodGroup("units")
    public long millisecondsPerUnit()
    {
        return 1;
    }

    @Override
    @Tested
    @UmlMethodGroup("arithmetic")
    public Duration minimum(Duration that)
    {
        return isLessThan(that) ? this : that;
    }

    @Override
    @UmlMethodGroup("arithmetic")
    public Duration minimum()
    {
        return INSTANTANEOUS;
    }

    /**
     * @return This duration minus that duration, but never a negative value
     */
    @Tested
    @UmlMethodGroup("arithmetic")
    public Duration minus(Duration that)
    {
        return minus(that, POSITIVE_ONLY);
    }

    /**
     * @return This duration minus that duration, but restricted to the given range
     */
    @Tested
    @UmlMethodGroup("arithmetic")
    public Duration minus(Duration that, Restriction restriction)
    {
        if (restriction == POSITIVE_ONLY)
        {
            if (that.isGreaterThan(this))
            {
                return INSTANTANEOUS;
            }
        }
        return new Duration(milliseconds() - that.milliseconds(), restriction);
    }

    @Tested
    @UmlMethodGroup("arithmetic")
    public Duration nearestHour()
    {
        return nearest(hours(1));
    }

    @Override
    @UmlExcludeMember
    public Duration newDuration(long milliseconds)
    {
        return milliseconds(milliseconds);
    }

    /**
     * @return The sum of this duration and that one, but never a negative value.
     */
    @Tested
    @UmlExcludeMember
    public Duration plus(Duration that)
    {
        return plus(that, POSITIVE_ONLY);
    }

    /**
     * @return The sum of this duration and that duration, but restricted to the given range
     */
    @Tested
    @UmlMethodGroup("arithmetic")
    @UmlExcludeMember
    public Duration plus(Duration that, Restriction restriction)
    {
        var sum = milliseconds() + that.milliseconds();
        if (restriction == POSITIVE_ONLY && sum < 0)
        {
            return INSTANTANEOUS;
        }
        return new Duration(sum, restriction);
    }

    @Override
    public String toString()
    {
        return asHumanReadableString();
    }
}
