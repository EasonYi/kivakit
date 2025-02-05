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

package com.telenav.kivakit.settings.stores;

import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.registry.Registry;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.resource.ResourceFolder;
import com.telenav.kivakit.resource.serialization.ObjectSerializer;
import com.telenav.kivakit.resource.serialization.ObjectSerializers;
import com.telenav.kivakit.settings.Settings;
import com.telenav.kivakit.settings.SettingsObject;
import com.telenav.kivakit.settings.SettingsStore;
import com.telenav.kivakit.settings.lexakai.DiagramSettings;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import java.util.Set;

import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static com.telenav.kivakit.settings.SettingsStore.AccessMode.DELETE;
import static com.telenav.kivakit.settings.SettingsStore.AccessMode.INDEX;
import static com.telenav.kivakit.settings.SettingsStore.AccessMode.LOAD;
import static com.telenav.kivakit.settings.SettingsStore.AccessMode.UNLOAD;

/**
 * <p>
 * A folder containing settings objects defined by <i>.properties</i> files.
 * </p>
 *
 * <p>
 * A {@link ResourceFolderSettingsStore} can be created with {@link ResourceFolderSettingsStore(Listener,
 * ResourceFolder)}. The specified package should contain a set of settings files, each of which can be passed to the
 * {@link ObjectSerializer} for the file's extension to deserialize the object. Object serializers are located with the
 * {@link ObjectSerializers} object found in the global {@link Registry}.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see BaseResourceSettingsStore
 * @see ObjectSerializers
 * @see Settings
 * @see SettingsStore
 * @see SettingsObject
 * @see Folder
 */
@UmlClassDiagram(diagram = DiagramSettings.class)
public class ResourceFolderSettingsStore extends BaseResourceSettingsStore
{
    /** The folder containing .properties files defining settings objects */
    private final ResourceFolder<?> folder;

    /**
     * @param folder The folder containing .properties files specifying settings objects
     */
    public ResourceFolderSettingsStore(Listener listener, ResourceFolder<?> folder)
    {
        listener.listenTo(this);

        this.folder = folder;
    }

    @Override
    public Set<AccessMode> accessModes()
    {
        return Set.of(INDEX, DELETE, UNLOAD, LOAD);
    }

    @Override
    public String name()
    {
        return "[FolderSettingsStore folder = " + folder.path() + "]";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @UmlExcludeMember
    public Set<SettingsObject> onLoad()
    {
        var objects = new ObjectSet<SettingsObject>();

        // Go through files in the folder
        for (var resource : folder.resources())
        {
            // get any serializer for the file extension,
            var serializer = require(ObjectSerializers.class, ObjectSerializers::new)
                    .serializer(resource.extension());
            if (serializer != null)
            {
                objects.addIfNotNull(read(resource));
            }
        }

        return objects;
    }

    @Override
    public boolean onSave(SettingsObject object)
    {
        return unsupported();
    }

    @Override
    protected boolean onDelete(SettingsObject object)
    {
        return unsupported();
    }
}
