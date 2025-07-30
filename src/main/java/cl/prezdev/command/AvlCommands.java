package cl.prezdev.command;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.stereotype.Component;

import cl.prezdev.device.DeviceManager;
import cl.prezdev.model.Avl;
import cl.prezdev.model.Queclink;
import cl.prezdev.model.Teltonika;
import cl.prezdev.service.ImeiService;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
@ShellComponent
@RequiredArgsConstructor
public class AvlCommands {

    @Value("${avl.simulation.send-interval-ms:5000}")
    private long sendIntervalMs;

    private final ImeiService imeiService;
    private final DeviceManager deviceManager;
    private final AtomicInteger nextId = new AtomicInteger(1);

    @ShellMethod(key = "avl add", value = "Adds N simulated devices of the given type (teltonika, queclink, etc.)")
    public String add(
        @ShellOption(help = "Device type") String type,
        @ShellOption(help = "Number of devices") int count
    ) {
        for (int i = 0; i < count; i++) {
            int id = nextId.getAndIncrement();
            Avl avl = gen(type);
            deviceManager.add(id, avl);
        }

        return "[OK] Added " + count + " devices of type " + type.toUpperCase();
    }

    @ShellMethod(key = "avl list", value = "Lists all active simulated devices.")
    public String list() {
        if (deviceManager.count() == 0) {
            return "[INFO] No simulated devices.";
        }

        return deviceManager.all().entrySet().stream()
                .map(entry -> "[ID " + entry.getKey() + "] Type: " + entry.getValue())
                .collect(Collectors.joining("\n"));
    }

    @ShellMethod(key = "avl stat", value = "Shows statistics of the current simulation.")
    public String stat() {
        return "[INFO] Active devices: " + deviceManager.count();
    }

    @ShellMethod(key = "avl remove all", value = "Remove all devices")
    public String removeAll() {
        int count = deviceManager.count();
        deviceManager.clear();

        return "[INFO] remove " + count + " devices";
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

    @ShellMethod(key = "avl start", value = "Starts all simulated AVL devices.")
    public String startAll() {
        if (deviceManager.count() == 0) {
            return "[WARN] No devices to start.";
        }

        deviceManager.startAll();
        return "[OK] All devices started.";
    }

    @ShellMethod(key = "avl stop", value = "Stops all simulated AVL devices.")
    public String stopAll() {
        if (deviceManager.count() == 0) {
            return "[WARN] No devices to stop.";
        }

        deviceManager.stopAll();
        return "[OK] All devices stopped.";
    }
}
