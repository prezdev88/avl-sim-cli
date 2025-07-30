package cl.prezdev.device;

import org.springframework.stereotype.Component;

import cl.prezdev.model.Avl;
import jakarta.annotation.PostConstruct;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DeviceManager {
    
    private Map<Integer, Avl> devices;

    @PostConstruct
    public void init() {
        this.devices = new ConcurrentHashMap<>();
    }

    public void add(int id, Avl avl) {
        devices.put(id, avl);
    }

    public int count() {
        return devices.size();
    }

    public Map<Integer, Avl> all() {
        return devices;
    }

    public void clear() {
        devices.clear();
    }

    public void startAll() {
        for (Map.Entry<Integer, Avl> entry : devices.entrySet()) {
            Avl avl = entry.getValue();
            
            if (avl.isAlive()) {
                continue;
            }

            avl.start();
        }
    }

    public void stopAll() {
        for (Avl avl : devices.values()) {
            avl.interrupt();
        }
    }
}

