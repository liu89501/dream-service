package com.dream.service.bound;

import com.dream.container.anno.Assign;
import com.dream.container.anno.Component;
import com.dream.container.anno.Transaction;
import com.dream.service.auth.AuthenticateControl;
import com.dream.service.auth.AuthenticatedInfo;
import com.dream.service.settings.DreamServiceSettings;
import com.dream.service.LogService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

import java.util.concurrent.TimeUnit;

@Component
@ChannelHandler.Sharable
public class AuthenticateHandler extends ChannelInboundHandlerAdapter
{
    public static final AttributeKey<AuthenticatedInfo> AUTHORIZATION_KEY = AttributeKey.valueOf("AUTHORIZATION");

    @Assign
    private DreamServiceSettings dreamServiceSettings;

    @Assign(require = false)
    private AuthenticateControl authenticateControl;

    @Override
    public void channelActive(ChannelHandlerContext ctx)
    {
        if (LogService.LOG.isInfoEnabled())
        {
            LogService.LOG.info("client connect: {}", ctx.channel().remoteAddress());
        }

        ctx.executor().schedule(() ->
        {
            // 指定时间内没有认证的话就断开连接
            if (ctx.channel().attr(AUTHORIZATION_KEY).get() == null)
            {
                ctx.close();
            }

        }, dreamServiceSettings.getAuthenticateTimeout(), TimeUnit.SECONDS);
    }

    @Transaction
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception
    {
        if (authenticateControl != null)
        {
            authenticateControl.OnLogout(ctx.channel().attr(AUTHORIZATION_KEY).get());
        }
    }
}
