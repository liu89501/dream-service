package com.dream.service;

import io.netty.channel.ChannelPipeline;

public interface ServiceChannelBuilder
{
    void buildChannel(ChannelPipeline pipeline);
}
