package com.ekkelenkamp.netatmo2wow.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Device 
{
    private Map<String, List<String>> moduleIds = new HashMap<String, List<String>>();
    private Map<String, String> moduleDataTypes = new HashMap<String, String>();
    
    public void addModuleToDevice(String deviceId, String moduleId, String dataType) 
    {
        if (moduleIds.get(deviceId) != null) 
        {
            List<String> modules = moduleIds.get(deviceId);
            modules.add(moduleId);
            moduleDataTypes.put(moduleId, dataType);
        } 
        else 
        {
            List<String> modules = new ArrayList<String>();
            modules.add(moduleId);
            moduleIds.put(deviceId, modules);
            moduleDataTypes.put(moduleId, dataType);
        }
    }

    public String getModuleDataType(String moduleId)
    {
    	return moduleDataTypes.get(moduleId);
    }
    
    public List<String> getModules(String deviceId) 
    {
        return moduleIds.get(deviceId);
    }

    public Map<String, List<String>> getDevices() 
    {
        return moduleIds;
    }    
}
