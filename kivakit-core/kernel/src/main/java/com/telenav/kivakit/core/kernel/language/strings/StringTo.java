package com.telenav.kivakit.core.kernel.language.strings;

import com.telenav.kivakit.core.kernel.interfaces.value.Source;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageString;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.collections.list.StringList;
import com.telenav.kivakit.core.kernel.language.strings.conversion.AsString;
import com.telenav.kivakit.core.kernel.language.strings.conversion.StringFormat;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageString.class)
public class StringTo
{
    /**
     * @return The lowest bits of the given value as a binary string
     */
    public static String binary(final int value, int bits)
    {
        final var builder = new StringBuilder();
        var mask = 1 << (bits - 1);
        while (bits-- > 0)
        {
            builder.append((value & mask) == 0 ? "0" : "1");
            mask >>>= 1;
        }
        return builder.toString();
    }

    /**
     * Converts the given object to a debug string. If the object supports the AsString interface, the {@link
     * AsString#asString(StringFormat)} method is called with {@link StringFormat#DEBUGGER}. If it does not, the
     * toString() method is called.
     *
     * @param object The object
     * @return A debug string for the object
     */
    public static String debug(final Object object)
    {
        if (object instanceof AsString)
        {
            return ((AsString) object).asString(StringFormat.DEBUGGER);
        }
        return string(object);
    }

    /**
     * @return The given enum value as a displayable string
     */
    public static String display(final Enum<?> enumValue)
    {
        final var words = StringList.split(enumValue.name(), "_");
        final var display = new StringList();
        for (final var word : words)
        {
            display.add(CaseFormat.capitalizeOnlyFirstLetter(word.toLowerCase()));
        }
        return display.join(" ");
    }

    /**
     * @return The given text trivially converted to HTML
     */
    public static String html(final String text)
    {
        return text.replaceAll("\n", "<br/>").replaceAll(" ", "&nbsp;");
    }

    /**
     * @return The value as a string or an empty string if it is null
     */
    public static String nonNullString(final Object value)
    {
        return value == null ? "" : string(value);
    }

    /**
     * @return The given object as a string or the given value if it is null
     */
    public static String string(final Object object, final String defaultValue)
    {
        if (object == null)
        {
            return defaultValue;
        }
        if (object instanceof Source)
        {
            return string(((Source<?>) object).get());
        }
        if (object instanceof Long)
        {
            final var value = (long) object;
            return String.format("%,d", value);
        }
        if (object instanceof Integer)
        {
            final var value = (int) object;
            return String.format("%,d", value);
        }
        return object.toString();
    }

    /**
     * @return The given object as a string or "null" if it is null
     */
    public static String string(final Object object)
    {
        return string(object, "null");
    }
}
