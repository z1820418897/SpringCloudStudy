package com.gc.webserver.aop;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LogControllerAop {

    /**
     * 指定切点
     *
     * 写表达式 匹配所有的AopController的接口方法
     * */
    @Pointcut("execution(public * com.gc.webserver.controller.AopController.*(..))")
    public void  aopControllerLog(){}

    /**
     * 前置通知 方法调用前被调用
     **/
    @Before("aopControllerLog()")
    public void aopConntroller_Before(JoinPoint joinPoint){
        log.info("我是前置通知");

        //获取目标方法的参数信息
        Object[] obj = joinPoint.getArgs();
        Signature signature = joinPoint.getSignature();
        //代理的是哪一个方法
        log.info("方法："+signature.getName());
        //AOP代理类的名字
        log.info("方法所在包:"+signature.getDeclaringTypeName());
        //AOP代理类的类（class）信息
        signature.getDeclaringType();
        MethodSignature methodSignature = (MethodSignature) signature;
        String[] strings = methodSignature.getParameterNames();
        log.info("参数名："+ Arrays.toString(strings));
        log.info("参数值ARGS : " + Arrays.toString(joinPoint.getArgs()));
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest req = attributes.getRequest();
        // 记录下请求内容
        log.info("请求URL : " + req.getRequestURL().toString());
        log.info("HTTP_METHOD : " + req.getMethod());
        log.info("IP : " + req.getRemoteAddr());
        log.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());

    }

    /**
     * 处理成功通知
     * */
    @AfterReturning(returning ="ret",pointcut = "aopControllerLog()")
    public void aopConntroller_AfterReturning(Object ret){
        //方法的返回值ret
        log.info("方法的返回值："+ret);

    }


    /**
     * 后置异常通知
     * */
    @AfterThrowing("aopControllerLog()")
    public void throwss(JoinPoint c){
        log.error("方法异常时执行.....");
    }


    /**
     * 后置最终通知，final增强，不管是否抛出异常或者正常都会执行
     * */
    @After("aopControllerLog()")
    public void after(JoinPoint jp){

    }


    /**
     * 环绕通知，环绕增强，相当于MethodInterceptor
     * */
    @Around("aopControllerLog()")
    public Object arround(ProceedingJoinPoint pjp) {
        try {
            Object o =  pjp.proceed();
            return o;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }



}
