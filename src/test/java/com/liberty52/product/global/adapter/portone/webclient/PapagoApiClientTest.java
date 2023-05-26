package com.liberty52.product.global.adapter.portone.webclient;

import com.liberty52.product.global.adapter.PapagoApiClient;
import com.liberty52.product.global.constants.TranslationConstants;
import com.liberty52.product.global.exception.internal.PapagoApiException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class PapagoApiClientTest {
    // 필요할 때만 테스트. 비용..
    @Autowired
    PapagoApiClient client;
    String origin = "어두운 파란색 배경, 디지털 아트에 있는 수족관에 있는 귀여운 열대어의 3D 렌더링";
    @Test
    void translate() throws PapagoApiException.TooLongTextForTranslationException, PapagoApiException.TranslationSameSourceTargetException {
        PapagoApiClient.TranslationDto.Response response = client.postTranslation(TranslationConstants.LangCode.KOREAN, TranslationConstants.LangCode.ENGLISH, origin);
        assertNotNull(response);
    }

    @Test
    void detect() {
        PapagoApiClient.LangDetectionDto.Response response = client.postLangDetection(origin);
        assertNotNull(response);
    }
}
