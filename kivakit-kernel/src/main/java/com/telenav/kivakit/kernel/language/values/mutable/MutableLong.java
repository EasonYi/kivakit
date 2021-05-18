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

package com.telenav.kivakit.kernel.language.values.mutable;

import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageValue;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A long value that can be changed. Useful for mutating something other than a field from within a lambda expression.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageValue.class)
@LexakaiJavadoc(complete = true)
public class MutableLong extends MutableValue<Long>
{
    public MutableLong()
    {
    }

    public MutableLong(final Long value)
    {
        super(value);
    }

    public void maximum(final long value)
    {
        set(Math.max(get(), value));
    }

    public void minimum(final long value)
    {
        set(Math.min(get(), value));
    }
}
