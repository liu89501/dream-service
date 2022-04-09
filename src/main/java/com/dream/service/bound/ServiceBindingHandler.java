package com.dream.service.bound;

import com.dream.container.anno.Assign;
import com.dream.container.anno.Component;
import com.dream.container.anno.LaunchArg;
import com.dream.service.ServiceContainer;
import com.dream.service.ServiceDescription;
import com.dream.container.utils.DreamUtils;
import com.dream.service.LogService;
import com.dream.service.ServiceExceptionHandler;
import com.dream.service.auth.AuthenticatedInfo;
import com.dream.service.codec.ParameterLoader;
import com.dream.service.codec.ParameterSaver;
import com.dream.service.codec.ServicePacketHandler;
import com.dream.service.utils.SerializeUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


@Sharable
@Component(proxy = false)
public class ServiceBindingHandler extends SimpleChannelInboundHandler<RoughingMessage>
{
    @Assign
    private ServiceContainer container;

    @Assign
    private ServicePacketHandler servicePacketHandler;

    @Assign
    private ServiceExceptionHandler serviceExceptionHandler;

    @LaunchArg("-use_dev_env")
    private boolean useDevEnvironment;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RoughingMessage message) throws Exception
    {
        ServiceDescription description = container.getService(message.getServiceMark());

        if (description == null)
        {
            LogService.LOG.error("请求了一个不存在的服务, mark: {}", message.getServiceMark());
            return;
        }

        AuthenticatedInfo authenticatedInfo;

        if (description.requireAuthenticate())
        {
            authenticatedInfo = ctx.channel().attr(AuthenticateHandler.AUTHORIZATION_KEY).get();

            if (authenticatedInfo == null)
            {
                LogService.LOG.info("客户端未授权");
                ctx.close();
                return;
            }

            if (description.isServerService() && !authenticatedInfo.isServer() && !useDevEnvironment)
            {
                LogService.LOG.info("非法调用服务器专用接口, MARK_ID: {}", message.getServiceMark());
                ctx.close();
                return;
            }
        }
        else
        {
            authenticatedInfo = null;
        }

        java.lang.reflect.Parameter[] parameters = description.method().getParameters();

        Object[] args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++)
        {
            Class<?> parameterType = parameters[i].getType();

            Object arg;

            if (ParameterLoader.class.isAssignableFrom(parameterType))
            {
                ParameterLoader loader = (ParameterLoader)parameterType.getConstructor().newInstance();
                loader.load(servicePacketHandler.buildPacket(message.getParameterData()));
                arg = loader;
            }
            else if (parameterType == ByteBuf.class)
            {
                arg = message.getParameterData();
            }
            else if (parameterType == AuthenticatedInfo.class)
            {
                arg = authenticatedInfo;
            }
            else if (parameterType == Channel.class)
            {
                arg = ctx.channel();
            }
            else if (DreamUtils.isCommonType(parameterType))
            {
                arg = SerializeUtils.loadPrimitiveParamType(parameterType,
                        servicePacketHandler.buildPacket(message.getParameterData()));
            }
            else
            {
                throw new IllegalArgumentException("不支持的参数类型");
            }

            args[i] = arg;
        }

        Object returnValue;
        try
        {
            returnValue = description.method().invoke(description.serviceInstance(), args);
        }
        catch (Throwable e)
        {
            LogService.LOG.error("service error", e);

            returnValue = serviceExceptionHandler.handleException(e, description.method());
        }

        if (returnValue == null)
        {
            return;
        }

        ByteBuf outBuf;
        if (returnValue instanceof ParameterSaver handleResult)
        {
            outBuf = ctx.alloc().directBuffer();
            handleResult.save(servicePacketHandler.buildPacket(outBuf));
        }
        else
        {
            outBuf = (ByteBuf) returnValue;
        }

        RoughingMessage outMessage = new RoughingMessage();
        outMessage.setServiceMark(message.getServiceMark());
        outMessage.setParameterData(outBuf);

        // ctx.channel().write 会从头部的handle开始
        ctx.writeAndFlush(outMessage);
    }
}
