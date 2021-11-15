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

package com.telenav.kivakit.configuration.settings;

import com.telenav.kivakit.configuration.project.lexakai.diagrams.DiagramConfiguration;
import com.telenav.kivakit.kernel.language.paths.PackagePath;
import com.telenav.kivakit.resource.path.Extension;
import com.telenav.kivakit.resource.resources.packaged.Package;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.unsupported;

/**
 * <p>
 * A Java package containing {@link SettingsObject}s.
 * </p>
 *
 * <p>
 * A {@link SettingsPackage} can be created with {@link #of(Package)} or {@link #of(PackagePath)}. The specified package
 * should contain a set of .properties files, each of which can be used to instantiate and populate a settings object.
 * <i>See {@link Settings} for details on how this works.</i>
 * </p>
 *
 * @author jonathanl (shibo)
 * @see Settings
 * @see SettingsStore
 * @see SettingsObject
 * @see Package
 * @see PackagePath
 */
@SuppressWarnings("ClassEscapesDefinedScope")
@UmlClassDiagram(diagram = DiagramConfiguration.class)
public class SettingsPackage extends BaseSettingsStore
{
    private static final Map<PackagePath, SettingsPackage> packages = new HashMap<>();

    public static SettingsPackage of(Package _package)
    {
        return of(_package.path());
    }

    public static SettingsPackage of(PackagePath path)
    {
        return packages.computeIfAbsent(path, ignored -> new SettingsPackage(path));
    }

    /** The path of this configuration package */
    private final PackagePath path;

    /**
     * @param path The path to the package where the configurations are stored
     */
    protected SettingsPackage(PackagePath path)
    {
        this.path = path;
    }

    @Override
    public boolean isReadOnly()
    {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @UmlExcludeMember
    public Set<SettingsObject> load()
    {
        // Go through .properties files in the package
        var _package = Package.packageFrom(path);
        trace("Loading resources from $", _package);
        Set<SettingsObject> entries = new HashSet<>();
        for (var resource : _package.resources(Extension.PROPERTIES::ends))
        {
            // load the properties file
            var configuration = loadFromProperties(resource);
            if (configuration != null)
            {
                // and add the configuration
                entries.add(configuration);
            }
        }
        trace("Loaded $ resources", entries.size());
        return entries;
    }

    @Override
    public String name()
    {
        return "[SettingsPackage package = " + path + "]";
    }

    @Override
    public void save(Set<SettingsObject> objects)
    {
        unsupported();
    }
}
