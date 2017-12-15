package com.aop.dataAop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

/**
 */
@Component
@Aspect
public class MultipleDataSourceAspectAdvice {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(* com.yeekit.admin.dao..*.*(..))")
    private void pointCutMethod() {
    }
//    //声明前置通知
//    @Before("pointCutMethod()")
//    public void doBefore() {
//        System.out.println("前置通知");
//    }

//    //声明后置通知
//    @AfterReturning(pointcut = "pointCutMethod()", returning = "result")
//    public void doAfterReturning(String result) {
//        System.out.println("后置通知");
//        System.out.println("---" + result + "---");
//    }

//    //声明例外通知
//    @AfterThrowing(pointcut = "pointCutMethod()", throwing = "e")
//    public void doAfterThrowing(Exception e) {
//        System.out.println("例外通知");
//        System.out.println(e.getMessage());
//    }

//    //声明最终通知
//    @After("pointCutMethod()")
//    public void doAfter() {
//        System.out.println("最终通知");
//    }

    //声明环绕通知
    @Around("pointCutMethod()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        logger.debug("--multiple data source aop--");
        Object object = pjp.getTarget();
        logger.debug("Target Class:"+object.getClass());
        DataSourceKey annotation = AnnotationUtils.findAnnotation(object.getClass(),DataSourceKey.class);
        if(null!=annotation){//没有注解的用默认值
            String value = annotation.value();
            if(null!=value&&value.length()>0){
                MultipleDataSource.setDataSourceKey(value);
                logger.debug("Data Source Key :"+value);
            }
        }
        return pjp.proceed();
    }
}