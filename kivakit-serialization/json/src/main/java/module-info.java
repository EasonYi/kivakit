open module kivakit.serialization.json
{
    // KivaKit
    requires transitive kivakit.conversion;

    // JSON
    requires gson;

    // Module exports
    exports com.telenav.kivakit.serialization.json;
    exports com.telenav.kivakit.serialization.json.serializers;
}
