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

package com.telenav.kivakit.core.messaging.messages.status;

import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.core.messaging.messages.OperationStatusMessage;
import com.telenav.kivakit.core.messaging.messages.Severity;
import com.telenav.kivakit.core.messaging.messages.lifecycle.OperationHalted;
import com.telenav.kivakit.core.lexakai.DiagramMessageType;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A problem that is severe enough to result in data loss, but not severe enough to halt the current operation. If the
 * operation does not succeed, {@link OperationHalted} should be used instead.
 *
 * <p>
 * {@link OperationStatusMessage}s in order of importance:
 * </p>
 * <ul>
 *     <li>Critical Alert - An operation failed and needs <i>immediate attention</i> from a human operator</li>
 *     <li>Alert - An operation failed and needs to be looked at by an operator soon</li>
 *     <li>FatalProblem - An unrecoverable problem has caused an operation to fail and needs to be addressed</li>
 *     <li><b>Problem</b> - Something has gone wrong and needs to be addressed, but it's not fatal to the current operation</li>
 *     <li>Glitch - A minor problem has occurred. Unlike a Warning, a Glitch indicates validation failure or minor data loss has occurred. Unlike a Problem, a Glitch indicates that the operation will definitely recover and continue.</li>
 *     <li>Warning - A minor issue occurred which should be corrected, but does not necessarily require attention</li>
 *     <li>Quibble - A trivial issue that does not require correction</li>
 *     <li>Announcement - Announcement of an important phase of an operation</li>
 *     <li>Narration - A step in some operation has started or completed</li>
 *     <li>Information - Commonly useful information that doesn't represent any problem</li>
 *     <li>Trace - Diagnostic information for use when debugging</li>
 * </ul>
 * <p>
 *  @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMessageType.class)
public class Problem extends OperationStatusMessage
{
    public Problem(String message, Object... arguments)
    {
        super(message);
        arguments(arguments);
    }

    public Problem(Throwable cause, String message, Object... arguments)
    {
        super(message + (cause == null ? "" : ": " + Message.escape(
                cause.getMessage() == null
                        ? cause.getClass().toString()
                        : cause.getMessage())));
        cause(cause);
        arguments(arguments);
    }

    public Problem()
    {
    }

    @Override
    public Severity severity()
    {
        return Severity.MEDIUM_HIGH;
    }

    @Override
    public Status status()
    {
        return Status.PROBLEM;
    }
}
