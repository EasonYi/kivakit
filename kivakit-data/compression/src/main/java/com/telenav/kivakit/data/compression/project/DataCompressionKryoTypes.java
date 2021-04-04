////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.data.compression.project;

import com.telenav.kivakit.core.serialization.kryo.KryoTypes;
import com.telenav.kivakit.data.compression.codecs.huffman.FastHuffmanDecoder;
import com.telenav.kivakit.data.compression.codecs.huffman.HuffmanCodec;
import com.telenav.kivakit.data.compression.codecs.huffman.character.HuffmanCharacterCodec;
import com.telenav.kivakit.data.compression.codecs.huffman.list.HuffmanStringListCodec;
import com.telenav.kivakit.data.compression.codecs.huffman.string.HuffmanStringCodec;
import com.telenav.kivakit.data.compression.codecs.huffman.tree.Code;
import com.telenav.kivakit.data.compression.codecs.huffman.tree.CodedSymbol;
import com.telenav.kivakit.data.compression.codecs.huffman.tree.Leaf;
import com.telenav.kivakit.data.compression.codecs.huffman.tree.Node;
import com.telenav.kivakit.data.compression.codecs.huffman.tree.Symbols;
import com.telenav.kivakit.data.compression.codecs.huffman.tree.Tree;

/**
 * @author jonathanl (shibo)
 */
public class DataCompressionKryoTypes extends KryoTypes
{
    public DataCompressionKryoTypes()
    {
        //----------------------------------------------------------------------------------------------
        // NOTE: To maintain backward compatibility of serialization, registration groups and the types
        // in each registration group must remain in the same order.
        //----------------------------------------------------------------------------------------------

        group("huffman", () ->
        {
            register(HuffmanCodec.class);
            register(HuffmanCharacterCodec.class);
            register(HuffmanStringListCodec.class);
            register(CodedSymbol.class);
            register(Code.class);
            register(Leaf.class);
            register(Node.class);
            register(Tree.class);
            register(Symbols.class);
            register(FastHuffmanDecoder.class);
            register(FastHuffmanDecoder.Table.class);
            register(FastHuffmanDecoder.Table.Entry.class);
            register(FastHuffmanDecoder.Table.Entry[].class);
            register(HuffmanStringCodec.class);
        });
    }
}
