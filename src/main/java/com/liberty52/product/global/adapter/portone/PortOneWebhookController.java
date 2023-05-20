package com.liberty52.product.global.adapter.portone;

import com.liberty52.product.global.adapter.portone.dto.PortOneWebhookDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PortOneWebhookController {

    private final PortOneService portOneService;

    @PostMapping("/port-one/webhook")
    @ResponseStatus(HttpStatus.OK)
    public void getWebhookFromPortOne(@RequestBody PortOneWebhookDto dto) {
        portOneService.hookPortOnePaymentInfo(dto);
    }

    @PostMapping("/port-one/webhook/test/{amount}")
    @ResponseStatus(HttpStatus.OK)
    public void getWebhookFromPortOneTest(@RequestBody PortOneWebhookDto dto, @PathVariable("amount") Long amount) {
        portOneService.hookPortOnePaymentInfoForTest(dto, amount);
    }

}
