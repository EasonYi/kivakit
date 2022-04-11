package com.telenav.kivakit.core.time;

import com.telenav.kivakit.core.language.primitive.Longs;
import com.telenav.kivakit.core.test.NoTestRequired;
import com.telenav.kivakit.core.test.Tested;
import com.telenav.kivakit.interfaces.lexakai.DiagramTimeDuration;
import com.telenav.kivakit.interfaces.time.LengthOfTime;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.UmlMethodGroup;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import java.util.Objects;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static com.telenav.kivakit.core.time.Hour.Type.HOUR;
import static com.telenav.kivakit.core.time.Hour.Type.HOUR_OF_MERIDIEM;
import static com.telenav.kivakit.core.time.Hour.Type.MILITARY_HOUR;
import static com.telenav.kivakit.core.time.HourOfWeek.hourOfWeek;
import static com.telenav.kivakit.core.time.Meridiem.AM;
import static com.telenav.kivakit.core.time.Meridiem.NO_MERIDIEM;
import static com.telenav.kivakit.core.time.Meridiem.PM;
import static com.telenav.kivakit.core.time.Meridiem.meridiemHour;

/**
 * Represents an hour of the day.
 *
 * <p><b>Creation</b></p>
 *
 * <ul>
 *     <li>{@link #am(long)} - A morning hour</li>
 *     <li>{@link #militaryHour(long)} - An hour of the day on a 24-hour clock</li>
 *     <li>{@link #hourOfDay(long, Meridiem)} - An hour of the day, AM or PM</li>
 *     <li>{@link #midnight()} - Hour zero</li>
 *     <li>{@link #noon()} - Hour twelve</li>
 *     <li>{@link #pm(long)} - An afternoon or evening hour</li>
 * </ul>
 *
 * <p><b>Accessors</b></p>
 *
 * <ul>
 *     <li>{@link #asUnits()} ()} - The number of hours</li>
 *     <li>{@link #meridiem()} - AM, PM or 24-hour</li>
 * </ul>
 *
 * <p><b>Conversions</b></p>
 *
 * <p>
 * In addition to the conversions provided by {@link LengthOfTime}, these conversions are provided:
 * </p>
 *
 * <ul>
 *     <li>{@link #asMilitaryHour()} - The hour on a 24-hour clock</li>
 *     <li>{@link #asMeridiemHour()} - The hour, either AM or PM</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings({ "unused", "SpellCheckingInspection" })
@UmlClassDiagram(diagram = DiagramTimeDuration.class)
public class Hour extends BaseDuration<Hour>
{
    private static final long millisecondsPerHour = 60 * 60 * 1_000;

    @Tested
    @UmlMethodGroup("factory")
    public static Hour am(long meridiemHour)
    {
        return hourOfDay(meridiemHour, AM);
    }

    @Tested
    @UmlMethodGroup("factory")
    public static Hour hour(long militaryHour)
    {
        return new Hour(HOUR, NO_MERIDIEM, militaryHour);
    }

    @Tested
    @UmlMethodGroup("factory")
    public static Hour hourOfDay(long hour, Meridiem meridiem)
    {
        return new Hour(HOUR_OF_MERIDIEM, meridiem, hour);
    }

    @Tested
    @UmlMethodGroup("factory")
    public static Hour midnight()
    {
        return am(12);
    }

    @Tested
    @UmlMethodGroup("factory")
    public static Hour militaryHour(long militaryHouor)
    {
        return new Hour(MILITARY_HOUR, NO_MERIDIEM, militaryHouor);
    }

    @Tested
    @UmlMethodGroup("factory")
    public static Hour noon()
    {
        return pm(12);
    }

    @Tested
    @UmlMethodGroup("factory")
    public static Hour pm(long meridiemHour)
    {
        return hourOfDay(meridiemHour, PM);
    }

    @SuppressWarnings("UnusedReturnValue")
    @NoTestRequired
    @UmlClassDiagram(diagram = DiagramTimeDuration.class)
    public enum Type
    {
        HOUR(0, Integer.MAX_VALUE),
        HOUR_OF_WEEK(0, 7 * 24),
        MILITARY_HOUR(0, 24),
        HOUR_OF_MERIDIEM(1, 12 + 1);

        private final int maximumExclusive;

        private final int minimum;

        @NoTestRequired
        Type(int minimum, int maximumExclusive)
        {
            this.minimum = minimum;
            this.maximumExclusive = maximumExclusive;
        }

        @Tested
        long ensureInRange(long hour)
        {
            ensure(Longs.isBetweenExclusive(hour, minimum, maximumExclusive), "Hour not valid: $", hour);
            return hour;
        }

        @Tested
        long militaryHour(Meridiem meridiem, long hour)
        {
            ensureInRange(hour);

            if (this == Type.HOUR_OF_MERIDIEM)
            {
                return meridiem.asMilitaryHour(hour);
            }
            return hour;
        }
    }

    @UmlAggregation(label = "has")
    private final Meridiem meridiem;

    /** The type of hour being modeled */
    @UmlAggregation(label = "is of type")
    private final Type type;

    @Tested
    @UmlExcludeMember
    protected Hour(Type type, Meridiem meridiem, long hour)
    {
        super(type.militaryHour(meridiem, hour) * millisecondsPerHour);

        this.type = ensureNotNull(type);
        this.meridiem = ensureNotNull(meridiem);
    }

    @NoTestRequired
    @UmlMethodGroup("conversion")
    public HourOfWeek asHourOfWeek()
    {
        return hourOfWeek(asMilitaryHour());
    }

    /**
     * Returns this hour of the day on a 12-hour AM/PM clock
     */
    @Tested
    @UmlMethodGroup("conversion")
    public long asMeridiemHour()
    {
        return meridiemHour(asMilitaryHour());
    }

    /**
     * Returns this hour of the day on a 24-hour clock
     */
    @Tested
    @UmlMethodGroup("conversion")
    public int asMilitaryHour()
    {
        return (int) (milliseconds() / millisecondsPerHour);
    }

    @Override
    @Tested
    public boolean equals(final Object object)
    {
        if (object instanceof Hour)
        {
            var that = (Hour) object;
            return this.quantum() == that.quantum();
        }
        return false;
    }

    @Override
    @Tested
    public int hashCode()
    {
        return Objects.hash(quantum());
    }

    @Tested
    @UmlMethodGroup("conversion")
    public boolean isMeridiem()
    {
        return type() == HOUR_OF_MERIDIEM;
    }

    @Tested
    @UmlMethodGroup("conversion")
    public boolean isMilitary()
    {
        return type() == MILITARY_HOUR;
    }

    @Override
    @UmlMethodGroup("arithmetic")
    public Hour maximum(Hour that)
    {
        return isGreaterThan(that) ? this : that;
    }

    @Override
    @UmlMethodGroup("arithmetic")
    public Hour maximum()
    {
        switch (type())
        {
            case HOUR:
                return newDuration(Long.MAX_VALUE);

            case MILITARY_HOUR:
            case HOUR_OF_MERIDIEM:
                return militaryHour(23);

            case HOUR_OF_WEEK:
                return hour(7 * 24 - 1);

            default:
                return unsupported();
        }
    }

    /**
     * Returns the {@link Meridiem} for this hour of the day
     */
    @UmlMethodGroup("properties")
    public Meridiem meridiem()
    {
        return meridiem;
    }

    @Override
    @UmlMethodGroup("units")
    public long millisecondsPerUnit()
    {
        return millisecondsPerHour;
    }

    @Override
    @UmlMethodGroup("arithmetic")
    public Hour minimum(Hour that)
    {
        return isLessThan(that) ? this : that;
    }

    @Override
    @UmlMethodGroup("arithmetic")
    public Hour minimum()
    {
        return militaryHour(0);
    }

    @Override
    @UmlExcludeMember
    public Hour newDuration(long milliseconds)
    {
        long militaryHour = (milliseconds / millisecondsPerUnit() + 24) % 24;

        if (isMeridiem())
        {
            return hourOfDay(militaryHour % 12, Meridiem.meridiem(militaryHour));
        }

        return militaryHour(militaryHour);
    }

    @Override
    @Tested
    @UmlExcludeMember
    public Hour newTimeOrDurationFromUnits(long units)
    {
        var rounded = (units + 24) % 24;
        return super.newTimeOrDurationFromUnits(rounded + 1);
    }

    @Override
    @UmlExcludeMember
    public long quantum()
    {
        return asMilitaryHour();
    }

    @Override
    @Tested
    public String toString()
    {
        switch (type())
        {
            case HOUR:
            case HOUR_OF_WEEK:
            case MILITARY_HOUR:
                return Long.toString(asUnits());

            case HOUR_OF_MERIDIEM:
                return asMeridiemHour() + meridiem().name().toLowerCase();

            default:
                return unsupported();
        }
    }

    @UmlMethodGroup("properties")
    public Type type()
    {
        return type;
    }
}
