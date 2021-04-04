////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.resource.writing;

import com.telenav.kivakit.core.resource.WritableResource;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramFileSystemFile;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramResource;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.io.IO;

import java.io.*;
import java.nio.charset.Charset;

/**
 * Resource reader provides a variety of convenient ways of reading a resource.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramFileSystemFile.class)
@UmlClassDiagram(diagram = DiagramResource.class)
public class ResourceWriter
{
    private final WritableResource resource;

    private final Charset charset;

    public ResourceWriter(final WritableResource resource)
    {
        this.resource = resource;
        charset = null;
    }

    public ResourceWriter(final WritableResource resource, final Charset charset)
    {
        this.resource = resource;
        this.charset = charset;
    }

    public Charset charset()
    {
        return charset;
    }

    public ObjectOutputStream objectOutputStream()
    {
        try
        {
            return new ObjectOutputStream(resource.openForWriting());
        }
        catch (final IOException e)
        {
            throw new IllegalStateException("Unable to open " + this, e);
        }
    }

    public PrintWriter printWriter()
    {
        return new PrintWriter(textWriter());
    }

    public void save(final Object... objects)
    {
        final var out = objectOutputStream();
        try
        {
            for (final var object : objects)
            {
                out.writeObject(object);
            }
        }
        catch (final Exception e)
        {
            throw new IllegalStateException("Unable to write object to " + this, e);
        }
        finally
        {
            IO.close(out);
        }
    }

    public void save(final String string)
    {
        try (final var out = printWriter())
        {
            out.print(string);
        }
    }

    public Writer textWriter()
    {
        final var out = resource.openForWriting();
        if (charset() == null)
        {
            return new OutputStreamWriter(out);
        }
        else
        {
            return new OutputStreamWriter(out, charset());
        }
    }

    @Override
    public String toString()
    {
        return resource.toString();
    }
}
