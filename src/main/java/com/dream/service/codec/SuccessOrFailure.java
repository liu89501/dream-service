package com.dream.service.codec;

public class SuccessOrFailure implements ParameterSaver
{
    private boolean serviceSuccess;

    private ParameterSaver parameter;

    public static final SuccessOrFailure FAIL = new SuccessOrFailure(false, null);
    public static final SuccessOrFailure SUCCESS = new SuccessOrFailure(true, null);

    public static SuccessOrFailure buildSuccess(ParameterSaver outParam)
    {
        return new SuccessOrFailure(true, outParam);
    }

    public static SuccessOrFailure build(boolean success, ParameterSaver outParam)
    {
        return new SuccessOrFailure(success, outParam);
    }

    public static SuccessOrFailure build(boolean success)
    {
        return success ? SUCCESS : FAIL;
    }

    public SuccessOrFailure(boolean serviceSuccess, ParameterSaver parameter)
    {
        this.serviceSuccess = serviceSuccess;
        this.parameter = parameter;
    }

    @Override
    public void save(Packet packet)
    {
        packet.writeBool(serviceSuccess);

        if (serviceSuccess && parameter != null)
        {
            parameter.save(packet);
        }
    }
}
