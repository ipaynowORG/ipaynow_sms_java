package cn.ipaynow.ipaynow_sms_demo;

import cn.ipaynow.sms.sdk.SmsSdk;

/**
 * Created by ipaynow1130 on 2017/11/8.
 */
public class Demo {


    private static SmsSdk smsSdk = new SmsSdk();

    public static void main(String [] args){

        //发送行业短信
//        System.out.println(smsSdk.send_hy("xxxxxxxxxxxxxx",
//                "1a的2",
//                null,
//                "https://op-tester.ipaynow.cn/paytest/notify"));

        //查询发送结果
        System.out.println(smsSdk.query("400001201711091044192182583","xxxxxxxxxxx"));

    }

}
