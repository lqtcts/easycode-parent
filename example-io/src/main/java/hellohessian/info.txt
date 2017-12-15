http://127.0.0.1:8080/Hessian-server/ServiceServlet

java领域中知名的有：RMI、XML-RPC、Binary-RPC、SOAP、CORBA、JMS


Hessian是一个轻量级的remoting on http工具,采用的是Binary RPC协议，
所以它很适合于发送二进制数据,同时又具有防火墙穿透能力
。Hessian一般是通过Web应用来提供服务，因此非常类似于平时我们用的 WebService。只是它不使用SOAP协议,但相比webservice而言更简单、快捷。

Binary-RPC(Remote Procedure Call Protocol，远程过程调用协议)是一种和RMI(Remote Method Invocation，远程方法调用)类似的远程调用的协议，
它和RMI 的不同之处在于它以标准的二进制格式来定义请求的信息 ( 请求的对象、方法、参数等 ) ，这样的好处是什么呢，就是在跨语言通讯的时候也可以使用。

Hessian的好处是精简高效，可以跨语言使用，而且协议规范公开，我们可以针对任意语言开发对其协议的实现

Hessian与WEB服务器结合非常好，借助WEB服务器的成熟功能，在处理大量用户并发访问时会有很大优势，在资源分配，线程排队，异常处理等方面都可以由成熟的WEB服务器保证

RMI需要开防火墙端口，Hessian不用

Caucho





Java
Flash/Flex
Python
C++
.NET C#
D
Erlang
PHP
Ruby
Objective C