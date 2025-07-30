package cl.prezdev.command;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.stereotype.Component;

import cl.prezdev.device.DeviceManager;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
@ShellComponent
@RequiredArgsConstructor
public class AvlCommands {

    private final DeviceManager deviceManager;
    private final AtomicInteger nextId = new AtomicInteger(1);

    @ShellMethod(key = "avl add", value = "Adds N simulated devices of the given type (teltonika, queclink, etc.)")
    public String add(
            @ShellOption(help = "Device type") String type,
            @ShellOption(help = "Number of devices") int count
    ) {
        for (int i = 0; i < count; i++) {
            int id = nextId.getAndIncrement();
            deviceManager.add(id, type.toUpperCase());
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
}
