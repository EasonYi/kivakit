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

package com.telenav.kivakit.kernel.logging.filters;

import com.telenav.kivakit.kernel.interfaces.comparison.Filter;
import com.telenav.kivakit.kernel.logging.LogEntry;
import com.telenav.kivakit.kernel.messaging.messages.Severity;

public class LogEntriesWithSeverityGreaterThanOrEqualTo implements Filter<LogEntry>
{
    private final Severity severity;

    public LogEntriesWithSeverityGreaterThanOrEqualTo(final Severity severity)
    {
        assert severity != null;
        this.severity = severity;
    }

    @Override
    public boolean accepts(final LogEntry entry)
    {
        return entry.message().severity().isGreaterThanOrEqualTo(severity);
    }

    @Override
    public String toString()
    {
        return "severityGreaterThanOrEqualTo(" + severity + ")";
    }
}
