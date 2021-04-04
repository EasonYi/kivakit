////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.types;

import com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.core.kernel.language.strings.PathStrings;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.fail;

/**
 * Class utility methods
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unchecked")
public class Classes
{
    public static <T> Class<T> forName(final ClassLoader loader, final String name)
    {
        try
        {
            return (Class<T>) loader.loadClass(name);
        }
        catch (final ClassNotFoundException e)
        {
            return null;
        }
    }

    public static <T> Class<T> forName(final String name)
    {
        try
        {
            return (Class<T>) Class.forName(name);
        }
        catch (final Exception e)
        {
            return null;
        }
    }

    public static boolean isPrimitive(final Class<?> type)
    {
        return Long.TYPE.equals(type) || Integer.TYPE.equals(type) || Short.TYPE.equals(type)
                || Character.TYPE.equals(type) || Byte.TYPE.equals(type) || Boolean.TYPE.equals(type)
                || Double.TYPE.equals(type) || Float.TYPE.equals(type);
    }

    public static InputStream openResource(final Class<?> base, final String path)
    {
        var in = base.getResourceAsStream(path);
        if (in == null)
        {
            final var loader = base.getClassLoader();
            if (loader != null)
            {
                in = loader.getResourceAsStream(path);
            }
        }
        if (in == null)
        {
            in = ClassLoader.getSystemClassLoader().getResourceAsStream(path);
        }
        if (in == null)
        {
            in = Classes.class.getResourceAsStream(path);
        }
        return in;
    }

    public static URI resourceUri(final Class<?> base, final String path)
    {
        try
        {
            return resourceUrl(base, path).toURI();
        }
        catch (final URISyntaxException e)
        {
            return Ensure.fail(e, "Unable to get URI for $:$", base, path);
        }
    }

    public static URL resourceUrl(final Class<?> base, final String path)
    {
        var resource = base.getResource(path);
        if (resource == null)
        {
            resource = base.getClassLoader().getResource(path);
        }
        if (resource == null)
        {
            resource = ClassLoader.getSystemClassLoader().getResource(path);
        }
        Ensure.ensure(resource != null);
        return resource;
    }

    public static String simpleName(final Class<?> type)
    {
        if (type != null)
        {
            if (type.isArray() || type.isPrimitive())
            {
                return type.getSimpleName();
            }
            return PathStrings.optionalSuffix(type.getName(), '.').replace('$', '.');
        }
        return "Unknown";
    }

    public static String simpleTopLevelClass(final Class<?> type)
    {
        final var name = PathStrings.optionalSuffix(type.getName(), '.');
        if (name.contains("$"))
        {
            return PathStrings.optionalHead(name, '$');
        }
        return name;
    }
}
