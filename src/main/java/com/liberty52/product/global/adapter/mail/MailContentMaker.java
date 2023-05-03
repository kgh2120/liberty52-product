package com.liberty52.product.global.adapter.mail;

import com.liberty52.product.global.adapter.mail.content.RequestDepositMail;
import com.liberty52.product.global.contants.ProductConstants;
import com.liberty52.product.global.contants.RepresentImageUrl;
import com.liberty52.product.service.entity.CustomProduct;
import com.liberty52.product.service.entity.Orders;
import com.liberty52.product.service.entity.payment.VBankPayment;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

public class MailContentMaker {

    private static final DateTimeFormatter DATE_FORMAT;
    static {
        DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }
    private static final DecimalFormat PRICE_FORMAT;
    static {
        PRICE_FORMAT = new DecimalFormat("###,###");
    }

    public static String makeOrderRequestDepositContent(String authName, Orders order) {
        String vBankInfo = ((VBankPayment.VBankPaymentInfo)(order.getPayment().getInfoAsDto())).getVbankInfo();
        String customerName = authName;
        String orderNum = order.getOrderNum();
        String orderedDate = order.getOrderDate().format(DATE_FORMAT);
        String receiverName = order.getOrderDestination().getReceiverName();
        String receiverPhone = order.getOrderDestination().getReceiverPhoneNumber();
        String address1 = "(" + order.getOrderDestination().getZipCode() + ")" + " " + order.getOrderDestination().getAddress1();
        String address2 = order.getOrderDestination().getAddress2();

        StringBuilder productListBuilder = new StringBuilder();
        for (CustomProduct product : order.getCustomProducts()) {
            String productInfo = String.format(
                    RequestDepositMail.ORDERED_PRODUCT_INFO,
                    RepresentImageUrl.LIBERTY52_FRAME_REPRESENTATIVE_URL,
                    product.getProduct().getName(),
                    product.getOptionsMap().get(ProductConstants.PROD_OPT_1),
                    product.getOptionsMap().get(ProductConstants.PROD_OPT_2),
                    product.getOptionsMap().get(ProductConstants.PROD_OPT_3),
                    product.getQuantity()
            );
            productListBuilder.append(productInfo);
        }
        String productList = productListBuilder.toString();

        String orderAmount = PRICE_FORMAT.format(order.getAmount() - order.getDeliveryPrice());
        Integer orderQuantity = order.getTotalQuantity();
        String deliveryPrice = PRICE_FORMAT.format(order.getDeliveryPrice());
        String finalAmount = PRICE_FORMAT.format(order.getAmount());

        return String.format(
                RequestDepositMail.ORDER_REQUEST_DEPOSIT_VBANK,
                vBankInfo,
                customerName, orderNum, orderedDate,
                receiverName, receiverPhone, address1, address2,
                productList,
                orderAmount, orderQuantity, deliveryPrice, finalAmount
        );
    }
}
