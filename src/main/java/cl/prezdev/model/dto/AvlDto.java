package cl.prezdev.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AvlDto {
    private Integer id;
    private String imei;
    private String provider;
    private boolean started;
}
