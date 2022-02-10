package com.telenav.kivakit.kernel.interfaces.io.serialization;

import com.telenav.kivakit.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.kernel.language.collections.map.ObjectMap;

public interface CollectionReader
{
    <Element> ObjectList<Element> readList();

    <Element> ObjectList<Element> readList(Class<Element> type);

    <Key, Value> ObjectMap<Key, Value> readMap();
}
