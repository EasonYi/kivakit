package com.telenav.kivakit.kernel.interfaces.io.serialization;

public interface PrimitiveWriter
{
    void writeBoolean(boolean value);

    void writeByte(byte value);

    void writeChar(char value);

    void writeDouble(double value);

    void writeFloat(float value);

    void writeInt(int value);

    void writeLong(long value);

    void writeShort(short value);

    void writeString(String value);
}
