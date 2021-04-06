////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.resource.spi;

import com.telenav.kivakit.core.resource.Resource;
import com.telenav.kivakit.core.resource.ResourceFolderIdentifier;
import com.telenav.kivakit.core.resource.ResourceIdentifier;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramResourceService;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

/**
 * Service provider interface that resolves resource identifiers
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramResourceService.class)
@UmlRelation(label = "parses", referent = ResourceFolderIdentifier.class)
public interface ResourceResolver
{
    /**
     * @return True if this resource factory understands the given resource identifier
     */
    boolean accepts(ResourceIdentifier identifier);

    /**
     * @return A new resource for the given resource identifier
     */
    @UmlRelation(label = "creates")
    Resource resolve(ResourceIdentifier identifier);
}
