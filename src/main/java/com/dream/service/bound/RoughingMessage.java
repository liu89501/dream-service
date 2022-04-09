package com.dream.service.bound;

import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCounted;

public class RoughingMessage implements ReferenceCounted
{
    private int serviceMark;

    private ByteBuf parameterData;

    public RoughingMessage()
    {
    }

    public RoughingMessage(int serviceMark, ByteBuf parameterData)
    {
        this.serviceMark = serviceMark;
        this.parameterData = parameterData;
    }

    public int getServiceMark()
    {
        return serviceMark;
    }

    public void setServiceMark(int serviceMark)
    {
        this.serviceMark = serviceMark;
    }

    public ByteBuf getParameterData()
    {
        return parameterData;
    }

    public void setParameterData(ByteBuf parameterData)
    {
        this.parameterData = parameterData;
    }

    @Override
    public int refCnt()
    {
        return 0;
    }

    @Override
    public ReferenceCounted retain()
    {
        return parameterData.retain();
    }

    @Override
    public ReferenceCounted retain(int increment)
    {
        return parameterData.retain(increment);
    }

    @Override
    public ReferenceCounted touch()
    {
        return this;
    }

    @Override
    public ReferenceCounted touch(Object hint)
    {
        return this;
    }

    @Override
    public boolean release()
    {
        return parameterData.release();
    }

    @Override
    public boolean release(int decrement)
    {
        return parameterData.release(decrement);
    }
}
