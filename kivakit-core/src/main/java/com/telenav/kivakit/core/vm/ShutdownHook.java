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

package com.telenav.kivakit.core.vm;

import com.telenav.kivakit.core.lexakai.DiagramLanguage;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Adds order-of-execution to {@link Runtime#addShutdownHook(Thread)}. Hooks can request that they be run {@link
 * Order#FIRST} or {@link Order#LAST}. The order of two hooks both requesting to be first or last is not defined. It is
 * only guaranteed that hooks asking to be run last will be run after hooks requesting to be run first.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguage.class)
public class ShutdownHook
{
    private static final LinkedList<ShutdownHook> queue = new LinkedList<>();

    static
    {
        var shutdown = new Thread(() ->
        {
            List<ShutdownHook> copy;
            synchronized (queue)
            {
                copy = new ArrayList<>(queue);
            }
            for (var hook : copy)
            {
                hook.execute();
            }
        });
        shutdown.setName("KivaKit Shutdown");
        Runtime.getRuntime().addShutdownHook(shutdown);
    }

    public static void register(Order order, Runnable code)
    {
        new ShutdownHook(order, code);
    }

    /**
     * The order that a hook should be run in, either among the set of first hooks, or among the set of last hooks. The
     * only guarantee is that a hook that is FIRST will run before any hook that is LAST
     */
    @LexakaiJavadoc(complete = true)
    public enum Order
    {
        /** The hook should be run before hooks that are marked as LAST */
        FIRST,

        /** The hook should be run after hooks that are marked as FIRST */
        LAST
    }

    private final Runnable code;

    private ShutdownHook(Order order, Runnable code)
    {
        synchronized (queue)
        {
            switch (order)
            {
                case FIRST:
                    queue.addFirst(this);
                    break;

                case LAST:
                    queue.add(this);
                    break;
            }
        }

        this.code = code;
    }

    private void execute()
    {
        code.run();
    }
}
