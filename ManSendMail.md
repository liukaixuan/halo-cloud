## 功能介绍 ##

发送纯文本或HTML格式的电子邮件。

## 示例代码 ##
```

//获取服务（JSP中示例）
SendMailService s = (SendMailService) GuzzWebApplicationContextUtil.getGuzzContext(session.getServletContext()).getService("sendMailService") ;

//发送纯文本邮件
s.sendPlainMail("halo-cloud@guzzservices.com", "xxxx@gmail.com", "test plain subject", "test plain content!") ;

//发送HTML格式邮件
s.sendHtmlMail("halo-cloud@guzzservices.com", "xxxx@gmail.com", "test html subject", "test <font color='red'>HTML</font> <p/> content!") ;

```

## 配置服务 ##

1. 配置本服务依赖的[通信信道服务](ManServiceChannel.md)，假设配置好的信道服务名称为”commandSocketChannelForServices”.

2. 在guzz.xml中增加此服务：

```
<service name="sendMailService" dependsOn="commandSocketChannelForServices" class="com.guzzservices.mail.impl.RemoteSendMailServiceImpl"/>
```

3. 配置服务参数（guzz的properties文件）：**不需要配置。**

## 服务查询结果API ##

邮件发送成功，返回true；发送失败，返回false。

邮件发送成功与否的可信度由服务器端的实现者确定，此处返回的成功不代表绝对成功。

对于私服，需要修改服务器端的配置文件，填写正确的邮件服务器信息，如：
```
[springConfiguration]
sendmail.authedIPs=192.168,26.36.46.56
sendmail.host=smtp.163.com
sendmail.port=25
sendmail.username=xxx
sendmail.password=xxxxxx
```

其中，sendmail.authedIPs为允许访问邮件服务的的IP地址或者以xx开头的IP地址列表，用英文逗号分隔。其他配置项类似于Outlook等邮件客户端软件的配置。

为防止垃圾邮件，cloud.guzzservices.com 的测试环境不提供默认的邮件发送服务。
