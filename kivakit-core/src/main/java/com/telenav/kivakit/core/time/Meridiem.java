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

package com.telenav.kivakit.core.time;

import com.telenav.kivakit.core.language.primitive.Longs;
import com.telenav.kivakit.core.lexakai.DiagramTime;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;

/**
 * Anti-meridiem (AM) or post-meridiem (PM) in the English system of keeping time, or NO_MERIDIEM for the rest of the
 * world.
 *
 * <p>
 * The meridiem value for a military hour can be obtained with {@link #meridiem(long)}, and the meridiem hour with
 * {@link #meridiemHour(long)}. The method {@link #asMilitaryHour(long)} can convert from a meridiem hour back to
 * military time.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see Hour#am(long)
 * @see Hour#pm(long)
 * @see Hour#asMeridiemHour()
 * @see Hour#asMilitaryHour()
 */
@SuppressWarnings({ "unused", "SpellCheckingInspection" })
@UmlClassDiagram(diagram = DiagramTime.class)
@LexakaiJavadoc(complete = true)
public enum Meridiem
{
    NO_MERIDIEM,
    AM,
    PM;

    /**
     * Returns the Meridiem for the given military (0-23) hour.
     */
    public static Meridiem meridiem(long militaryHour)
    {
        ensure(Longs.isBetweenInclusive(militaryHour, 0, 23));

        return militaryHour < 12 ? AM : PM;
    }

    public static long meridiemHour(long militaryHour)
    {
        ensure(Longs.isBetweenInclusive(militaryHour, 0, 23));

        if (militaryHour == 0 || militaryHour == 12)
        {
            return 12;
        }

        return militaryHour < 12
                ? militaryHour
                : militaryHour - 12;
    }

    long asMilitaryHour(long meridiemHour)
    {
        ensure(Longs.isBetweenInclusive(meridiemHour, 1, 12));

        switch (this)
        {
            case PM:
                if (meridiemHour == 12)
                {
                    return 12;
                }
                return meridiemHour + 12;

            case AM:
                if (meridiemHour == 12)
                {
                    return 0;
                }
                return meridiemHour;

            case NO_MERIDIEM:
                return meridiemHour;

            default:
                return unsupported();
        }
    }
}
