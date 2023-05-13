package uz.devops.ratelimit.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ResponseDTO<T> {
    private Long requestCount;
    private String message;
    private Boolean success;
    private T data;
}
