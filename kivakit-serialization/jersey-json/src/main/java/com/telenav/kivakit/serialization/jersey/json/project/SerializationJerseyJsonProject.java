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

package com.telenav.kivakit.serialization.jersey.json.project;

import com.telenav.kivakit.kernel.language.objects.Lazy;
import com.telenav.kivakit.kernel.project.Project;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

/**
 * Project class for kivakit-core-serialization-jersey-json
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public class SerializationJerseyJsonProject extends Project
{
    private static final Lazy<SerializationJerseyJsonProject> project = Lazy.of(SerializationJerseyJsonProject::new);

    public static SerializationJerseyJsonProject get()
    {
        return project.get();
    }

    protected SerializationJerseyJsonProject()
    {
    }
}
