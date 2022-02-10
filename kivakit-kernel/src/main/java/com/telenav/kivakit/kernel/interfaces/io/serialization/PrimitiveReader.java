package com.telenav.kivakit.kernel.interfaces.io.serialization;

public interface PrimitiveReader
{
    boolean readBoolean();

    byte readByte();

    char readChar();

    double readDouble();

    float readFloat();

    int readInt();

    long readLong();

    short readShort();

    String readString();
}
