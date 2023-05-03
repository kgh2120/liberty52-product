package com.liberty52.product.service.controller.guest;

import com.liberty52.product.service.applicationservice.MonoItemOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GuestMonoItemOrderController {

    private final MonoItemOrderService monoItemOrderService;


}
