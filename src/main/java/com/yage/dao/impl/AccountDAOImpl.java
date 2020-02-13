package com.yage.dao.impl;


import com.yage.dao.AccountDAO;
import com.yage.domain.Account;
import com.yage.utils.ConnectionUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * @Author LOL_toulan
 * @Message
 */
@Repository("accountDAO")
public class AccountDAOImpl implements AccountDAO {

    @Autowired
    private QueryRunner runner;
    @Autowired
    private ConnectionUtils connectionUtils;

    /*public void setConnectionUtils(ConnectionUtils connectionUtils) {
        this.connectionUtils = connectionUtils;
    }

    public void setRunner(QueryRunner runner) {
        this.runner = runner;
    }*/

    @Override
    public List<Account> findAllAccount() {
        List<Account> accountList = null;
        try {
            String sql = "select * from account";
            accountList = runner.query(connectionUtils.getThreadConnection(),sql, new BeanListHandler<Account>(Account.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accountList;
    }

    @Override
    public Account findAccountById(int id) {
        Account account = null;
        try {
            String sql = "select * from account where id = ?";
            account = runner.query(connectionUtils.getThreadConnection(),sql, new BeanHandler<Account>(Account.class), id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }

    @Override
    public void saveAccount(Account account) {
        try {
            String sql = "insert into account(name,money)values(?,?)";
            runner.update(connectionUtils.getThreadConnection(),sql, account.getName(), account.getMoney());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateAccount(Account account) {
        try {
            String sql = "update account set name=?,money=? where id =?";
            runner.update(connectionUtils.getThreadConnection(),sql, account.getName(), account.getMoney(), account.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(int id) {
        try {
            String sql = "delete from account where id =?";
            runner.update(connectionUtils.getThreadConnection(),sql, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Account findByName(String name) {
        List<Account> accounts = null;
        try {
            String sql = "select * from account where name = ?";
            accounts = runner.query(connectionUtils.getThreadConnection(),sql, new BeanListHandler<Account>(Account.class), name);
            if (accounts == null || accounts.size() == 0) {
                return null;
            }
            if (accounts.size() > 1) {
                throw new RuntimeException("结果集不唯一");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts.get(0);
    }
}