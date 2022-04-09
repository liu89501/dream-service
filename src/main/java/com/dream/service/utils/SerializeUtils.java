package com.dream.service.utils;

import com.dream.service.codec.Packet;

public interface SerializeUtils
{
    /**
     * 限制加载集合的大小 防止集合类的size属性读取错误( 如果数据包是损坏的，那么解码的集合size可能会非常大，
     * 这会导致分配一个超大的内存从而影响性能甚至JVM崩溃, 在这里限制下大于这个数量的集合会直接报错 )
     */
    int Load_List_Size_Limit = 0xFFFF;

    static int getListSize(Packet packet)
    {
        int size = packet.readInt();

        if (size > Load_List_Size_Limit)
        {
            throw new IllegalArgumentException("读取的数据长度过大: " + size);
        }

        return size;
    }

    static Object loadPrimitiveParamType(Class<?> parameterType, Packet packet)
    {
        if (parameterType == int.class || parameterType == Integer.class)
        {
            return packet.readInt();
        }
        else if (parameterType == byte.class || parameterType == Byte.class)
        {
            return packet.readByte();
        }
        else if (parameterType == short.class || parameterType == Short.class)
        {
            return packet.readShort();
        }
        else if (parameterType == long.class || parameterType == Long.class)
        {
            return packet.readLong();
        }
        else if (parameterType == String.class)
        {
            return packet.readString();
        }
        else if (parameterType == boolean.class || parameterType == Boolean.class)
        {
            return packet.readBool();
        }

        throw new IllegalArgumentException("不支持的类型: " + parameterType);
    }

    static void savePrimitiveParam(Object param, Packet packet)
    {
        if (param instanceof Integer intVal)
        {
            packet.writeInt(intVal);
        }
        else if (param instanceof Byte byteVal)
        {
            packet.writeByte(byteVal);
        }
        else if (param instanceof Short shortVal)
        {
            packet.writeShort(shortVal);
        }
        else if (param instanceof Long longVal)
        {
            packet.writeLong(longVal);
        }
        else if (param instanceof String strVal)
        {
            packet.writeString(strVal);
        }
        else if (param instanceof Boolean boolVal)
        {
            packet.writeBool(boolVal);
        }
        else
        {
            throw new IllegalArgumentException("不支持的类型, " + param.getClass());
        }
    }
}
