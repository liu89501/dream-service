package com.dream.service.codec;

import java.nio.charset.Charset;

public interface Packet
{
    int readInt();
    long readLong();
    byte readByte();
    short readShort();
    float readFloat();
    double readDouble();
    boolean readBool();
    String readString();
    String readString(Charset charset);
    void readBytes(byte[] bytes);

    /**
     * 可读的长度
     * @return length
     */
    int available();

    void writeInt(int value);
    void writeLong(long value);
    void writeByte(byte value);
    void writeShort(short value);
    void writeFloat(float value);
    void writeDouble(double value);
    void writeBool(boolean value);
    void writeString(String value);
    void writeString(String value, Charset charset);
    void writeBytes(byte[] bytes);
}
