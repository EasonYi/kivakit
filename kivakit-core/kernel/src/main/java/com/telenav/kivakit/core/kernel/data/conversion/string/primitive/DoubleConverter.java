////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.data.conversion.string.primitive;

import com.telenav.kivakit.core.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramDataConversionPrimitive;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

/**
 * Converts a double to and from a string.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramDataConversionPrimitive.class)
public class DoubleConverter extends BaseStringConverter<Double>
{
    /**
     * @param listener The listener to hear any conversion issues
     */
    public DoubleConverter(final Listener listener)
    {
        super(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @UmlExcludeMember
    protected Double onConvertToObject(final String value)
    {
        return Double.valueOf(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @UmlExcludeMember
    protected String onConvertToString(final Double value)
    {
        if (Double.isNaN(value) || Double.isInfinite(value))
        {
            return "N/A";
        }
        else
        {
            return value.toString();
        }
    }
}
