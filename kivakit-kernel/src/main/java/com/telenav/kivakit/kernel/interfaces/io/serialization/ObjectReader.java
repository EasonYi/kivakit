package com.telenav.kivakit.kernel.interfaces.io.serialization;

import com.telenav.kivakit.kernel.language.values.version.Version;
import com.telenav.kivakit.kernel.language.values.version.VersionedObject;

public interface ObjectReader
{
    <T> T readObject(Class<T> type);

    Version readVersion();

    /**
     * @return A versioned object
     */
    <T> VersionedObject<T> readVersionedObject();
}
