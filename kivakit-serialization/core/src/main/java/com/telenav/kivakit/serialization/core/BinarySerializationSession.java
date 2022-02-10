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

import com.telenav.kivakit.kernel.interfaces.io.serialization.BinarySerializer;
import com.telenav.kivakit.kernel.language.io.ProgressiveInput;
import com.telenav.kivakit.kernel.language.io.ProgressiveOutput;
import com.telenav.kivakit.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.kernel.language.values.version.Version;
import com.telenav.kivakit.kernel.language.values.version.VersionedObject;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.serialization.core.project.lexakai.diagrams.DiagramSerializationCore;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * An abstraction for binary serialization.
 *
 * <p>
 * This interface allows the serialization of {@link VersionedObject}s, during a bracketed serialization session. This
 * design provides ease-of-use while ensuring that all serialized objects as well as the serialization stream itself are
 * assigned a version.
 * </p>
 *
 * <p><b>Creating a Session</b></p>
 *
 * <p>
 * The method {@link BinarySerializationSessionFactory#session(Listener)} can be called to obtain a {@link
 * BinarySerializationSession} instance.
 * </p>
 *
 * <p><b>Opening a Session</b></p>
 *
 * <p>
 * A serialization session is initiated by calling {@link #open(Type, Version, InputStream, OutputStream)}, after which
 * versioned objects can be then read with {@link #readVersionedObject()} and written with {@link
 * #write(VersionedObject)}.
 * </p>
 *
 * <p><b>Example</b></p>
 *
 * <pre>
 * var serialization = threadSerialization();
 * serialization.startSession(Type.RESOURCE, input, output);
 * serialization.write(new VersionedObject&lt;&gt;("hello"));
 * serialization.endSession();
 * </pre>
 *
 * @author jonathanl (shibo)
 * @see BinarySerializationSessionFactory
 * @see VersionedObject
 * @see Version
 */
@UmlClassDiagram(diagram = DiagramSerializationCore.class)
public interface BinarySerializationSession extends
        SerializationSession,
        BinarySerializer
{
    /**
     * @param listener The listener for the session
     * @return The serialization session for the calling thread
     */
    static BinarySerializationSession threadLocal(Listener listener)
    {
        return BinarySerializationSessionFactory.threadLocal().session(listener);
    }

    /**
     * The type of serialization session. This determines the order of
     */
    enum Type
    {
        /** This session is interacting with a client socket */
        CLIENT,

        /** This session is interacting with a server socket */
        SERVER,

        /** This session is serializing to a resource */
        RESOURCE
    }

    @Override
    default void close()
    {
        onClose();
    }

    /**
     *
     */
    default boolean isActive()
    {
        return isReading() || isWriting();
    }

    /**
     * @return True if data is being read
     */
    boolean isReading();

    /**
     * @return True if data is being written
     */
    boolean isWriting();

    /**
     * Ends a serialization session, flushing any pending output.
     */
    void onClose();

    /**
     * @return Opens the given socket for reading and writing. Version handshaking is performed automatically for {@link
     * Type#SERVER}s and {@link Type#CLIENT}s with the version of the connected endpoint returned to the caller.
     */
    default Version open(Type type,
                         Version version,
                         Socket socket,
                         ProgressReporter reporter)
    {
        try
        {
            trace("Opening socket");
            return open
                    (
                            type,
                            version,
                            new ProgressiveInput(socket.getInputStream(), reporter),
                            new ProgressiveOutput(socket.getOutputStream(), reporter)
                    );
        }
        catch (Exception e)
        {
            fatal(e, "Socket connection failed");
            return null;
        }
    }

    /**
     * Opens this session for reading
     *
     * @return The version or an exception is thrown
     */
    default Version open(Type type, Version version, InputStream input)
    {
        return open(type, version, input, null);
    }

    /**
     * Opens this session for writing
     *
     * @return The version or an exception is thrown
     */
    default Version open(Type type, Version version, OutputStream output)
    {
        return open(type, version, null, output);
    }

    /**
     * Opens this session for reading and writing. Retains the given input and output streams for future use, and
     * performs version handshaking per the {@link Type} parameter.
     *
     * @return The resource, client or server version, or an exception
     */
    Version open(Type type, Version version, InputStream input, OutputStream output);
}
