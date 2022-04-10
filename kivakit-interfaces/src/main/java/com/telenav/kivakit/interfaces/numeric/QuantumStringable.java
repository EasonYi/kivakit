package com.telenav.kivakit.interfaces.numeric;

import com.telenav.kivakit.interfaces.string.Stringable;

@SuppressWarnings("SpellCheckingInspection")
public interface QuantumStringable extends Quantizable, Stringable
{
    default String asCommaSeparatedString()
    {
        return String.format("%,d", quantum());
    }

    default String asSimpleString()
    {
        return Long.toString(quantum());
    }

    @Override
    default String asString(Format format)
    {
        switch (format)
        {
            case PROGRAMMATIC:
            case COMPACT:
            case DEBUG:
                return asSimpleString();

            case USER_LABEL:
            case USER_MULTILINE:
            case USER_SINGLE_LINE:
            default:
                return asCommaSeparatedString();
        }
    }
}
