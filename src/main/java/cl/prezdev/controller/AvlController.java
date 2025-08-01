package cl.prezdev.controller;

import cl.prezdev.model.response.AddAvlResponse;
import cl.prezdev.model.response.ListAvlsResponse;
import cl.prezdev.model.response.RemoveAllAvlsResponse;
import cl.prezdev.model.response.StartAllResponse;
import cl.prezdev.model.response.StatResponse;
import cl.prezdev.model.response.StopAllResponse;
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
    public ResponseEntity<AddAvlResponse> addAvls(@RequestParam String type, @RequestParam int count) {
        return ResponseEntity.ok(avlService.addAvls(type, count));
    }

    @GetMapping("/list")
    public ResponseEntity<ListAvlsResponse> listAvls(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(avlService.listAvls(page, size));
    }

    @GetMapping("/stat")
    public ResponseEntity<StatResponse> getStats() {
        return ResponseEntity.ok(avlService.getStats());
    }

    @DeleteMapping("/remove-all")
    public ResponseEntity<RemoveAllAvlsResponse> removeAll() {
        return ResponseEntity.ok(avlService.removeAllAvls());
    }

    @PostMapping("/start")
    public ResponseEntity<StartAllResponse> startAll() {
        return ResponseEntity.ok(avlService.startAll());
    }

    @PostMapping("/stop")
    public ResponseEntity<StopAllResponse> stopAll() {
        return ResponseEntity.ok(avlService.stopAll());
    }
}
