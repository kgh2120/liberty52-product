package com.liberty52.product.global.config;

import com.liberty52.product.service.applicationservice.MonoItemOrderService;
import com.liberty52.product.service.entity.OptionDetail;
import com.liberty52.product.service.entity.Product;
import com.liberty52.product.service.entity.ProductOption;
import com.liberty52.product.service.entity.ProductState;
import com.liberty52.product.service.repository.CartItemRepository;
import com.liberty52.product.service.repository.OptionDetailRepository;
import com.liberty52.product.service.repository.ProductOptionRepository;
import com.liberty52.product.service.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DBInitConfig {
    private final DBInitService initService;

    @PostConstruct
    public void init() {
        initService.init();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class DBInitService {
        private final MonoItemOrderService monoItemOrderService;
        private final CartItemRepository customProductRepository;
        private final ProductRepository productRepository;
        private final ProductOptionRepository productOptionRepository;
        private static final String LIBERTY = "Liberty 52_Frame";
        private final OptionDetailRepository optionDetailRepository;

        public void init() {
            Product product = productRepository.save(Product.create(LIBERTY, ProductState.ON_SAIL, 10000000L));

            ProductOption option = ProductOption.create("거치 방식", true);
            option.associate(product);
            option = productOptionRepository.save(option);

            OptionDetail detailEasel = OptionDetail.create("이젤 거치형", 100000);
            detailEasel.associate(option);
            detailEasel = optionDetailRepository.save(detailEasel);

            OptionDetail detailWall = OptionDetail.create("벽걸이형", 200000);
            detailWall.associate(option);
            detailWall = optionDetailRepository.save(detailWall);
        }
    }
}
