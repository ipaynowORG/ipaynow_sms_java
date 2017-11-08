package cn.ipaynow.sms.sdk;

import cn.ipaynow.util.EncryDecryUtils;
import cn.ipaynow.util.FormDateReportConvertor;
import cn.ipaynow.util.PropertiesLoader;
import cn.ipaynow.util.RandomUtil;
import cn.ipaynow.util.httpkit.HttpsTookit;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ipaynow1130 on 2017/11/8.
 */
public class SmsSdk
{
    private  HttpsTookit httpsTookit;
    private final String url = "https://sms.ipaynow.cn";

    public SmsSdk() {
        try {
            httpsTookit = new HttpsTookit(null,null);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        }
    }


    public  String send_hy(String mobile,String content,String mhtOrderNo) {
        return send(mobile,content,"S01",mhtOrderNo);
    }
    public  String send_yx(String mobile,String content,String mhtOrderNo) {
        return send(mobile,content,"YX_01",mhtOrderNo);
    }
    private  String send(String mobile,String content,String type,String mhtOrderNo) {
        try {

            Map<String,String> requestMap = new HashMap<String, String>();

            requestMap.put("funcode", type);
            requestMap.put("appId", PropertiesLoader.getAppId());
            if(StringUtils.isEmpty(mhtOrderNo)){
                requestMap.put("mhtOrderNo", RandomUtil.getRandomStr(13));
            }else{
                requestMap.put("mhtOrderNo", mhtOrderNo);
            }
            requestMap.put("mobile",mobile);
            requestMap.put("content", URLEncoder.encode(content,"utf-8"));
            requestMap.put("notifyUrl","https://op-tester.ipaynow.cn/paytest/notify");

            String toRSAStr = FormDateReportConvertor.postFormLinkReport(requestMap);
            //message=base64(appId=xxx)| base64(3DES(报文原文))|base64(MD5(报文原文+&+ md5Key))
            String message1 = "appId="+PropertiesLoader.getAppId()+"";
            message1 = EncryDecryUtils.base64Encrypt(message1);//base64(appId=xxx)
            String message2 = toRSAStr;
            message2 = EncryDecryUtils.encryptFromDESBase64(PropertiesLoader.get3Des(),message2);// base64(3DES(报文原文)
            String message3 = EncryDecryUtils.base64Encrypt(EncryDecryUtils.md5(toRSAStr.toString().trim() +"&"+ PropertiesLoader.getMd5()));//base64(MD5(报文原文+&+ md5Key))
            String message = message1+"|"+message2+"|"+message3+"";
            message = URLEncoder.encode(message,"UTF-8");


            String result = httpsTookit.doPost(url,"funcode="+type+"&message="+message,null,null,"UTF-8");
            result = result.trim();

            //解包失败 或者 验签失败的时候res.split("\\|").length==2
            ///////////////////////////////////////////////////////////////////////////////////////////////////
            if(result.split("\\|").length==2){
                String return2 = result.split("\\|")[1];
                //错误原因
                System.err.println(EncryDecryUtils.base64Decrypt(return2));
                return null;
            }
            //正常返回res.split("\\|").length==3
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            String return1 = result.split("\\|")[0];
            String return2 = result.split("\\|")[1];
            String return3 = result.split("\\|")[2];

            //解析第二部分，获取原始报文 先解密base再解密3des
            String originalMsg  =  EncryDecryUtils.decryptFromBase64DES(PropertiesLoader.get3Des(), return2);
//            System.out.println("返回的报文原文是:"+originalMsg);

            //解析第三部分，获取原始签名
            String originalSign  =  EncryDecryUtils.base64Decrypt(return3);
//            System.out.println("返回的报文原始签名是:"+originalSign);

            String mySign = EncryDecryUtils.md5(originalMsg.trim()+"&"+PropertiesLoader.getMd5());//.trim() 很重要
//            System.out.println("商户生成的签名是:"+mySign);

            if(originalSign.equals(mySign)){

                Map map = form2Map(originalMsg);
                if(map.get("funcode") != null && map.get("funcode").equals(type) &&
                        map.get("responseCode") != null && map.get("responseCode").toString().trim().equals("00") &&
                        map.get("responseMsg") != null && map.get("responseMsg").toString().trim().equals("success") &&
                        map.get("status") != null && map.get("status").toString().trim().equals("00")){
                    return map.get("nowpayTransId").toString();
                }
                return  null;
            }else{
                System.err.println("验证签名不正确");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private Map form2Map(String s) {
        Map result = new HashMap();
        for(String tmp : s.split("&")){
            result.put(tmp.split("=")[0],tmp.split("=")[1]);
        }
        return result;
    }
}
