package com.liberty52.product.global.adapter.cloud;

import com.liberty52.product.global.adapter.cloud.dto.AuthProfileDto;
import com.liberty52.product.service.controller.dto.AuthClientDataResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;
import java.util.Set;

@FeignClient(value = "auth", primary = false)
public interface AuthServiceClient {

    @GetMapping(value = "/my")
    AuthProfileDto getAuthProfile(@RequestHeader(HttpHeaders.AUTHORIZATION) String authId);

    @PostMapping("/info")
    Map<String, AuthClientDataResponse> retrieveAuthData(@RequestBody Set<String> ids);

}
