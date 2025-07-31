package cl.prezdev.device;

import org.springframework.stereotype.Component;

import cl.prezdev.model.Avl;
import jakarta.annotation.PostConstruct;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AvlManager {
    
    private Map<Integer, Avl> avls;

    @PostConstruct
    public void init() {
        this.avls = new ConcurrentHashMap<>();
    }

    public void add(int id, Avl avl) {
        avls.put(id, avl);
    }

    public int count() {
        return avls.size();
    }

    public Map<Integer, Avl> all() {
        return avls;
    }

    public void clear() {
        avls.clear();
    }

    public void startAll() {
        for (Map.Entry<Integer, Avl> entry : avls.entrySet()) {
            Avl avl = entry.getValue();
            
            if (avl.isAlive()) {
                continue;
            }

            avl.start();
        }
    }

    public void stopAll() {
        for (Avl avl : avls.values()) {
            avl.interrupt();
        }
    }
}

