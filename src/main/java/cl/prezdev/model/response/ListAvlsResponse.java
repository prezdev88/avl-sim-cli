package cl.prezdev.model.response;

import java.util.List;

import cl.prezdev.model.dto.AvlDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ListAvlsResponse {
    private List<AvlDto> avls;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;
}
