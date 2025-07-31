package cl.prezdev.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public abstract class Avl extends Thread {

    private long pauseMs;
    private String imei;
    private String provider;
    private boolean started;

    protected Avl(String imei, String provider, long pauseMs) {
        this.imei = imei;
        this.provider = provider;
        this.pauseMs = pauseMs;
    }

    @Override
    public void run() {
        try {
            started = true;
            while (!isInterrupted()) {
                String frame = generateFrame();
                sendFrame(frame);
                Thread.sleep(pauseMs);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            started = false;
        }
    }

    protected abstract String generateFrame();

    protected void sendFrame(String frame) {
        log.info("[{}] IMEI={} sent frame: {}", provider, imei, frame);
    }
}