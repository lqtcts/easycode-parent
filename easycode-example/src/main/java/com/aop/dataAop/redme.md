
本例子 主要是实现多数据源  基于注解的动态切换
还有一点，我们可以查到，加载这连个配置文件的累分别是：
spring（applicationContext），SpringMVC（webApplicationContext）（希望没记错，记错了自己对应修改）
第一步，要明确，我们是在SpringMVC上aop监测，那么所有的报扫描注入都在SpringMVC的配置文件中完成，不要再spring的配置文件中完成，
    不然在开启代理后，发现还是没有起到任何作用。（当然，aopalliance-1.0.jar，aspectjweaver-1.8.6.jar这两个jar包不可缺少）
第二步，在SpringMVC的配置文件中开启它的代理模式：<aop:aspectj-autoproxy expose-proxy="true"></aop:aspectj-autoproxy>

第三步，进行aop监测相关方法的类的编写

最后就是一个小技巧，就是先测pointcut表达式的具体到某一个具体函数，成功后，在用.*来代替，在测，知道成功完成为止!!!!!
希望对大家有帮住，也希望有大牛深入剖析其中的原理。有看到的同学请给我转个链接，谢谢。