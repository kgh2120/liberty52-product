package com.liberty52.product.service.applicationservice;

import com.liberty52.product.service.controller.dto.CartModifyRequestDto;
import com.liberty52.product.service.entity.Cart;
import com.liberty52.product.service.entity.CustomProduct;
import com.liberty52.product.service.entity.CustomProductOption;
import com.liberty52.product.service.entity.Product;
import com.liberty52.product.service.repository.CartItemRepository;
import com.liberty52.product.service.repository.CartRepository;
import com.liberty52.product.service.repository.CustomProductOptionRepository;
import com.liberty52.product.service.repository.OptionDetailRepository;
import com.liberty52.product.service.repository.ProductOptionRepository;
import com.liberty52.product.service.repository.ProductRepository;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CartItemModifyServiceTest {

  @Autowired
  MonoItemOrderService monoItemOrderService;
  @Autowired
  CartItemRepository customProductRepository;
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
  CartItemModifyService cartItemModifyService;


  CartItemModifyServiceTest() throws IOException {
  }

  String productName = "Liberty 52_Frame";
  String detailName = "이젤 거치형";
  String authId = UUID.randomUUID().toString();

  String customProductId1;
  String customProductId2;
  String customProductId3;
  MockMultipartFile imageFile = new MockMultipartFile("image", "test.png", "image/jpeg",
      new FileInputStream("src/test/resources/static/test.jpg"));

  List<MultipartFile> imageFiles;
  @BeforeEach
  void beforeEach() {
    Cart cart = cartRepository.save(Cart.create(authId));
    CustomProduct customProduct1 = CustomProduct.create("example-url", 3, authId);
    CustomProduct customProduct2 = CustomProduct.create("example-url", 4, authId);
    CustomProduct customProduct3 = CustomProduct.create("example-url", 5, authId);
    customProduct1.associateWithCart(cart);
    customProduct2.associateWithCart(cart);
    customProduct3.associateWithCart(cart);

    Product product = productRepository.findByName(productName).get();
    customProduct1.associateWithProduct(product);
    customProduct2.associateWithProduct(product);
    customProduct3.associateWithProduct(product);

    customProductRepository.save(customProduct1);
    customProductRepository.save(customProduct2);
    customProductRepository.save(customProduct3);

    CustomProductOption customProductOption1 = CustomProductOption.create();
    customProductOption1.associate(optionDetailRepository.findByName("이젤 거치형").get());
    customProductOption1.associate(optionDetailRepository.findByName("무광백색").get());
    customProductOption1.associate(customProduct1);
    customProductOptionRepository.save(customProductOption1);

    CustomProductOption customProductOption2 = CustomProductOption.create();
    customProductOption2.associate(optionDetailRepository.findByName("벽걸이형").get());
    customProductOption2.associate(optionDetailRepository.findByName("유광백색").get());
    customProductOption2.associate(customProduct2);
    customProductOptionRepository.save(customProductOption2);

    CustomProductOption customProductOption3 = CustomProductOption.create();
    customProductOption3.associate(optionDetailRepository.findByName(detailName).get());
    customProductOption3.associate(optionDetailRepository.findByName("유광실버").get());
    customProductOption3.associate(customProduct3);
    customProductOptionRepository.save(customProductOption3);

    customProductId1 = customProduct1.getId();
    customProductId2 = customProduct2.getId();
    customProductId3 = customProduct3.getId();

    imageFiles = new ArrayList<>();
  }


  @Test
  void modifyCartItemList() {
    List<String> options1 = new ArrayList<>(List.of("벽걸이형","유광실버"));
    List<String> options2 = new ArrayList<>(List.of("이젤 거치형","무광실버"));
    List<String> options3 = new ArrayList<>(List.of("벽걸이형","무광백색"));
    CartModifyRequestDto cartModifyRequestDto1 = CartModifyRequestDto.create(options1, 1,customProductId1);
    CartModifyRequestDto cartModifyRequestDto2 = CartModifyRequestDto.create(options2, 2,customProductId2);
    CartModifyRequestDto cartModifyRequestDto3 = CartModifyRequestDto.create(options3, 3,customProductId3);

    List<CartModifyRequestDto> cartModifyRequestDtoList = new ArrayList<>();
    cartModifyRequestDtoList.add(cartModifyRequestDto1);
    cartModifyRequestDtoList.add(cartModifyRequestDto2);
    cartModifyRequestDtoList.add(cartModifyRequestDto3);

    imageFiles.add(imageFile);
    imageFiles.add(null);
    imageFiles.add(imageFile);

    cartItemModifyService.modifyCartItemList(authId,cartModifyRequestDtoList,imageFiles);

    cartModifyRequestDtoList.forEach(cmrd -> {
      CustomProduct customProduct = customProductRepository.findById(cmrd.getCustomProductId()).get();

      Assertions.assertEquals(cmrd.getQuantity(),customProduct.getQuantity());
      Assertions.assertEquals(cmrd.getOptions().size(),customProduct.getOptions().size());

      Collections.sort(cmrd.getOptions());
      List<String> actualList = customProduct.getOptions().stream()
          .map(cpo -> cpo.getOptionDetail().getName())
          .sorted()
          .toList();
      for (int i = 0; i < cmrd.getOptions().size(); i++) {
        Assertions.assertEquals(cmrd.getOptions().get(i), actualList.get(i));
      }
    });
  }

}
