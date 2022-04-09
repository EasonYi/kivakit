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

/**
 * Interface for objects supporting a last modification time
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
public interface ModifiedAt
{
    default Time modifiedAt()
    {
        throw new UnsupportedOperationException("Cannot retrieve last modified time from: " + getClass());
    }

    default boolean wasChangedAfter(ModifiedAt that)
    {
        return modifiedAt().isAfter(that.modifiedAt());
    }

    default boolean wasChangedBefore(ModifiedAt that)
    {
        return modifiedAt().isBefore(that.modifiedAt());
    }
}
