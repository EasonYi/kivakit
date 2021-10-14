open module kivakit.serialization.kryo
{
    // KivaKit
    requires transitive kivakit.serialization.core;
    requires kivakit.test;

    // Kryo
    requires transitive kryo;
    requires transitive de.javakaffee.kryoserializers;
    requires minlog;

    // Module exports
    exports com.telenav.kivakit.serialization.kryo;
    exports com.telenav.kivakit.serialization.kryo.project.lexakai.diagrams;
}
