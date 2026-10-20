package org.example.event.config.client;

import org.example.event.dto.response.DomainResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "domain-service",
        url = "${domain-service.url}"
)
public interface DomainClient {

    @GetMapping("/api/domain/name/{name}")
    DomainResponseDto getDomainByName(
            @PathVariable String name);

//    @GetMapping("/api/domain/exists/{domainName}")
//    boolean isDomainExists(@PathVariable String domainName);
}