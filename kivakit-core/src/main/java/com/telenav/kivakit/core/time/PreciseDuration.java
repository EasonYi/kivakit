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

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

import static com.telenav.kivakit.core.ensure.Ensure.unsupported;

public class PreciseDuration extends BaseDuration<PreciseDuration>
{
    private static final double WEEKS_PER_YEAR = 52.177457;

    private static final ThreadMXBean cpu = ManagementFactory.getThreadMXBean();

    public static PreciseDuration cpuTime()
    {
        if (!cpu.isThreadCpuTimeSupported() || !cpu.isThreadCpuTimeEnabled())
        {
            throw new UnsupportedOperationException();
        }
        return nanoseconds(cpu.getCurrentThreadCpuTime());
    }

    public static PreciseDuration microseconds(double microseconds)
    {
        return nanoseconds((long) (microseconds * 1_000));
    }

    public static PreciseDuration milliseconds(double milliseconds)
    {
        return microseconds(milliseconds * 1_000);
    }

    public static PreciseDuration nanoseconds(long nanoseconds)
    {
        return new PreciseDuration(nanoseconds);
    }

    public static PreciseDuration seconds(double seconds)
    {
        return milliseconds(seconds * 1_000);
    }

    private final long nanoseconds;

    private PreciseDuration(long nanoseconds)
    {
        super(nanoseconds / 1_000_000);
        this.nanoseconds = nanoseconds;
    }

    public double asMicroseconds()
    {
        return nanoseconds / 1000.0;
    }

    public long asNanoseconds()
    {
        return nanoseconds;
    }

    public boolean isGreaterThan(PreciseDuration that)
    {
        return nanoseconds > that.nanoseconds;
    }

    public boolean isLessThan(PreciseDuration that)
    {
        return nanoseconds < that.nanoseconds;
    }

    @Override
    public PreciseDuration maximum(PreciseDuration that)
    {
        return isGreaterThan(that) ? this : that;
    }

    @Override
    public PreciseDuration maximum()
    {
        return nanoseconds(Long.MAX_VALUE);
    }

    @Override
    public long millisecondsPerUnit()
    {
        return unsupported();
    }

    @Override
    public PreciseDuration minimum(PreciseDuration that)
    {
        return nanoseconds(that.nanoseconds);
    }

    @Override
    public PreciseDuration minimum()
    {
        return nanoseconds(0);
    }

    public PreciseDuration minus(PreciseDuration that)
    {
        return nanoseconds(nanoseconds - that.nanoseconds);
    }

    @Override
    public PreciseDuration newInstance(long milliseconds)
    {
        return milliseconds(milliseconds);
    }

    public PreciseDuration plus(PreciseDuration that)
    {
        return nanoseconds(nanoseconds + that.nanoseconds);
    }

    @Override
    public String toString()
    {
        return asDuration().asString();
    }

    /**
     * Converts a value to a unit-suffixed value, taking care of English singular/plural suffix.
     *
     * @param value a <code>double</code> value to format
     * @param units the units to apply singular or plural suffix to
     * @return a <code>String</code> representation
     */
    @Override
    public String unitString(double value, String units)
    {
        return String.format("%.1f", value) + " " + units + (value > 1.0 ? "s" : "");
    }
}
