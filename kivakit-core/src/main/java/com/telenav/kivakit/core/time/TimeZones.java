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

import com.telenav.kivakit.core.lexakai.DiagramTime;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Utility methods to map between {@link ZoneId}s and their display names
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramTime.class)
@LexakaiJavadoc(complete = true)
public class TimeZones
{
    /** Map from a short display name (PST) to its zone id (America/Los Angeles) */
    private static final Map<String, String> shortDisplayNameToZoneId = new HashMap<>();

    static
    {
        for (var zone : ZoneId.getAvailableZoneIds())
        {
            var zoneId = ZoneId.of(zone);
            shortDisplayNameToZoneId.put(shortDisplayName(zoneId), zone);
        }
    }

    /**
     * @return True if the given zone id represents UTC time
     */
    public static boolean isUtc(ZoneId zone)
    {
        return zone.getId().equals("UTC") || zone.getId().equals("GMT") || zone.getId().equals(("UT"));
    }

    /**
     * @return True if the given identifier is a valid short display name (PST)
     */
    public static boolean isValidShortDisplayName(String identifier)
    {
        return parseShortDisplayName(Listener.none(), identifier) != null;
    }

    /**
     * @return True if the given identifier is a valid zone id (America/Los Angeles)
     */
    public static boolean isValidZoneId(String identifier)
    {
        return parseZoneId(Listener.none(), identifier) != null;
    }

    /**
     * @return The zone id (America/Los Angeles) for the given short display name (PST), or null if there is none
     */
    public static ZoneId parseShortDisplayName(Listener listener, String displayName)
    {
        var zoneId = shortDisplayNameToZoneId.get(displayName);
        if (zoneId != null)
        {
            return ZoneId.of(zoneId);
        }
        listener.problem("Invalid zone display name (PST): $", displayName);
        return null;
    }

    /**
     * @return The zone id (America/Los Angeles) for the given identifier, or null if there is none
     */
    public static ZoneId parseZoneId(Listener listener, String identifier)
    {
        try
        {
            return ZoneId.of(identifier);
        }
        catch (Exception e)
        {
            listener.problem("Invalid zone identifier (America/Los Angeles): $", identifier);
            return null;
        }
    }

    /**
     * @return The zone id for the given identifier (America/Los Angeles, or PST), or null if there is none
     */
    public static ZoneId parseZoneIdOrDisplayName(Listener listener, String identifier)
    {
        var zone = parseShortDisplayName(Listener.none(), identifier);
        if (zone == null)
        {
            zone = parseZoneId(Listener.none(), identifier);
        }
        if (zone == null)
        {
            listener.problem("Not a valid zone identifier (America/Los Angeles) or short display name (PST): $", identifier);
        }
        return zone;
    }

    /**
     * @return The short display name (PST) for the given zone id (America/Los Angeles)
     */
    public static String shortDisplayName(ZoneId zone)
    {
        return zone.getDisplayName(TextStyle.SHORT, Locale.getDefault());
    }

    public static ZoneId utc()
    {
        return ZoneId.of("UTC");
    }
}
