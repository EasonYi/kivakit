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

package com.telenav.kivakit.kernel.language.io;

import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageIo;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.InputStream;

/**
 * An input stream built around a StringBuilder to read it.
 *
 * @author matthieun
 */
@UmlClassDiagram(diagram = DiagramLanguageIo.class)
public class StringInput extends InputStream
{
    private int index;

    private final String toRead;

    public StringInput(final String toRead)
    {
        this.toRead = toRead;
    }

    @Override
    public int read()
    {
        if (index < toRead.length())
        {
            final var result = toRead.charAt(index);
            index++;
            return result;
        }
        else
        {
            return -1;
        }
    }
}
