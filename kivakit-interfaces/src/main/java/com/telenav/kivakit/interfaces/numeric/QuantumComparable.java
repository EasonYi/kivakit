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

package com.telenav.kivakit.interfaces.numeric;

import com.telenav.kivakit.interfaces.lexakai.DiagramNumeric;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A {@link QuantumComparable} is a {@link Comparable} and {@link Quantizable} object that compares quanta.
 *
 * <p><b>Comparisons</b></p>
 *
 * <p>
 * {@link QuantumComparable} values can be compared in several ways:
 * </p>
 *
 * <ul>
 *     <li>{@link #isApproximately(Value, Value)}</li>
 *     <li>{@link #isBetweenExclusive(Quantizable, Quantizable)}</li>
 *     <li>{@link #isBetweenInclusive(Quantizable, Quantizable)}</li>
 *     <li>{@link #isGreaterThan(Value)}</li>
 *     <li>{@link #isGreaterThanOrEqualTo(Value)}</li>
 *     <li>{@link #isLessThan(Value)}</li>
 *     <li>{@link #isLessThanOrEqualTo(Value)}</li>
 *     <li>{@link #isNonZero()}</li>
 *     <li>{@link #isZero()}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramNumeric.class)
public interface QuantumComparable<Value extends Quantizable> extends Quantizable
{
    default boolean isApproximately(Value that, Value within)
    {
        return Math.abs(quantum() - that.quantum()) <= within.quantum();
    }

    default boolean isBetweenExclusive(Value a, Value b)
    {
        return isGreaterThanOrEqualTo(a) && isLessThan(b);
    }

    default boolean isBetweenInclusive(Value a, Value b)
    {
        return isGreaterThanOrEqualTo(a) && isLessThanOrEqualTo(b);
    }

    default boolean isGreaterThan(Value that)
    {
        return quantum() > that.quantum();
    }

    default boolean isGreaterThanOrEqualTo(Value that)
    {
        return quantum() >= that.quantum();
    }

    default boolean isLessThan(Value that)
    {
        return quantum() < that.quantum();
    }

    default boolean isLessThanOrEqualTo(Value that)
    {
        return quantum() <= that.quantum();
    }

    default boolean isNonZero()
    {
        return quantum() != 0;
    }

    default boolean isZero()
    {
        return quantum() == 0;
    }
}
