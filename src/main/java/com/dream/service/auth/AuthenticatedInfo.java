package com.dream.service.auth;

public class AuthenticatedInfo
{
    // serverId 字段只在服务器登录时才会有值
    private int playerId;
    //private String platformAccountId;
    private String serverId;
    private boolean isServer;

    public AuthenticatedInfo(int playerId, String serverId, boolean isServer)
    {
        this.playerId = playerId;
        this.serverId = serverId;
        this.isServer = isServer;
    }

    public int getPlayerId()
    {
        return playerId;
    }

    public void setPlayerId(int playerId)
    {
        this.playerId = playerId;
    }

    public String getServerId()
    {
        return serverId;
    }

    public void setServerId(String serverId)
    {
        this.serverId = serverId;
    }

    public boolean isServer()
    {
        return isServer;
    }

    public void setServer(boolean server)
    {
        isServer = server;
    }
}
