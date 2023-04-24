package com.liberty52.product.global.adapter.portone;

import com.liberty52.product.global.adapter.portone.dto.PortOneWebhookDto;
import com.liberty52.product.service.applicationservice.MonoItemOrderServiceImpl;
import com.liberty52.product.service.controller.dto.PreregisterOrderRequestDto;
import com.liberty52.product.service.controller.dto.PreregisterOrderResponseDto;
import com.liberty52.product.service.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class PortOneWebhookServiceImplTest {

    @Autowired
    private PortOneWebhookServiceImpl portOneWebhookService;
    @Autowired
    private MonoItemOrderServiceImpl monoItemOrderService;
    @Autowired
    private OrdersRepository ordersRepository;

    private final String AUTH_ID = UUID.randomUUID().toString();
    private static final String LIBERTY = "Liberty 52_Frame";
    private static final String OPTION_1 = "이젤 거치형";
    private static final String OPTION_2 = "1mm 두께 승화전사 인쇄용 알루미늄시트";
    private static final String OPTION_3 = "유광실버";
    MockMultipartFile imageFile = new MockMultipartFile("image", "test.png", "image/jpeg", new FileInputStream("src/test/resources/static/test.jpg"));

    String orderId;
    Long amount;

    PortOneWebhookServiceImplTest() throws IOException {
    }

    void test_confirmCardPayment_multiThread() throws InterruptedException {
        PreregisterOrderResponseDto dto = monoItemOrderService.preregisterCardPaymentOrders(AUTH_ID,
                PreregisterOrderRequestDto.forTest(
                        LIBERTY, List.of(OPTION_1, OPTION_2, OPTION_3), 2, List.of(),
                        "receiverName", "receiverEmail", "receiverPhoneNumber", "address1", "address2", "zipCode"),
                imageFile);

        orderId = dto.getMerchantId();
        amount = dto.getAmount();

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        CountDownLatch cl = new CountDownLatch(2);
        executorService.submit(() -> {
            System.out.println("S1");
            portOneWebhookService.testForhook(PortOneWebhookDto.testOf(orderId, "paid"), LIBERTY, amount, AUTH_ID);
            cl.countDown();
        });
        executorService.submit(() -> {
            System.out.println("S2");
            monoItemOrderService.confirmFinalApprovalOfCardPayment(AUTH_ID, orderId);
            cl.countDown();
        });
        cl.await();
    }


}