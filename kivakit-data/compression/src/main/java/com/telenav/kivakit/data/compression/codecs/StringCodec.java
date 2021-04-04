////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  © 2020 Telenav - All rights reserved.                                                                              /
//  This software is the confidential and proprietary information of Telenav ("Confidential Information").             /
//  You shall not disclose such Confidential Information and shall use it only in accordance with the                  /
//  terms of the license agreement you entered into with Telenav.                                                      /
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.data.compression.codecs;

import com.telenav.kivakit.data.compression.Codec;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.unsupported;

/**
 * A string codec compresses entire strings as symbols. Frequent strings in map data input, like "highway" can be
 * compressed to a single byte. Less frequent strings can be compressed one character at a time with {@link
 * CharacterCodec}.
 *
 * @author jonathanl (shibo)
 */
public interface StringCodec extends Codec<String>
{
    /**
     * Trains this string codec on the given string
     */
    default void train(final String string)
    {
        unsupported();
    }

    /**
     * Trains this codec on the given sequence of strings
     */
    default void train(final Iterable<String> strings)
    {
        for (final var string : strings)
        {
            train(string);
        }
    }
}
