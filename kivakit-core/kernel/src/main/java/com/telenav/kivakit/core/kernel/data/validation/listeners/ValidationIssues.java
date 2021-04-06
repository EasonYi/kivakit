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

package com.telenav.kivakit.core.kernel.data.validation.listeners;

import com.telenav.kivakit.core.kernel.messaging.listeners.MessageCounter;
import com.telenav.kivakit.core.kernel.messaging.listeners.MessageList;
import com.telenav.kivakit.core.kernel.messaging.Message;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramDataValidation;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A {@link MessageList} and {@link MessageCounter} that captures validation issues. The number of issues of different
 * types can be retrieved with {@link #count(Class)}, passing in the type of message for which a count is desired. The
 * method {@link #countWorseThanOrEqualTo(Message.Status)} gives a count of all messages that are at least as bad or
 * worse than the given message status value. For example, <i>countWorseThanOrEqualTo(Status.PROBLEM)</i>.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramDataValidation.class)
public class ValidationIssues extends MessageList
{
    public ValidationIssues()
    {
        // Keep anything that doesn't represent outright success
        super(message -> !message.status().succeeded());
    }
}
