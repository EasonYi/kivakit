////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.interfaces.lifecycle;

import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramInterfaceLifeCycle;

/**
 * Pause and resume an operation.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramInterfaceLifeCycle.class)
public interface Pausable extends Operation
{
    /**
     * Pause the activity
     */
    void pause();

    /**
     * Resume the activity
     */
    void resume();
}
