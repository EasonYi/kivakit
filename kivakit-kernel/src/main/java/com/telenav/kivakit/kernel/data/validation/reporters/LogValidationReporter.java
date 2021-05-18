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

package com.telenav.kivakit.kernel.data.validation.reporters;

import com.telenav.kivakit.kernel.data.validation.BaseValidationReporter;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramDataValidationReporter;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

/**
 * A validation reporter that logs any messages reported to it.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramDataValidationReporter.class)
public class LogValidationReporter extends BaseValidationReporter
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    @Override
    @UmlExcludeMember
    public void report(final Message message)
    {
        LOGGER.log(message);
    }
}
