package com.telenav.kivakit.core.kernel.data.validation.reporters;

import com.telenav.kivakit.core.kernel.data.validation.BaseValidationReporter;
import com.telenav.kivakit.core.kernel.messaging.Message;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramDataValidationReporter;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

/**
 * A validation reporter that asserts false when it hears a message
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramDataValidationReporter.class)
public class AssertingValidationReporter extends BaseValidationReporter
{
    @Override
    @UmlExcludeMember
    public void report(final Message message)
    {
        assert false : message.description();
    }
}
