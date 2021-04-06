////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.resource.path;

import com.telenav.kivakit.core.test.UnitTest;
import org.junit.Test;

public class ExtensionTest extends UnitTest
{
    @Test
    public void testKnown()
    {
        Extension previous = null;
        for (final Extension extension : Extension.known())
        {
            if (previous != null)
            {
                ensure(previous.length().isGreaterThanOrEqualTo(extension.length()));
            }
            previous = extension;
        }
    }

    @Test
    public void testWithoutKnownExtension()
    {
        ensureEqual(new FileName("a"), new FileName("a.txt").withoutKnownExtensions());
        ensureEqual(new FileName("a"), new FileName("a.txt.gz").withoutKnownExtensions());
        ensureEqual(new FileName("a"), new FileName("a.osm.pbf").withoutKnownExtensions());
        ensureEqual(new FileName("a"), new FileName("a.osm").withoutKnownExtensions());
        ensureEqual(new FileName("a"), new FileName("a.pbf").withoutKnownExtensions());
        ensureEqual(new FileName("a.unknown"), new FileName("a.unknown").withoutKnownExtensions());
        ensureEqual(new FileName("a.b.c"), new FileName("a.b.c.txt").withoutKnownExtensions());
        ensureEqual(new FileName("a.b.c"), new FileName("a.b.c.txt.gz").withoutKnownExtensions());
        ensureEqual(new FileName("a.b.c"), new FileName("a.b.c.osm.pbf").withoutKnownExtensions());
        ensureEqual(new FileName("a.b.c"), new FileName("a.b.c.osm").withoutKnownExtensions());
        ensureEqual(new FileName("a.b.c"), new FileName("a.b.c.pbf").withoutKnownExtensions());
        ensureEqual(new FileName("a.b.c.unknown"), new FileName("a.b.c.unknown").withoutKnownExtensions());
    }
}
