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

package kernel.messaging;

import com.telenav.kivakit.core.kernel.language.values.count.MutableCount;
import com.telenav.kivakit.core.kernel.messaging.Repeater;
import com.telenav.kivakit.core.kernel.messaging.messages.status.Information;
import com.telenav.kivakit.core.kernel.messaging.repeaters.BaseRepeater;
import org.junit.Test;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureEqual;

public class RepeaterTest
{
    @Test
    public void test()
    {
        final Repeater repeater = new BaseRepeater(getClass());
        final var count = new MutableCount();
        repeater.addListener(message -> count.increment());
        ensureEqual(0L, count.asLong());
        repeater.receive(new Information("Test"));
        ensureEqual(1L, count.asLong());
        repeater.receive(new Information("Test"));
        ensureEqual(2L, count.asLong());
    }
}
