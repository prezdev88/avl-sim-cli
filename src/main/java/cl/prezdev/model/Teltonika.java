package cl.prezdev.model;

public class Teltonika extends Avl {

    public Teltonika(String imei, long pauseMs) {
        super(imei, "TELTONIKA", pauseMs);
    }

    @Override
    protected String generateFrame() {
        return "TELTONIKA_FRAME_" + System.currentTimeMillis();
    }
}
