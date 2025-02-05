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

package com.telenav.kivakit.filesystem.loader;

import com.telenav.kivakit.core.logging.Logger;
import com.telenav.kivakit.core.logging.LoggerFactory;
import com.telenav.kivakit.core.messaging.Debug;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.filesystem.FilePath;
import com.telenav.kivakit.filesystem.local.LocalFileSystemService;
import com.telenav.kivakit.filesystem.spi.FileSystemService;
import com.telenav.kivakit.resource.lexakai.DiagramFileSystemService;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.visibility.UmlNotPublicApi;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Loads {@link FileSystemService}s with the Java {@link ServiceLoader} and chooses a {@link FileSystemService}
 * implementation for a given path by calling {@link FileSystemService#accepts(FilePath)} on each service until a match
 * is found.
 *
 * @author jonathanl (shibo)
 * @see FileSystemService
 * @see ServiceLoader
 */
@UmlClassDiagram(diagram = DiagramFileSystemService.class)
@UmlNotPublicApi
@LexakaiJavadoc(complete = true)
public class FileSystemServiceLoader
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    private static final Debug DEBUG = new Debug(LOGGER);

    private static final LocalFileSystemService LOCAL = new LocalFileSystemService();

    /** Loaded filesystem services */
    @UmlAggregation(label = "loads")
    private static List<FileSystemService> services;

    /**
     * @return The {@link FileSystemService} to use for the given path
     */
    @Nullable
    public static FileSystemService fileSystem(Listener listener, FilePath path)
    {
        assert path != null;

        // For each loaded filesystem service
        for (var service : services())
        {
            // if it accepts the path,
            if (service.accepts(path))
            {
                // choose that service
                return service;
            }
        }

        // otherwise, if it's a local path,
        if (LOCAL.accepts(path))
        {
            // choose the local filesystem service.
            return LOCAL;
        }

        // Couldn't find an applicable server.
        listener.problem("No filesystem service understands: " + path);
        return null;
    }

    private static synchronized List<FileSystemService> services()
    {
        if (services == null)
        {
            services = new ArrayList<>();
            for (var service : ServiceLoader.load(FileSystemService.class))
            {
                DEBUG.trace("Loaded file system '${class}'", service.getClass());
                services.add(service);
            }
        }
        return services;
    }
}
