package com.zeroone.star.capital.utils;

public class StatusConvertUtil {

    public static String check(Integer status) {
        if (status == null) return "未核对";
        return status == 1 ? "已核对" : "未核对";
    }

    public static String nucleus(Integer status) {
        if (status == null) return "未核销";
        switch (status) {
            case 1: return "部分核销";
            case 2: return "已核销";
            default: return "未核销";
        }
    }

    public static String examine(Integer status) {
        return (status != null && status == 1) ? "已审核" : "未审核";
    }

    public static String cse(Integer status) {
        if (status == null) return "未结算";
        switch (status) {
            case 1: return "部分结算";
            case 2: return "已结算";
            case 3: return "无需结算";
            default: return "未结算";
        }
    }

    public static String invoice(Integer status) {
        if (status == null) return "未开票";
        switch (status) {
            case 1: return "部分开票";
            case 2: return "已开票";
            case 3: return "无需开具";
            default: return "未开票";
        }
    }

    // 核销类型
    public static String bill(String bill){
        switch (bill){
            case "cia":
                return "预收";
            case "re":
                return "应收";
            case "pia":
                return "预付";
            case "cw":
                return "应付";
            case "sre":
                return "销退";
            case "sell":
                return "销售";
            case "buy":
                return "采购";
            case "bre":
                return "购退";
            default:
                return "其他";
        }
    }

    /**
     * 根据单据类型/表名返回中文业务类型
     */
    public static String mold(String mold) {
        if (mold == null) return "其他";

        switch (mold) {
            case "imy":
                return "收款单";
            case "omy":
                return "付款单";
            case "sell":
                return "销售单";
            case "sre":
                return "销售退货单";
            case "ice":
                return "其它收入单";
            case "buy":
                return "采购单";
            case "bre":
                return "采购退货单";
            case "oce":
                return "其它支出单";
            default:
                return "其他";
        }
    }

}
