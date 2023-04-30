package com.liberty52.product.stub;

import com.liberty52.product.global.adapter.cloud.AuthServiceClient;
import com.liberty52.product.global.adapter.cloud.dto.AuthProfileDto;
import com.liberty52.product.service.controller.dto.AuthClientDataResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.liberty52.product.service.utils.MockConstants.MOCK_AUTHOR_NAME;
import static com.liberty52.product.service.utils.MockConstants.MOCK_AUTHOR_PROFILE_URL;

public class StubAuthServiceClient implements AuthServiceClient {
    @Override
    public AuthProfileDto getAuthProfile(String authId) {
        return new AuthProfileDto("mju.omnm@gmail.com", "빌드 테스터");
    }

    @Override
    public Map<String, AuthClientDataResponse> retrieveAuthData(Set<String> ids) {

        Map<String, AuthClientDataResponse> map = new HashMap<>();

        ids.forEach(id
                -> map.put(id, new AuthClientDataResponse(MOCK_AUTHOR_NAME, MOCK_AUTHOR_PROFILE_URL)));

        System.out.println(map);

        return map;
    }
}
