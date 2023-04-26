package com.liberty52.product.service.applicationservice;

import com.liberty52.product.MockS3Test;
import com.liberty52.product.service.controller.dto.CartModifyRequestDto;
import com.liberty52.product.service.entity.Cart;
import com.liberty52.product.service.entity.CustomProduct;
import com.liberty52.product.service.entity.CustomProductOption;
import com.liberty52.product.service.entity.Product;
import com.liberty52.product.service.repository.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CartItemModifyServiceTest extends MockS3Test {

  @Autowired
  CustomProductRepository customProductRepository;
  @Autowired
  ProductRepository productRepository;
  @Autowired
  ProductOptionRepository productOptionRepository;
  @Autowired
  CustomProductOptionRepository customProductOptionRepository;

  @Autowired
  OptionDetailRepository optionDetailRepository;
  @Autowired
  CartRepository cartRepository;
  @Autowired
  ApplicationEventPublisher eventPublisher;

  @Autowired
  CartItemModifyService cartItemModifyService;


  CartItemModifyServiceTest() throws IOException {
  }

  String productName = "Liberty 52_Frame";
  String detailName = "이젤 거치형";
  String authId = UUID.randomUUID().toString();

  String customProductId;
  MockMultipartFile imageFile = new MockMultipartFile("image", "test.png", "image/jpeg",
      new FileInputStream("src/test/resources/static/test.jpg"));

  @BeforeEach
  void beforeEach() {
    Cart cart = cartRepository.save(Cart.create(authId));
    CustomProduct customProduct = CustomProduct.create("example-url", 3, authId);
    customProduct.associateWithCart(cart);

    Product product = productRepository.findByName(productName).get();
    customProduct.associateWithProduct(product);

    customProductRepository.save(customProduct);

    CustomProductOption customProductOption = CustomProductOption.create();
    customProductOption.associate(optionDetailRepository.findByName(detailName).get());
    customProductOption.associate(optionDetailRepository.findByName("무광백색").get());
    customProductOption.associate(customProduct);
    customProductOptionRepository.save(customProductOption);

    customProductId = customProduct.getId();
  }


  @Test
  void modify() {
    List<String> options = new ArrayList<>(List.of("유광백색","벽걸이형")); //불변 객체를 ArrayList로 감싸 변할 수 있게
    int quantity = 5;
    CartModifyRequestDto cartModifyRequestDto = CartModifyRequestDto.create(options, quantity);

    cartItemModifyService.modifyUserCartItem(authId,cartModifyRequestDto,imageFile,customProductId);

    CustomProduct customProduct = customProductRepository.findById(customProductId).get();
    Assertions.assertEquals(quantity,customProduct.getQuantity());

    Assertions.assertEquals(options.size(),customProduct.getOptions().size());

    Collections.sort(options);
    List<String> actualList = customProduct.getOptions().stream()
        .map(cpo -> cpo.getOptionDetail().getName())
        .sorted()
        .toList();
    for (int i = 0; i < options.size(); i++) {
      Assertions.assertEquals(options.get(i), actualList.get(i));
    }
  }

}
