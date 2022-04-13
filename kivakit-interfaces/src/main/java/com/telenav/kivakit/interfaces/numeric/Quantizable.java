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

import com.telenav.kivakit.interfaces.collection.Indexed;
import com.telenav.kivakit.interfaces.collection.LongKeyed;
import com.telenav.kivakit.interfaces.lexakai.DiagramNumeric;
import com.telenav.kivakit.interfaces.model.Identifiable;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A quantizable object can be turned into a quantum value.
 *
 * <p>
 * A quantum is a discrete value that can be retrieved by calling the functional interface method {@link #quantum()} .
 * Quanta are a unification point among classes which have a quantum representation, allowing certain operations to be
 * applied to quantizable objects uniformly (such as comparison, converting to and from string representations, or using
 * a quantum value as an index or identifier in a map or data store).
 * </p>
 *
 * <p>
 * Examples of quantizable objects include objects that are {@link Indexed}, {@link Identifiable} or {@link LongKeyed}.
 * In addition, most objects representing counting or time are quantizable.
 * </p>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@FunctionalInterface
@UmlClassDiagram(diagram = DiagramNumeric.class)
public interface Quantizable extends DoubleQuantizable
{
    /**
     * {@inheritDoc}
     *
     * <p>
     * All {@link Quantizable} objects are {@link DoubleQuantizable}
     * </p>
     */
    @Override
    default double doubleQuantum()
    {
        return quantum();
    }

    default boolean isNonZero()
    {
        return quantum() != 0;
    }

    default boolean isZero()
    {
        return quantum() == 0;
    }

    /**
     * Returns the discrete value for this object
     */
    long quantum();
}
