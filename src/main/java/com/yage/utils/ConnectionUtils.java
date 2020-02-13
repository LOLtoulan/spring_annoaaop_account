package com.yage.utils;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


/**
 * 获取Connection的工具类
 */
@Component("connectionUtils")
public class ConnectionUtils {

    private ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();

    //获取数据源
    @Autowired
    private DataSource dataSource;

    //通过设置set属性，通过xml对dataSource进行注入
//    public void setDataSource(DataSource dataSource) {
//        this.dataSource = dataSource;
//    }

    /**
     * 获取当前线程上的连接
     * @return
     */
    public  Connection getThreadConnection(){


        try {
            //通过threadLocal获取当前线程上的连接
            Connection connection = threadLocal.get();
            //判断当前线程上是否有连接
            if (connection == null) {
                //从数据源上获取一个连接，并和当前线程绑定
                connection = dataSource.getConnection();
                //将connection设置到当前线程中
                threadLocal.set(connection);
            }
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeConnection(){
        threadLocal.remove();
    }
}
