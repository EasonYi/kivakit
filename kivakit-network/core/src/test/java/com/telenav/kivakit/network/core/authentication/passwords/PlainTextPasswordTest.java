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

package com.telenav.kivakit.network.core.authentication.passwords;

import com.telenav.kivakit.test.UnitTest;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("SpellCheckingInspection")
public class PlainTextPasswordTest extends UnitTest
{
    @Test
    public void test()
    {
        var password = new PlainTextPassword("passw0rd");
        ensure(password.matches(new PlainTextPassword("passw0rd")));
        ensureFalse(password.matches(new PlainTextPassword("password")));
    }

    @Test
    public void testToString()
    {
        var password = new PlainTextPassword("This*is8hi");
        Assert.assertEquals(password.toString(), "This*is8hi");
        Assert.assertEquals(password.asString(), "**********");
    }
}
