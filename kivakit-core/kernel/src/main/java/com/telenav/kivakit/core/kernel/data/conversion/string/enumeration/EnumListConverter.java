////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.data.conversion.string.enumeration;

import com.telenav.kivakit.core.kernel.data.conversion.string.collection.BaseListConverter;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramDataConversionOther;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Converts lists of enum values to and from comma-separated strings.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramDataConversionOther.class)
public class EnumListConverter<T extends Enum<T>> extends BaseListConverter<T>
{
    public EnumListConverter(final Listener listener, final Class<T> enumType)
    {
        super(listener, new EnumConverter<>(listener, enumType), DEFAULT_DELIMITER);
    }
}
