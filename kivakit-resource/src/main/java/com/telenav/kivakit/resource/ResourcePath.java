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

package com.telenav.kivakit.resource;

import com.telenav.kivakit.commandline.SwitchParser;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.logging.Logger;
import com.telenav.kivakit.core.logging.LoggerFactory;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.path.Path;
import com.telenav.kivakit.core.path.StringPath;
import com.telenav.kivakit.core.string.Strip;
import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.filesystem.FilePath;
import com.telenav.kivakit.resource.lexakai.DiagramResource;
import com.telenav.kivakit.resource.lexakai.DiagramResourcePath;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.net.URI;
import java.util.List;
import java.util.function.Function;

/**
 * A path to a resource of any kind. By default, the separator character for a resource is forward slash. But in the
 * case of some file paths this may be overridden. For example, in {@link FilePath}.
 * <p>
 * This class contains numerous methods which down-cast the return value of the superclass to make use easier for
 * clients. Methods that are unique to this class mainly have to do with filenames and file paths:
 * </p>
 * <ul>
 *     <li>{@link #asFile()} - This resource path as a file</li>
 *     <li>{@link #asFilePath()} - This resource path as a file path</li>
 *     <li>{@link #fileName()} - The last component of this path as a {@link FileName}</li>
 *     <li>{@link #normalized()} - This path without characters invalid in a resource path</li>
 *     <li>{@link #withExtension(Extension)} - This path with the given extension</li>
 * </ul>
 *
 * <p><b>Parsing</b></p>
 *
 * <ul>
 *     <li>{@link #parseResourcePath(Listener listener, String)} - The given string as a resource path</li>
 *     <li>{@link #parseUnixResourcePath(Listener listener, String)} - The given string as a slash-separated UNIX resource path</li>
 * </ul>
 *
 * <p><b>Factories</b></p>
 *
 * <ul>
 *     <li>{@link #resourcePath(StringPath)} - The given string path as a resource path</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramResource.class)
@UmlClassDiagram(diagram = DiagramResourcePath.class)
public class ResourcePath extends StringPath implements UriIdentified
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    /**
     * @return A resource path for the given string
     */
    public static ResourcePath parseResourcePath(Listener listener, String path)
    {
        return FilePath.parseFilePath(listener, path);
    }

    /**
     * @return A UNIX-style resource path for the given string
     */
    public static ResourcePath parseUnixResourcePath(Listener listener, String path)
    {
        String root = null;
        if (path.startsWith("/"))
        {
            root = "/";
            path = Strip.leading(path, "/");
        }
        return new ResourcePath(new StringList(), root, StringList.split(path, "/"));
    }

    /**
     * @return A resource path for the given string path
     */
    public static ResourcePath resourcePath(StringPath path)
    {
        return new ResourcePath(new StringList(), path.rootElement(), path.elements());
    }

    public static SwitchParser.Builder<ResourcePath> resourcePathSwitchParser(Listener listener,
                                                                              String name,
                                                                              String description)
    {
        return SwitchParser.builder(ResourcePath.class)
                .name(name)
                .converter(new ResourcePath.Converter(listener))
                .description(description);
    }

    /**
     * Converts to and from {@link ResourcePath}s
     *
     * @author jonathanl (shibo)
     */
    @LexakaiJavadoc(complete = true)
    public static class Converter extends BaseStringConverter<ResourcePath>
    {
        public Converter(Listener listener)
        {
            super(listener);
        }

        @Override
        protected ResourcePath onToValue(String value)
        {
            return parseResourcePath(this, value);
        }
    }

    /** The {@link URI} schemes for this resource path */
    private StringList schemes;

    /**
     * @param schemes The list of schemes for this path
     * @param root The root element
     * @param elements The path elements
     */
    protected ResourcePath(StringList schemes, String root, List<String> elements)
    {
        super(root, elements);

        if (schemes != null)
        {
            this.schemes = schemes.copy();
        }
        else
        {
            this.schemes = new StringList();
        }
    }

    /**
     * Copy constructor
     */
    protected ResourcePath(ResourcePath that)
    {
        super(that);
        schemes = that.schemes.copy();
    }

    protected ResourcePath(StringList schemes, List<String> elements)
    {
        super(elements);
        this.schemes = schemes.copy();
    }

    public ResourcePath absolute()
    {
        return this;
    }

    /**
     * @return This resource path as a file
     */
    public File asFile()
    {
        return File.parseFile(LOGGER, asString());
    }

    /**
     * @return This resource path as a file path
     */
    public FilePath asFilePath()
    {
        return FilePath.parseFilePath(LOGGER, asString());
    }

    /**
     * @return The file extension of this resource path's filename
     */
    public Extension extension()
    {
        return fileName().extension();
    }

    /**
     * @return The file name of this resource path
     */
    public FileName fileName()
    {
        var last = last();
        return last == null ? null : FileName.parseFileName(LOGGER, last);
    }

    /**
     * @return True if this file path has a scheme
     */
    public boolean hasScheme()
    {
        return schemes.isNonEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String join()
    {
        // NOTE: We call super.join(String) here because it is not overridden
        return schemes.join(":")
                + (hasScheme() ? ":" : "")
                + super.join(separator());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath last(int n)
    {
        return (ResourcePath) super.last(n);
    }

    /**
     * @return This path without characters that are unacceptable in a resource path
     */
    public ResourcePath normalized()
    {
        return transformed(element -> element
                .replace(',', '_')
                .replace(';', '_')
                .replace(' ', '_')
                .replace('\'', '_'));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath parent()
    {
        return (ResourcePath) super.parent();
    }

    public ResourcePath relativeTo(ResourcePath path)
    {
        if (startsWith(path))
        {
            return withoutOptionalPrefix(path);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath root()
    {
        return (ResourcePath) super.root();
    }

    /**
     * @return Any schemes for this filepath. For example, a file such as "jar:file:/test.zip" would have the schemes
     * "jar" and "file". In "s3://telenav/file.txt", there is only one scheme, "s3".
     */
    public StringList schemes()
    {
        return schemes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath subpath(int start, int end)
    {
        return (ResourcePath) super.subpath(start, end);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath transformed(Function<String, String> consumer)
    {
        return (ResourcePath) super.transformed(consumer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public URI uri()
    {
        return URI.create(join());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath withChild(Path<String> that)
    {
        return (ResourcePath) super.withChild(that);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath withChild(String element)
    {
        return (ResourcePath) super.withChild(element);
    }

    /**
     * @return This resource path with the given extension
     */
    public FilePath withExtension(Extension extension)
    {
        return (FilePath) withoutLast().withChild(last() + extension);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath withParent(String element)
    {
        return (ResourcePath) super.withParent(element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath withParent(Path<String> that)
    {
        return (ResourcePath) super.withParent(that);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath withRoot(String root)
    {
        return (ResourcePath) super.withRoot(root);
    }

    public ResourcePath withSchemes(StringList schemes)
    {
        var copy = (ResourcePath) copy();
        copy.schemes = schemes.copy();
        return copy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath withSeparator(String separator)
    {
        return (ResourcePath) super.withSeparator(separator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath withoutFirst()
    {
        return (ResourcePath) super.withoutFirst();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath withoutLast()
    {
        return (ResourcePath) super.withoutLast();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath withoutOptionalPrefix(Path<String> prefix)
    {
        return (ResourcePath) super.withoutOptionalPrefix(prefix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath withoutOptionalSuffix(Path<String> suffix)
    {
        return (ResourcePath) super.withoutOptionalSuffix(suffix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath withoutPrefix(Path<String> prefix)
    {
        return (ResourcePath) super.withoutPrefix(prefix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath withoutRoot()
    {
        return (ResourcePath) super.withoutRoot();
    }

    /**
     * @return This filepath without any scheme
     */
    public ResourcePath withoutSchemes()
    {
        ResourcePath copy = (ResourcePath) copy();
        copy.schemes = new StringList();
        return copy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourcePath withoutSuffix(Path<String> suffix)
    {
        return (ResourcePath) super.withoutSuffix(suffix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ResourcePath onCopy(String root, List<String> elements)
    {
        return new ResourcePath(schemes(), root, elements);
    }
}
