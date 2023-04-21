package com.liberty52.product.global.adapter;

import com.liberty52.product.service.controller.dto.AuthClientDataResponse;
import java.util.Map;
import java.util.Set;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "auth", primary = false)
public interface AuthClient {

    @PostMapping("/info")
    Map<String, AuthClientDataResponse> retrieveAuthData(@RequestBody Set<String> ids);

}
