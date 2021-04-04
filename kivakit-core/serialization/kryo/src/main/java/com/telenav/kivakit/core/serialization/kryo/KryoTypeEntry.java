package

        com.telenav.kivakit.core.serialization.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.telenav.kivakit.core.kernel.language.objects.Hash;
import com.telenav.kivakit.core.kernel.messaging.Message;

/**
 * Kryo type entries in the entries map
 */
class KryoTypeEntry
{
    /** The serialized type */
    private Class<?> type;

    /** Any (optional) custom serializer */
    private Serializer<?> serializer;

    /** The Kryo registration identifier */
    private int identifier;

    KryoTypeEntry()
    {
    }

    KryoTypeEntry(final KryoTypeEntry that)
    {
        type = that.type;
        serializer = that.serializer;
        identifier = that.identifier;
    }

    public KryoTypeEntry copy()
    {
        return new KryoTypeEntry(this);
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof KryoTypeEntry)
        {
            final KryoTypeEntry that = (KryoTypeEntry) object;
            return type.equals(that.type) && identifier == that.identifier;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Hash.many(type, identifier);
    }

    @Override
    public String toString()
    {
        return Message.format("${class} ($)", type(), identifier());
    }

    KryoTypeEntry identifier(final int identifier)
    {
        this.identifier = identifier;
        return this;
    }

    int identifier()
    {
        return identifier;
    }

    void register(final Kryo kryo)
    {
        if (serializer == null)
        {
            kryo.register(type, identifier());
        }
        else
        {
            kryo.register(type, serializer, identifier());
        }
    }

    Serializer<?> serializer()
    {
        return serializer;
    }

    KryoTypeEntry serializer(final Serializer<?> serializer)
    {
        this.serializer = serializer;
        return this;
    }

    KryoTypeEntry type(final Class<?> type)
    {
        this.type = type;
        return this;
    }

    Class<?> type()
    {
        return type;
    }
}
