package com.dream.service.codec;

import io.netty.buffer.ByteBuf;

public interface ServicePacketHandler
{
    Packet buildPacket(ByteBuf byteBuf);
}
