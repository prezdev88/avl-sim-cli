package cl.prezdev.device;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DeviceManager {
    
    private final Map<Integer, String> devices = new ConcurrentHashMap<>();

    public void add(int id, String type) {
        devices.put(id, type);
    }

    public int count() {
        return devices.size();
    }

    public Map<Integer, String> all() {
        return devices;
    }

    public void clear() {
        devices.clear();
    }
}

