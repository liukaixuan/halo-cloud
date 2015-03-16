## 功能介绍 ##
从一段HTML代码中抽取所有纯文本正文，以及html img标签引用的图片信息。抽出的纯文本和图片分开存放。

在使用可以指定抽取文本的最大长度，以及图片的最大个数。

演示地址：<a href='http://cloud.guzzservices.com/services/test/testHtml2Plain.jsp'><a href='http://cloud.guzzservices.com/services/test/testHtml2Plain.jsp'>http://cloud.guzzservices.com/services/test/testHtml2Plain.jsp</a></a>


## 示例代码 ##
```
//第1行，获取服务（JSP中写的）
Html2PlainExtractService html2PlainExtractService = (Html2PlainExtractService) GuzzWebApplicationContextUtil.getGuzzContext(session.getServletContext()).getService("html2PlainExtractService") ;

//第2行，解析html，最长返回137个字符的纯文本，和最多一张图
PlainExtractResult result = html2PlainExtractService.extractTextWithImage(msg.getContent(), 137, 1) ;

if(result.isTextCutted()){
	out.println("plainContent:" + result.getPlainText() + "...") ;
}else{
	out.println("plainContent:" + result.getPlainText()) ;
}

if(result.getImages() != null){
	out.println("imageUrl:" + result.getImages()[0] ;
}
```

## 配置服务 ##

1. 配置本服务依赖的[通信信道服务](ManServiceChannel.md)。假设配置好的信道服务名称为”commandSocketChannelForServices”.

2. 在guzz.xml中增加此服务：
```
<service name="html2PlainExtractService" configName="html2PlainExtractService" dependsOn="commandSocketChannelForServices" class="com.guzzservices.text.impl.RemoteHtml2PlainExtractServiceImpl"/>
```

4. 配置服务参数（guzz的properties文件）：**不需要配置**

## 服务API ##

在需要解析html的地方，获取或注入html2PlainExtractService，java接口为：

`com.guzzservices.text.Html2PlainExtractService`

API定义：
```
package com.guzzservices.text;
public interface Html2PlainExtractService {

	/**
	 * 抽取普通文本，不包含图片信息。
	 *
	 * @param htmlText 传入的html
	 */
	public PlainExtractResult extractText(String htmlText) throws Exception ;

	/**
	 * 抽取普通文本，不包含图片信息。
	 *
	 * @param htmlText 传入的html
	 * @param resultLengthLimit 抽取的普通文本最长只需要截取到的长度。0表示抽取所有。
	 */
	public PlainExtractResult extractText(String htmlText, int resultLengthLimit) throws Exception ;

	/**
	 * 抽取普通文本，包含所有图片信息。图片信息会另存，不会混合到返回的普通文本中。
	 *
	 * @param htmlText 传入的html
	 * @param resultLengthLimit 抽取的普通文本最长只需要截取到的长度。0表示抽取所有。
	 */
	public PlainExtractResult extractTextWithAllImages(String htmlText, int resultLengthLimit) throws Exception ;

	/**
	 * 抽取普通文本，含图片信息。图片信息会另存，不会混合到返回的普通文本中。
	 *
	 * @param htmlText 传入的html
	 * @param resultLengthLimit 抽取的普通文本最长只需要截取到的长度。0表示抽取所有。
	 * @param imageCountLimit 最多只需要提取的图片数。-1表示抽取所有；0表示不抽取；其他表示抽取的图片张数。
	 */
	public PlainExtractResult extractTextWithImage(String htmlText, int resultLengthLimit, int imageCountLimit) throws Exception ;

}
```

**PlainExtractResult定义：**

```
package com.guzzservices.text;
public class PlainExtractResult {

	/**
	 * 抽取的纯文本内容，自动合并多余的空格。
	 */
	public String getPlainText() ;

        /**
	 * 返回抽取的纯文本内容，并删除重复的换行。
	 *
	 * @param newLineSymbol 将合并后的'\r'和'\r\n' 统一换成newLineSymbol
	 * @param maxNewLinesRepeatCount 内容中一处换行的地方最多允许出现多少个'\r'或'\r\n'。用于控制大段的空行。
	 *
	 * @return 返回的内容不会控制字数，因换行替换成newLineSymbol可能使得返回的字符串超过调用时传入的最大允许长度，需要应用自己控制。
	 */
	public String getPlainTextDropNewLine(String newLineSymbol, int maxNewLinesRepeatCount)

	/**
	 * 返回的纯文本内容是否是截断过的（还有更多文本，由于字数限制，没有返回）。
	 */
	public boolean isTextCutted() ;

	/**
	 * HTML中发现的图片地址。如果没有图片，返回null。
	 */
	public String[] getImages() ;

    /**
	 * HTML中发现的图片的名称（title和alt属性的值）。如果没有图片，返回null。返回的数组顺序与getImages()返回的图片地址相对应。
	 */
	public String[] getImageTitles()

	/**
	 * 除了返回的图片地址，html中是否还有更多图片地址？
	 */
	public boolean isMoreImagesIgnored() ;
	
}
```