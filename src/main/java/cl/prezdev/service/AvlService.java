package cl.prezdev.service;

public interface AvlService {
    String addDevices(String type, int count);

    String listDevices();
    
    String getStats();
    
    String removeAllDevices();
    
    String startAll();
    
    String stopAll();
}
