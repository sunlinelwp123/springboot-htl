package com.open.boot.common;

import java.math.BigDecimal;

public class test {
    public static boolean  checkPremiumSimpleRechargeAmountCompare(String simpleRechargeAmount, String totalPremium){
        BigDecimal amount = new BigDecimal(simpleRechargeAmount);
        BigDecimal premium = new BigDecimal(totalPremium);
        if ( (amount.subtract(premium)).compareTo(BigDecimal.ZERO) >=0){
            return  true;
        }
        return  false;
    }

    public static void main(String[] args){
        BigDecimal amount = new BigDecimal("2");
        BigDecimal premium = new BigDecimal("1");
        amount = amount.subtract(premium);

        System.out.println(amount);
        if("1".equals(null)){
            System.out.println(amount);
        }
    }
}
