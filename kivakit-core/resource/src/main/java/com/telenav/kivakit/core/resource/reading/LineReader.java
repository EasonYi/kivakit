////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.resource.reading;

import com.telenav.kivakit.core.resource.ReadableResource;
import com.telenav.kivakit.core.kernel.language.io.IO;
import com.telenav.kivakit.core.kernel.language.collections.list.StringList;
import com.telenav.kivakit.core.kernel.messaging.broadcasters.Multicaster;
import com.telenav.kivakit.core.kernel.language.progress.ProgressReporter;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Reads the provided {@link ReadableResource} reporting progress to the given {@link ProgressReporter}.
 *
 * @author jonathanl (shibo)
 */
public class LineReader extends Multicaster implements Iterable<String>
{
    /** The resource to read */
    private final ReadableResource resource;

    /** The handler for reporting progress */
    private final ProgressReporter reporter;

    public LineReader(final ReadableResource resource, final ProgressReporter reporter)
    {
        this.resource = resource;
        this.reporter = reporter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Iterator<String> iterator()
    {
        return stream().iterator();
    }

    /**
     * @return The lines in this resource as a list of strings
     */
    public StringList lines()
    {
        final var list = new StringList();
        list.addAll(this);
        return list;
    }

    /**
     * Calls the given consumer with each line
     */
    public void lines(final Consumer<String> consumer)
    {
        final var reader = new LineNumberReader(resource.reader(reporter).textReader());
        try
        {
            reporter.start();
            while (true)
            {
                final var next = reader.readLine();
                if (next == null)
                {
                    reporter.end();
                    return;
                }
                reporter.next();
                consumer.accept(next);
            }
        }
        catch (final IOException e)
        {
            throw new IllegalStateException(
                    "Exception thrown while reading " + resource + " at line " + reader.getLineNumber(), e);
        }
        finally
        {
            IO.close(reader);
        }
    }

    /**
     * @return The lines produced by this reader as a {@link Stream}
     */
    public Stream<String> stream()
    {
        final var reader = new LineNumberReader(resource.reader(reporter).textReader());
        try
        {
            reporter.start();
            return reader.lines().peek(line -> reporter.next()).onClose(closer(reader));
        }
        catch (final Exception e)
        {
            try
            {
                reader.close();
            }
            catch (final IOException ex)
            {
                try
                {
                    e.addSuppressed(ex);
                }
                catch (final Throwable ignore)
                {
                }
            }
            throw e;
        }
        finally
        {
            reporter.end();
        }
    }

    private Runnable closer(final Closeable closeable)
    {
        return () ->
        {
            try
            {
                closeable.close();
            }
            catch (final IOException e)
            {
                throw new UncheckedIOException(e);
            }
        };
    }
}
