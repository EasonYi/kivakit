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

package com.telenav.kivakit.interfaces.lifecycle;

import com.telenav.kivakit.interfaces.lexakai.DiagramLifeCycle;
import com.telenav.kivakit.interfaces.time.LengthOfTime;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * An operation that can be stopped
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLifeCycle.class)
@LexakaiJavadoc(complete = true)
public interface Stoppable extends Operation
{
    /**
     * Stops this task, waiting no more than the given wait time before giving up.
     */
    void stop(LengthOfTime wait);

    /**
     * Stops this task, blocking until the operation is completed
     */
    default void stop()
    {
        stop(LengthOfTime.MAXIMUM);
    }
}
