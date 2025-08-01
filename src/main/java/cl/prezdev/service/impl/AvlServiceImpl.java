package cl.prezdev.service.impl;

import cl.prezdev.device.AvlManager;
import cl.prezdev.model.Avl;
import cl.prezdev.model.Queclink;
import cl.prezdev.model.Teltonika;
import cl.prezdev.model.dto.AvlDto;
import cl.prezdev.model.response.*;
import cl.prezdev.service.AvlService;
import cl.prezdev.service.ImeiService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
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
        log.info("[INIT] AvlService initialized with sendIntervalMs={}", sendIntervalMs);
    }

    @Override
    public AddAvlResponse addAvls(String type, int count) {
        log.info("[ADD] Adding {} devices of type {}", count, type);
        for (int i = 0; i < count; i++) {
            int id = nextId.getAndIncrement();
            Avl avl = gen(type);
            avlManager.add(id, avl);
            log.debug("[ADD] Added device ID={} IMEI={} Type={}", id, avl.getImei(), avl.getProvider());
        }
        return new AddAvlResponse(count, type.toUpperCase());
    }

    @Override
    public ListAvlsResponse listAvls(int page, int size) {
        log.info("[LIST] Listing AVL devices - page={}, size={}", page, size);
        
        if (avlManager.count() == 0) {
            log.warn("[LIST] No simulated devices found");
            return new ListAvlsResponse(Collections.emptyList(), page, size, 0, 0, true, true);
        }

        List<AvlDto> allAvls = map(avlManager.all());
        long totalElements = allAvls.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, allAvls.size());
        
        List<AvlDto> pagedAvls = (startIndex >= allAvls.size()) 
            ? Collections.emptyList() 
            : allAvls.subList(startIndex, endIndex);
            
        boolean isFirst = page == 0;
        boolean isLast = page >= totalPages - 1;
        
        log.debug("[LIST] Returning {} devices for page {} of {}", pagedAvls.size(), page + 1, totalPages);
        
        return new ListAvlsResponse(pagedAvls, page, size, totalElements, totalPages, isFirst, isLast);
    }

    @Override
    public StatResponse getStats() {
        int count = avlManager.count();
        log.info("[STATS] Active device count: {}", count);
        return new StatResponse(count);
    }

    @Override
    public RemoveAllAvlsResponse removeAllAvls() {
        int count = avlManager.count();
        log.info("[REMOVE] Removing all devices, count={}", count);
        avlManager.clear();
        return new RemoveAllAvlsResponse(count);
    }

    @Override
    public StartAllResponse startAll() {
        if (avlManager.count() == 0) {
            log.warn("[START] No devices to start");
            return new StartAllResponse("No avls to start.");
        }

        log.info("[START] Starting all devices");
        avlManager.startAll();
        return new StartAllResponse("[OK] All avls started.");
    }

    @Override
    public StopAllResponse stopAll() {
        if (avlManager.count() == 0) {
            log.warn("[STOP] No devices to stop");
            return new StopAllResponse("No avls to stop.");
        }

        log.info("[STOP] Stopping all devices");
        avlManager.stopAll();
        return new StopAllResponse("All avls stopped.");
    }

    private Avl gen(String type) {
        String provider = type.toUpperCase();
        String imei = imeiService.generateImei();
        log.debug("[GEN] Generating device of type={} with IMEI={}", provider, imei);

        return switch (provider) {
            case "TELTONIKA" -> new Teltonika(imei, sendIntervalMs);
            case "QUECLINK"  -> new Queclink(imei, sendIntervalMs);
            default -> throw new IllegalArgumentException("Unsupported provider: " + provider);
        };
    }

    private List<AvlDto> map(Map<Integer, Avl> avls) {
        log.debug("[MAP] Mapping {} devices to DTOs", avls.size());
        return avls.entrySet().stream()
            .map(entry -> new AvlDto(
                entry.getKey(),
                entry.getValue().getImei(),
                entry.getValue().getProvider(),
                entry.getValue().isStarted()))
            .toList();
    }
}
