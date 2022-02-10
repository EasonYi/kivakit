package com.telenav.kivakit.kernel.interfaces.io.serialization;

import com.telenav.kivakit.kernel.messaging.Repeater;

public interface ObjectSerializer extends
        ObjectReader,
        ObjectWriter,
        Repeater
{
}
