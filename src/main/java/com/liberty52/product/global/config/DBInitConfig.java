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

            ProductOption option1 = ProductOption.create("거치 방식", true);
            option1.associate(product);
            option1 = productOptionRepository.save(option1);

            OptionDetail detailEasel = OptionDetail.create("이젤 거치형", 100000);
            detailEasel.associate(option1);
            detailEasel = optionDetailRepository.save(detailEasel);

            OptionDetail detailWall = OptionDetail.create("벽걸이형", 200000);
            detailWall.associate(option1);
            detailWall = optionDetailRepository.save(detailWall);

            ProductOption option2 = ProductOption.create("기본소재", true);
            option2.associate(product);
            option2 = productOptionRepository.save(option2);

            OptionDetail material = OptionDetail.create("1mm 두께 승화전사 인쇄용 알루미늄시트", 0);
            material.associate(option2);
            material = optionDetailRepository.save(material);

            ProductOption option3 = ProductOption.create("기본소재 옵션", true);
            option3.associate(product);
            option3 = productOptionRepository.save(option3);

            OptionDetail materialOption1 = OptionDetail.create("유광실버", 600000);
            materialOption1.associate(option3);
            materialOption1 = optionDetailRepository.save(materialOption1);

            OptionDetail materialOption2 = OptionDetail.create("무광실버", 400000);
            materialOption2.associate(option3);
            materialOption2 = optionDetailRepository.save(materialOption2);

            OptionDetail materialOption3 = OptionDetail.create("유광백색", 300000);
            materialOption3.associate(option3);
            materialOption3 = optionDetailRepository.save(materialOption3);

            OptionDetail materialOption4 = OptionDetail.create("무광백색", 500000);
            materialOption4.associate(option3);
            materialOption4 = optionDetailRepository.save(materialOption4);

        }
    }
}
