////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.resource.reading;

import com.telenav.kivakit.core.filesystem.File;
import com.telenav.kivakit.core.filesystem.Folder;
import com.telenav.kivakit.core.kernel.language.io.IO;
import com.telenav.kivakit.core.kernel.language.io.ProgressiveInput;
import com.telenav.kivakit.core.kernel.language.objects.Lazy;
import com.telenav.kivakit.core.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.core.kernel.language.time.Time;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.resource.CopyMode;
import com.telenav.kivakit.core.resource.Resource;
import com.telenav.kivakit.core.resource.ResourceIdentifier;
import com.telenav.kivakit.core.resource.ResourcePath;
import com.telenav.kivakit.core.resource.WritableResource;
import com.telenav.kivakit.core.resource.compression.Codec;
import com.telenav.kivakit.core.resource.compression.codecs.NullCodec;
import com.telenav.kivakit.core.resource.path.FilePath;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramFileSystemFile;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramResource;
import com.telenav.kivakit.core.resource.spi.ResourceFactoryServiceRegistry;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@UmlClassDiagram(diagram = DiagramResource.class)
@UmlClassDiagram(diagram = DiagramFileSystemFile.class)
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

    /**
     * Copies this readable resource to the given folder safely
     *
     * @param destination The file to copy to
     */
    public void safeCopyTo(final Folder destination, final CopyMode mode, final ProgressReporter reporter)
    {
        safeCopyTo(destination.file(fileName()), mode, reporter);
    }

    /**
     * Copies this readable resource to the given file safely (ensuring that a corrupted copy of the file never exists).
     * This is done by first copying to a temporary file in the same folder. If the copy operation is successful, the
     * destination file is then removed and the temporary file is renamed to the destination file's name.
     *
     * @param destination The file to copy to
     */
    @Override
    public void safeCopyTo(final File destination, final CopyMode mode, final ProgressReporter reporter)
    {
        if (mode.canCopy(this, destination))
        {
            trace("Safe copy $ to $", this, destination);
            final var temporary = destination.parent().temporaryFile(destination.fileName());
            copyTo(temporary, mode, reporter);
            if (destination.exists())
            {
                destination.delete();
            }
            temporary.renameTo(destination);
        }
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

    protected Resource resourceForIdentifier(final ResourceIdentifier identifier)
    {
        return ResourceFactoryServiceRegistry.forIdentifier(identifier);
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
