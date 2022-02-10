package com.telenav.kivakit.serialization.core;

import com.telenav.kivakit.kernel.interfaces.io.Closeable;
import com.telenav.kivakit.kernel.interfaces.io.Flushable;
import com.telenav.kivakit.kernel.interfaces.naming.Named;
import com.telenav.kivakit.kernel.language.values.version.Versioned;

/**
 * Super-interface for object and binary serialization sessions.
 *
 * @author jonathanl (shibo)
 * @see BinarySerializationSession
 * @see ObjectSerializationSession
 */
public interface SerializationSession extends
        Named,
        Versioned,
        Closeable,
        Flushable
{
}
