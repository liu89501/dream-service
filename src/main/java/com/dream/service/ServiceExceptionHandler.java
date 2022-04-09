package com.dream.service;

import java.lang.reflect.Method;

public interface ServiceExceptionHandler
{
    Object handleException(Throwable throwable, Method serviceMethod);
}
