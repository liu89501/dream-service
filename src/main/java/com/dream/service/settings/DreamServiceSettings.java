package com.dream.service.settings;

import com.dream.container.anno.Config;
import com.fasterxml.jackson.annotation.JsonProperty;

@Config(classpath = "settings-service.json")
public class DreamServiceSettings
{
    @JsonProperty("auth-timeout")
    private long authenticateTimeout;

    public long getAuthenticateTimeout()
    {
        return authenticateTimeout;
    }

    public void setAuthenticateTimeout(long authenticateTimeout)
    {
        this.authenticateTimeout = authenticateTimeout;
    }
}
