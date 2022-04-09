package com.dream.service;

import com.dream.container.anno.Assign;
import com.dream.container.anno.Component;
import com.dream.service.bound.AuthenticateHandler;
import com.dream.service.bound.ServiceBindingHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

@Component(proxy = false, instant = true)
public class ServiceChannelInitialize extends ChannelInitializer<SocketChannel>
{
    @Assign
    private AuthenticateHandler authenticateHandler;

    @Assign
    private ServiceBindingHandler serviceBindingHandler;

    @Assign
    private ServiceChannelBuilder serviceChannelBuilder;

    @Override
    public void initChannel(SocketChannel socketChannel) throws Exception
    {
        ChannelPipeline pipeline = socketChannel.pipeline();

        pipeline.addLast(authenticateHandler);

        serviceChannelBuilder.buildChannel(pipeline);

        pipeline.addLast(serviceBindingHandler);
    }
}
