## 功能介绍 ##

通过传入的IP地址，返回IP所在的地理位置。如传入“58.16.209.19”，返回“贵州省六盘水市 ”。

返回的地理位置又分为2种精确度，程序可以按照自身需要选择。三种精确度分别为：城市（地市级），详细位置。例如对于“58.16.209.19”，两种精度的值为：
```
城市:贵州省六盘水市
详细地址:六枝特区腾龙网吧
```

对于某些不包含省份信息的地理位置，如“清华大学”，IP服务对数据库进行了修正，通过cityMarker标记其所在城市。例如清华大学的IP，返回结果的cityMarker将标记为“北京”。对于国外IP，cityMarker统一标记为“海外”，以方面调用者快速判断。

## 示例代码 ##
```
//第1行，获取IP反查服务（JSP中写的）
IPLocateServiceipService = (IPLocateService) GuzzWebApplicationContextUtil.getGuzzContext(session.getServletContext()).getService("chinaIPLocateService") ;  

//第2行，执行查询。findLocation方法传入要查询的IP地址。
LocateResult result = s.findLocation("59.66.106.0") ; 

//第3行，按照精确度要求，读取地理位置
if(result != null){
	out.println("city:" + result.getCityName()) ;
	out.println("detail:" + result.getDetailLocation()) ;
	out.println("location:" + result.fullLocation ()) ;
}else{
	out.println("IP not found.") ;
}
```

## 配置服务 ##

1. 配置本服务依赖的[通信信道服务](ManServiceChannel.md)，假设配置好的信道服务名称为”commandSocketChannelForServices”.

2. 在guzz.xml中增加此服务：

```
<service name="chinaIPLocateService" dependsOn="commandSocketChannelForServices" class="com.guzzservices.dir.ip.ChinaIPLocateServiceImpl">
```

3. 配置服务参数（guzz的properties文件）：**不需要配置。**

## 服务查询结果API ##

执行查询时，返回的是LocateResult对象，此对象有一些方法和变量按照不同精确度和用途存储地理信息。

LocateResult API：

```
package com.guzzservices.dir.ip;
public class LocateResult implements Serializable {

	/**城市的附加标记。如“清华大学”，则为”北京“；如为“北京海淀”，则为null；对于外国，值为“海外”；  */
	private String cityMarker ;

	/**查询地市级名称，如：贵州省六盘水市*/
	private String cityName ;

	/**详细地址，如：六枝特区腾龙网吧*/
	private String detailLocation ;

	/**
	 * 完整地址
	 */
	public String fullLocation(){
		StringBuilder sb = new StringBuilder() ;

		if(this.cityMarker != null){
			sb.append(this.cityMarker) ;
		}

		if(this.cityName != null){
			sb.append(this.cityName) ;
		}

		if(this.detailLocation != null){
			sb.append(this.detailLocation) ;
		}

		return sb.toString()  ;
	}

	public String getCityName() {
		return cityName;
	}

	public String getDetailLocation() {
		return detailLocation;
	}

	public String toString(){
		StringBuilder sb = new StringBuilder() ;
		sb.append("cityMarker:").append(cityMarker)
		  .append(",cityName:").append(cityName)
		  .append(",detailLocation:").append(detailLocation) ;

		return sb.toString() ;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public void setDetailLocation(String detailLocation) {
		this.detailLocation = detailLocation;
	}

	public String getCityMarker() {
		return cityMarker;
	}

	public void setCityMarker(String cityMarker) {
		this.cityMarker = cityMarker;
	}

}

```
