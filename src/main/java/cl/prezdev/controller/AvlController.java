package cl.prezdev.controller;

import cl.prezdev.service.AvlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/avl")
@RequiredArgsConstructor
public class AvlController {

    private final AvlService avlService;

    @PostMapping("/add")
    public ResponseEntity<String> addDevices(@RequestParam String type, @RequestParam int count) {
        return ResponseEntity.ok(avlService.addDevices(type, count));
    }

    @GetMapping("/list")
    public ResponseEntity<String> listDevices() {
        return ResponseEntity.ok(avlService.listDevices());
    }

    @GetMapping("/stat")
    public ResponseEntity<String> getStats() {
        return ResponseEntity.ok(avlService.getStats());
    }

    @DeleteMapping("/remove-all")
    public ResponseEntity<String> removeAll() {
        return ResponseEntity.ok(avlService.removeAllDevices());
    }

    @PostMapping("/start")
    public ResponseEntity<String> startAll() {
        return ResponseEntity.ok(avlService.startAll());
    }

    @PostMapping("/stop")
    public ResponseEntity<String> stopAll() {
        return ResponseEntity.ok(avlService.stopAll());
    }
}

