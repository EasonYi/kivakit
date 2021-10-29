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

package com.telenav.kivakit.resource.resources.other;

import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.kernel.language.collections.list.StringList;
import com.telenav.kivakit.kernel.language.collections.map.string.VariableMap;
import com.telenav.kivakit.kernel.language.locales.Locale;
import com.telenav.kivakit.kernel.language.paths.PackagePath;
import com.telenav.kivakit.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.kernel.language.reflection.Type;
import com.telenav.kivakit.kernel.language.reflection.populator.ObjectPopulator;
import com.telenav.kivakit.kernel.language.reflection.property.PropertyFilter;
import com.telenav.kivakit.kernel.language.strings.AsciiArt;
import com.telenav.kivakit.kernel.language.values.count.Count;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.WritableResource;
import com.telenav.kivakit.resource.path.FilePath;
import com.telenav.kivakit.resource.project.lexakai.diagrams.DiagramResourceType;
import com.telenav.kivakit.resource.resources.packaged.PackageResource;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * A property map is a {@link VariableMap} with strings as both keys and values.
 *
 * <p><b>Creating and Loading Property Maps</b></p>
 *
 * <p>
 * A property map can be created or loaded from a {@link Resource} with:
 * </p>
 *
 * <ul>
 *     <li>{@link #create()} - Creates an empty property map</li>
 *     <li>{@link #of(VariableMap)} - Creates a property map from the given variable map</li>
 *     <li>{@link #load(Listener, Resource)} - Loads property map from the given resource</li>
 *     <li>{@link #localized(Listener, PackagePath, Locale)} - Loads a property map from the given package with a relative path
 *      from the given {@link Locale} of the form "locales/[language-name](/[country-name])?.</li>
 * </ul>
 *
 * <p><b>Conversions</b></p>
 *
 * <ul>
 *     <li>{@link #copy()} - A copy of this property map</li>
 *     <li>{@link #asObject(Listener, Class)} - Creates a new instance of the given class and populates its
 *     properties with the values in this property map using {@link ObjectPopulator}</li>
 *     <li>{@link #asInt(String)} - The given value as an int</li>
 *     <li>{@link #asLong(String)} - The given value as a long</li>
 *     <li>{@link #asDouble(String)} - The given value as a double</li>
 *     <li>{@link #asCount(String)} - The given value as a {@link Count}</li>
 * </ul>
 *
 * <p><b>Adding to Property Maps</b></p>
 *
 * <p>
 * In addition to the methods inherited from {@link VariableMap}, property maps add the following methods:
 * </p>
 *
 * <ul>
 *     <li>{@link #comment(String, String)} - Attaches the given comment to the given key</li>
 *     <li>{@link #add(Object, PropertyFilter)} - Adds the properties of the given object that match the filter to this map</li>
 * </ul>
 *
 *
 * <p><b>Saving Property Maps</b></p>
 *
 * <ul>
 *     <li>{@link #save(WritableResource)} - Saves this property map to the given resource</li>
 *     <li>{@link #save(String, WritableResource)} - Saves this property map to the given resource with the given heading</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see Locale
 */
@UmlClassDiagram(diagram = DiagramResourceType.class)
@LexakaiJavadoc(complete = true)
public class PropertyMap extends VariableMap<String>
{
    /**
     * @return An empty property map
     */
    public static PropertyMap create()
    {
        return new PropertyMap();
    }

    public static PropertyMap load(Listener listener, Resource resource)
    {
        if (resource.exists())
        {
            return load(resource, ProgressReporter.NULL);
        }
        listener.warning("Unable to load property map from: $", resource);
        return new PropertyMap();
    }

    public static PropertyMap load(Listener listener, PackagePath _package, String path)
    {
        return load(listener, PackageResource.of(_package, FilePath.parseFilePath(path)));
    }

    public static PropertyMap load(Listener listener, Class<?> _package, String path)
    {
        return load(listener, PackagePath.packagePath(_package), path);
    }

    public static PropertyMap localized(Listener listener, PackagePath path, Locale locale)
    {
        return PropertyMap.load(listener, path, locale.path().join("/"));
    }

    public static PropertyMap of(VariableMap<String> variables)
    {
        var map = create();
        for (var key : variables.keySet())
        {
            map.put(key, variables.get(key));
        }
        return map;
    }

    private final Map<String, String> comments = new HashMap<>();

    protected PropertyMap()
    {
    }

    public void add(Object object, PropertyFilter filter)
    {
        Type<?> type = Type.of(object);
        for (var property : type.properties(filter))
        {
            if (!"class".equals(property.name()))
            {
                var getter = property.getter();
                if (getter != null)
                {
                    var value = getter.get(object);
                    if (value != null)
                    {
                        put(property.name(), value.toString());
                    }
                }
            }
        }
    }

    /**
     * @return The given value as a {@link Folder}
     */
    public File asFile(String key)
    {
        return File.parse(get(key));
    }

    /**
     * @return The given value as a {@link Folder}
     */
    public Folder asFolder(String key)
    {
        return Folder.parse(asPath(key));
    }

    /**
     * @return This property map as a JSON string
     */
    public String asJson()
    {
        return "{ " + doubleQuoted().join(", ") + " }";
    }

    /**
     * @return Associate a comment with the given key. The comment will be written out when the property map is saved.
     */
    public PropertyMap comment(String key, String comment)
    {
        comments.put(key, comment);
        return this;
    }

    @Override
    public PropertyMap copy()
    {
        var map = new PropertyMap();
        for (var key : keySet())
        {
            map.put(key, get(key));
        }
        return map;
    }

    /**
     * @return This property map with all values expanded using the values in the given property map
     */
    public PropertyMap expandedWith(VariableMap<String> that)
    {
        var map = new PropertyMap();
        for (var key : keySet())
        {
            var value = get(key);
            map.put(key, that.expand(value));
        }
        return map;
    }

    @Override
    public String join(String separator)
    {
        var entries = new StringList();
        var keys = new ArrayList<>(keySet());
        keys.sort(Comparator.naturalOrder());
        for (var key : keys)
        {
            var comment = comments.get(key);
            if (comment != null)
            {
                entries.add("");
                entries.add("# " + comment);
            }
            entries.add(key + " = " + get(key));
        }
        return entries.join(separator);
    }

    public void save(WritableResource resource)
    {
        save(resource.baseName().name(), resource);
    }

    public void save(String heading, WritableResource resource)
    {
        var out = resource.printWriter();
        out.println(AsciiArt.box(heading, '#', '#'));
        out.println("");
        out.println(this);
        out.close();
    }

    /**
     * @return Loads the given properties resource, interpolating system variables into each value
     */
    private static PropertyMap load(Resource resource, ProgressReporter reporter)
    {
        var properties = new PropertyMap();
        var linePattern = Pattern.compile("(?<key>[^=]*?)\\s*=\\s*(?<value>[^=]*)");
        int lineNumber = 1;
        for (var line : resource.reader().lines(reporter))
        {
            var trimmed = line.trim();
            if (!trimmed.isEmpty() && !trimmed.startsWith("#") && !trimmed.startsWith("//"))
            {
                var matcher = linePattern.matcher(line);
                if (matcher.matches())
                {
                    var key = matcher.group("key");
                    var value = matcher.group("value");
                    properties.put(key, value);
                }
                else
                {
                    Ensure.fail("Cannot parse line $:$: $", resource.fileName(), lineNumber, line);
                }
            }
            lineNumber++;
        }
        return properties;
    }
}
