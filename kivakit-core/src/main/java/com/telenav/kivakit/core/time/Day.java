package com.telenav.kivakit.core.time;

import com.telenav.kivakit.core.language.primitive.Longs;
import com.telenav.kivakit.core.test.NoTestRequired;
import com.telenav.kivakit.core.test.Tested;
import com.telenav.kivakit.core.time.DayOfWeek.Standard;
import com.telenav.kivakit.interfaces.lexakai.DiagramTimeDuration;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.UmlMethodGroup;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import java.util.Objects;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static com.telenav.kivakit.core.time.Day.Type.DAY;
import static com.telenav.kivakit.core.time.Day.Type.DAY_OF_MONTH;
import static com.telenav.kivakit.core.time.Day.Type.DAY_OF_UNIX_EPOCH;
import static com.telenav.kivakit.core.time.Day.Type.DAY_OF_WEEK;
import static com.telenav.kivakit.core.time.Day.Type.DAY_OF_YEAR;
import static com.telenav.kivakit.core.time.DayOfWeek.Standard.ISO;
import static com.telenav.kivakit.core.time.DayOfWeek.Standard.JAVA;
import static com.telenav.kivakit.core.time.DayOfWeek.isoDayOfWeek;
import static com.telenav.kivakit.core.time.DayOfWeek.javaDayOfWeek;
import static java.lang.Integer.MAX_VALUE;

