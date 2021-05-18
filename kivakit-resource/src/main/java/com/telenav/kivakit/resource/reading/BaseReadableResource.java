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

package com.telenav.kivakit.resource.reading;

import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.kernel.language.io.IO;
import com.telenav.kivakit.kernel.language.io.ProgressiveInput;
import com.telenav.kivakit.kernel.language.objects.Lazy;
import com.telenav.kivakit.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.kernel.language.time.Time;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.resource.CopyMode;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.ResourcePath;
import com.telenav.kivakit.resource.WritableResource;
import com.telenav.kivakit.resource.compression.Codec;
import com.telenav.kivakit.resource.compression.codecs.NullCodec;
import com.telenav.kivakit.resource.path.FilePath;
import com.telenav.kivakit.resource.project.lexakai.diagrams.DiagramFileSystemFile;
import com.telenav.kivakit.resource.project.lexakai.diagrams.DiagramResource;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * A base implementation of the {@link Resource} interface. Adds the following methods:
 *
 * <ul>
 *     <li>{@link #codec(Codec)} - Applies a {@link Codec} to this resource</li>
 * </ul>
 * <p>
 * All other methods are documented in the {@link Resource} superinterface.
 */
@UmlClassDiagram(diagram = DiagramResource.class)
@UmlClassDiagram(diagram = DiagramFileSystemFile.class)
@LexakaiJavadoc(complete = true)
public abstract class BaseReadableResource extends BaseRepeater implements Resource
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    /**
     * The temporary cache folder for storing materialized files
     */
    private static final Lazy<Folder> cacheFolder = Lazy.of(() ->
            Folder.temporaryForProcess(Folder.Type.CLEAN_UP_ON_EXIT).ensureExists());

    /**
     * Local copy if the resource is cached from a remote location
     */
    private File materialized;

    /**
     * StringPath to resource
     */
    private final ResourcePath path;

    /**
     * Character mapping (default is UTF-8)
     */
    private Charset charset = StandardCharsets.UTF_8;

    /**
     * Compression (default is none, but may be derived from resource extension)
     */
    private Codec codec;

    /**
     * For serialization
     */
    protected BaseReadableResource()
    {
        path = null;
    }

    protected BaseReadableResource(final ResourcePath path)
    {
        this.path = path;
    }

    protected BaseReadableResource(final BaseReadableResource that)
    {
        path = that.path;
        codec = that.codec;
        charset = that.charset;
        materialized = that.materialized;
    }

    @Override
    public final Charset charset()
    {
        return charset;
    }

    @Override
    public Codec codec()
    {
        if (codec == null)
        {
            if (extension() != null)
            {
                codec = extension().codec();
            }
            else
            {
                codec = new NullCodec();
            }
        }
        return codec;
    }

    @SuppressWarnings("UnusedReturnValue")
    public BaseReadableResource codec(final Codec codec)
    {
        this.codec = codec;
        return this;
    }

    /**
     * Copies the data in this resource to the destination.
     */
    @Override
    public void copyTo(final WritableResource destination, final CopyMode mode, final ProgressReporter reporter)
    {
        // If we can copy from this resource to the given resource in this mode,
        if (mode.canCopy(this, destination))
        {
            // copy the resource stream (which might involve compression or decompression or both).
            if (!IO.copyAndClose(openForReading(reporter), destination.openForWriting()))
            {
                throw new IllegalStateException("Unable to copy " + this + " to " + destination);
            }
        }
    }

    /**
     * Remove any materialized local copy if this is a remote resource that's been cached
     */
    @Override
    public void dematerialize()
    {
        synchronized (uniqueIdentifier())
        {
            if (materialized != null && materialized.exists())
            {
                try
                {
                    materialized.delete();
                    trace("Dematerialized ${debug} from local cache", materialized);
                    materialized = null;
                }
                catch (final Exception e)
                {
                    LOGGER.warning("Unable to dematerialize $", materialized);
                }
            }
        }
    }

    @Override
    public boolean exists()
    {
        try
        {
            InputStream in = null;
            var exists = false;
            try
            {
                in = openForReading();
                exists = in != null;
            }
            finally
            {
                if (in != null)
                {
                    in.close();
                }
            }
            return exists;
        }
        catch (final Exception e)
        {
            return false;
        }
    }

    // Ensure the remote resource is locally accessible
    @Override
    public Resource materialized(final ProgressReporter reporter)
    {
        synchronized (uniqueIdentifier())
        {
            if (isRemote() || isPackaged())
            {
                if (materialized == null)
                {
                    final var cached = cacheFile();
                    if (!cached.exists())
                    {
                        cached.parent().ensureExists();
                        final var start = Time.now();
                        trace("Materializing $ to $", this, cached.path().absolute());
                        safeCopyTo(cached, CopyMode.OVERWRITE, reporter);
                        trace("Materialized ${debug} ($) from ${debug} in ${debug}", cached.path().absolute(),
                                cached.bytes(), this, start.elapsedSince());
                    }
                    materialized = cached;
                }
                return materialized;
            }
            return this;
        }
    }

    @Override
    public InputStream openForReading(final ProgressReporter reporter)
    {
        // Open the input stream,
        final var in = onOpenForReading();
        if (in == null)
        {
            return null;
        }

        // add a decompression layer if need be,
        final var decompressed = codec().decompressed(IO.buffer(in));

        // and if there is a reporter,
        if (reporter != null)
        {
            // start it up
            reporter.start(fileName().name());
            reporter.steps(bytes());

            // and return a progressive input which will call the reporter.
            return new ProgressiveInput(decompressed, reporter);
        }

        return decompressed;
    }

    @Override
    public ResourcePath path()
    {
        return path;
    }

    @Override
    public String toString()
    {
        return fileName().toString();
    }

    protected void charset(final Charset charset)
    {
        this.charset = charset;
    }

    private File cacheFile()
    {
        // Flatten path being cached into a long filename by turning all file system meta characters
        // into underscores.
        // For example, "a/b/c.txt" becomes "a_b_c.txt"
        return File.parse(cacheFolder.get() + "/" + path().toString().replaceAll("[/:]", "_"));
    }

    private String uniqueIdentifier()
    {
        final var path = path();
        if (path instanceof FilePath)
        {
            final var filepath = (FilePath) path;
            return filepath.absolute().toString();
        }
        return path().toString();
    }
}
