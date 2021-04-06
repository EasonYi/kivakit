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

package com.telenav.kivakit.core.kernel.data.conversion.string.language;

import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramDataConversionOther;

import java.util.regex.Pattern;

/**
 * Converts between {@link Class} objects and fully-qualified class names.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramDataConversionOther.class)
public class PatternConverter extends BaseStringConverter<Pattern>
{
    /**
     * @param listener The listener to hear any conversion issues
     */
    public PatternConverter(final Listener listener)
    {
        super(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Pattern onConvertToObject(final String text)
    {
        return Pattern.compile(text);
    }
}
