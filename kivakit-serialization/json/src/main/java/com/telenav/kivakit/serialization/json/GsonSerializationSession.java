package com.telenav.kivakit.serialization.json;

import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.kernel.language.values.version.Version;
import com.telenav.kivakit.kernel.language.values.version.VersionedObject;
import com.telenav.kivakit.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.serialization.core.ObjectSerializationSession;

public class GsonSerializationSession extends BaseRepeater implements ObjectSerializationSession
{
    @Override
    public void close()
    {

    }

    @Override
    public void flush(final Duration maximumWaitTime)
    {

    }

    @Override
    public <T> T readObject(final Class<T> type)
    {
        return null;
    }

    @Override
    public Version readVersion()
    {
        return null;
    }

    @Override
    public <T> VersionedObject<T> readVersionedObject()
    {
        return null;
    }

    @Override
    public Version version()
    {
        return null;
    }

    @Override
    public <T> void write(final VersionedObject<T> object)
    {

    }

    @Override
    public void writeObject(final Object object)
    {

    }

    @Override
    public void writeVersion(final Version version)
    {

    }
}
