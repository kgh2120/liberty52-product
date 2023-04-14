package com.liberty52.product.service.applicationservice;

import com.liberty52.product.global.exception.external.InvalidQuantityException;
import com.liberty52.product.global.exception.external.ResourceNotFoundException;
import com.liberty52.product.service.controller.dto.MonoItemOrderRequestDto;
import com.liberty52.product.service.controller.dto.MonoItemOrderResponseDto;
import com.liberty52.product.service.entity.*;
import com.liberty52.product.service.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@AutoConfigureMockMvc
class MonoItemOrderServiceImplTest {
    @Autowired
    MonoItemOrderService monoItemOrderService;
    @Autowired
    CartItemRepository customProductRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductOptionRepository productOptionRepository;
    @Autowired
    private OrdersRepository ordersRepository;

    MonoItemOrderServiceImplTest() throws IOException {
    }

    String productName = "Liberty 52_Frame";
    String detailName = "이젤 거치형";
    String authId = UUID.randomUUID().toString();
    MockMultipartFile imageFile = new MockMultipartFile("image", "test.png", "image/jpeg", new FileInputStream("src/test/resources/static/test.jpg"));

    @Test
    void save() throws IOException {
        int quantity = 2;
        int deliveryPrice = 120000;
        MonoItemOrderResponseDto dto = save(productName, detailName, quantity, deliveryPrice);

        Orders orders = ordersRepository.findById(dto.getId()).get();
        Assertions.assertEquals(OrderStatus.ORDERED, orders.getOrderStatus());
        Assertions.assertEquals(authId, orders.getAuthId());
    }

    @Test
    void wrongProductName() {
        int quantity = 2;
        int deliveryPrice = 120000;
        Assertions.assertThrows(ResourceNotFoundException.class, () -> save("wrong name", detailName, quantity, deliveryPrice));
    }

    @Test
    void wrongOptionName() {
        int quantity = 2;
        int deliveryPrice = 120000;
        Assertions.assertThrows(ResourceNotFoundException.class, () -> save(productName, "wrong name", quantity, deliveryPrice));
    }

    @Test
    void invalidQuantity() {
        int quantity = 0;
        int deliveryPrice = 120000;
        Assertions.assertThrows(InvalidQuantityException.class, () -> save(productName, detailName, quantity, deliveryPrice));
    }

    MonoItemOrderResponseDto save(String productName, String detailName, int quantity, int deliveryPrice) {
        return monoItemOrderService.save(authId, imageFile, MonoItemOrderRequestDto.createForTest(productName, List.of(detailName), quantity, deliveryPrice, createDestinationDto()));
    }

    private MonoItemOrderRequestDto.DestinationDto createDestinationDto() {
        return MonoItemOrderRequestDto.DestinationDto.create("receiverName", "receiverEmail", "receiverPhoneNumber", "address1", "address2", "zipCode");
    }
}
