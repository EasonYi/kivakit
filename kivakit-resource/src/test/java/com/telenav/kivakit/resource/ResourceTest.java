////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.resource;

import com.telenav.kivakit.properties.PropertyMap;
import com.telenav.kivakit.test.UnitTest;
import org.junit.Test;

public class ResourceTest extends UnitTest
{
    @Test
    public void testResolution()
    {
        var properties = Resource.resolve(this, "classpath:com/telenav/kivakit/resource/ResourceTest.properties");
        ensureEqual("b", PropertyMap.load(properties).get("a"));
    }
}
