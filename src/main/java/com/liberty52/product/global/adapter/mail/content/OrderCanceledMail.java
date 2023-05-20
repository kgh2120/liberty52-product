package com.liberty52.product.global.adapter.mail.content;

public class OrderCanceledMail {

    public static String ORDER_CANCELED(String title) {
        return ORDER_CANCELED.replace("TITLE_TARGET", title);
    }

    /**
     * 주문 취소 처리 메일.
     * <br> 1: 고객명, string
     * <br> 2: 주문번호, string
     * <br> 3: 주문일자, string
     * <br> 4: 취소사유, string
     * <br> 5: 총주문금액, string (주문 금액 - 배송비)
     * <br> 6: 배송비, string
     * <br> 7: 결제수단, string
     * <br> 8: 최종 결제금액, string (주문금액 + 배송비)
     * <br> 9: 수령인, string
     * <br> 10: 연락처, string
     * <br> 11: 배송지, string (address1, address2)
     * <br> 12: 취소상품 리스트, string (html)
     * <br> 13: 주문금액. string (주문금액 - 배송비)
     * <br> 14: 수량, number
     * <br> 15: 취소비용, string
     * <br> 16: 환불금액, string (주문금액 - 취소비용)
     * <br> 17: 배송비, string
     * <br> 18: 총주문금액, string
     * <br> 19: 총배송비 합계. string
     * <br> 20: 총 취소비용, string
     * <br> 21: 최종 환불 금액, string
     */
    public static final String ORDER_CANCELED =
            "<div class=\"\">\n" +
                    "    <div class=\"aHl\"></div>\n" +
                    "    <div id=\":p1\" tabindex=\"-1\"></div>\n" +
                    "    <div id=\":oq\" class=\"ii gt\"\n" +
                    "        jslog=\"20277; u014N:xr6bB; 1:WyIjdGhyZWFkLWY6MTc2MzQxMDE3NTg5ODI1OTg5NiIsbnVsbCxudWxsLG51bGwsbnVsbCxudWxsLG51bGwsbnVsbCxudWxsLG51bGwsbnVsbCxudWxsLG51bGwsW11d; 4:WyIjbXNnLWY6MTc2MzQxMDE3NTg5ODI1OTg5NiIsbnVsbCxbXV0.\">\n" +
                    "        <div id=\":op\" class=\"a3s aiL \">\n" +
                    "            <table style=\"width:100%%;margin:0;background:#eee\">\n" +
                    "                <tbody>\n" +
                    "                    <tr>\n" +
                    "                        <td height=\"50\"></td>\n" +
                    "                    </tr>\n" +
                    "                    <tr>\n" +
                    "                        <td>\n" +
                    "                            <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%%\">\n" +
                    "                                <tbody>\n" +
                    "                                    <tr>\n" +
                    "                                        <td>\n" +
                    "                                            <table style=\"margin:0 auto;background:#fff;max-width:600px;width:100%%\"\n" +
                    "                                                cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                    "                                                <tbody>\n" +
                    "                                                    <tr>\n" +
                    "                                                        <td width=\"5%%\" style=\"border-bottom:1px solid #eee\"></td>\n" +
                    "                                                        <td style=\"border-bottom:1px solid #eee\">\n" +
                    "                                                            <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\"\n" +
                    "                                                                width=\"100%%\">\n" +
                    "                                                                <tbody>\n" +
                    "                                                                    <tr>\n" +
                    "                                                                        <th height=\"40\"></th>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                    <tr>\n" +
                    "                                                                        <td\n" +
                    "                                                                            style=\"text-align:center;color:#757575;font-size:18px\">\n" +
                    "                                                                            Liberty52</td>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                    <tr>\n" +
                    "                                                                        <th height=\"20\"></th>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                    <tr>\n" +
                    "                                                                        <td\n" +
                    "                                                                            style=\"text-align:center;font-size:26px;color:#757575\">\n" +
                    "                                                                            TITLE_TARGET\n" +
                    "                                                                        </td>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                    <tr>\n" +
                    "                                                                        <td height=\"20\"></td>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                </tbody>\n" +
                    "                                                            </table>\n" +
                    "                                                        </td>\n" +
                    "                                                        <td width=\"5%%\" style=\"border-bottom:1px solid #eee\"></td>\n" +
                    "                                                    </tr>\n" +
                    "                                                    <tr>\n" +
                    "                                                        <td height=\"40\" colspan=\"3\"></td>\n" +
                    "                                                    </tr>\n" +
                    "                                                    <tr>\n" +
                    "                                                        <td width=\"5%%\">&nbsp;&nbsp;</td>\n" +
                    "                                                        <td>\n" +
                    "                                                            <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\"\n" +
                    "                                                                width=\"100%%\">\n" +
                    "                                                                <tbody>\n" +
                    "                                                                    <tr>\n" +
                    "                                                                        <td\n" +
                    "                                                                            style=\"color:#777;font-size:14px;width:100px;padding-right:10px\">\n" +
                    "                                                                            고객명</td>\n" +
                    "                                                                        <td style=\"font-size:14px\">\n" +
                    "                                                                            %s\n" +
                    "                                                                        </td>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                    <tr height=\"10\">\n" +
                    "                                                                        <td colspan=\"2\"></td>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                    <tr>\n" +
                    "                                                                        <td\n" +
                    "                                                                            style=\"color:#777;font-size:14px;width:100px;padding-right:10px\">\n" +
                    "                                                                            주문번호</td>\n" +
                    "                                                                        <td style=\"font-size:14px\">\n" +
                    "                                                                            <a href=\"\"\n" +
                    "                                                                                style=\"color:#212121;text-decoration:underline\"\n" +
                    "                                                                                target=\"_blank\" data-saferedirecturl=\"\">\n" +
                    "                                                                                %s\n" +
                    "                                                                            </a>\n" +
                    "                                                                        </td>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                    <tr height=\"10\">\n" +
                    "                                                                        <td colspan=\"2\"></td>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                    <tr>\n" +
                    "                                                                        <td\n" +
                    "                                                                            style=\"color:#777;font-size:14px;width:100px;padding-right:10px\">\n" +
                    "                                                                            주문일자</td>\n" +
                    "                                                                        <td style=\"font-size:14px\">\n" +
                    "                                                                            %s\n" +
                    "                                                                        </td>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                    <tr height=\"10\">\n" +
                    "                                                                        <td colspan=\"2\"></td>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                    <tr>\n" +
                    "                                                                        <td\n" +
                    "                                                                            style=\"color:#777;font-size:14px;width:100px;padding-right:10px\">\n" +
                    "                                                                            취소사유</td>\n" +
                    "                                                                        <td style=\"font-size:14px\">\n" +
                    "                                                                            %s\n" +
                    "                                                                        </td>\n" +
                    "                                                                    </tr>" +
                    "                                                                </tbody>\n" +
                    "                                                            </table>\n" +
                    "                                                        </td>\n" +
                    "                                                        <td width=\"5%%\">&nbsp;&nbsp;</td>\n" +
                    "                                                    </tr>\n" +
                    "                                                    <tr>\n" +
                    "                                                        <td height=\"50\" colspan=\"3\"></td>\n" +
                    "                                                    </tr>\n" +
                    "                                                    <tr>\n" +
                    "                                                        <td width=\"5%%\"></td>\n" +
                    "                                                        <td>\n" +
                    "                                                            <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\"\n" +
                    "                                                                width=\"100%%\">\n" +
                    "                                                                <tbody>\n" +
                    "                                                                    <tr>\n" +
                    "                                                                        <th colspan=\"2\"\n" +
                    "                                                                            style=\"text-align:left;font-size:14px;border-bottom:2px solid #000;padding-bottom:10px\">\n" +
                    "                                                                            결제정보\n" +
                    "                                                                        </th>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                    <tr height=\"10\">\n" +
                    "                                                                        <td colspan=\"2\"></td>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                    <tr>\n" +
                    "                                                                        <td style=\"color:#777;font-size:14px\">\n" +
                    "                                                                            총 주문금액\n" +
                    "                                                                        </td>\n" +
                    "                                                                        <td style=\"font-size:14px;text-align:right\">\n" +
                    "                                                                            %s원\n" +
                    "                                                                        </td>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                    <tr height=\"10\">\n" +
                    "                                                                        <td colspan=\"2\"></td>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                    <tr>\n" +
                    "                                                                        <td style=\"color:#777;font-size:14px\">\n" +
                    "                                                                            배송비\n" +
                    "                                                                        </td>\n" +
                    "                                                                        <td style=\"font-size:14px;text-align:right\">\n" +
                    "                                                                            %s원\n" +
                    "                                                                        </td>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                    <tr height=\"10\">\n" +
                    "                                                                        <td colspan=\"2\"></td>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                    <tr>\n" +
                    "                                                                        <td style=\"color:#777;font-size:14px\">\n" +
                    "                                                                            결제수단\n" +
                    "                                                                        </td>\n" +
                    "                                                                        <td style=\"font-size:14px;text-align:right\">\n" +
                    "                                                                            %s\n" +
                    "                                                                        </td>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                    <tr height=\"10\">\n" +
                    "                                                                        <td colspan=\"2\"></td>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                    <tr>\n" +
                    "                                                                        <td\n" +
                    "                                                                            style=\"font-size:18px;padding:15px 0;border-top:1px solid rgba(0,0,0,0.1);border-bottom:1px solid rgba(0,0,0,0.2)\">\n" +
                    "                                                                            최종 결제금액</td>\n" +
                    "                                                                        <th\n" +
                    "                                                                            style=\"font-size:18px;text-align:right;padding:15px 0;border-top:1px solid rgba(0,0,0,0.1);border-bottom:1px solid rgba(0,0,0,0.2);color:#212121\">\n" +
                    "                                                                            %s원</th>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                    <tr height=\"50\">\n" +
                    "                                                                        <td colspan=\"2\"></td>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                </tbody>\n" +
                    "                                                            </table>\n" +
                    "                                                        </td>\n" +
                    "                                                    </tr>\n" +
                    "                                                    <tr>\n" +
                    "                                                        <td width=\"5%%\"></td>\n" +
                    "                                                        <td>\n" +
                    "                                                            <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\"\n" +
                    "                                                                width=\"100%%\">\n" +
                    "                                                                <tbody>\n" +
                    "                                                                    <tr>\n" +
                    "                                                                        <th colspan=\"2\"\n" +
                    "                                                                            style=\"text-align:left;font-size:14px;border-bottom:2px solid #000;padding-bottom:10px\">\n" +
                    "                                                                            배송정보</th>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                    <tr height=\"10\">\n" +
                    "                                                                        <td colspan=\"2\"></td>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                    <tr>\n" +
                    "                                                                        <td\n" +
                    "                                                                            style=\"color:#777;font-size:14px;width:100px;padding-right:10px\">\n" +
                    "                                                                            수령인</td>\n" +
                    "                                                                        <td style=\"font-size:14px\">%s</td>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                    <tr height=\"10\">\n" +
                    "                                                                        <td colspan=\"2\"></td>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                    <tr>\n" +
                    "                                                                        <td\n" +
                    "                                                                            style=\"color:#777;font-size:14px;width:100px;padding-right:10px\">\n" +
                    "                                                                            연락처</td>\n" +
                    "                                                                        <td style=\"font-size:14px\">%s\n" +
                    "                                                                        </td>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                    <tr height=\"10\">\n" +
                    "                                                                        <td colspan=\"2\"></td>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                    <tr>\n" +
                    "                                                                        <td\n" +
                    "                                                                            style=\"color:#777;font-size:14px;width:100px;padding-right:10px;vertical-align:top\">\n" +
                    "                                                                            배송지</td>\n" +
                    "                                                                        <td style=\"font-size:14px\">\n" +
                    "                                                                            <div>%s <br>%s</div>\n" +
                    "                                                                        </td>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                    <tr height=\"10\">\n" +
                    "                                                                        <td colspan=\"2\"></td>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                    <tr height=\"5\">\n" +
                    "                                                                        <td colspan=\"2\"\n" +
                    "                                                                            style=\"border-bottom:1px solid #eee\"></td>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                    <tr height=\"50\">\n" +
                    "                                                                        <td colspan=\"2\"></td>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                </tbody>\n" +
                    "                                                            </table>\n" +
                    "                                                        </td>\n" +
                    "                                                    </tr>\n" +
                    "\n" +
                    "                                                    <tr>\n" +
                    "                                                        <td width=\"5%%\">&nbsp;&nbsp;</td>\n" +
                    "                                                        <td>\n" +
                    "\n" +
                    "                                                            <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\"\n" +
                    "                                                                width=\"100%%\">\n" +
                    "                                                                <tbody>\n" +
                    "                                                                    <tr>\n" +
                    "                                                                        <th colspan=\"2\"\n" +
                    "                                                                            style=\"text-align:left;font-size:14px;border-bottom:2px solid #000;padding-bottom:10px\">\n" +
                    "                                                                            취소상품</th>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                    <tr>\n" +
                    "                                                                        <td colspan=\"2\">\n" +
                    "                                                                            <table cellpadding=\"0\" cellspacing=\"0\"\n" +
                    "                                                                                border=\"0\" width=\"100%%\">\n" +
                    "                                                                                <tbody>\n" +
                    "                                                                                    <tr height=\"10\">\n" +
                    "                                                                                        <td colspan=\"2\"></td>\n" +
                    "                                                                                    </tr>\n" +
                    "                                                                                    <tr>\n" +
                    "                                                                                        <!-- 주문 상품 리스트 Section -->\n" +
                    "                                                                                        %s\n" +
                    "                                                                                    </tr>\n" +
                    "                                                                                    <tr height=\"10\">\n" +
                    "                                                                                        <td colspan=\"2\"></td>\n" +
                    "                                                                                    </tr>\n" +
                    "                                                                                    <tr>\n" +
                    "                                                                                        <td\n" +
                    "                                                                                            style=\"color:#777;font-size:14px;border-top:1px solid rgba(0,0,0,0.1);padding-top:10px\">\n" +
                    "                                                                                            주문금액</td>\n" +
                    "                                                                                        <td\n" +
                    "                                                                                            style=\"font-size:14px;border-top:1px solid rgba(0,0,0,0.1);padding-top:10px;text-align:right\">\n" +
                    "                                                                                            %s원</td>\n" +
                    "                                                                                    </tr>\n" +
                    "                                                                                    <tr>\n" +
                    "                                                                                        <td height=\"10\" colspan=\"2\">\n" +
                    "                                                                                        </td>\n" +
                    "                                                                                    </tr>\n" +
                    "                                                                                    <tr>\n" +
                    "                                                                                        <td\n" +
                    "                                                                                            style=\"color:#777;font-size:14px\">\n" +
                    "                                                                                            수량</td>\n" +
                    "                                                                                        <td\n" +
                    "                                                                                            style=\"font-size:14px;text-align:right\">\n" +
                    "                                                                                            %d개</td>\n" +
                    "                                                                                    </tr>\n" +
                    "                                                                                    <tr height=\"10\">\n" +
                    "                                                                                        <td colspan=\"2\"></td>\n" +
                    "                                                                                    </tr>\n" +
                    "                                                                                    <tr>\n" +
                    "                                                                                        <td\n" +
                    "                                                                                            style=\"color:#777;font-size:14px\">\n" +
                    "                                                                                            취소비용</td>\n" +
                    "                                                                                        <td\n" +
                    "                                                                                            style=\"font-size:14px;text-align:right\">\n" +
                    "                                                                                            -%s원</td>\n" +
                    "                                                                                    </tr>\n" +
                    "                                                                                    <tr height=\"10\">\n" +
                    "                                                                                        <td colspan=\"2\"></td>\n" +
                    "                                                                                    </tr>\n" +
                    "                                                                                    <tr>\n" +
                    "                                                                                        <td\n" +
                    "                                                                                            style=\"color:#777;font-size:14px;border-bottom:1px solid rgba(0,0,0,0.1);padding-bottom:10px\">\n" +
                    "                                                                                            환불금액</td>\n" +
                    "                                                                                        <td\n" +
                    "                                                                                            style=\"font-size:14px;border-bottom:1px solid rgba(0,0,0,0.1);padding-bottom:10px;text-align:right\">\n" +
                    "                                                                                            <strong>%s원</strong>\n" +
                    "                                                                                        </td>\n" +
                    "                                                                                    </tr>\n" +
                    "                                                                                    <tr height=\"10\">\n" +
                    "                                                                                        <td colspan=\"2\"></td>\n" +
                    "                                                                                    </tr>\n" +
                    "                                                                                </tbody>\n" +
                    "                                                                            </table>\n" +
                    "                                                                        </td>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                    <tr>\n" +
                    "                                                                        <td\n" +
                    "                                                                            style=\"color:#777;font-size:14px;border-bottom:1px solid rgba(0,0,0,0.1);padding-bottom:10px\">\n" +
                    "                                                                            배송비</td>\n" +
                    "                                                                        <td\n" +
                    "                                                                            style=\"font-size:14px;border-bottom:1px solid rgba(0,0,0,0.1);padding-bottom:10px;text-align:right\">\n" +
                    "                                                                            %s원</td>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                    <tr>\n" +
                    "                                                                        <td style=\"font-size:18px;padding:15px 0 5px\">총\n" +
                    "                                                                            주문금액</td>\n" +
                    "                                                                        <td\n" +
                    "                                                                            style=\"font-size:18px;text-align:right;padding:15px 0 5px;color:#212121\">\n" +
                    "                                                                            %s원</td>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                    <tr>\n" +
                    "                                                                        <td style=\"font-size:18px;padding:5px 0\">\n" +
                    "                                                                            취소 배송비 합계\n" +
                    "                                                                        </td>\n" +
                    "                                                                        <td\n" +
                    "                                                                            style=\"font-size:18px;text-align:right;padding:5px 0;color:#212121\">\n" +
                    "                                                                            %s원</td>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                    <tr>\n" +
                    "                                                                        <td style=\"font-size:18px;padding:5px 0\">\n" +
                    "                                                                            총 취소비용\n" +
                    "                                                                        </td>\n" +
                    "                                                                        <td\n" +
                    "                                                                            style=\"font-size:18px;text-align:right;padding:5px 0;color:#212121\">\n" +
                    "                                                                            -%s원</td>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                    <tr>\n" +
                    "                                                                        <td\n" +
                    "                                                                            style=\"font-size:18px;padding:15px 0;border-bottom:1px solid rgba(0,0,0,0.2)\">\n" +
                    "                                                                            최종 환불 금액</td>\n" +
                    "                                                                        <th\n" +
                    "                                                                            style=\"font-size:18px;text-align:right;padding:15px 0;border-bottom:1px solid rgba(0,0,0,0.2);color:#212121\">\n" +
                    "                                                                            %s원 </th>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                    <tr height=\"50\">\n" +
                    "                                                                        <td></td>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                </tbody>\n" +
                    "                                                                \n" +
                    "                                                            </table>\n" +
                    "                                                        </td>\n" +
                    "                                                        <td width=\"5%%\"></td>\n" +
                    "                                                    </tr>\n" +
                    "                                                    <tr>\n" +
                    "                                                        <td width=\"5%%\">&nbsp;&nbsp;</td>\n" +
                    "                                                        <td>\n" +
                    "                                                            <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\"\n" +
                    "                                                                width=\"100%%\">\n" +
                    "                                                                <tbody>\n" +
                    "                                                                    <tr>\n" +
                    "                                                                        <td style=\"text-align:center\">\n" +
                    "                                                                            <a href=\"https://liberty52.com\"\n" +
                    "                                                                                style=\"padding:12px 16px;width:270px;color:#ffffff;background-color:#212121;border-color:#00b8ff;display:inline-block;text-decoration:none;font-size:20px\"\n" +
                    "                                                                                target=\"_blank\">Liberty52</a>\n" +
                    "                                                                        </td>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                    <tr height=\"50\">\n" +
                    "                                                                        <td></td>\n" +
                    "                                                                    </tr>\n" +
                    "                                                                </tbody>\n" +
                    "                                                            </table>\n" +
                    "                                                        </td>\n" +
                    "                                                        <td width=\"5%%\"></td>\n" +
                    "                                                    </tr>\n" +
                    "                                                </tbody>\n" +
                    "                                            </table>\n" +
                    "                                        </td>\n" +
                    "                                    </tr>\n" +
                    "                                </tbody>\n" +
                    "                            </table>\n" +
                    "                        </td>\n" +
                    "                    </tr>\n" +
                    "                </tbody>\n" +
                    "                <tfoot style=\"background:#eee;display:block;width:600px;margin:0 auto\">\n" +
                    "                    <tr style=\"display:block;text-align:center\">\n" +
                    "                        <th style=\"font-size:12px;font-weight:normal;color:#767676;padding:10px 0 50px;display:block\">©\n" +
                    "                            Liberty52</th>\n" +
                    "                    </tr>\n" +
                    "                </tfoot>\n" +
                    "            </table>\n" +
                    "            <div class=\"yj6qo\"></div>\n" +
                    "            <div class=\"adL\">\n" +
                    "            </div>\n" +
                    "        </div>\n" +
                    "    </div>\n" +
                    "    <div id=\":p5\" class=\"ii gt\" style=\"display:none\">\n" +
                    "        <div id=\":p6\" class=\"a3s aiL \"></div>\n" +
                    "    </div>\n" +
                    "    <div class=\"hi\"></div>\n" +
                    "</div>";

}
