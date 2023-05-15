package com.liberty52.product.global.adapter.mail;

import com.liberty52.product.global.adapter.mail.content.OrderedCompletedMail;
import com.liberty52.product.global.adapter.mail.content.OrderedProductInfoSection;
import com.liberty52.product.global.adapter.mail.content.RequestDepositMail;
import com.liberty52.product.global.contants.ProductConstants;
import com.liberty52.product.global.contants.RepresentImageUrl;
import com.liberty52.product.service.entity.CustomProduct;
import com.liberty52.product.service.entity.Orders;
import com.liberty52.product.service.entity.payment.CardPayment;
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

        String productList = makeOrderedProductInfoSection(order);

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

    public static String makeCardOrderedCompletedContent(String authName, Orders order) {
        // 기본정보
        String customerName = authName;
        String orderNum = order.getOrderNum();
        String orderedDate = order.getOrderDate().format(DATE_FORMAT);

        // 배송정보
        String receiverName = order.getOrderDestination().getReceiverName();
        String receiverPhone = order.getOrderDestination().getReceiverPhoneNumber();
        String address1 = "(" + order.getOrderDestination().getZipCode() + ")" + " " + order.getOrderDestination().getAddress1();
        String address2 = order.getOrderDestination().getAddress2();

        // 결제정보
        CardPayment.CardPaymentInfo paymentInfo = ((CardPayment) (order.getPayment())).getInfoAsDto();
        String cardName = paymentInfo.getCardName();
        String cardNumber = paymentInfo.getCardNumber();
        String cardQuota = paymentInfo.getCardQuota() > 0 ? paymentInfo.getCardQuota()+"개월" : "일시불";
        String paidAt = paymentInfo.getPaidAt().format(DATE_FORMAT);

        // 주문상품 정보
        String productInfo = makeOrderedProductInfoSection(order);
        String orderAmount = PRICE_FORMAT.format(order.getAmount() - order.getDeliveryPrice());
        Integer orderQuantity = order.getTotalQuantity();
        String deliveryPrice = PRICE_FORMAT.format(order.getDeliveryPrice());
        String finalAmount = PRICE_FORMAT.format(order.getAmount());

        return String.format(OrderedCompletedMail.CardOrdered.CONFIRM_CARD_ORDER_COMPLETED,
                customerName, orderNum, orderedDate,
                receiverName, receiverPhone, address1, address2,
                cardName, cardNumber, cardQuota, paidAt,
                productInfo,
                orderAmount, orderQuantity, deliveryPrice, finalAmount
        );
    }

    public static String makeVbankOrderedCompletedContent(String authName, Orders order) {
        // 기본정보
        String customerName = authName;
        String orderNum = order.getOrderNum();
        String orderedDate = order.getOrderDate().format(DATE_FORMAT);

        // 배송정보
        String receiverName = order.getOrderDestination().getReceiverName();
        String receiverPhone = order.getOrderDestination().getReceiverPhoneNumber();
        String address1 = "(" + order.getOrderDestination().getZipCode() + ")" + " " + order.getOrderDestination().getAddress1();
        String address2 = order.getOrderDestination().getAddress2();

        // 결제정보
        VBankPayment.VBankPaymentInfo paymentInfo = ((VBankPayment) order.getPayment()).getInfoAsDto();
        String vbankAcc = paymentInfo.getVbankInfo();
        String depositorBank = paymentInfo.getDepositorBank();
        String depositorName = paymentInfo.getDepositorName();
        String depositorAcc = paymentInfo.getDepositorAccount();
        String isApplyCashReceipt = paymentInfo.getIsApplyCashReceipt() ? "신청" : "미신청";
        String paidAt = paymentInfo.getPaidAt().format(DATE_FORMAT);

        // 주문 상품 정보
        String productInfo = makeOrderedProductInfoSection(order);
        String orderAmount = PRICE_FORMAT.format(order.getAmount() - order.getDeliveryPrice());
        Integer orderQuantity = order.getTotalQuantity();
        String deliveryPrice = PRICE_FORMAT.format(order.getDeliveryPrice());
        String finalAmount = PRICE_FORMAT.format(order.getAmount());

        return String.format(OrderedCompletedMail.VBankOrdered.CONFIRM_VBANK_ORDER_COMPLETED,
                customerName, orderNum, orderedDate,
                receiverName, receiverPhone, address1, address2,
                vbankAcc, depositorBank, depositorName, depositorAcc, isApplyCashReceipt, paidAt,
                productInfo,
                orderAmount, orderQuantity, deliveryPrice, finalAmount
        );
    }


    private static String makeOrderedProductInfoSection(Orders order) {
        StringBuilder sb = new StringBuilder();
        for (CustomProduct product : order.getCustomProducts()) {
            String productInfo = String.format(
                    OrderedProductInfoSection.ORDERED_PRODUCT_INFO,
                    RepresentImageUrl.LIBERTY52_FRAME_REPRESENTATIVE_URL,
                    product.getProduct().getName(),
                    product.getOptionsMap().get(ProductConstants.PROD_OPT_1),
                    product.getOptionsMap().get(ProductConstants.PROD_OPT_2),
                    product.getOptionsMap().get(ProductConstants.PROD_OPT_3),
                    product.getQuantity()
            );
            sb.append(productInfo);
        }
        return sb.toString();
    }

}
