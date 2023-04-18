package com.liberty52.product.service.utils;

import static com.liberty52.product.service.utils.MockConstants.MOCK_ADDRESS;
import static com.liberty52.product.service.utils.MockConstants.MOCK_AUTH_ID;
import static com.liberty52.product.service.utils.MockConstants.MOCK_PRICE;
import static com.liberty52.product.service.utils.MockConstants.MOCK_PRODUCT_NAME;
import static com.liberty52.product.service.utils.MockConstants.MOCK_PRODUCT_REPRESENT_URL;
import static com.liberty52.product.service.utils.MockConstants.MOCK_QUANTITY;
import static com.liberty52.product.service.utils.MockConstants.MOCK_RECEIVER_EMAIL;
import static com.liberty52.product.service.utils.MockConstants.MOCK_RECEIVER_NAME;
import static com.liberty52.product.service.utils.MockConstants.MOCK_RECEIVER_PHONE_NUMBER;
import static com.liberty52.product.service.utils.MockFactory.createProduct;

import com.liberty52.product.service.entity.CustomProduct;
import com.liberty52.product.service.entity.CustomProductOption;
import com.liberty52.product.service.entity.OptionDetail;
import com.liberty52.product.service.entity.OrderDestination;
import com.liberty52.product.service.entity.Orders;
import com.liberty52.product.service.entity.Product;
import com.liberty52.product.service.entity.ProductOption;
import com.liberty52.product.service.entity.ProductState;
import jakarta.persistence.EntityManager;
import java.util.List;

public class TestInitiator {

    public static String initDataForTestingOrder(EntityManager em) {
        Product product = createProduct(MOCK_PRODUCT_NAME, ProductState.ON_SAIL, MOCK_PRICE);
        em.persist(product);


        ProductOption option1 = ProductOption.create("거치 방식", true);
        option1.associate(product);
        em.persist(option1);

        OptionDetail detailEasel = OptionDetail.create("이젤 거치형", 100000);
        detailEasel.associate(option1);
        em.persist(detailEasel);

        OptionDetail detailWall = OptionDetail.create("벽걸이형", 200000);
        detailWall.associate(option1);
        em.persist(detailWall);

        ProductOption option2 = ProductOption.create("기본소재", true);
        option2.associate(product);
        em.persist(option2);

        OptionDetail material = OptionDetail.create("1mm 두께 승화전사 인쇄용 알루미늄시트", 0);
        material.associate(option2);
        em.persist(material);

        ProductOption option3 = ProductOption.create("기본소재 옵션", true);
        option3.associate(product);
        em.persist(option3);

        OptionDetail materialOption1 = OptionDetail.create("유광실버", 600000);
        materialOption1.associate(option3);
        em.persist(materialOption1);

        OptionDetail materialOption2 = OptionDetail.create("무광실버", 400000);
        materialOption2.associate(option3);
        em.persist(materialOption2);

        OptionDetail materialOption3 = OptionDetail.create("유광백색", 300000);
        materialOption3.associate(option3);
        em.persist(materialOption3);

        OptionDetail materialOption4 = OptionDetail.create("무광백색", 500000);
        materialOption4.associate(option3);
        em.persist(materialOption4);


        CustomProduct customProduct = CustomProduct.create(MOCK_PRODUCT_REPRESENT_URL, MOCK_QUANTITY, MOCK_AUTH_ID);
        customProduct.associateWithProduct(product);
        em.persist(customProduct);

        CustomProductOption customProductOption1 = CustomProductOption.create();
        customProductOption1.associate(materialOption1); // 600000
        customProductOption1.associate(customProduct);
        em.persist(customProductOption1);

        CustomProductOption customProductOption2 = CustomProductOption.create();
        customProductOption2.associate(materialOption2); // 400000
        customProductOption2.associate(customProduct);
        em.persist(customProductOption2);

        CustomProductOption customProductOption3 = CustomProductOption.create();
        customProductOption3.associate(materialOption3); // 300000
        customProductOption3.associate(customProduct);
        em.persist(customProductOption3);




        OrderDestination destination = OrderDestination.create(MOCK_RECEIVER_NAME,
                MOCK_RECEIVER_EMAIL
                , MOCK_RECEIVER_PHONE_NUMBER, MOCK_ADDRESS, MOCK_ADDRESS, MOCK_ADDRESS);
        Orders order = Orders.create(MOCK_AUTH_ID,0,destination);
        order.associateWithCustomProduct(List.of(customProduct));
        em.persist(order);
        em.flush();
        return order.getId();
    }


}

