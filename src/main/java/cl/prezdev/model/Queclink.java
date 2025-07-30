package cl.prezdev.model;

public class Queclink extends Avl {

    public Queclink(String imei, long pauseMs) {
        super(imei, "QUECLINK", pauseMs);
    }

    @Override
    protected String generateFrame() {
        return "QUECLINK_FRAME_" + System.currentTimeMillis();
    }
}
