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

package com.telenav.kivakit.core.value.level;

import com.telenav.kivakit.core.language.primitive.Doubles;
import com.telenav.kivakit.core.lexakai.DiagramCount;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.string.Strip;
import com.telenav.kivakit.interfaces.numeric.Arithmetic;
import com.telenav.kivakit.interfaces.numeric.Percentage;
import com.telenav.kivakit.interfaces.numeric.QuantumComparable;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.telenav.kivakit.core.ensure.Ensure.unsupported;

/**
 * A percentage of any range (not only from 0 to 100%). A percent object can be created with {@link #percent(double)},
 * passing in a percentage, like 50 or 90 to get 50% or 90%.
 *
 * <p><b>Operations</b></p>
 *
 * <ul>
 *     <li>{@link #dividedBy(double)} - This percentage divided by the given value</li>
 *     <li>{@link #inverse()} - This percentage's distance from 100%</li>
 *     <li>{@link #minus(Percent)} - This percentage minus the given percentage</li>
 *     <li>{@link #plus(Percent)} - This percentage plus the given percentage</li>
 *     <li>{@link #scale(double)} - The given value scaled by this percentage</li>
 *     <li>{@link #scale(int)} - The given value scaled by this percentage</li>
 *     <li>{@link #scale(long)} - The given value scaled by this percentage</li>
 *     <li>{@link #times(double)} - This percentage times the given percentage</li>
 * </ul>
 *
 * <p><b>Conversions</b></p>
 *
 * <ul>
 *     <li>{@link #asInt()} - This percentage rounded to an int value</li>
 *     <li>{@link #unitValue()} - This percentage as a parametric value (divided by 100.0)</li>
 *     <li>{@link #unitValue()} - This value as a unit value between 0 and 1, inclusive </li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramCount.class)
@LexakaiJavadoc(complete = true)
public final class Percent implements
        Comparable<Percent>,
        Percentage,
        QuantumComparable<Percent>,
        Arithmetic<Percent>
{
    public static final Percent _0 = new Percent(0);

    public static final Percent _50 = new Percent(50);

    public static final Percent _100 = new Percent(100);

    public static Percent parsePercent(Listener listener, String value)
    {
        return Percent.percent(Double.parseDouble(value.endsWith("%")
                ? Strip.ending(value, "%")
                : value));
    }

    public static Percent percent(double percent)
    {
        return new Percent(percent);
    }

    private double percent;

    Percent(double percent)
    {
        this.percent = percent;
    }

    private Percent()
    {
    }

    public int asInt()
    {
        return (int) percent;
    }

    public Level asLevel()
    {
        return new Level(unitValue());
    }

    @Override
    public int compareTo(@NotNull Percent that)
    {
        return Double.compare(percent, that.percent);
    }

    public Percent dividedBy(double divisor)
    {
        return new Percent(percent / divisor);
    }

    @Override
    public Percent dividedBy(Percent percent)
    {
        return unsupported();
    }

    @Override
    public double doubleQuantum()
    {
        return percent();
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof Percent)
        {
            Percent that = (Percent) object;
            return percent == that.percent;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(percent);
    }

    public Percent inverse()
    {
        return new Percent(100.0 - unitValue());
    }

    @Override
    public Percent minus(Percent that)
    {
        return new Percent(percent - that.percent);
    }

    @Override
    public double percent()
    {
        return percent;
    }

    @Override
    public Percent plus(Percent that)
    {
        return new Percent(percent + that.percent);
    }

    @Override
    public long quantum()
    {
        return (long) percent();
    }

    public double scale(double value)
    {
        return value * unitValue();
    }

    public long scale(long value)
    {
        return (long) (value * unitValue());
    }

    public int scale(int value)
    {
        return (int) (value * unitValue());
    }

    @Override
    public Percent times(Percent percent)
    {
        return times(percent.unitValue());
    }

    public Percent times(double scaleFactor)
    {
        return new Percent(percent * scaleFactor);
    }

    @Override
    public String toString()
    {
        return Doubles.format(percent, 1) + "%";
    }

    /**
     * @return This percentage as a value from 0 to 1, both inclusive.
     */
    @Override
    public double unitValue()
    {
        return Doubles.inRange(percent / 100.0, 0.0, 1.0);
    }
}
