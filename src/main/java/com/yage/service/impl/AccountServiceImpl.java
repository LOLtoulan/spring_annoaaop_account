package com.yage.service.impl;


import com.yage.dao.AccountDAO;
import com.yage.domain.Account;
import com.yage.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author LOL_toulan
 * @Message 没有事务控制的实现类
 */
@Service("accountService")
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDAO accountDAO;

   /* public void setAccountDAO(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }*/

    @Override
    public List<Account> findAllAccount() {

        List<Account> accounts = accountDAO.findAllAccount();

        return accounts;

    }

    @Override
    public Account findAccountById(int id) {

        return accountDAO.findAccountById(id);

    }

    @Override
    public void saveAccount(Account account) {

        accountDAO.saveAccount(account);
    }

    @Override
    public void updateAccount(Account account) {

        accountDAO.updateAccount(account);
    }

    @Override
    public void deleteById(int id) {

        accountDAO.deleteById(id);

    }

    @Override
    public void transfer(String sourceName, String targetName, Float money) {


        //2.1执行操作
        if (sourceName.equals(targetName)) {
            throw new RuntimeException("不能给同一个账户转账");

        }
        // 2.2根据名称查询转入账户（是否有对应账户）
        Account sourceAccount = accountDAO.findByName(sourceName);
        // 2.3根据名称查询转出账户（是否有对应账户）
        Account targetAccount = accountDAO.findByName(targetName);
        if (sourceAccount != null && targetAccount != null && sourceAccount.getMoney() > money) {
            // 2.4转出账户扣钱（查询账户的钱是否比转出的钱多）
            sourceAccount.setMoney(sourceAccount.getMoney() - money);
            // 2.5更新转入账户
            accountDAO.updateAccount(sourceAccount);
            //2.6模拟网络出现问题
            //int c = 1 / 0;
            // 2.7转入账户加钱
            targetAccount.setMoney(targetAccount.getMoney() + money);
            // 2.8更新转出账户
            accountDAO.updateAccount(targetAccount);
        } else {
            // 2.9如果因其他情况导致转账操作无法进行，则回滚事务到转账之前
            throw new RuntimeException("原账户余额不足或存在不存在用户");
        }

        /*try {
            //1.开启事务
            txManager.beginTransaction();
            //2.执行操作

            //2.1根据名称查询转出账户
            Account source = accountDAO.findByName(sourceName);
            //2.2根据名称查询转入账户
            Account target = accountDAO.findByName(targetName);
            //2.3转出账户减钱
            source.setMoney(source.getMoney()-money);
            //2.4转入账户加钱
            target.setMoney(target.getMoney()+money);
            //2.5更新转出账户
            accountDAO.updateAccount(source);

            //int i=1/0;

            //2.6更新转入账户
            accountDAO.updateAccount(target);
            //3.提交事务
            txManager.commit();

        }catch (Exception e){
            //4.回滚操作
            txManager.rollback();
            e.printStackTrace();
        }finally {
            //5.释放连接
            txManager.release();
        }*/
    }
}
