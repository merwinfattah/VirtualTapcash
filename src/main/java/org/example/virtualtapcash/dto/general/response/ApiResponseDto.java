package org.example.virtualtapcash.dto.general.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseDto<T> {
    private String status;
    private T data;
    private String message;
}
