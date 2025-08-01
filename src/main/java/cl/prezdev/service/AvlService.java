package cl.prezdev.service;

import cl.prezdev.model.response.AddAvlResponse;
import cl.prezdev.model.response.ListAvlsResponse;
import cl.prezdev.model.response.RemoveAllAvlsResponse;
import cl.prezdev.model.response.StartAllResponse;
import cl.prezdev.model.response.StatResponse;
import cl.prezdev.model.response.StopAllResponse;

public interface AvlService {
    AddAvlResponse addAvls(String type, int count);

    ListAvlsResponse listAvls(int page, int size);
    
    StatResponse getStats();
    
    RemoveAllAvlsResponse removeAllAvls();
    
    StartAllResponse startAll();
    
    StopAllResponse stopAll();
}
