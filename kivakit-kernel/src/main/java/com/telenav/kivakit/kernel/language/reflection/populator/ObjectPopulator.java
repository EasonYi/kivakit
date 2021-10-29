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

package com.telenav.kivakit.kernel.language.reflection.populator;

import com.telenav.kivakit.kernel.language.reflection.Type;
import com.telenav.kivakit.kernel.language.reflection.property.PropertyFilter;
import com.telenav.kivakit.kernel.language.reflection.property.PropertyValueSource;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageReflection;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Populates object properties given a property filter and a source of property values.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageReflection.class)
public class ObjectPopulator
{
    private final PropertyValueSource source;

    private final Listener listener;

    private final PropertyFilter filter;

    public ObjectPopulator(Listener listener, PropertyFilter filter, PropertyValueSource source)
    {
        this.listener = listener;
        this.filter = filter;
        this.source = source;
    }

    /**
     * Populates the given object using the values obtained from the property value source
     *
     * @param object The object to populate
     * @return The populated object (for method chaining)
     */
    public <T> T populate(T object)
    {
        // Go through each property on the object,
        for (var property : Type.of(object).properties(filter))
        {
            // get any value for the given property,
            var value = source.valueFor(property);

            // and if the value is non-null,
            if (value != null)
            {
                // set the property value,
                var message = property.set(listener, object, value);

                // and if something went wrong,
                if (message instanceof Problem)
                {
                    // notify any listeners.
                    listener.receive(message);
                }
            }
            else
            {
                if (!property.isOptional())
                {
                    listener.warning("No value found for property: $", property.name());
                }
            }
        }

        return object;
    }
}
