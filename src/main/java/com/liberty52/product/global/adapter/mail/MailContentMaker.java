package com.liberty52.product.global.adapter.mail;

import com.liberty52.product.global.adapter.mail.content.OrderCanceledMail;
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
import java.util.Map;

public class MailContentMaker {

    private static final DateTimeFormatter DATE_FORMAT;
    static {
        DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }
    private static final DecimalFormat PRICE_FORMAT;
    static {
        PRICE_FORMAT = new DecimalFormat("###,###");
    }

    public static String makeOrderRequestDepositContent(Orders order) {
        String vBankInfo = ((VBankPayment.VBankPaymentInfo)(order.getPayment().getInfoAsDto())).getVbankInfo();
        String customerName = order.getOrderDestination().getReceiverName();
        String orderNum = order.getOrderNum();
        String orderedDate = order.getOrderDate().format(DATE_FORMAT);
        String receiverName = order.getOrderDestination().getReceiverName();
        String receiverPhone = order.getOrderDestination().getReceiverPhoneNumber();
        String address1 = "(" + order.getOrderDestination().getZipCode() + ")" + " " + order.getOrderDestination().getAddress1();
        String address2 = order.getOrderDestination().getAddress2();

        String productList = orderedProductInfoSection(order);

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

    public static String makeCardOrderedCompletedContent(Orders order) {
        // 기본정보
        String customerName = order.getOrderDestination().getReceiverName();
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
        String productInfo = orderedProductInfoSection(order);
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

    public static String makeVbankOrderedCompletedContent(Orders order) {
        // 기본정보
        String customerName = order.getOrderDestination().getReceiverName();
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
        String productInfo = orderedProductInfoSection(order);
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

    private static String orderedProductInfoSection(Orders order) {
        StringBuilder sb = new StringBuilder();
        for (CustomProduct customProduct : order.getCustomProducts()) {
            Map<String, String> optionsMap = customProduct.getOptionsMap();
            String productInfo = String.format(
                    OrderedProductInfoSection.ORDERED_PRODUCT_INFO,
                    RepresentImageUrl.LIBERTY52_FRAME_REPRESENTATIVE_URL,
                    customProduct.getProduct().getName(),
                    optionsMap.get(ProductConstants.PROD_OPT_1),
                    optionsMap.get(ProductConstants.PROD_OPT_2),
                    optionsMap.get(ProductConstants.PROD_OPT_3),
                    customProduct.getQuantity()
            );
            sb.append(productInfo);
        }
        return sb.toString();
    }

    public static String makeOrderCanceledContent(String contentTitle, Orders order) {
        String customerName = order.getOrderDestination().getReceiverName();
        String orderNum = order.getOrderNum();
        String orderDate = order.getOrderDate().format(DATE_FORMAT);
        String cancelReason = order.getCanceledOrders().getReason();

        String orderAmount = PRICE_FORMAT.format(order.getAmount() - order.getDeliveryPrice());
        String deliveryPrice = PRICE_FORMAT.format(order.getDeliveryPrice());
        String paymentType = order.getPayment().getType().getKorName();
        String finalAmount = PRICE_FORMAT.format(order.getAmount());

        String receiverName = order.getOrderDestination().getReceiverName();
        String receiverPhone = order.getOrderDestination().getReceiverPhoneNumber();
        String address1 = "(" + order.getOrderDestination().getZipCode() + ")" + " " + order.getOrderDestination().getAddress1();
        String address2 = order.getOrderDestination().getAddress2();

        String productInfo = orderedProductInfoSection(order);

        Integer orderQuantity = order.getTotalQuantity();

        String cancelFee = PRICE_FORMAT.format(order.getCanceledOrders().getFee());
        String refundAmount = PRICE_FORMAT.format(order.getAmount() - order.getCanceledOrders().getFee());

        return String.format(OrderCanceledMail.ORDER_CANCELED(contentTitle),
                customerName, orderNum, orderDate, cancelReason,
                orderAmount, deliveryPrice, paymentType, finalAmount,
                receiverName, receiverPhone, address1, address2,
                productInfo,
                orderAmount, orderQuantity,
                cancelFee, refundAmount,
                deliveryPrice, orderAmount, deliveryPrice,
                cancelFee, refundAmount);
    }

}
