package org.example.event.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatusCode;

@Getter
@Setter
@ToString
@Builder
public class ErrorResponseDto {

    private HttpStatusCode errorCode;
    private String apiPath;
    private String errorMsg;
}
