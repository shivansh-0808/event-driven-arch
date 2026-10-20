package org.example.event.config.client;

import org.example.event.dto.response.ApplicationResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "application-service",
        url = "${application-service.url}"
)
public interface ApplicationClient {

    @GetMapping("/api/application/name/{name}")
    ApplicationResponseDto getApplicationByName(
            @PathVariable String name);

//    @GetMapping("/api/application/exists/{applicationName}")
//    boolean isApplicationExists(@PathVariable String applicationName);
}