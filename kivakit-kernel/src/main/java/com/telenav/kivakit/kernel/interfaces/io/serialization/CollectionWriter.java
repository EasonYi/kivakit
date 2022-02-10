package com.telenav.kivakit.kernel.interfaces.io.serialization;

import java.util.List;
import java.util.Map;

public interface CollectionWriter
{
    <Element> void writeList(List<Element> list);

    <Element> void writeList(List<Element> list, Class<Element> type);

    <Key, Value> void writeMap(Map<Key, Value> map);
}
