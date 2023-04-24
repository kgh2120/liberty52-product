package com.liberty52.product.service.entity;

import com.liberty52.product.global.adapter.S3Uploader;
import com.liberty52.product.service.applicationservice.MonoItemOrderService;
import com.liberty52.product.service.controller.dto.MonoItemOrderRequestDto;
import com.liberty52.product.service.controller.dto.MonoItemOrderResponseDto;
import com.liberty52.product.service.repository.OptionDetailRepository;
import com.liberty52.product.service.repository.OrdersRepository;
import com.liberty52.product.service.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class OrdersEntityTest {

    @Autowired
    private MonoItemOrderService monoItemOrderService;
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OptionDetailRepository optionDetailRepository;
    @Autowired
    private S3Uploader s3Uploader;

    private static final String LIBERTY = "Liberty 52_Frame";
    private static final String OPTION_1 = "이젤 거치형";
    private static final String OPTION_2 = "1mm 두께 승화전사 인쇄용 알루미늄시트";
    private static final String OPTION_3 = "유광실버";
    private static final int QUANTITY = 2;
    private static final int DELIVERY_PRICE = 150_000;

    String authId = UUID.randomUUID().toString();
    MockMultipartFile imageFile = new MockMultipartFile("image", "test.png", "image/jpeg", new FileInputStream("src/test/resources/static/test.jpg"));
    private String orderId;

    OrdersEntityTest() throws IOException {}

    @Test
    void test_getTotalAmount() {
        MonoItemOrderRequestDto requestDto = MonoItemOrderRequestDto.createForTest(
                LIBERTY,
                List.of(OPTION_1, OPTION_2, OPTION_3),
                QUANTITY,
                DELIVERY_PRICE,
                createDestinationDto());

        MonoItemOrderResponseDto save = monoItemOrderService.save(authId, imageFile, requestDto);
        orderId = save.getId();

        Orders orders = ordersRepository.findById(orderId).get();

        long expected = getExpectedPrice();
        orders.calcTotalAmountAndSet();
        long actual = orders.getAmount();
        Assertions.assertNotNull(orders);
        Assertions.assertEquals(expected, actual);
    }

    private Long getExpectedPrice() {
        long expected = 0;
        expected += productRepository.findByName(LIBERTY).get().getPrice();
        expected += optionDetailRepository.findByName(OPTION_1).get().getPrice();
        expected += optionDetailRepository.findByName(OPTION_2).get().getPrice();
        expected += optionDetailRepository.findByName(OPTION_3).get().getPrice();
        expected *= QUANTITY;
        expected += DELIVERY_PRICE;
        return expected;
    }

    private MonoItemOrderRequestDto.DestinationDto createDestinationDto() {
        return MonoItemOrderRequestDto.DestinationDto.create("receiverName", "receiverEmail", "receiverPhoneNumber", "address1", "address2", "zipCode");
    }

    @AfterEach
    public void deleteS3Image() {
        Orders orders = ordersRepository.findById(orderId).get();
        orders.getCustomProducts().forEach(customProduct -> {
            String imageUrl = customProduct.getUserCustomPictureUrl();
            s3Uploader.delete(imageUrl);
        });
    }


}