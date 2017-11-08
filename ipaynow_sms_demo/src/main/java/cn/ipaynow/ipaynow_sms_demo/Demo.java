package cn.ipaynow.ipaynow_sms_demo;

import cn.ipaynow.sms.sdk.SmsSdk;

/**
 * Created by ipaynow1130 on 2017/11/8.
 */
public class Demo {


    private static SmsSdk smsSdk = new SmsSdk();

    public static void main(String [] args){

        System.out.println(smsSdk.send_yx("13401190417","1a的2",null));

    }
}
