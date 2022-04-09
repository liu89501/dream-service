package com.dream.service;


import java.lang.reflect.Method;

public record ServiceDescription(Method method, Object serviceInstance, int mark,
                                 boolean requireAuthenticate, boolean isServerService)
{

}
