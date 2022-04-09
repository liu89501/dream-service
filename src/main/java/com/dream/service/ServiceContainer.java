package com.dream.service;

import com.dream.container.Container;
import com.dream.container.InitializeTemporaryParams;
import com.dream.container.InstanceDefinition;
import com.dream.service.anno.DedicatedServer;
import com.dream.service.anno.Mark;
import com.dream.service.anno.Service;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class ServiceContainer implements Container
{
    private final Map<Integer, ServiceDescription> serviceInstances = new HashMap<>();

    @Override
    public boolean canHosting(Class<?> scannedClass)
    {
        return scannedClass.isAnnotationPresent(Service.class);
    }

    @Override
    public void add(Class<?> scannedClass) throws Exception
    {
        Object serviceInstance = scannedClass.getConstructor().newInstance();

        for (Method method : scannedClass.getMethods())
        {
            Mark mark = method.getDeclaredAnnotation(Mark.class);
            if (mark == null)
            {
                continue;
            }

            int markValue = mark.value();
            if (serviceInstances.get(markValue) != null)
            {
                throw new RuntimeException("Repeated Mark " + markValue);
            }

            ServiceDescription description = new ServiceDescription(method, serviceInstance, markValue,
                    mark.authenticate(), method.isAnnotationPresent(DedicatedServer.class));

            serviceInstances.put(markValue, description);
        }
    }

    @Override
    public List<InstanceDefinition> getInstances()
    {
        HashSet<Object> instances = new HashSet<>();

        for (Map.Entry<Integer, ServiceDescription> entry : serviceInstances.entrySet())
        {
            instances.add(entry.getValue().serviceInstance());
        }

        return instances.stream()
                .map(InstanceDefinition::new)
                .toList();
    }

    @Override
    public void initializeContainerParam(InitializeTemporaryParams initializeTemporaryParams)
    {
    }


    public ServiceDescription getService(int mark)
    {
        return serviceInstances.get(mark);
    }

}
