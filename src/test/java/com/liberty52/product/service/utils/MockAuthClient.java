package com.liberty52.product.service.utils;

import static com.liberty52.product.service.utils.MockConstants.MOCK_AUTHOR_NAME;
import static com.liberty52.product.service.utils.MockConstants.MOCK_AUTHOR_PROFILE_URL;

import com.liberty52.product.global.adapter.AuthClient;
import com.liberty52.product.service.controller.dto.AuthClientDataResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("test")
@Primary
@Service
public class MockAuthClient implements AuthClient {

    @Override
    public Map<String, AuthClientDataResponse> retrieveAuthData(Set<String> ids) {

        Map<String, AuthClientDataResponse> map = new HashMap<>();

        ids.forEach(id
                -> map.put(id, new AuthClientDataResponse(MOCK_AUTHOR_NAME, MOCK_AUTHOR_PROFILE_URL)));

        return map;
    }
}
