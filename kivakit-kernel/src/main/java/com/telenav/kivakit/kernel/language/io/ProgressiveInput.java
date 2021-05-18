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

import com.telenav.kivakit.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.kernel.language.progress.reporters.Progress;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageIo;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.IOException;
import java.io.InputStream;

/**
 * An {@link InputStream} that reports progress as bytes are read.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageIo.class)
@LexakaiJavadoc(complete = true)
public class ProgressiveInput extends InputStream
{
    private final InputStream input;

    private final ProgressReporter reporter;

    public ProgressiveInput(final InputStream input, final ProgressReporter reporter)
    {
        this.input = input;
        this.reporter = reporter;
    }

    public ProgressiveInput(final InputStream input)
    {
        this(input, Progress.create());
    }

    @Override
    public int available() throws IOException
    {
        return input.available();
    }

    @Override
    public void close() throws IOException
    {
        input.close();
        reporter.end();
    }

    @Override
    public int read(final byte[] bytes, final int offset, final int length) throws IOException
    {
        final var read = input.read(bytes, offset, length);
        if (read > 0)
        {
            reporter.next(read);
        }
        return read;
    }

    @Override
    public int read(final byte[] bytes) throws IOException
    {
        final var read = input.read(bytes);
        if (read > 0)
        {
            reporter.next(read);
        }
        return read;
    }

    @Override
    public int read() throws IOException
    {
        final var read = input.read();
        if (read > 0)
        {
            reporter.next();
        }
        return read;
    }

    @Override
    public synchronized void reset()
    {
        reporter.reset();
        try
        {
            super.reset();
        }
        catch (final IOException ignored)
        {
        }
    }
}