/**
 * Represents the day of a point in time, including:
 *
 * <ul>
 *     <li>DAY</li>
 *     <li>DAY_OF_UNIX_EPOCH</li>
 *     <li>DAY_OF_MONTH</li>
 *     <li>DAY_OF_WEEK</li>
 *     <li>DAY_OF_YEAR</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@Tested
@UmlClassDiagram(diagram = DiagramTimeDuration.class)
public class Day extends BaseTime<Day>
{
    private static final int millisecondsPerDay = 24 * 60 * 60 * 1_000;

    /**
     * @return A day since the start of a month, from 1 to 31
     */
    @Tested
    @UmlMethodGroup("factory")
    public static Day dayOfMonth(long day)
    {
        return new Day(DAY_OF_MONTH, ISO, day);
    }

    /**
     * @return A day since the start of the UNIX epoch, from 0 to n
     */
    @Tested
    @UmlMethodGroup("factory")
    public static Day dayOfUnixEpoch(long day)
    {
        return new Day(DAY_OF_UNIX_EPOCH, ISO, day);
    }

    /**
     * @return A day since the start of the week in the given day-of-week {@link Standard}
     */
    @Tested
    @UmlMethodGroup("factory")
    public static Day dayOfWeek(long day, Standard standard)
    {
        return new Day(DAY_OF_WEEK, standard, day);
    }

    /**
     * @return A day since the start of the year, from 0 to 365 (in leap years)
     */
    @Tested
    @UmlMethodGroup("factory")
    public static Day dayOfYear(long day)
    {
        return new Day(DAY_OF_YEAR, ISO, day);
    }

    /**
     * @return An absolute day from 0 to n
     */
    @Tested
    @UmlMethodGroup("factory")
    public static Day days(long day)
    {
        return new Day(DAY, ISO, day);
    }

    /**
     * The type of day
     */
    @NoTestRequired
    @UmlClassDiagram(diagram = DiagramTimeDuration.class)
    public enum Type
    {
        /** A number of days */
        DAY,

        /** A day of the UNIX epoch */
        DAY_OF_UNIX_EPOCH,

        /** A day of the month (from 1 to */
        DAY_OF_MONTH,

        /** A day of the week (stored as ISO, from 0 to 6, inclusive) */
        DAY_OF_WEEK,

        /** A day of the year */
        DAY_OF_YEAR
    }

    private Standard standard;

    /** The type of day this is */
    @UmlAggregation(label = "is of type")
    private Type type;

    @NoTestRequired
    @UmlExcludeMember
    protected Day(Type type, Standard standard, long day)
    {
        super(day * millisecondsPerDay);

        this.type = type;
        this.standard = standard;

        ensure(isValid());
    }

    /**
     * @return This day as a day of the week if it is a day of the week, otherwise, an exception will be thrown.
     */
    @Tested
    @UmlMethodGroup("conversion")
    public DayOfWeek asDayOfWeek()
    {
        ensure(type() == DAY_OF_WEEK);

        return standard == JAVA
                ? javaDayOfWeek(asUnits())
                : isoDayOfWeek(asUnits());
    }

    /**
     * @return This day as a zero-based index
     */
    @Tested
    @UmlMethodGroup("conversion")
    public int asIndex()
    {
        switch (type())
        {
            case DAY:
            case DAY_OF_UNIX_EPOCH:
            case DAY_OF_YEAR:
                return (int) asUnits();

            case DAY_OF_WEEK:
                return (int) (standard == JAVA
                        ? asUnits() - 1
                        : asUnits());

            case DAY_OF_MONTH:
                return (int) (asUnits() - 1);

            default:
                return unsupported();
        }
    }

    public long day()
    {
        return asUnits();
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof Day)
        {
            Day that = (Day) object;
            return this.asUnits() == that.asUnits()
                    && type() == that.type();
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(asUnits(), type());
    }

    /**
     * @return False if this day is invalid, true if it might be valid. If true is returned, the day might still be
     * invalid in some context, for example, the 31st day of the month doesn't exist for all months.
     */
    @Tested
    @UmlMethodGroup("validation")
    public boolean isValid()
    {
        switch (type)
        {
            case DAY:
                return asUnits() >= 0;

            case DAY_OF_MONTH:
                return Longs.isBetweenInclusive(asUnits(), 1, 31);

            case DAY_OF_WEEK:
                if (standard() == ISO)
                {
                    return Longs.isBetweenInclusive(asUnits(), 0, 6);
                }
                if (standard() == JAVA)
                {
                    return Longs.isBetweenInclusive(asUnits(), 1, 7);
                }
                return unsupported();

            case DAY_OF_YEAR:
                return Longs.isBetweenInclusive(asUnits(), 0, 365);

            case DAY_OF_UNIX_EPOCH:
                return Longs.isBetweenInclusive(asUnits(), 0, MAX_VALUE);

            default:
                return unsupported();
        }
    }

    @Override
    @UmlMethodGroup("arithmetic")
    public Day maximum(Day that)
    {
        return isGreaterThan(that) ? this : that;
    }

    @Override
    @UmlMethodGroup("arithmetic")
    public Day maximum()
    {
        return null;
    }

    @Override
    @UmlMethodGroup("inits")
    public long millisecondsPerUnit()
    {
        return millisecondsPerDay;
    }

    @Override
    @UmlMethodGroup("arithmetic")
    public Day minimum(Day that)
    {
        return isLessThan(that) ? this : that;
    }

    @Override
    @UmlMethodGroup("arithmetic")
    public Day minimum()
    {
        return null;
    }

    @Override
    @UmlExcludeMember
    public Duration newDuration(long milliseconds)
    {
        return Duration.milliseconds(milliseconds);
    }

    @Override
    public Day newTime(long epochMilliseconds)
    {
        var day = Day.days(units(epochMilliseconds));
        day.standard = this.standard;
        day.type = this.type;
        return day;
    }

    @NoTestRequired
    @UmlExcludeMember
    public Standard standard()
    {
        return standard;
    }

    @Override
    public String toString()
    {
        switch (type())
        {
            case DAY_OF_WEEK:
                return asDayOfWeek().toString();

            case DAY:
            case DAY_OF_YEAR:
            case DAY_OF_UNIX_EPOCH:
            case DAY_OF_MONTH:
            default:
                return "day " + day();
        }
    }

    @Tested
    @UmlMethodGroup("properties")
    public Type type()
    {
        return type;
    }
}
