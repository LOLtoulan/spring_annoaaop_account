package com.yage.utils;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

/**
 * 和事务相关的公具类，包含了开启事物，提交事务，关闭事务，回滚事务
 */
@Component("txManager")
@Aspect
public class TransactionManager {


    @Autowired
    private ConnectionUtils connectionUtils;



    @Pointcut("execution(* com.yage.service.impl.*.*(..))")
    public void pt1(){}

    /**
     * 开启事务
     */
    //@Before("pt1()")
    public void beginTransaction(){
        try {
            connectionUtils.getThreadConnection().setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 提交事务
     */
    //@AfterReturning("pt1()")
    public void commit(){
        try {
            connectionUtils.getThreadConnection().commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 回滚事务
     */
    //@AfterThrowing("pt1()")
    public void rollback(){
        try {
            connectionUtils.getThreadConnection().rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭事务
     */
   // @After("pt1()")
    public void release(){
        // 我们这里将连接关闭，其实并没有关闭，而是将连接还回到连接池中。
        // 当然线程也是一样，我们将线程关闭其实并不是将线程关闭，而是将线程还回到线程池中，
        // 这就出现了问题，我们并没有将线程彻底关闭，那么下一次再 connectionUtils.getThreadConnection()
        // 的方法中，因为我们的连接和线程是绑在一起的，所以我们可能获得的是一个已经还回到线程池中的线程的连接，
        // 该连接当然是无效的，所以我们要在最后将线程和来连接进行解绑。
        try {
            connectionUtils.getThreadConnection().close();//还回连接池中
            connectionUtils.removeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * 环绕通知
     * @param pjp
     * @return
     */
    @Around("pt1()")
    public Object aroundAdvice(ProceedingJoinPoint pjp){
        Object rtValue = null;
        try {
            //1.获取参数
            Object[] args = pjp.getArgs();
            //2.开启事务
            this.beginTransaction();
            //3.执行方法
            rtValue = pjp.proceed(args);
            //4.提交事务
            this.commit();

            //返回结果
            return  rtValue;

        }catch (Throwable e){
            //5.回滚事务
            this.rollback();
            throw new RuntimeException(e);
        }finally {
            //6.释放资源
            this.release();
        }
    }
}
