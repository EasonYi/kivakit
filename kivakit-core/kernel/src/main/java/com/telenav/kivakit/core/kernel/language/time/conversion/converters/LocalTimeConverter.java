package com.telenav.kivakit.core.kernel.language.time.conversion.converters;

import com.telenav.kivakit.core.kernel.language.time.LocalTime;
import com.telenav.kivakit.core.kernel.language.time.conversion.BaseFormattedLocalTimeConverter;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageTime;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.time.conversion.TimeFormat;
import com.telenav.kivakit.core.kernel.messaging.Listener;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageTime.class)
public class LocalTimeConverter extends BaseFormattedLocalTimeConverter
{
    public LocalTimeConverter(final Listener listener, final ZoneId zone)
    {
        super(listener, TimeFormat.TIME, zone);
    }

    public LocalTimeConverter(final Listener listener)
    {
        super(listener, TimeFormat.TIME);
    }

    @Override
    protected boolean addTimeZone()
    {
        return false;
    }

    @Override
    protected String onConvertToString(final LocalTime value)
    {
        return DateTimeFormatter.ofPattern("h.mma").format(value.javaLocalDateTime());
    }
}
