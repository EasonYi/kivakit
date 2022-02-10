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

package com.telenav.kivakit.serialization.core;

import com.telenav.kivakit.kernel.interfaces.factory.Factory;
import com.telenav.kivakit.kernel.messaging.Listener;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureNotNull;

/**
 * Creates new instances of {@link BinarySerializationSession} using the {@link Factory} passed to the constructor.
 *
 * <p><b>Thread-Local Session Factories</b></p>
 *
 * <p>
 * The convenience methods {@link #threadLocal(BinarySerializationSessionFactory)} and {@link #threadLocal()} can be
 * used to store and retrieve a session factory by thread.
 * </p>
 *
 * @author jonathanl (shibo)
 */
public class BinarySerializationSessionFactory
{
    private static ThreadLocal<BinarySerializationSessionFactory> local;

    /**
     * Sets the thread-local session factory
     */
    public static void threadLocal(BinarySerializationSessionFactory factory)
    {
        local = ThreadLocal.withInitial(() -> factory);
    }

    /**
     * @return Retrieves the thread-local session factory
     */
    public static BinarySerializationSessionFactory threadLocal()
    {
        ensureNotNull(local);
        return local.get();
    }

    /** Factory that produces serialization objects */
    private final Factory<BinarySerializationSession> factory;

    /**
     * @param factory The factory for creating {@link BinarySerializationSession} objects
     */
    public BinarySerializationSessionFactory(Factory<BinarySerializationSession> factory)
    {
        this.factory = factory;
    }

    /**
     * @return A thread-local {@link BinarySerializationSession} object with only the given listener
     */
    public BinarySerializationSession session(Listener listener)
    {
        return listener.listenTo(factory.newInstance());
    }
}
