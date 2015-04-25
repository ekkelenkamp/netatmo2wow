package com.ekkelenkamp.netatmo2wow.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Device {

    private Map<String, List<String>> moduleIds = new HashMap<String, List<String>>();

    public void addModuleToDevice(String deviceId, String moduleId) {
        if (moduleIds.get(deviceId) != null) {
            List<String> modules = moduleIds.get(deviceId);
            modules.add(moduleId);
        } else {
            List<String> modules = new ArrayList<String>();
            modules.add(moduleId);
            moduleIds.put(deviceId, modules);

        }
    }

    public List<String> getModules(String deviceId) {
        return moduleIds.get(deviceId);
    }

    public Map<String, List<String>> getDevices() {
        return moduleIds;
    }
}
