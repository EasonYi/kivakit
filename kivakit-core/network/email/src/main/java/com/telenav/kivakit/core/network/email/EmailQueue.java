////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.network.email;

import com.telenav.kivakit.core.network.email.project.lexakai.diagrams.DiagramEmail;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.interfaces.io.Closeable;
import com.telenav.kivakit.core.kernel.language.time.Duration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * A simple email queue with no persistent backing
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramEmail.class)
class EmailQueue implements Closeable
{
    private volatile boolean closed;

    private final ArrayBlockingQueue<Email> queue = new ArrayBlockingQueue<>(1000);

    /**
     * Closes the queue to new entries
     */
    @Override
    public void close()
    {
        closed = true;
    }

    public boolean isClosed()
    {
        return closed;
    }

    public boolean isEmpty()
    {
        return queue.isEmpty();
    }

    public boolean offer(final Email email, final Duration maximumWait)
    {
        if (!closed)
        {
            try
            {
                return queue.offer(email, maximumWait.asMilliseconds(), TimeUnit.MILLISECONDS);
            }
            catch (final InterruptedException ignored)
            {
            }
        }
        return false;
    }

    public void sent(final Email ignored)
    {
    }

    public Email take()
    {
        try
        {
            return queue.take();
        }
        catch (final InterruptedException ignored)
        {
            return null;
        }
    }
}
