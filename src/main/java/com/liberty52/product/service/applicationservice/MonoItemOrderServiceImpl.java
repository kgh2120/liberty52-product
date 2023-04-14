package com.liberty52.product.service.applicationservice;

import com.liberty52.product.global.adapter.S3Uploader;
import com.liberty52.product.global.exception.external.ResourceNotFoundException;
import com.liberty52.product.service.controller.dto.MonoItemOrderRequestDto;
import com.liberty52.product.service.controller.dto.MonoItemOrderResponseDto;
import com.liberty52.product.service.entity.*;
import com.liberty52.product.service.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MonoItemOrderServiceImpl implements MonoItemOrderService {

    private static final String RESOURCE_NAME_PRODUCT = "Product";
    private static final String PARAM_NAME_PRODUCT_NAME = "name";
    private static final String RESOURCE_NAME_OPTION_DETAIL = "OptionDetail";
    private static final String PARAM_NAME_OPTION_DETAIL_NAME = "name";
    private final ProductRepository productRepository;
    private final S3Uploader s3Uploader;
    private final CustomProductRepository customProductRepository;
    private final OrdersRepository ordersRepository;
    private final OptionDetailRepository optionDetailRepository;
    private final CustomProductOptionRepository customProductOptionRepository;

    @Override
    public MonoItemOrderResponseDto save(String authId, MultipartFile imageFile, MonoItemOrderRequestDto dto) {
        // Valid and get resources
        Product product = productRepository.findByName(dto.getProductName())
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME_PRODUCT, PARAM_NAME_PRODUCT_NAME, dto.getProductName()));
        List<OptionDetail> details = new ArrayList<>();
        for (String optionName : dto.getOptions()) {
            OptionDetail optionDetail = optionDetailRepository.findByName(optionName)
                    .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME_OPTION_DETAIL, PARAM_NAME_OPTION_DETAIL_NAME, optionName));
            details.add(optionDetail);
        }

        OrderDestination orderDestination = OrderDestination.create("", "", "", "", "", "");

        // Save Order
        Orders order = Orders.create(authId, dto.getDeliveryPrice(), orderDestination); // OrderDestination will be saved by cascading
        order = ordersRepository.save(order);

        // Upload Image
        String imgUrl = s3Uploader.upload(imageFile);

        // Save CustomProduct
        CustomProduct customProduct = CustomProduct.create(imgUrl, dto.getQuantity(), authId);
        customProduct.associateWithProduct(product);
        customProduct.associateWithOrder(order);
        customProduct = customProductRepository.save(customProduct);

        // Save CustomProductOption
        for (OptionDetail detail : details) {
            CustomProductOption customProductOption = CustomProductOption.create();
            customProductOption.associate(customProduct);
            customProductOption.associate(detail);
            customProductOptionRepository.save(customProductOption);
        }

        return MonoItemOrderResponseDto.create(order.getId(), order.getOrderDate(), order.getOrderStatus());
    }
}
