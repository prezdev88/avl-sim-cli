package cl.prezdev.service.impl;

import cl.prezdev.device.AvlManager;
import cl.prezdev.model.Avl;
import cl.prezdev.model.Queclink;
import cl.prezdev.model.Teltonika;
import cl.prezdev.model.dto.AvlDto;
import cl.prezdev.model.response.AddAvlResponse;
import cl.prezdev.model.response.ListAvlsResponse;
import cl.prezdev.model.response.RemoveAllAvlsResponse;
import cl.prezdev.model.response.StartAllResponse;
import cl.prezdev.model.response.StatResponse;
import cl.prezdev.model.response.StopAllResponse;
import cl.prezdev.service.AvlService;
import cl.prezdev.service.ImeiService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class AvlServiceImpl implements AvlService {

    private final AvlManager avlManager;
    private final ImeiService imeiService;

    private AtomicInteger nextId;

    @Value("${avl.simulation.send-interval-ms:5000}")
    private long sendIntervalMs;

    @PostConstruct
    public void init() {
        nextId = new AtomicInteger(1);
    }

    @Override
    public AddAvlResponse addAvls(String type, int count) {
        for (int i = 0; i < count; i++) {
            int id = nextId.getAndIncrement();
            Avl avl = gen(type);
            avlManager.add(id, avl);
        }
        
        return new AddAvlResponse(count, type.toUpperCase());
    }

    @Override
    public ListAvlsResponse listAvls() {
        if (avlManager.count() == 0) {
            return new ListAvlsResponse(Collections.emptyList());
        }

        return new ListAvlsResponse(map(avlManager.all()));
    }

    @Override
    public StatResponse getStats() {
        return new StatResponse(avlManager.count());
    }

    @Override
    public RemoveAllAvlsResponse removeAllAvls() {
        int count = avlManager.count();
        avlManager.clear();
        return new RemoveAllAvlsResponse(count);
    }

    @Override
    public StartAllResponse startAll() {
        if (avlManager.count() == 0) {
            return new StartAllResponse("No avls to start.");
        }

        avlManager.startAll();

        return new StartAllResponse("[OK] All avls started.");
    }

    @Override
    public StopAllResponse stopAll() {
        if (avlManager.count() == 0) {
            return new StopAllResponse("No avls to stop.");
        }
        
        avlManager.stopAll();

        return new StopAllResponse("All avls stopped.");
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

    private List<AvlDto> map(Map<Integer, Avl> avls) {
        return avls.entrySet().stream()
            .map(entry -> {
                Integer id = entry.getKey();
                Avl avl = entry.getValue();
                return new AvlDto(id, avl.getImei(), avl.getProvider(), avl.isStarted());
            })
            .toList();
    }
}
