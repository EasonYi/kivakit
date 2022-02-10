package com.telenav.kivakit.kernel.interfaces.io.serialization;

import com.telenav.kivakit.kernel.language.values.version.Version;
import com.telenav.kivakit.kernel.language.values.version.VersionedObject;

public interface ObjectWriter
{
    /**
     * Saves the given versioned object
     */
    <T> void write(VersionedObject<T> object);

    void writeObject(Object object);

    void writeVersion(Version version);
}
