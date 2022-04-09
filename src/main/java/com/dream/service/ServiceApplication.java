package com.dream.service;

import com.dream.container.anno.Assign;
import com.dream.container.anno.Component;
import com.dream.container.anno.Exec;
import com.dream.container.anno.LaunchArg;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

@Component(proxy = false, instant = true)
public class ServiceApplication
{
    @Assign
    private ServiceChannelInitialize channelInitialize;

    @LaunchArg("-port")
    private int listenPort;

    @Exec
    public void run()
    {
        LogService.LOG.info("service application startup. listen port: {}", listenPort);

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(channelInitialize);

        bootstrap.bind(listenPort);
    }
}
