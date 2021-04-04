////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.string;

import com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.core.kernel.language.strings.conversion.AsIndentedString;
import com.telenav.kivakit.core.kernel.language.strings.conversion.AsStringIndenter;
import com.telenav.kivakit.core.kernel.language.strings.conversion.StringFormat;
import com.telenav.kivakit.core.kernel.language.values.count.Maximum;
import org.junit.Test;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureEqual;

/**
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
public class AsIndentedStringTest implements AsIndentedString
{
    static class Foo implements AsIndentedString
    {
        @KivaKitIncludeProperty
        private final int y = 1;

        @KivaKitIncludeProperty
        private final int z = 3;

        @KivaKitIncludeProperty
        private final Bar bar = new Bar();

        @Override
        public String toString()
        {
            return "Foo";
        }
    }

    static class Bar implements AsIndentedString
    {
        @KivaKitIncludeProperty
        private final int y = 5;

        @KivaKitIncludeProperty
        private final int z = 7;

        @Override
        public String toString()
        {
            return "Bar";
        }
    }

    @KivaKitIncludeProperty
    private final int x = 9;

    @KivaKitIncludeProperty
    private final Foo foo = new Foo();

    @Test
    public void test()
    {
        final var foo = new Foo();
        ensureEqual(
                "bar:\n" +
                        "  y: 5\n" +
                        "  z: 7\n" +
                        "y: 1\n" +
                        "z: 3", foo.asString());
    }

    @Test
    public void testPrune()
    {
        final var indenter = new AsStringIndenter(StringFormat.TEXT)
                .levels(Maximum._4)
                .pruneAt(Bar.class);
        asString(StringFormat.TEXT, indenter);
        ensureEqual("foo:\n" +
                "  bar: Bar\n" +
                "  y: 1\n" +
                "  z: 3\n" +
                "x: 9", indenter.toString());
    }
}
