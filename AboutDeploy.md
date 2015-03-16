#部署分为部署云服务端和客户端使用



## 客户端使用服务 ##

halo-cloud为guzz标准服务，使用方法请参看：http://code.google.com/p/guzz/wiki/TutorialService

关于guzz以及配置等，请参看：http://code.google.com/p/guzz/


## 部署服务器端私有云 ##


如果你只是想看看什么样的，可以使用公用的云测试平台：http://cloud.guzzservices.com/services/login.jsp，做客户端实验即可。

部署说明：

1. halo-cloud 基于java语言，使用spring IOC + springMVC + guzz做管理台和数据库持久开发；

2. halo-cloud 需要部署Apache Zookeeper做配置管理，我测试用的是最新的zookeeper3.3.3版本；需要安装zk。请先安装zookeeper：http://zookeeper.apache.org/doc/trunk/zookeeperStarted.html

3. halo-cloud 使用memcached做缓存和session管理。安装memcached。

4. 运行在Tomcat6 + JDK6以上，别的没有测试过。安装Tomcat6和JDK6，并配置tomcat启动起来。

5. 安装Mysql5+数据，用 /doc 下的guzzServicesDB.sql 创建数据库表。

6. 部署代码到tomcat中，修改/WEB-INF/guzz\_services.properties 进行各项配置。包括memcached,mysql以及zookeeper的连接信息。

7. 集群：云端最少只需要1台机器即可，多台机器之间没有任何关联，可以任意部署。也就是Share-Nothing架构。


### 一般升级方法 ###

/WEB-INF/guzz\_services.properties 不能覆盖，需根据最新变化修改。classpath下的log4j.properties等配置文件根据需要修改。其他的应该都是直接覆盖即可。


### 代码结构 ###

/src 下是服务器端专用代码。

/conf 存放配置文件

/test 存放测试代码

其他源代码目录直接打成jar包，当作客户端代码来用。我们发布的名字是：guzzServices\_common.jar

### 代码获取 ###

云端代码请直接从svn checkout，Downloads中不在提供下载。

客户端代码可以从Downloads中下载，也可以在下载并导入的云端工程中自己打包。打包的方式是：打包除src和conf目录以外的所有源代码目录。

从Downloads中下载的客户端代码不保证最新。
