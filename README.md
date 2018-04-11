# Optimus
nextTrucking 项目改造为springcloud框架的主工程。
当前版本分为以下几个子工程。

##backend
模拟当前backend项目，springboot版本和当前后台系统保持一致。用以验证各个服务组建client调用和业务返回处理。


##common
新项目公共组建包，包括各个公用方法组建。

util工具包

dto包

respons封装包

基础自定义异常包

资源文件读取工具包

组建服务包健康检查工厂

##eureka
springcloud 注册分发中心（当前为单例）

注册时需要用户名和密码验证。

##kafkaConsumer
kafka业务消息处理中心，各个不同处理通道实现AbstractMessageSinker类中的方法做各自不同的业务处理。

##messageCenterService
分为api接口类和core核心实现类

api接口类封装了接口client，其他程序通过依赖api类访问core中的程序。

core实现了向kafkaf集群发送消息的操作，core不可以直接调用，需要通过api接口调用。



# Optimus
