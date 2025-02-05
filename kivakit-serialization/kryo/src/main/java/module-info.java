open module kivakit.serialization.kryo
{
    // KivaKit
    requires kivakit.resource;
    requires kivakit.serialization.core;

    // Kryo
    requires transitive kryo;
    requires minlog;

    // Module exports
    exports com.telenav.kivakit.serialization.kryo;
    exports com.telenav.kivakit.serialization.kryo.lexakai;
    exports com.telenav.kivakit.serialization.kryo.types;
}
