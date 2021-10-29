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

import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.kernel.language.matchers.AnythingMatcher;
import com.telenav.kivakit.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.resource.spi.ResourceFolderResolverServiceLoader;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

import java.util.List;

import static com.telenav.kivakit.filesystem.Folder.Type.NORMAL;
import static com.telenav.kivakit.resource.CopyMode.OVERWRITE;

/**
 * A resource container is an abstraction that provides access to hierarchical resources, independent of implementation.
 * {@link Folder} is a {@link ResourceFolder}, but {@link Package is also a resource container.
 *
 * @author jonathanl (shibo)
 */
public interface ResourceFolder extends UriIdentified
{
    static ResourceFolderIdentifier identifier(String identifier)
    {
        return new ResourceFolderIdentifier(identifier);
    }

    static ResourceFolder resolve(String identifier)
    {
        return resolve(new ResourceFolderIdentifier(identifier));
    }

    static ResourceFolder resolve(ResourceFolderIdentifier identifier)
    {
        return ResourceFolderResolverServiceLoader.resolve(identifier);
    }

    /**
     * Converts to and from {@link ResourceFolder}s by resolving strings via {@link ResourceFolderIdentifier}s.
     *
     * @author jonathanl (shibo)
     */
    @LexakaiJavadoc(complete = true)
    class Converter extends BaseStringConverter<ResourceFolder>
    {
        public Converter(Listener listener)
        {
            super(listener);
        }

        @Override
        protected ResourceFolder onToValue(String value)
        {
            return new ResourceFolderIdentifier(value).resolve();
        }
    }

    /**
     * @return The child resource container at the given relative path
     */
    ResourceFolder folder(String path);

    ResourceFolderIdentifier identifier();

    boolean isMaterialized();

    default Folder materialize()
    {
        return materializeTo(Folder.temporaryForProcess(NORMAL));
    }

    default Folder materializeTo(Folder folder)
    {
        if (!isMaterialized())
        {
            folder.mkdirs().clearAll();
            for (var resource : resources())
            {
                var destination = folder.file(resource.fileName());
                resource.safeCopyTo(destination, OVERWRITE);
            }
        }
        return folder;
    }

    /**
     * @return The resource of the given in this container
     */
    Resource resource(String name);

    /**
     * @return The resources in this folder matching the given matcher
     */
    List<? extends Resource> resources(Matcher<? super Resource> matcher);

    /**
     * @return The resources in this folder
     */
    default List<? extends Resource> resources()
    {
        return resources(new AnythingMatcher<>());
    }

    /**
     * Copy the resources in this package to the given folder
     */
    default void safeCopyTo(Folder folder, CopyMode mode, ProgressReporter reporter)
    {
        for (var at : resources())
        {
            var destination = folder.mkdirs().file(at.fileName());
            if (mode.canCopy(at, destination))
            {
                at.safeCopyTo(destination, mode, reporter);
            }
        }
    }
}
