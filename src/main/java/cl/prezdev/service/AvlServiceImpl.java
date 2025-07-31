package cl.prezdev.service;

import cl.prezdev.device.DeviceManager;
import cl.prezdev.model.Avl;
import cl.prezdev.model.Queclink;
import cl.prezdev.model.Teltonika;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvlServiceImpl implements AvlService {

    private final ImeiService imeiService;
    private final DeviceManager deviceManager;
    private final AtomicInteger nextId = new AtomicInteger(1);

    @Value("${avl.simulation.send-interval-ms:5000}")
    private long sendIntervalMs;

    @Override
    public String addDevices(String type, int count) {
        for (int i = 0; i < count; i++) {
            int id = nextId.getAndIncrement();
            Avl avl = gen(type);
            deviceManager.add(id, avl);
        }
        return "[OK] Added " + count + " devices of type " + type.toUpperCase();
    }

    @Override
    public String listDevices() {
        if (deviceManager.count() == 0) {
            return "[INFO] No simulated devices.";
        }

        return deviceManager.all().entrySet().stream()
                .map(entry -> "[ID " + entry.getKey() + "] Type: " + entry.getValue())
                .collect(Collectors.joining("\n"));
    }

    @Override
    public String getStats() {
        return "[INFO] Active devices: " + deviceManager.count();
    }

    @Override
    public String removeAllDevices() {
        int count = deviceManager.count();
        deviceManager.clear();
        return "[INFO] Removed " + count + " devices";
    }

    @Override
    public String startAll() {
        if (deviceManager.count() == 0) {
            return "[WARN] No devices to start.";
        }
        deviceManager.startAll();
        return "[OK] All devices started.";
    }

    @Override
    public String stopAll() {
        if (deviceManager.count() == 0) {
            return "[WARN] No devices to stop.";
        }
        deviceManager.stopAll();
        return "[OK] All devices stopped.";
    }

    private Avl gen(String type) {
        String provider = type.toUpperCase();
        String imei = imeiService.generateImei();

        return switch (provider) {
            case "TELTONIKA" -> new Teltonika(imei, sendIntervalMs);
            case "QUECLINK"  -> new Queclink(imei, sendIntervalMs);
            default -> throw new IllegalArgumentException("Unsupported provider: " + provider);
        };
    }
}
