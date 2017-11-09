# 短信SDK使用说明 #

## 版本变更记录 ##

- 1.0.0 : 初稿


## 目录 ##

[1. 概述](#1)

&nbsp;&nbsp;&nbsp;&nbsp;[1.1 简介](#1.1)

&nbsp;&nbsp;&nbsp;&nbsp;[1.2 如何获取](#1.2)

[2. API](#2)

&nbsp;&nbsp;&nbsp;&nbsp;[2.1 聚合交易API](#2.1)

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[行业短信发送](#2.1.1)

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[营销短信发送](#2.1.2)

&nbsp;&nbsp;&nbsp;&nbsp;[2.2 接受通知(状态报告)](#2.2)

&nbsp;&nbsp;&nbsp;&nbsp;[2.3 查询短信发送结果](#2.3)


[3. 完整DEMO](#3)


<h2 id='1'> 1. 概述 </h2>

<h4 id='1.1'> 1.1 简介 </h4>

- 短信SDK。

<h4 id='1.2'> 1.2 如何获取 </h4>

[获取源码](https://github.com/ipaynowORG/ipaynow_sms_java)

[demo源码](https://github.com/ipaynowORG/ipaynow_sms_java)

Maven坐标如下

	<dependency>
	       <groupId>com.github.ipaynow</groupId>
           <artifactId>ipaynow_sms_sdk</artifactId>
           <version>1.0.0</version>
	</dependency>





<h2 id='2'> 2. API </h2>

业务客户端使用SDK的相关类: cn.ipaynow.sms.sdk.SmsSdk

<h4 id='2.1'> 2.1 短信发送 </h4>

<h5 id='2.1.1'></h4>

- 行业短信发送

            /**
             * 发送行业短信(需要在运营后台-短信服务管理 中进行配置)
             * @param mobile    发送手机号
             * @param content   发送内容
             * @param mhtOrderNo    商户订单号,可为空(自动生成)。商户订单号和状态报告通知中的相关字段对应
             * @param notifyUrl 后台通知地址
             * @return  现在支付订单号,和状态报告通知中的相关字段对应
             */
            public  String send_hy(String mobile,String content,String mhtOrderNo,String notifyUrl)

<h5 id='2.1.2'></h4>

- 营销短信发送

            /**
             * 发送营销短信(需要在运营后台-短信服务管理 中进行配置)
             * @param mobile    发送手机号
             * @param content   发送内容
             * @param mhtOrderNo    商户订单号,可为空(自动生成)。商户订单号和状态报告通知中的相关字段对应
             * @param notifyUrl 后台通知地址
             * @return  现在支付订单号,和状态报告通知中的相关字段对应
             */
            public  String send_yx(String mobile,String content,String mhtOrderNo,String notifyUrl)


<h4 id='2.2'>2.2 接受通知(状态报告)</h4>

通知方式采用httppost方式通知,接受demo如下

        //获取通知数据需要从body中流式读取
        BufferedReader reader = req.getReader();
        StringBuilder reportBuilder = new StringBuilder();
        String tempStr = "";
        while((tempStr = reader.readLine()) != null){
               reportBuilder.append(tempStr);
        }
        //报文数据字符串
        String reportContent = reportBuilder.toString();


字段含义如下:

<table>
        <tr>
            <th>字段名称</th>
            <th>字段Key</th>
            <th>备注</th>
        </tr>
        <tr>
            <td>功能码</td>
            <td>funcode</td>
            <td>定值：N001</td>
        </tr>
        <tr>
            <td>接口版本号</td>
            <td>version</td>
            <td>定值：1.0.0</td>
         </tr>
<tr>
            <td>商户应用唯一标识</td>
            <td>appId</td>
            <td></td>
         </tr>
<tr>
            <td>商户订单号</td>
            <td>mhtOrderNo</td>
            <td></td>
         </tr>
    </table>

<h4 id='2.3'> 2.3 查询短信发送结果 </h4>

- xxxxx

        /**
         * 商户微信App支付订单查询
         * @param mhtOrderNo    商户订单号
         * @param appId 商户的AppId,https://mch.ipaynow.cn ->商户中心->应用信息可以新增应用或查看appKey
         * @param appKey 商户的AppKey,https://mch.ipaynow.cn ->商户中心->应用信息可以新增应用或查看appKey
         * @return
         */
        public Map queryOrderWxApp(String mhtOrderNo,App app)

<h2 id='3'> 3. 完整DEMO </h2>

            直接运行cn.ipaynow.ipaynow_pay_demo.Main
            访问
            http://127.0.0.1:7072/paytest/index.html